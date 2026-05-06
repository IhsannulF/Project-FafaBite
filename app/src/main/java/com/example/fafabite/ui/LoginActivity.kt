package com.example.fafabite.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fafabite.MainActivity
import com.example.fafabite.R
import com.example.fafabite.api.ApiConfig
import com.example.fafabite.models.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvSignUp = findViewById<TextView>(R.id.tvSignUp)

        // Balik ke Pilih Peran jika ingin daftar akun baru
        tvSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, PilihPeranActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Indikator loading agar user tidak klik berkali-kali
            btnLogin.text = "Loading..."
            btnLogin.isEnabled = false

            // Memanggil API via Retrofit (Pastikan IP Laptop di ApiConfig sudah benar!)
            ApiConfig.getApiService().loginUser(email, password).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    btnLogin.text = "Sign In"
                    btnLogin.isEnabled = true

                    if (response.isSuccessful && response.body() != null) {
                        val loginResponse = response.body()!!

                        if (loginResponse.sukses) {
                            Toast.makeText(this@LoginActivity, "Selamat Datang!", Toast.LENGTH_SHORT).show()

                            // Mengambil peran dari object data di Response API
                            // Pastikan di Model LoginResponse.kt kamu ada field 'role'
                            val roleUser = loginResponse.data?.role

                            // Panggil portal pemisah jalur
                            masukSesuaiPeran(roleUser ?: "")

                        } else {
                            Toast.makeText(this@LoginActivity, loginResponse.pesan, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Error dari Server: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    btnLogin.text = "Sign In"
                    btnLogin.isEnabled = true
                    // Jika ini muncul, cek: 1. IP Laptop, 2. XAMPP/Laragon menyala, 3. 'php artisan serve --host=0.0.0.0'
                    Toast.makeText(this@LoginActivity, "Koneksi Gagal: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    /**
     * FUNGSI PORTAL: Menentukan halaman Dashboard mana yang akan dibuka
     */
    private fun masukSesuaiPeran(role: String) {
        val intent: Intent

        // Sesuaikan string 'konsumen' dan 'pedagang' dengan isi kolom 'role' di tabel users milikmu
        if (role.equals("konsumen", ignoreCase = true)) {
            intent = Intent(this, MainActivity::class.java)
        } else if (role.equals("pedagang", ignoreCase = true)) {
            intent = Intent(this, RestoranMainActivity::class.java)
        } else {
            Toast.makeText(this, "Akses ditolak: Peran tidak dikenali ($role)", Toast.LENGTH_SHORT).show()
            return
        }

        // Hapus history halaman Login agar user tidak bisa 'back' ke sini lagi setelah masuk
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}