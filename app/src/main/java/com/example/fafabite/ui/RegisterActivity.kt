package com.example.fafabite.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fafabite.R

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // 1. Tangkap tombol dari desain XML
        val btnRegister = findViewById<Button>(R.id.btnRegisterSubmit)
        val tvPindahLogin = findViewById<TextView>(R.id.tvGoToLogin)

        // 2. Perintah saat tombol "Daftar" diklik
        btnRegister.setOnClickListener {
            // TODO: Nanti di sini kita masukkan kode Retrofit API (register.php)
            // Untuk sementara, kita munculkan pesan Popup (Toast)
            Toast.makeText(this, "Tombol Register ditekan! Siap disambung ke Database.", Toast.LENGTH_SHORT).show()
        }

        // 3. Perintah saat teks "Sudah punya akun?" diklik
        tvPindahLogin.setOnClickListener {
            // Pindah ke halaman Login
            val intent = Intent(this, LoginActivity::class.java) // Ganti LoginActivity jika nama filemu berbeda
            startActivity(intent)
            finish() // Tutup halaman register agar tidak menumpuk
        }
    }
}