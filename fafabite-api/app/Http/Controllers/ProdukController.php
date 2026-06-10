<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Produk;
use Illuminate\Support\Facades\DB; // Tambahkan ini untuk fungsi join
use Illuminate\Support\Facades\Validator;

class ProdukController extends Controller
{
    // ==========================================
    // 1. FITUR UPLOAD PRODUK (Oleh Penjual)
    // ==========================================
    public function uploadProduk(Request $request)
    {
        try {
            $validator = Validator::make($request->all(), [
                'id_toko'      => 'required',
                'nama_makanan' => 'required',
                'harga_asli'   => 'required',
                'harga_diskon' => 'required',
                'stok'         => 'required',
                'waktu_pickup' => 'required',
                'status'       => 'required',
                'foto_makanan' => 'required|image|max:6120', 
            ]);

            if ($validator->fails()) {
                return response()->json([
                    'sukses' => false,
                    'pesan'  => 'Data tidak lengkap: ' . $validator->errors()->first()
                ]);
            }

            if (!$request->hasFile('foto_makanan')) {
                return response()->json([
                    'sukses' => false,
                    'pesan'  => 'Oops, file foto tidak terbaca oleh server.'
                ]);
            }

            // --- BAGIAN YANG DIUBAH MULAI DARI SINI ---
            $file = $request->file('foto_makanan');
            
            // Bikin nama file unik beserta ekstensi aslinya (contoh: 171829392.jpg)
            $namaFile = time() . '.' . $file->getClientOriginalExtension(); 
            
            // Pindahkan file LANGSUNG ke folder public/file-makanan
            $file->move(public_path('file-makanan'), $namaFile);
            // --- BAGIAN YANG DIUBAH SELESAI ---

            $produk = Produk::create([
                'id_toko'      => $request->id_toko,
                'nama_makanan' => $request->nama_makanan,
                'harga_asli'   => $request->harga_asli,
                'harga_diskon' => $request->harga_diskon,
                'stok'         => $request->stok,
                'waktu_pickup' => $request->waktu_pickup,
                'status'       => $request->status,
                'foto_makanan' => $namaFile, // Simpan nama filenya saja agar rapi
            ]);

            return response()->json([
                'sukses' => true,
                'pesan'  => 'Mantap! Makanan berhasil ditambahkan.',
                'data'   => $produk
            ]);

        } catch (\Exception $e) {
            return response()->json([
                'sukses' => false,
                'pesan'  => 'Error Server: ' . $e->getMessage()
            ]);
        }
    }

    // ==========================================
    // 2. FITUR AMBIL PRODUK PER TOKO (Menu Resto)
    // ==========================================
    public function getProdukByToko($id_toko)
    {
        try {

        // --- JALANKAN SAPU BERSIH DULU ---
            $this->bersihkanDataKedaluwarsa();
            
            $produk = Produk::where('id_toko', $id_toko)
                            ->orderBy('created_at', 'desc')
                            ->get();

            return response()->json([
                'sukses' => true,
                'pesan'  => 'Berhasil mengambil data produk toko',
                'data'   => $produk
            ]);
            
        } catch (\Exception $e) {
            return response()->json([
                'sukses' => false,
                'pesan'  => 'Error Server: ' . $e->getMessage()
            ]);
        }
    }

    // ==========================================
    // 3. FITUR BERANDA PEMBELI (Pisah Flash Sale & Sekitar)
    // ==========================================
    public function getBerandaMakanan()
    {
        try {

        // --- JALANKAN SAPU BERSIH DULU MAKANAN KADULUWARSA ---
            $this->bersihkanDataKedaluwarsa();


            // Ambil waktu saat ini dan 2 hari ke depan
            $sekarang = \Carbon\Carbon::now();
            $duaHariLagi = \Carbon\Carbon::now()->addDays(2);

            // A. FLASH SALE: Makanan yang waktu_pickup-nya sisa 2 hari lagi
            $flashSale = DB::table('produks')
                ->join('tokos', 'produks.id_toko', '=', 'tokos.id_toko')
                ->select('produks.*', 'tokos.nama_toko')
                ->where('produks.stok', '>', 0)
                ->whereBetween('produks.waktu_pickup', [$sekarang, $duaHariLagi])
                ->orderBy('produks.waktu_pickup', 'asc') // Urutkan dari yang paling mau habis
                ->get();

            // B. SEKITAR KITA: Semua makanan (untuk sekarang kita anggap semua di Sidoarjo)
            $sekitarKita = DB::table('produks')
                ->join('tokos', 'produks.id_toko', '=', 'tokos.id_toko')
                ->select('produks.*', 'tokos.nama_toko')
                ->where('produks.stok', '>', 0)
                ->orderBy('produks.created_at', 'desc') // Urutkan dari yang paling baru diupload
                ->get();

            return response()->json([
                'sukses' => true,
                'pesan'  => 'Data beranda berhasil dimuat',
                'data'   => [
                    'flash_sale' => $flashSale,
                    'sekitar_kita' => $sekitarKita
                ]
            ]);

        } catch (\Exception $e) {
            return response()->json([
                'sukses' => false,
                'pesan'  => 'Error Server: ' . $e->getMessage()
            ]);
        }
    }

    // ==========================================
    // 4. FITUR EDIT PRODUK (Oleh Penjual)
    // ==========================================
    public function updateProduk(Request $request, $id)
    {
        try {
            $produk = Produk::find($id);

            if (!$produk) {
                return response()->json([
                    'sukses' => false,
                    'pesan'  => 'Makanan tidak ditemukan.'
                ]);
            }

            // Update data teks (Gunakan input baru, atau tetapkan data lama jika kosong)
            $produk->nama_makanan = $request->input('nama_makanan', $produk->nama_makanan);
            $produk->harga_asli   = $request->input('harga_asli', $produk->harga_asli);
            $produk->harga_diskon = $request->input('harga_diskon', $produk->harga_diskon);
            $produk->stok         = $request->input('stok', $produk->stok);
            $produk->waktu_pickup = $request->input('waktu_pickup', $produk->waktu_pickup);
            $produk->status       = $request->input('status', $produk->status);

            // Jika ada upload foto baru, timpa foto yang lama
            if ($request->hasFile('foto_makanan')) {
                $file = $request->file('foto_makanan');
                $namaFile = time() . '.' . $file->getClientOriginalExtension(); 
                
                // Pindahkan file LANGSUNG ke folder public/file-makanan
                $file->move(public_path('file-makanan'), $namaFile);
                
                // Simpan nama file yang baru ke database
                $produk->foto_makanan = $namaFile;
            }

            // Simpan perubahan ke database
            $produk->save();

            return response()->json([
                'sukses' => true,
                'pesan'  => 'Mantap! Data makanan berhasil diupdate.',
                'data'   => $produk
            ]);

        } catch (\Exception $e) {
            return response()->json([
                'sukses' => false,
                'pesan'  => 'Error Server: ' . $e->getMessage()
            ]);
        }
    }

    // ==========================================
    // 5. FITUR OTOMATIS: SAPU BERSIH MAKANAN KEDALUWARSA
    // ==========================================
    private function bersihkanDataKedaluwarsa()
    {
        $sekarang = \Carbon\Carbon::now();

        // Cari semua makanan yang jam pickup-nya sudah kelewat batas waktu sekarang
        $makananBasi = Produk::where('waktu_pickup', '<', $sekarang)->get();

        foreach ($makananBasi as $item) {
            // 1. Hapus file foto fisik dari folder public
            $pathFoto = public_path('file-makanan/' . $item->foto_makanan);
            if (file_exists($pathFoto) && !empty($item->foto_makanan)) {
                unlink($pathFoto); // Perintah hapus file
            }

            // 2. Hapus datanya dari database MySQL
            $item->delete();
        }
    }

    // ==========================================
    // 6. FITUR HAPUS DATA MAKANAN
    // ==========================================
    public function destroy($id)
    {
        $produk = \App\Models\Produk::find($id);

        if (!$produk) {
            return response()->json([
                'status' => 'error',
                'message' => 'Data makanan tidak ditemukan'
            ], 404);
        }

        // Hapus data dari database
        $produk->delete();

        return response()->json([
            'status' => 'success',
            'message' => 'Menu makanan berhasil dihapus'
        ]);
    }

}