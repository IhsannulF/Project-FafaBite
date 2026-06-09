<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('pesanans', function (Blueprint $table) {
            $table->id();
            // Menyambungkan pesanan ini ke toko mana, pembeli siapa, dan makanan apa
            $table->unsignedBigInteger('id_toko'); // Buat kolomnya dulu
    $table->foreign('id_toko')->references('id_toko')->on('tokos')->onDelete('cascade');
            $table->foreignId('id_user')->constrained('users')->onDelete('cascade'); // Pembeli
            $table->foreignId('id_produk')->constrained('produks')->onDelete('cascade');
            
            // Catatan transaksinya
            $table->string('nomor_order')->unique(); // Misal: FAFA-8892
            $table->integer('jumlah_pesan'); // Beli berapa porsi?
            $table->integer('total_harga'); // Total yang harus dibayar
            
            // Status: menunggu, disiapkan, selesai, batal
            $table->enum('status_pesanan', ['menunggu', 'disiapkan', 'selesai', 'batal'])->default('menunggu');
            
            $table->timestamps(); // Ini PENTING untuk mengecek "Pendapatan HARI INI"
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('pesanans');
    }
};
