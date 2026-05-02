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

        // Beri perintah pindah halaman saat "Sign up" diklik
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

            btnLogin.text = "Loading..."
            btnLogin.isEnabled = false

            // Memanggil API via Retrofit
            ApiConfig.getApiService().loginUser(email, password).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    btnLogin.text = "Sign In"
                    btnLogin.isEnabled = true

                    if (response.isSuccessful && response.body() != null) {
                        val loginResponse = response.body()!!

                        if (loginResponse.sukses) {
                            Toast.makeText(this@LoginActivity, loginResponse.pesan, Toast.LENGTH_SHORT).show()

                            // =========================================================
                            // AMBIL ROLE DARI API DAN LEMPAR KE FUNGSI PORTAL
                            // =========================================================
                            // CATATAN: Ganti 'loginResponse.role' di bawah ini sesuai dengan
                            // struktur data class LoginResponse kamu.
                            // Bisa jadi 'loginResponse.data.role' atau 'loginResponse.user.role'
                            val roleUser = loginResponse.data?.role

                            // Panggil fungsi pembagi jalur
                            masukSesuaiPeran(roleUser ?: "")

                        } else {
                            Toast.makeText(this@LoginActivity, loginResponse.pesan, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    btnLogin.text = "Sign In"
                    btnLogin.isEnabled = true
                    Toast.makeText(this@LoginActivity, "Koneksi Gagal: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    // ==========================================
    // FUNGSI PORTAL MASUK (PEMBAGI JALUR)
    // ==========================================
    private fun masukSesuaiPeran(role: String) {
        val intent: Intent

        // Cek isi string role dari database/API (pastikan huruf kecil/besarnya sama dengan di database)
        if (role.equals("konsumen", ignoreCase = true)) {
            intent = Intent(this, MainActivity::class.java)
        } else if (role.equals("pedagang", ignoreCase = true)) {
            intent = Intent(this, RestoranMainActivity::class.java)
        } else {
            // Jika role tidak dikenali atau kosong
            Toast.makeText(this, "Role user tidak valid ($role)", Toast.LENGTH_SHORT).show()
            return
        }

        // Bersihkan tumpukan halaman agar saat di-back tidak kembali ke halaman Login
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}