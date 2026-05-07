package com.example.fafabite.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fafabite.R
import com.example.fafabite.api.ApiConfig
import com.example.fafabite.models.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterRestoranActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_restoran)

        val tvBack = findViewById<TextView>(R.id.tvHeaderResto)

        // Mengambil semua inputan sesuai dengan ID di XML kamu
        val etNama = findViewById<EditText>(R.id.etNamaResto)
        val etEmail = findViewById<EditText>(R.id.etEmailResto)
        val etAlamat = findViewById<EditText>(R.id.etAlamatResto) // <-- Ini tambahan untuk Alamat
        val etPassword = findViewById<EditText>(R.id.etPasswordResto)

        val btnDaftar = findViewById<Button>(R.id.btnRegisterRestoSubmit)
        val tvPindahLogin = findViewById<TextView>(R.id.tvGoToLoginResto)

        // 1. Tombol Kembali (Teks Header)
        tvBack.setOnClickListener {
            finish()
        }

        // 2. Tombol Daftar Mitra Restoran
        btnDaftar.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val alamat = etAlamat.text.toString().trim() // Ambil teks alamat
            val password = etPassword.text.toString().trim()

            // Validasi agar tidak ada kolom yang kosong (termasuk alamat)
            if (nama.isEmpty() || email.isEmpty() || alamat.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua kolom termasuk alamat harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Ubah teks tombol jadi loading
            btnDaftar.text = "Loading..."
            btnDaftar.isEnabled = false

            // 3. Panggil API Register via Retrofit dengan ROLE: "pedagang"
            // CATATAN: Jika di database Laravel kamu ada kolom 'alamat',
            // kamu perlu menambahkan parameter alamat di ApiService.kt nantinya.
            // Sementara ini kita panggil sesuai fungsi di ApiService yang sudah ada.
            ApiConfig.getApiService().registerUser(
                namaLengkap = nama,   // Nama user diisi dengan nama resto
                email = email,
                pass = password,
                role = "pedagang",
                namaToko = nama,      // Nama toko juga diisi dengan nama resto
                alamat = alamat       // Alamat toko
            ).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    // Kembalikan teks tombol
                    btnDaftar.text = "Daftar Menjadi Mitra"
                    btnDaftar.isEnabled = true

                    if (response.isSuccessful && response.body() != null) {
                        val responData = response.body()!!

                        if (responData.sukses) {
                            Toast.makeText(this@RegisterRestoranActivity, "Pendaftaran Mitra Berhasil! Silakan Login.", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@RegisterRestoranActivity, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            // Jika masuk ke sini, berarti Laravel yang sengaja mengirim tulisan "Email sudah digunakan"
                            Toast.makeText(this@RegisterRestoranActivity, "Ditolak Server: ${responData.pesan}", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        // INI YANG PALING PENTING! Jika gagal karena sistem (misal tabel toko belum dibuat), pesan error aslinya akan muncul di sini.
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(this@RegisterRestoranActivity, "Error ${response.code()}: $errorBody", Toast.LENGTH_LONG).show()

                        // Print ke terminal Logcat agar bisa kita baca selengkapnya
                        android.util.Log.e("API_ERROR", "Code: ${response.code()}, Body: $errorBody")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    btnDaftar.text = "Daftar Menjadi Mitra"
                    btnDaftar.isEnabled = true
                    Toast.makeText(this@RegisterRestoranActivity, "Koneksi Gagal: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }

        // 4. Teks Pindah ke Login
        tvPindahLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            // Membersihkan tumpukan halaman agar tidak menumpuk saat di-back
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}