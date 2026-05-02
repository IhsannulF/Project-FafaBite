package com.example.fafabite

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fafabite.ui.RiwayatActivity
import com.example.fafabite.ui.AkunActivity
import com.example.fafabite.ui.PesananActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Cari elemen Navbar dari desain activity_main.xml
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // 1. Pastikan ikon Home menyala saat di halaman ini
        bottomNav.selectedItemId = R.id.nav_home

        // 2. Berikan perintah saat ikon navbar ditekan
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Sudah di Home, jadi biarkan saja (tidak ngapa-ngapain)
                    true
                }
                R.id.nav_riwayat -> {
                    // Pindah ke halaman Riwayat
                    startActivity(Intent(this, RiwayatActivity::class.java))
                    overridePendingTransition(0, 0) // Agar transisi mulus tanpa loading
                    true
                }
                R.id.nav_akun -> {
                    // Pindah ke halaman Akun/Profil
                    startActivity(Intent(this, AkunActivity::class.java))
                    overridePendingTransition(0, 0) // Agar transisi mulus
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