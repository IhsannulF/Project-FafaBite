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
        Schema::create('tokos', function (Blueprint $table) {
            $table->id('id_toko'); // Primary Key khusus
            
            // Relasi ke tabel users (karena toko dimiliki oleh user penjual)
            $table->foreignId('id_user')->references('id')->on('users')->onDelete('cascade');
            
            $table->string('nama_toko');
            $table->text('alamat')->nullable();
            $table->string('latitude')->nullable();
            $table->string('longitude')->nullable();
            $table->time('jam_tutup')->nullable();
            
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('tokos');
    }
};
