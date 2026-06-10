package com.example.fafabite.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fafabite.MainActivity
import com.example.fafabite.R
import com.example.fafabite.adapter.RiwayatPesananAdapter
import com.example.fafabite.api.ApiConfig
import com.example.fafabite.api.ResponseRiwayatPesanan
import com.example.fafabite.api.RiwayatPesananItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatActivity : AppCompatActivity() {

    // Variabel untuk RecyclerView dan Data
    private lateinit var rvRiwayat: RecyclerView
    private lateinit var adapter: RiwayatPesananAdapter
    private var listRiwayatAsli: List<RiwayatPesananItem> = listOf()

    // Variabel untuk Tombol Filter
    private lateinit var btnSemua: TextView
    private lateinit var btnSelesai: TextView
    private lateinit var btnDiproses: TextView
    private lateinit var btnSiap: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)

        // ==========================================
        // 1. PENGATURAN BOTTOM NAVIGATION & FAB
        // ==========================================
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.nav_riwayat

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_riwayat -> true
                R.id.nav_akun -> {
                    startActivity(Intent(this, AkunActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_pesanan -> {
                    startActivity(Intent(this, PesananActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                else -> false
            }
        }

        val fabLokasi = findViewById<FloatingActionButton>(R.id.fabCariLokasi)
        fabLokasi.setOnClickListener {
            val intent = Intent(this, LokasiActivity::class.java)
            startActivity(intent)
        }

        // ==========================================
        // 2. PENGATURAN RECYCLERVIEW & FILTER
        // ==========================================
        rvRiwayat = findViewById(R.id.rvRiwayatPesanan)
        rvRiwayat.layoutManager = LinearLayoutManager(this)

        btnSemua = findViewById(R.id.btnFilterSemua)
        btnSelesai = findViewById(R.id.btnFilterSelesai)
        btnDiproses = findViewById(R.id.btnFilterDiproses)
        btnSiap = findViewById(R.id.btnFilterSiap)

        // Siapkan Adapter awal
        adapter = RiwayatPesananAdapter(listRiwayatAsli)
        rvRiwayat.adapter = adapter

        // ==========================================
        // 3. LOGIKA KLIK TOMBOL FILTER
        // ==========================================
        btnSemua.setOnClickListener {
            ubahWarnaTombol(btnSemua)
            adapter.perbaruiData(listRiwayatAsli)
        }

        btnDiproses.setOnClickListener {
            ubahWarnaTombol(btnDiproses)
            val dataFilter = listRiwayatAsli.filter {
                it.statusPesanan.equals("menunggu", ignoreCase = true) ||
                        it.statusPesanan.equals("disiapkan", ignoreCase = true)
            }
            adapter.perbaruiData(dataFilter)
        }

        btnSiap.setOnClickListener {
            ubahWarnaTombol(btnSiap)
            val dataFilter = listRiwayatAsli.filter {
                it.statusPesanan.equals("siap_diambil", ignoreCase = true)
            }
            adapter.perbaruiData(dataFilter)
        }

        btnSelesai.setOnClickListener {
            ubahWarnaTombol(btnSelesai)
            val dataFilter = listRiwayatAsli.filter {
                it.statusPesanan.equals("selesai", ignoreCase = true) ||
                        it.statusPesanan.equals("batal", ignoreCase = true)
            }
            adapter.perbaruiData(dataFilter)
        }
    }

    // ==========================================
    // 4. AMBIL DATA DINAMIS DENGAN ONRESUME
    // ==========================================
    override fun onResume() {
        super.onResume()

        // Mengambil ID User secara dinamis dari SharedPreferences
        val sharedPref = getSharedPreferences("FafaBitePrefs", Context.MODE_PRIVATE)
        val idUserSaatIni = sharedPref.getInt("ID_USER", 0)

        if (idUserSaatIni != 0) {
            ambilDataRiwayat(idUserSaatIni)
        } else {
            Toast.makeText(this, "Sesi login berakhir, silakan login ulang", Toast.LENGTH_SHORT).show()
        }
    }

    private fun ambilDataRiwayat(idUser: Int) {
        ApiConfig.getApiService().getRiwayatPesanan(idUser).enqueue(object : Callback<ResponseRiwayatPesanan> {
            override fun onResponse(call: Call<ResponseRiwayatPesanan>, response: Response<ResponseRiwayatPesanan>) {
                if (response.isSuccessful && response.body() != null) {
                    listRiwayatAsli = response.body()!!.data

                    // Default menampilkan semua data saat pertama kali dimuat
                    ubahWarnaTombol(btnSemua)
                    adapter.perbaruiData(listRiwayatAsli)
                } else {
                    Toast.makeText(this@RiwayatActivity, "Belum ada riwayat pesanan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseRiwayatPesanan>, t: Throwable) {
                Toast.makeText(this@RiwayatActivity, "Gagal koneksi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun ubahWarnaTombol(tombolAktif: TextView) {
        val warnaTidakAktif = Color.parseColor("#33FFFFFF")
        val warnaTeksTidakAktif = Color.WHITE
        val warnaAktifBg = Color.WHITE
        val warnaAktifTeks = ContextCompat.getColor(this, R.color.fafa_blue_primary)

        val daftarTombol = listOf(btnSemua, btnSelesai, btnDiproses, btnSiap)
        for (btn in daftarTombol) {
            btn.backgroundTintList = android.content.res.ColorStateList.valueOf(warnaTidakAktif)
            btn.setTextColor(warnaTeksTidakAktif)
        }

        tombolAktif.backgroundTintList = android.content.res.ColorStateList.valueOf(warnaAktifBg)
        tombolAktif.setTextColor(warnaAktifTeks)
    }
}