package com.example.fafabite.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fafabite.R
import com.example.fafabite.adapter.MakananAdapter
import com.example.fafabite.api.ApiConfig
import com.example.fafabite.api.ResponseListProduk
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuRestoranActivity : AppCompatActivity() {

    private lateinit var rvMakanan: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvMenuKosong: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_restoran)

        rvMakanan = findViewById(R.id.rvMakanan)
        progressBar = findViewById(R.id.progressBarMenu)
        tvMenuKosong = findViewById(R.id.tvMenuKosong)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavResto)
        val fabTambahMenu = findViewById<FloatingActionButton>(R.id.fabTambahMenuResto)

        // Set up RecyclerView
        rvMakanan.layoutManager = LinearLayoutManager(this)

        // Set ikon "Menu" menyala
        bottomNav.selectedItemId = R.id.nav_makanan_resto

        // Logika Navbar Restoran
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home_resto -> {
                    startActivity(Intent(this, RestoranMainActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish() // Tutup halaman ini biar tidak menumpuk
                    true
                }
                R.id.nav_makanan_resto -> true
                R.id.nav_pesanan_resto -> {
                    startActivity(Intent(this@MenuRestoranActivity, PesananRestoranActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_profil_resto -> {
                    startActivity(Intent(this, ProfilRestoranActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                else -> false
            }
        }

        // Tombol Tambah Menu (FAB)
        fabTambahMenu.setOnClickListener {
            // Sekarang tombol ini benar-benar membuka halaman Tambah Makanan!
            startActivity(Intent(this, TambahMakananActivity::class.java))
        }

        // Tarik data makanan saat halaman dibuka
        fetchMenuData()
    }

    // Refresh data otomatis jika kembali dari halaman Tambah Makanan
    override fun onResume() {
        super.onResume()
        fetchMenuData()
    }

    private fun fetchMenuData() {
        // Tampilkan loading
        progressBar.visibility = View.VISIBLE
        rvMakanan.visibility = View.GONE
        tvMenuKosong.visibility = View.GONE

        // Ambil ID User dari dompet seperti di Dashboard
        val sharedPref = getSharedPreferences("FafaBitePrefs", Context.MODE_PRIVATE)
        val idToko = sharedPref.getInt("ID_USER", 1).toString()

        ApiConfig.getApiService().getProdukByToko(idToko).enqueue(object : Callback<ResponseListProduk> {
            override fun onResponse(call: Call<ResponseListProduk>, response: Response<ResponseListProduk>) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body() != null) {
                    val listMakanan = response.body()!!.data

                    if (listMakanan.isNotEmpty()) {
                        // Jika ada data, pasang ke Adapter
                        rvMakanan.visibility = View.VISIBLE
                        val adapter = MakananAdapter(listMakanan)
                        rvMakanan.adapter = adapter
                    } else {
                        // Jika toko belum punya makanan sama sekali
                        tvMenuKosong.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(this@MenuRestoranActivity, "Gagal memuat menu", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseListProduk>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@MenuRestoranActivity, "Error koneksi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}