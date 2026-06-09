<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\ProdukController;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\DashboardController;
use App\Http\Controllers\Api\PesananController;
use App\Http\Controllers\Api\TokoController;

Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');

// --- ROUTE UNTUK AUTH (LOGIN & REGISTER) ---
Route::post('/register', [AuthController::class, 'register']);
Route::post('/login', [AuthController::class, 'login']);

// --- ROUTE UNTUK PRODUK ---
Route::post('/tambah-produk', [ProdukController::class, 'uploadProduk']);



// --- ROUTE UNTUK DASHBOARD PENJUAL ---
// Pintu masuk ini butuh ID Toko agar datanya tidak tertukar dengan warung lain
Route::get('/dashboard-penjual/{id_toko}', [DashboardController::class, 'getDashboardPenjual']);

// Ambil daftar makanan berdasarkan ID Toko
Route::get('/produk/toko/{id_toko}', [ProdukController::class, 'getProdukByToko']);
Route::get('/beranda/makanan', [ProdukController::class, 'getBerandaMakanan']);

Route::post('/produk/update/{id}', [App\Http\Controllers\ProdukController::class, 'updateProduk']);

// API Pesanan
Route::get('/pesanan/toko/{id_toko}', [PesananController::class, 'getPesananToko']);
Route::post('/pesanan/update-status/{id}', [PesananController::class, 'updateStatus']);

// Route untuk mengambil profil toko
Route::get('/toko/profil/{id_toko}', [TokoController::class, 'getProfil']);

// Rute untuk mengambil riwayat pesanan pembeli
Route::get('/riwayat-pesanan/{id_user}', [App\Http\Controllers\Api\PesananController::class, 'getRiwayatPesanan']);
Route::get('/pesanan-toko/{id_toko}', [App\Http\Controllers\Api\PesananController::class, 'getPesananToko']);

// Rute untuk melakukan pemesanan (Checkout)
Route::post('/checkout', [App\Http\Controllers\Api\PesananController::class, 'checkout']);
Route::post('/pesanan-update/{id}', [App\Http\Controllers\Api\PesananController::class, 'updateStatus']);