package com.example.fafabite.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fafabite.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MenuRestoranActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_restoran)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavResto)
        val fabTambahMenu = findViewById<FloatingActionButton>(R.id.fabTambahMenuResto)

        // Set ikon "Menu" menyala
        bottomNav.selectedItemId = R.id.nav_makanan_resto

        // Logika Navbar Restoran
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home_resto -> {
                    startActivity(Intent(this, RestoranMainActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_makanan_resto -> true // Sudah di sini
                R.id.nav_pesanan_resto -> {
                    startActivity(Intent(this@MenuRestoranActivity, PesananRestoranActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_profil_resto -> {
                    startActivity(Intent(this, ProfilRestoranActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }

        // Tombol Tambah Menu (FAB)
        fabTambahMenu.setOnClickListener {
            Toast.makeText(this, "Buka Form Tambah Makanan", Toast.LENGTH_SHORT).show()
            // Nanti arahkan ke form: startActivity(Intent(this, TambahMakananActivity::class.java))
        }
    }
}