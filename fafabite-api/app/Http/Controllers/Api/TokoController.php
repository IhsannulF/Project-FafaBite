<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Toko; 
use Illuminate\Http\Request;

class TokoController extends Controller
{
    // Fungsi untuk mengambil data profil toko
    public function getProfil($id_toko)
    {
        // Mencari toko di database berdasarkan id_toko
        $toko = Toko::where('id_toko', $id_toko)->first();

        // Jika ID toko tidak ada di database, kirim pesan error
        if (!$toko) {
            return response()->json([
                'status' => 'error',
                'message' => 'Toko tidak ditemukan'
            ], 404);
        }

        // Jika ketemu, kirimkan data nama_toko dan alamat ke Android
        return response()->json([
            'status' => 'success',
            'data' => $toko
        ]);
    }
}