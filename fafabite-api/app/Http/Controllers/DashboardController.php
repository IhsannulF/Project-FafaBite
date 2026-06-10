<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Toko;
use App\Models\Produk;
use App\Models\Pesanan;
use Carbon\Carbon;

class DashboardController extends Controller
{
    public function getDashboardPenjual($id_toko)
    {
        try {
            $toko = Toko::find($id_toko);
            $namaToko = $toko ? $toko->nama_toko : 'Toko Tidak Ditemukan';
            
            // 1. menggunakan zona waktu Jakarta (WIB)
            $hariIni = Carbon::now('Asia/Jakarta')->toDateString();

            // Hitung Angka-angka Statistik
            $pendapatanHariIni = Pesanan::where('id_toko', $id_toko)
                ->whereDate('created_at', $hariIni) // Filter pasti akurat ke hari ini
                ->where('status_pesanan', '!=', 'batal')
                ->sum('total_harga');

            $pesananBaru = Pesanan::where('id_toko', $id_toko)
                ->where('status_pesanan', 'menunggu')
                ->count();

            $sisaStok = Produk::where('id_toko', $id_toko)->sum('stok');

            // 2. TAMBAHAN: Agar "Stok Terjual" juga menampilkan angka hari ini saja
            $stokTerjual = Pesanan::where('id_toko', $id_toko)
                ->whereDate('created_at', $hariIni) // Tambahkan filter tanggal di sini juga
                ->where('status_pesanan', '!=', 'batal')
                ->sum('jumlah_pesan');

            // --- BAGIAN BARU: Ambil Daftar Antrean Pesanan "Butuh Perhatian" ---
            $orderButuhPerhatian = Pesanan::with(['user', 'produk'])
                ->where('id_toko', $id_toko)
                ->where('status_pesanan', 'menunggu')
                ->latest() 
                ->take(5)  
                ->get()
                ->map(function ($order) {
                    return [
                        'nomor_order' => $order->nomor_order,
                        'nama_pemesan' => $order->user ? $order->user->name : 'Tanpa Nama',
                        'detail_pesanan' => $order->jumlah_pesan . 'x ' . ($order->produk ? $order->produk->nama_makanan : 'Produk Terhapus'),
                        'status' => $order->status_pesanan
                    ];
                });

            // Bungkus Semua Data
            return response()->json([
                'sukses' => true,
                'data' => [
                    'nama_toko' => $namaToko,
                    'pendapatan_hari_ini' => (int) $pendapatanHariIni,
                    'pesanan_baru' => (int) $pesananBaru,
                    'stok_terjual' => (int) $stokTerjual,
                    'sisa_stok' => (int) $sisaStok,
                    'order_butuh_perhatian' => $orderButuhPerhatian 
                ]
            ]);

        } catch (\Exception $e) {
            return response()->json([
                'sukses' => false,
                'pesan' => 'Error Server: ' . $e->getMessage()
            ]);
        }
    }
}