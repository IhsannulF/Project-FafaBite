package com.example.fafabite.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fafabite.MainActivity
import com.example.fafabite.R
import com.google.android.material.bottomnavigation.BottomNavigationView

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
                    true
                }
                R.id.nav_riwayat -> {
                    startActivity(Intent(this, RiwayatActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.nav_akun -> true // Tetap di sini

                R.id.nav_pesanan -> {
                    startActivity(Intent(this, PesananActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }

                else -> false
            }
        }

        // Menyalakan tombol tengah (FAB) Cari Lokasi
        val fabLokasi = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabCariLokasi)
        fabLokasi.setOnClickListener {
            val intent = Intent(this, com.example.fafabite.ui.LokasiActivity::class.java)
            startActivity(intent)
        }

        // 1. Cari tombol atau teks Logout kamu dari desain activity_akun.xml
        // CATATAN: Ganti 'R.id.tvLogout' dengan ID asli tombol logout di desainmu!
        val btnLogout = findViewById<android.view.View>(R.id.tvLogout)

        // 2. Beri perintah saat diklik
        btnLogout.setOnClickListener {
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