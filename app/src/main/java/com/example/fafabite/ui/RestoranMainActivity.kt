package com.example.fafabite.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fafabite.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.switchmaterial.SwitchMaterial

class RestoranMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restoran_main)

        val switchToko = findViewById<SwitchMaterial>(R.id.switchStatusToko)
        val btnTambahMenu = findViewById<Button>(R.id.btnTambahMakanan)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavResto)

        // 1. Logika Switch Toko
        switchToko.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switchToko.text = "Buka "
                Toast.makeText(this, "Toko Anda BUKA", Toast.LENGTH_SHORT).show()
            } else {
                switchToko.text = "Tutup "
                Toast.makeText(this, "Toko Anda TUTUP", Toast.LENGTH_SHORT).show()
            }
        }

        // 2. Logika Quick Action
        btnTambahMenu.setOnClickListener {
            Toast.makeText(this, "Halaman Tambah Makanan Surplus", Toast.LENGTH_SHORT).show()
        }

        // 3. Logika Navbar Restoran
        bottomNav.selectedItemId = R.id.nav_home_resto // Sorot ikon Home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home_resto -> true // Sudah di sini
                R.id.nav_makanan_resto -> {
                    // Pindah ke halaman Menu Makanan
                    startActivity(
                        Intent(
                            this@RestoranMainActivity,
                            MenuRestoranActivity::class.java
                        )
                    )
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.nav_profil_resto -> {
                    startActivity(Intent(this, ProfilRestoranActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_pesanan_resto -> {
                    startActivity(Intent(this@RestoranMainActivity, PesananRestoranActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
    }
}