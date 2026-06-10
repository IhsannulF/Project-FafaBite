package com.example.fafabite.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fafabite.MainActivity
import com.example.fafabite.R
import com.example.fafabite.adapter.PesananAktifAdapter
import com.example.fafabite.api.ApiConfig
import com.example.fafabite.api.ResponseRiwayatPesanan
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PesananActivity : AppCompatActivity() {

    private lateinit var rvPesananAktif: RecyclerView
    private lateinit var layoutKosong: LinearLayout
    private lateinit var adapter: PesananAktifAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesanan)

        // ==========================================
        // 1. PENGATURAN BOTTOM NAVIGATION
        // ==========================================
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.nav_pesanan

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish() // Menutup halaman agar tidak menumpuk di memori
                    true
                }
                R.id.nav_riwayat -> {
                    startActivity(Intent(this, RiwayatActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_akun -> {
                    startActivity(Intent(this, AkunActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_pesanan -> true // Tetap di sini
                else -> false
            }
        }

        val fabLokasi = findViewById<FloatingActionButton>(R.id.fabCariLokasi)
        fabLokasi.setOnClickListener {
            startActivity(Intent(this, LokasiActivity::class.java))
        }

        // ==========================================
        // 2. PENGATURAN TAMPILAN DATA & ADAPTER
        // ==========================================
        rvPesananAktif = findViewById(R.id.rvPesananAktif)
        layoutKosong = findViewById(R.id.layoutKosong)

        rvPesananAktif.layoutManager = LinearLayoutManager(this)
        adapter = PesananAktifAdapter(listOf())
        rvPesananAktif.adapter = adapter
    }

    // ==========================================
    // 3. AMBIL DATA DINAMIS DENGAN ONRESUME
    // ==========================================
    override fun onResume() {
        super.onResume()

        // Mengambil ID User secara dinamis dari SharedPreferences
        val sharedPref = getSharedPreferences("FafaBitePrefs", Context.MODE_PRIVATE)
        val idUserSaatIni = sharedPref.getInt("ID_USER", 0)

        if (idUserSaatIni != 0) {
            ambilPesananAktif(idUserSaatIni)
        } else {
            Toast.makeText(this, "Sesi login tidak valid, silakan login ulang", Toast.LENGTH_SHORT).show()
            rvPesananAktif.visibility = View.GONE
            layoutKosong.visibility = View.VISIBLE
        }
    }

    private fun ambilPesananAktif(idUser: Int) {
        ApiConfig.getApiService().getRiwayatPesanan(idUser).enqueue(object : Callback<ResponseRiwayatPesanan> {
            override fun onResponse(call: Call<ResponseRiwayatPesanan>, response: Response<ResponseRiwayatPesanan>) {
                if (response.isSuccessful && response.body() != null) {
                    val semuaPesanan = response.body()!!.data

                    // Hanya saring pesanan yang masih berjalan (termasuk siap diambil)
                    val pesananAktif = semuaPesanan.filter {
                        val status = it.statusPesanan.lowercase()
                        status == "menunggu" || status == "disiapkan" || status == "siap_diambil"
                    }

                    // Tampilkan atau Sembunyikan List sesuai hasil
                    if (pesananAktif.isEmpty()) {
                        rvPesananAktif.visibility = View.GONE
                        layoutKosong.visibility = View.VISIBLE
                    } else {
                        rvPesananAktif.visibility = View.VISIBLE
                        layoutKosong.visibility = View.GONE
                        adapter.perbaruiData(pesananAktif)
                    }
                } else {
                    rvPesananAktif.visibility = View.GONE
                    layoutKosong.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<ResponseRiwayatPesanan>, t: Throwable) {
                Toast.makeText(this@PesananActivity, "Error Koneksi: ${t.message}", Toast.LENGTH_SHORT).show()
                rvPesananAktif.visibility = View.GONE
                layoutKosong.visibility = View.VISIBLE
            }
        })
    }
}