<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Toko extends Model
{
    use HasFactory;

    // Supaya Laravel tahu nama tabel dan primary key-nya
    protected $table = 'tokos';
    protected $primaryKey = 'id_toko';

    protected $fillable = [
        'id_user',
        'nama_toko',
        'alamat',
        'latitude',
        'longitude',
        'jam_tutup',
        'saldo'
    ];
}