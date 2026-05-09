package com.example.fafabite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fafabite.adapter.MakananAdapter
import com.example.fafabite.adapter.MakananSekitarAdapter
import com.example.fafabite.api.ApiConfig
import com.example.fafabite.api.ResponseBerandaMakanan
import com.example.fafabite.ui.RiwayatActivity
import com.example.fafabite.ui.AkunActivity
import com.example.fafabite.ui.PesananActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    // 1. Siapkan DUA wadah untuk list makanan
    private lateinit var rvFlashSale: RecyclerView
    private lateinit var rvSekitarKita: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ==========================================================
        // DINAMISASI NAMA PEMBELI DARI SHAREDPREFERENCES
        // ==========================================================
        val sharedPref = getSharedPreferences("FafaBitePrefs", Context.MODE_PRIVATE)
        val namaUser = sharedPref.getString("NAMA_USER", "Pengguna")

        val layoutHeader = findViewById<LinearLayout>(R.id.layoutHeader)
        if (layoutHeader != null && layoutHeader.childCount >= 3) {
            val tvSapaan = layoutHeader.getChildAt(2) as TextView
            tvSapaan.text = "Halo, $namaUser! 👋"
        }

        // ==========================================================
        // PERSIAPAN 2 RECYCLERVIEW (HORIZONTAL & VERTIKAL)
        // ==========================================================
        rvFlashSale = findViewById(R.id.rvFlashSale)
        rvFlashSale.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        rvSekitarKita = findViewById(R.id.rvSekitarKita)
        // LinearLayoutManager tanpa parameter tambahan otomatis menjadi Vertikal (ke bawah)
        rvSekitarKita.layoutManager = LinearLayoutManager(this)

        // Panggil API ke Laravel
        loadFlashSaleMakanan()

        // ==========================================================
        // LOGIKA NAVIGASI BAWAH
        // ==========================================================
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.nav_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_riwayat -> {
                    startActivity(Intent(this, RiwayatActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
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

        val fabLokasi = findViewById<FloatingActionButton>(R.id.fabCariLokasi)
        fabLokasi.setOnClickListener {
            val intent = Intent(this, com.example.fafabite.ui.LokasiActivity::class.java)
            startActivity(intent)
        }
    }

    // ==========================================================
    // FUNGSI AMBIL DATA DARI LARAVEL (PISAH 2 LIST)
    // ==========================================================
    private fun loadFlashSaleMakanan() {
        Toast.makeText(this, "Sedang memuat makanan...", Toast.LENGTH_SHORT).show()

        ApiConfig.getApiService().getBerandaMakanan().enqueue(object : Callback<ResponseBerandaMakanan> {
            override fun onResponse(call: Call<ResponseBerandaMakanan>, response: Response<ResponseBerandaMakanan>) {
                if (response.isSuccessful && response.body() != null) {
                    val responData = response.body()!!

                    if (responData.sukses && responData.data != null) {
                        // Ambil data yang sudah dipisah oleh Laravel
                        val dataFlashSale = responData.data.flashSale
                        val dataSekitar = responData.data.sekitarKita

                        Toast.makeText(this@MainActivity, "Sukses! Flash: ${dataFlashSale.size}, Sekitar: ${dataSekitar.size}", Toast.LENGTH_SHORT).show()

                        // Pasang Flash Sale (Menyamping)
                        val adapterFlash = MakananAdapter(dataFlashSale)
                        rvFlashSale.adapter = adapterFlash

                        // Pasang Sekitar Kita (Memanjang ke bawah)
                        val adapterSekitar = MakananSekitarAdapter(dataSekitar)
                        rvSekitarKita.adapter = adapterSekitar

                    } else {
                        Toast.makeText(this@MainActivity, "Data kosong", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Server Error: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBerandaMakanan>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Gagal Koneksi: ${t.message}", Toast.LENGTH_LONG).show()
                Log.e("API_BERANDA_ERROR", "Error detail: ${t.message}")
            }
        })
    }
}