<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Pesanan extends Model
{
    use HasFactory;

    protected $guarded = ['id']; 

    // --- TAMBAHKAN DUA FUNGSI INI ---
    // Menyambungkan ID User ke tabel users untuk mengambil nama pembeli
    public function user()
    {
        return $this->belongsTo(User::class, 'id_user', 'id');
    }

    // Menyambungkan ID Produk ke tabel produks untuk mengambil nama makanan
    public function produk()
    {
        return $this->belongsTo(Produk::class, 'id_produk', 'id');
    }
}