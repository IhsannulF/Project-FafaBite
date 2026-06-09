<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up()
    {
        Schema::table('users', function (Blueprint $table) {
            // Menambahkan kolom role dengan pilihan 'pembeli' atau 'penjual'
            // Default-nya kita set 'pembeli' agar setiap orang yang daftar otomatis jadi pembeli dulu
            $table->enum('role', ['pembeli', 'penjual'])->default('pembeli')->after('email');
        });
    }

    public function down()
    {
        Schema::table('users', function (Blueprint $table) {
            $table->dropColumn('role');
        });
    }
};