<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;
use App\Models\User;


class UserController extends Controller
{
    // 1. Fungsi Mengambil Data Profil & Saldo
    public function getProfile($id)
    {
        $user = User::find($id);

        if (!$user) {
            return response()->json([
                'status' => 'error',
                'message' => 'User tidak ditemukan'
            ], 404);
        }

        return response()->json([
            'status' => 'success',
            'data' => $user
        ]);
    }


    // 2. Fungsi Update Profil (Nama, Email, Alamat, Foto Profil)
    public function updateProfil(Request $request)
    {
        // 1. Validasi data
        $request->validate([
            'id_user' => 'required|integer',
            'nama' => 'required|string',
            'email' => 'required|email',
            'alamat' => 'nullable|string', // Tambahkan validasi alamat
            'foto' => 'nullable|image|mimes:jpeg,png,jpg|max:2048'
        ]);

        // 2. Cari user berdasarkan ID
        $user = \App\Models\User::find($request->id_user);

        if (!$user) {
            return response()->json([
                'status' => 'error',
                'message' => 'Pengguna tidak ditemukan'
            ], 404);
        }

        // 3. Update data teks untuk tabel users
        $user->name = $request->nama;
        $user->email = $request->email;

        // 4. Logika Upload Foto
        if ($request->hasFile('foto')) {
            if ($user->foto_profil && Storage::exists('public/profil/' . $user->foto_profil)) {
                Storage::delete('public/profil/' . $user->foto_profil);
            }

            $file = $request->file('foto');
            $namaFile = time() . '_' . $file->getClientOriginalName();
            $file->storeAs('public/profil', $namaFile);
            
            $user->foto_profil = $namaFile;
        }

        // 5. Simpan perubahan ke tabel users
        $user->save();

        // ========================================================
        // 6. SINKRONISASI KE TABEL TOKOS (KHUSUS PENJUAL)
        // ========================================================
        if ($user->role === 'penjual') {
            // Samain pencariannya pakai kolom 'id_user' juga
            $toko = \App\Models\Toko::where('id_user', $user->id)->first();
            
            if ($toko) {
                // Samakan nama toko dengan nama profil
                $toko->nama_toko = $request->nama; 
                
                // Jika user mengisi alamat baru, update juga alamat tokonya
                if ($request->filled('alamat')) {
                    $toko->alamat = $request->alamat;
                }

                // Opsional: Jika tabel tokos punya kolom foto, bisa di-update sekalian
                // Tapi karena di tabelmu nggak ada, bagian ini dibiarkan saja
                // $toko->foto_toko = $user->foto_profil;

                $toko->save();
            }
        }

        // 7. Kembalikan respons sukses
        return response()->json([
            'status' => 'success',
            'message' => 'Profil dan data toko berhasil diperbarui',
            'data' => $user
        ], 200);
    }

    // 3. Fungsi Simulasi Top Up Instan
    public function topUpFafaPay(Request $request)
    {
        $request->validate([
            'id_user' => 'required|integer',
            'nominal' => 'required|integer|min:10000'
        ]);

        $user = User::find($request->id_user);

        if (!$user) {
            return response()->json([
                'status' => 'error',
                'message' => 'User tidak ditemukan'
            ], 404);
        }

        // Tambahkan saldo saat ini dengan nominal top up
        $user->saldo = $user->saldo + $request->nominal;
        $user->save();

        return response()->json([
            'status' => 'success',
            'message' => 'Top Up Instan Berhasil!',
            'saldo_baru' => $user->saldo
        ]);
    }

    // 4. Fungsi Tarik Dana (Khusus Penjual)
    public function tarikDana(Request $request)
    {
        $request->validate([
            'id_user' => 'required|integer',
            'nominal' => 'required|integer|min:10000' // Minimal tarik Rp 10.000
        ]);

        $user = \App\Models\User::find($request->id_user);

        if (!$user) {
            return response()->json([
                'status' => 'error',
                'message' => 'User tidak ditemukan'
            ], 404);
        }

        // Cek apakah saldo cukup
        if ($user->saldo < $request->nominal) {
            return response()->json([
                'status' => 'error',
                'message' => 'Saldo tidak mencukupi untuk penarikan ini'
            ], 400); // 400 Bad Request
        }

        // Kurangi saldo saat ini dengan nominal penarikan
        $user->saldo = $user->saldo - $request->nominal;
        $user->save();

        return response()->json([
            'status' => 'success',
            'message' => 'Penarikan Dana Berhasil!',
            'saldo_baru' => $user->saldo
        ]);
    }
}