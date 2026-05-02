package com.example.fafabite.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fafabite.R

class RegisterRestoranActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_restoran)

        val btnDaftar = findViewById<Button>(R.id.btnRegisterRestoSubmit)
        val tvPindahLogin = findViewById<TextView>(R.id.tvGoToLoginResto)
        val tvBack = findViewById<TextView>(R.id.tvHeaderResto)

        // 1. Tombol Kembali (Teks Header)
        tvBack.setOnClickListener {
            finish()
        }

        // 3. Teks Pindah ke Login
        tvPindahLogin.setOnClickListener {
            // 2. Tombol Daftar Mitra
            val intent = Intent(this, LoginActivity::class.java)
            // Membersihkan tumpukan halaman agar tidak menumpuk saat di-back
           intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }
}