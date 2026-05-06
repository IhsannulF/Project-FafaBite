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

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // 1. Tangkap elemen dari desain XML sesuai ID yang baru
        val tvHeaderRegister = findViewById<TextView>(R.id.tvHeaderRegister)
        val etName = findViewById<EditText>(R.id.etName)
        val etEmailReg = findViewById<EditText>(R.id.etEmailReg)
        val etPasswordReg = findViewById<EditText>(R.id.etPasswordReg)

        val btnRegister = findViewById<Button>(R.id.btnRegisterSubmit)
        val tvPindahLogin = findViewById<TextView>(R.id.tvGoToLogin)

        // 2. Aksi untuk tombol Back (Kembali) di Header
        tvHeaderRegister.setOnClickListener {
            finish()
        }

        // 3. Perintah saat tombol "Create Account" diklik
        btnRegister.setOnClickListener {
            val nama = etName.text.toString().trim()
            val email = etEmailReg.text.toString().trim()
            val password = etPasswordReg.text.toString().trim()

            // Validasi agar tidak ada kolom yang kosong
            if (nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Ubah teks tombol jadi loading
            btnRegister.text = "Loading..."
            btnRegister.isEnabled = false

            // 4. Panggil API Register via Retrofit (Peran: konsumen)
            ApiConfig.getApiService().registerUser(
                namaLengkap = nama,
                email = email,
                pass = password,
                role = "konsumen",
                namaToko = "-", // Kosongkan karena pembeli tidak punya toko
                alamat = "-"    // Kosongkan karena pembeli tidak punya alamat toko
            ).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    // Kembalikan teks tombol
                    btnRegister.text = "Create Account"
                    btnRegister.isEnabled = true

                    if (response.isSuccessful && response.body() != null) {
                        val responData = response.body()!!

                        if (responData.sukses) {
                            Toast.makeText(this@RegisterActivity, "Pendaftaran Berhasil! Silakan Login.", Toast.LENGTH_SHORT).show()

                            // Jika sukses, arahkan ke LoginActivity dan bersihkan riwayat halaman
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@RegisterActivity, responData.pesan, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@RegisterActivity, "Gagal mendaftar, periksa kembali data Anda.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    btnRegister.text = "Create Account"
                    btnRegister.isEnabled = true
                    Toast.makeText(this@RegisterActivity, "Koneksi Gagal: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }

        // 5. Perintah saat teks "Sign in" diklik
        tvPindahLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}