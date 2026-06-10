<?php
namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Pesanan;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class PesananController extends Controller
{
    // 1. Mengambil Daftar Pesanan Berdasarkan ID Toko (Untuk Penjual)
    public function getPesananToko($id_toko)
    {
        $pesanans = Pesanan::with(['user', 'produk'])
            ->where('id_toko', $id_toko)
            ->orderBy('created_at', 'desc')
            ->get();

        if ($pesanans->isEmpty()) {
            return response()->json([
                'status' => 'error',
                'message' => 'Belum ada pesanan masuk',
                'data' => []
            ], 404);
        }

        return response()->json([
            'status' => 'success',
            'message' => 'Data pesanan berhasil diambil',
            'data' => $pesanans
        ]);
    }

    // 2. Mengupdate Status Pesanan (Terima / Tolak / Selesai)
    public function updateStatus(Request $request, $id)
    {
        try {
            DB::beginTransaction();

            $pesanan = Pesanan::find($id);

            if (!$pesanan) {
                return response()->json(['status' => 'error', 'message' => 'Pesanan tidak ditemukan'], 404);
            }

            $status_baru = $request->status_pesanan; 
            $status_lama = $pesanan->status_pesanan;

            // LOGIKA REFUND: Kalau pesanan ditolak/dibatalkan, kembalikan uang dan stok
            if ($status_baru == 'batal' && $status_lama != 'batal') {
                
                // 1. Kembalikan Saldo Pembeli
                $user = \App\Models\User::find($pesanan->id_user);
                if ($user) {
                    $user->saldo += $pesanan->total_harga;
                    $user->save();
                }

                // 2. Kembalikan Stok Makanan
                $produk = \App\Models\Produk::find($pesanan->id_produk);
                if ($produk) {
                    $produk->stok += $pesanan->jumlah_pesan;
                    $produk->save();
                }
            }

            // LOGIKA PENERIMAAN DANA: Kalau pesanan selesai, uang diteruskan ke Penjual
            if ($status_baru == 'selesai' && $status_lama != 'selesai') {
                $penjual = \App\Models\User::find($pesanan->id_toko);
                if ($penjual) {
                    $penjual->saldo += $pesanan->total_harga;
                    $penjual->save();
                }
            }

            // Update statusnya menjadi yang baru
            $pesanan->status_pesanan = $status_baru;
            $pesanan->save();

            DB::commit();

            return response()->json([
                'status' => 'success',
                'message' => 'Pesanan berhasil diubah menjadi: ' . $status_baru,
                'data' => $pesanan
            ]);

        } catch (\Exception $e) {
            DB::rollBack();
            return response()->json([
                'status' => 'error',
                'message' => 'Gagal mengupdate pesanan: ' . $e->getMessage()
            ], 500);
        }
    }

    // 3. Mengambil Riwayat Pesanan Berdasarkan ID Pembeli (User)
    public function getRiwayatPesanan($id_user)
    {
        try {
            $riwayat = DB::table('pesanans')
                ->join('tokos', 'pesanans.id_toko', '=', 'tokos.id_toko')
                ->join('produks', 'pesanans.id_produk', '=', 'produks.id')
                ->select(
                    'pesanans.*', 
                    'tokos.nama_toko', 
                    'produks.nama_makanan',
                    'produks.foto_makanan'
                )
                ->where('pesanans.id_user', $id_user)
                ->orderBy('pesanans.created_at', 'desc')
                ->get();

            return response()->json([
                'status'  => 'success',
                'message' => 'Berhasil mengambil riwayat pesanan',
                'data'    => $riwayat
            ]);

        } catch (\Exception $e) {
            return response()->json([
                'status'  => 'error',
                'message' => 'Error Server: ' . $e->getMessage()
            ], 500);
        }
    }

    // 4. FITUR CHECKOUT & POTONG SALDO FAFAPAY
    public function checkout(Request $request)
    {
        try {
            DB::beginTransaction();

            $user = User::find($request->id_user);
            $produk = \App\Models\Produk::find($request->id_produk);
            $jumlah_pesan = $request->jumlah_pesan;

            if (!$user || !$produk) {
                return response()->json(['status' => 'error', 'message' => 'Data User atau Produk tidak ditemukan'], 404);
            }

            if ($produk->stok < $jumlah_pesan) {
                return response()->json(['status' => 'error', 'message' => 'Mohon maaf, stok makanan tidak mencukupi'], 400);
            }

            $total_harga = $produk->harga_diskon * $jumlah_pesan;

            if ($user->saldo < $total_harga) {
                return response()->json(['status' => 'error', 'message' => 'Saldo FafaPay Anda tidak mencukupi'], 400);
            }

            // Potong saldo Pembeli & Kurangi Stok Sementara
            $user->saldo -= $total_harga;
            $user->save();

            $produk->stok -= $jumlah_pesan;
            $produk->save();

            $pin_acak = 'FAFA-' . rand(1000, 9999);

            $pesanan = new Pesanan();
            $pesanan->id_toko = $produk->id_toko;
            $pesanan->id_user = $user->id; // Akan menunjuk ke ID 4 jika dari Android dikirim 4
            $pesanan->id_produk = $produk->id;
            $pesanan->nomor_order = $pin_acak;
            $pesanan->jumlah_pesan = $jumlah_pesan;
            $pesanan->total_harga = $total_harga;
            $pesanan->status_pesanan = 'disiapkan';
            $pesanan->save();

            DB::commit();

            return response()->json([
                'status' => 'success',
                'message' => 'Pembayaran berhasil! Menunggu konfirmasi restoran.',
                'data' => $pesanan
            ]);

        } catch (\Exception $e) {
            DB::rollBack();
            return response()->json([
                'status' => 'error',
                'message' => 'Terjadi kesalahan sistem: ' . $e->getMessage()
            ], 500);
        }
    }
}