<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Produk extends Model
{
    use HasFactory;

    // Tabel yang digunakan
    protected $table = 'produks';

    // Kolom yang boleh diisi dari Android
    protected $fillable = [
        'id_toko',
        'nama_makanan',
        'harga_asli',
        'harga_diskon',
        'stok',
        'waktu_pickup',
        'status',
        'foto_makanan',
    ];
}