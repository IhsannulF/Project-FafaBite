package com.example.fafabite.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fafabite.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class PesananRestoranActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesanan_restoran)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavResto)
        val btnTerima = findViewById<Button>(R.id.btnTerimaPesanan)
        val btnScanQR = findViewById<Button>(R.id.btnScanQR)

        // Set ikon "Pesanan" menyala
        bottomNav.selectedItemId = R.id.nav_pesanan_resto

        // Logika Navbar
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
                R.id.nav_pesanan_resto -> true // Tetap di sini

                R.id.nav_profil_resto -> {
                    startActivity(Intent(this, ProfilRestoranActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }

        // Logika Tombol Aksi Pesanan
        btnTerima?.setOnClickListener {
            Toast.makeText(this, "Pesanan Diterima! Pindahkan ke tab Diproses.", Toast.LENGTH_SHORT).show()
        }

        btnScanQR?.setOnClickListener {
            Toast.makeText(this, "Membuka Kamera untuk Scan QR...", Toast.LENGTH_SHORT).show()
        }
    }
}