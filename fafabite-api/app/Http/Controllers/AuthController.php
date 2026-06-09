<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class AuthController extends Controller
{
    // ==========================================
    // FUNGSI REGISTER (Menerima data dari Android)
    // ==========================================
    public function register(Request $request)
    {
        try {
            // Tangkap role yang dikirim dari Android (pembeli / penjual)
            // Jika Android tidak mengirim role, otomatis kita anggap 'pembeli'
            $role = $request->role ? $request->role : 'pembeli';

            // 1. Simpan ke tabel 'users' bawaan Laravel
            $idUser = DB::table('users')->insertGetId([
                // Gunakan nama_toko sebagai nama user jika dia penjual, jika pembeli gunakan nama biasa
                'name'       => $request->nama ?: $request->nama_toko, 
                'email'      => $request->email,
                'password'   => $request->password,
                'role'       => $role, 
                'created_at' => now(),
                'updated_at' => now()
            ]);

            // 2. JIKA DIA PENJUAL, masukkan data tambahannya ke tabel 'tokos'
            if ($role === 'penjual') {
                DB::table('tokos')->insert([
                    'id_user'   => $idUser,
                    'nama_toko' => $request->nama_toko,
                    'alamat'    => $request->alamat,
                    'created_at'=> now(),
                    'updated_at'=> now()
                ]);
            }

            // 3. Beri balikan sukses ke Android
            return response()->json([
                'sukses' => true,
                'pesan'  => 'Pendaftaran ' . ucfirst($role) . ' Berhasil! Silakan Login.'
            ]);

        } catch (\Exception $e) {
            return response()->json([
                'sukses' => false,
                'pesan'  => 'Error Server: ' . $e->getMessage()
            ]);
        }
    }

    // ==========================================
    // FUNGSI LOGIN (Mengecek akun dari Android)
    // ==========================================
    public function login(Request $request)
    {
        $user = DB::table('users')->where('email', $request->email)->first();

        // Cek apakah user ada DAN passwordnya cocok
        if ($user && $user->password == $request->password) {
            
            // Logika $cekToko dihapus karena sekarang kita bisa
            // langsung mengambil 'role' bawaan dari tabel users!

            return response()->json([
                'sukses' => true,
                'pesan'  => 'Login berhasil!',
                'data'   => [
                    'id_user'      => $user->id,
                    'nama_lengkap' => $user->name,
                    'email'        => $user->email,
                    'role'         => $user->role // LANGSUNG DIAMBIL DARI DATABASE
                ]
            ]);

        } else {
            return response()->json([
                'sukses' => false,
                'pesan'  => 'Email atau Password salah!'
            ]);
        }
    }
}