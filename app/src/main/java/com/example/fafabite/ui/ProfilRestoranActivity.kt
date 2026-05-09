package com.example.fafabite.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fafabite.R
import com.example.fafabite.api.ApiConfig
import com.example.fafabite.api.ResponseProfilToko
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilRestoranActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_restoran)

        // 1. Inisialisasi Views
        val btnTarikDana = findViewById<Button>(R.id.btnTarikDana)
        val menuInfoToko = findViewById<TextView>(R.id.menuInformasiToko)
        val menuJamOps = findViewById<TextView>(R.id.menuJamOperasional)
        val menuBantuan = findViewById<TextView>(R.id.menuPusatBantuan)
        val btnLogout = findViewById<TextView>(R.id.tvLogoutResto)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavResto)

        // 2. Set Ikon Profil di Navbar menyala
        bottomNav.selectedItemId = R.id.nav_profil_resto

        // 3. Ambil data dari SharedPreferences (ID_USER yang disimpan saat Login)
        val sharedPref = getSharedPreferences("FafaBitePrefs", Context.MODE_PRIVATE)
        val idUserLogin = sharedPref.getInt("ID_USER", 0)

        // =========================================================
        // DINAMISASI: Panggil API sesuai dengan ID User yang login
        // =========================================================
        if (idUserLogin != 0) {
            getDataProfil(idUserLogin)
        } else {
            // Jika ID tidak ditemukan, kembalikan ke Login
            Toast.makeText(this, "Sesi berakhir, silakan login kembali", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // 4. Logika Pindah Halaman Navbar
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
                R.id.nav_profil_resto -> true
                else -> false
            }
        }

        // 5. Logika Tombol Aksi
        btnTarikDana.setOnClickListener {
            Toast.makeText(this, "Fitur Penarikan Dana segera hadir!", Toast.LENGTH_SHORT).show()
        }

        // 6. Logika Keluar Akun (Logout) - Sekarang menghapus session
        btnLogout.setOnClickListener {
            // Bersihkan data login dari memori HP
            val editor = sharedPref.edit()
            editor.clear()
            editor.apply()

            Toast.makeText(this, "Berhasil Keluar Akun", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    // ==============================================================
    // FUNGSI UNTUK MENGAMBIL DATA PROFIL DARI LARAVEL
    // ==============================================================
    private fun getDataProfil(idToko: Int) {
        ApiConfig.getApiService().getProfilToko(idToko).enqueue(object : Callback<ResponseProfilToko> {
            override fun onResponse(call: Call<ResponseProfilToko>, response: Response<ResponseProfilToko>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    val dataToko = response.body()?.data

                    if (dataToko != null) {
                        val tvNamaToko = findViewById<TextView>(R.id.tvNamaToko)
                        val tvAlamatToko = findViewById<TextView>(R.id.tvEmailToko)

                        tvNamaToko.text = dataToko.namaToko
                        tvAlamatToko.text = dataToko.alamat
                    }
                } else {
                    Toast.makeText(this@ProfilRestoranActivity, "Gagal memuat profil toko", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseProfilToko>, t: Throwable) {
                Toast.makeText(this@ProfilRestoranActivity, "Koneksi Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}