package com.example.fafabite.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fafabite.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfilRestoranActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_restoran)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavResto)
        val tvLogout = findViewById<TextView>(R.id.tvLogoutResto)

        // Set ikon "Toko/Profil" menyala
        bottomNav.selectedItemId = R.id.nav_profil_resto

        // Logika Navbar Restoran
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home_resto -> {
                    startActivity(Intent(this, RestoranMainActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_makanan_resto -> {
                    startActivity(Intent(this, MenuRestoranActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_pesanan_resto -> {
                    startActivity(Intent(this, PesananRestoranActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_profil_resto -> true // Tetap di sini
                else -> false
            }
        }

        // Logika Tombol Logout
        tvLogout.setOnClickListener {
            Toast.makeText(this, "Berhasil Keluar dari Toko", Toast.LENGTH_SHORT).show()
            // Kembali ke halaman Login
            val intent = Intent(this, LoginActivity::class.java)
            // Membersihkan history agar saat di-back tidak kembali ke profil restoran
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}