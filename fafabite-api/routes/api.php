<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

// --- IMPORT SEMUA CONTROLLER DI SINI ---
use App\Http\Controllers\AuthController;
use App\Http\Controllers\ProdukController;
use App\Http\Controllers\DashboardController;
use App\Http\Controllers\Api\PesananController;
use App\Http\Controllers\Api\TokoController;
use App\Http\Controllers\Api\UserController; // Pastikan ini di-import juga

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
*/

// Rute bawaan Laravel Sanctum untuk mengecek data user yang sedang login dengan token
Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');


// ==========================================
// 1. ROUTE AUTENTIKASI (LOGIN & REGISTER)
// ==========================================

// Mendaftarkan akun baru (Pembeli/Penjual) ke dalam database MySQL
Route::post('/register', [AuthController::class, 'register']);

// Mencocokkan email & password pengguna saat mencoba masuk ke aplikasi
Route::post('/login', [AuthController::class, 'login']);


// ==========================================
// 2. ROUTE PRODUK / MAKANAN
// ==========================================

// Mengambil semua daftar makanan dari berbagai toko untuk ditampilkan di halaman Beranda Pembeli
Route::get('/beranda/makanan', [ProdukController::class, 'getBerandaMakanan']);

// Mengambil daftar makanan spesifik milik satu toko (Ditampilkan di halaman Menu Restoran)
Route::get('/produk/toko/{id_toko}', [ProdukController::class, 'getProdukByToko']);

// Menyimpan menu makanan baru beserta fotonya yang ditambahkan oleh penjual
Route::post('/tambah-produk', [ProdukController::class, 'uploadProduk']);

// Memperbarui (edit) data nama, harga, stok, atau foto makanan yang sudah ada
Route::post('/produk/update/{id}', [ProdukController::class, 'updateProduk']);

// Menghapus menu makanan secara permanen dari database
Route::delete('/produk/{id}', [ProdukController::class, 'destroy']);


// ==========================================
// 3. ROUTE TRANSAKSI & PESANAN
// ==========================================

// Mengeksekusi pembayaran, memotong saldo FafaPay, dan membuat riwayat pesanan baru
Route::post('/checkout', [PesananController::class, 'checkout']);

// Mengambil daftar pesanan yang masuk secara khusus ke toko penjual tertentu
Route::get('/pesanan/toko/{id_toko}', [PesananController::class, 'getPesananToko']);

// Mengambil daftar riwayat pesanan (jajan) yang pernah dilakukan oleh seorang pembeli
Route::get('/riwayat-pesanan/{id_user}', [PesananController::class, 'getRiwayatPesanan']);

// Bagi penjual untuk mengubah status pesanan (contoh: dari 'Menunggu' menjadi 'Disiapkan' atau 'Selesai')
Route::post('/pesanan/update-status/{id}', [PesananController::class, 'updateStatus']);


// ==========================================
// 4. ROUTE DASHBOARD, PROFIL & KEUANGAN
// ==========================================

// Mengambil ringkasan data statistik (Total Pendapatan, Total Pesanan, dll) untuk beranda penjual
Route::get('/dashboard-penjual/{id_toko}', [DashboardController::class, 'getDashboardPenjual']);

// Mengambil data spesifik toko (seperti Nama Toko dan Alamat Toko)
Route::get('/toko/profil/{id_toko}', [TokoController::class, 'getProfil']);

// Mengambil data profil user secara umum (Nama Lengkap, Email, dan sisa Saldo FafaPay)
Route::get('/user/{id}', [UserController::class, 'getProfile']);
Route::get('user/{id}', [UserController::class, 'getProfile']);
Route::get('toko/profil/{id_toko}', [TokoController::class, 'getProfil']);

// Memproses penambahan nominal uang ke dalam saldo dompet FafaPay pengguna
Route::post('/topup', [UserController::class, 'topUpFafaPay']);

// Menerima data nama baru, email baru, dan file foto profil untuk disimpan ke database
Route::post('/update-profil', [UserController::class, 'updateProfil']);

// Rute untuk penarikan dana penjual
Route::post('/tarik-dana', [UserController::class, 'tarikDana']);