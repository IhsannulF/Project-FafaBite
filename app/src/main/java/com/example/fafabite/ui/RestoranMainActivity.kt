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

        // 1. Tangkap elemen dari XML
        val btnTambahMakanan = findViewById<Button>(R.id.btnTambahMakanan)
        val switchStatusToko = findViewById<SwitchMaterial>(R.id.switchStatusToko)
        val bottomNavResto = findViewById<BottomNavigationView>(R.id.bottomNavResto)

        // 2. Aksi Tombol Tambah Makanan -> Arahkan ke form Insert
        btnTambahMakanan.setOnClickListener {
            val intent = Intent(this, TambahMakananActivity::class.java)
            startActivity(intent)
        }

        // 3. Aksi Switch Buka/Tutup Toko (Interaktif)
        switchStatusToko.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switchStatusToko.text = "Buka "
                Toast.makeText(this, "Toko Anda sekarang BUKA", Toast.LENGTH_SHORT).show()
            } else {
                switchStatusToko.text = "Tutup "
                Toast.makeText(this, "Toko Anda sekarang TUTUP", Toast.LENGTH_SHORT).show()
            }
        }

        // Menandai ikon 'Dashboard' agar otomatis menyala saat halaman ini dibuka
        bottomNavResto.selectedItemId = R.id.nav_home_resto

        // 4. Aksi Navigasi Bawah (SUDAH DIHUBUNGKAN KE HALAMAN LAIN)
        bottomNavResto.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home_resto -> {
                    // Kita sudah di halaman Dashboard, jadi tidak perlu pindah
                    true
                }

                R.id.nav_makanan_resto -> {
                    // Pindah ke halaman Menu Makanan
                    val intent = Intent(this, MenuRestoranActivity::class.java)
                    startActivity(intent)
                    // Hapus animasi bawaan agar pindah tab terasa natural
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.nav_pesanan_resto -> {
                    // Pindah ke halaman Pesanan
                    val intent = Intent(this, PesananRestoranActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.nav_profil_resto -> {
                    // Pindah ke halaman Profil/Akun Toko
                    val intent = Intent(this, ProfilRestoranActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                }

                else -> false
            }
        }
    }
}