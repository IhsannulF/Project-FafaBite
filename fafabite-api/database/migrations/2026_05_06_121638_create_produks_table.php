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
        Schema::create('produks', function (Blueprint $table) {
            $table->id();
            // id_toko terhubung dengan id di tabel users (karena penjual login pakai akun user)
            $table->foreignId('id_toko')->references('id')->on('users')->onDelete('cascade'); 
            
            $table->string('nama_makanan');
            $table->integer('harga_asli');
            $table->integer('harga_diskon');
            $table->integer('stok');
            $table->dateTime('waktu_pickup'); // Ini yang akan menerima format 2026-05-06 21:00:00
            $table->enum('status', ['tersedia', 'habis'])->default('tersedia');
            $table->string('foto_makanan'); // Menyimpan nama/path file gambar
            
            $table->timestamps();
        });
    }
    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('produks');
    }
};
