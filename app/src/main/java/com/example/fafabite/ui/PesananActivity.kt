package com.example.fafabite.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fafabite.MainActivity
import com.example.fafabite.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class PesananActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesanan)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // 1. Nyalakan ikon Pesanan
        bottomNav.selectedItemId = R.id.nav_pesanan

        // 2. Logika navigasi ke halaman lain
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
                R.id.nav_pesanan -> true // Tetap di sini

                R.id.nav_akun -> {
                    startActivity(Intent(this, AkunActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
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
    }
}