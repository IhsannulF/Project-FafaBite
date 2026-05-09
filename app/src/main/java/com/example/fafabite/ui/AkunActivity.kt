package com.example.fafabite.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fafabite.MainActivity
import com.example.fafabite.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AkunActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_akun)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // 1. Nyalakan ikon Akun/Profil
        bottomNav.selectedItemId = R.id.nav_akun

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish() // TAMBAHAN: Tutup halaman ini agar memori HP tidak penuh
                    true
                }
                R.id.nav_riwayat -> {
                    startActivity(Intent(this, RiwayatActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish() // TAMBAHAN
                    true
                }

                R.id.nav_akun -> true // Tetap di sini

                R.id.nav_pesanan -> {
                    startActivity(Intent(this, PesananActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish() // TAMBAHAN
                    true
                }

                else -> false
            }
        }

        // Menyalakan tombol tengah (FAB) Cari Lokasi
        val fabLokasi = findViewById<FloatingActionButton>(R.id.fabCariLokasi)
        fabLokasi.setOnClickListener {
            val intent = Intent(this, LokasiActivity::class.java)
            startActivity(intent)
        }

        // ==========================================
        // LOGIKA LOGOUT YANG SUDAH DIPERBAIKI
        // ==========================================
        val btnLogout = findViewById<View>(R.id.tvLogout)

        btnLogout.setOnClickListener {
            // 1. Buka keranjang SharedPreferences
            val sharedPref = getSharedPreferences("FafaBitePrefs", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()

            // 2. BERSIHKAN SEMUA DATA LOGIN (Ini yang bikin kamu terpental sebelumnya)
            editor.clear()
            editor.apply()

            Toast.makeText(this, "Berhasil Keluar dari Akun", Toast.LENGTH_SHORT).show()

            // 3. Arahkan kembali ke halaman Login
            val intent = Intent(this, LoginActivity::class.java)

            // 4. Bersihkan tumpukan halaman agar tidak bisa di-back masuk lagi ke aplikasi
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}