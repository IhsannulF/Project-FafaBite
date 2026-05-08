package com.example.fafabite.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fafabite.R
import com.example.fafabite.adapter.PesananAdapter
import com.example.fafabite.api.ApiConfig
import com.example.fafabite.api.PesananItem
import com.example.fafabite.api.ResponsePesanan
import com.example.fafabite.api.ResponseUpdateStatus
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PesananRestoranActivity : AppCompatActivity() {

    private lateinit var rvPesanan: RecyclerView
    private lateinit var adapter: PesananAdapter
    private var listPesananFull = ArrayList<PesananItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesanan_restoran)

        // ==========================================
        // 1. LOGIKA NAVBAR BAWAH (Dari Kodingan Lama)
        // ==========================================
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavResto)

        // Set ikon "Pesanan" menyala
        bottomNav.selectedItemId = R.id.nav_pesanan_resto

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
                    startActivity(Intent(this, ProfilRestoranActivity::class.java)) // Pastikan class ini sudah kamu buat ya
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }

        // ==========================================
        // 2. LOGIKA RECYCLERVIEW & ADAPTER BARU
        // ==========================================
        rvPesanan = findViewById(R.id.rvPesanan)
        rvPesanan.layoutManager = LinearLayoutManager(this)

        adapter = PesananAdapter(
            listPesananFull,
            onUpdateStatus = { id, status -> prosesUpdateStatus(id, status) },
            onScanQR = { nomorOrder -> Toast.makeText(this, "Membuka Kamera untuk Scan QR Order $nomorOrder...", Toast.LENGTH_SHORT).show() }
        )
        rvPesanan.adapter = adapter

        // ==========================================
        // 3. AMBIL DATA DARI API LARAVEL
        // ==========================================
        getDataPesanan(1) // Ganti angka 1 dengan ID Toko yang sedang login nanti

        // ==========================================
        // 4. PASANG LOGIKA FILTER TAB
        // ==========================================
        setupFilter()
    }

    private fun getDataPesanan(idToko: Int) {
        ApiConfig.getApiService().getPesananToko(idToko).enqueue(object : Callback<ResponsePesanan> {
            override fun onResponse(call: Call<ResponsePesanan>, response: Response<ResponsePesanan>) {
                if (response.isSuccessful) {
                    val data = response.body()?.data ?: listOf()
                    listPesananFull.clear()
                    listPesananFull.addAll(data)
                    adapter.updateData(listPesananFull) // Tampilkan semua di awal
                }
            }

            override fun onFailure(call: Call<ResponsePesanan>, t: Throwable) {
                Toast.makeText(this@PesananRestoranActivity, "Koneksi Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun prosesUpdateStatus(idPesanan: Int, statusBaru: String) {
        ApiConfig.getApiService().updateStatusPesanan(idPesanan, statusBaru).enqueue(object : Callback<ResponseUpdateStatus> {
            override fun onResponse(call: Call<ResponseUpdateStatus>, response: Response<ResponseUpdateStatus>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@PesananRestoranActivity, "Berhasil diupdate!", Toast.LENGTH_SHORT).show()
                    getDataPesanan(1) // Refresh data agar posisi kartu berpindah otomatis
                }
            }

            override fun onFailure(call: Call<ResponseUpdateStatus>, t: Throwable) {
                Toast.makeText(this@PesananRestoranActivity, "Gagal update status", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupFilter() {
        val tabSemua: TextView = findViewById(R.id.tabSemua)
        val tabMenunggu: TextView = findViewById(R.id.tabMenunggu)
        val tabDiproses: TextView = findViewById(R.id.tabDiproses)

        tabSemua.setOnClickListener {
            updateTabUI(tabSemua, listOf(tabMenunggu, tabDiproses))
            adapter.updateData(listPesananFull)
        }

        tabMenunggu.setOnClickListener {
            updateTabUI(tabMenunggu, listOf(tabSemua, tabDiproses))
            val filter = listPesananFull.filter { it.statusPesanan.lowercase() == "menunggu" }
            adapter.updateData(filter)
        }

        tabDiproses.setOnClickListener {
            updateTabUI(tabDiproses, listOf(tabSemua, tabMenunggu))
            // Menangkap berbagai variasi kata "diproses" di database
            val filter = listPesananFull.filter {
                it.statusPesanan.lowercase() == "disiapkan" ||
                        it.statusPesanan.lowercase() == "proses" ||
                        it.statusPesanan.lowercase() == "diproses"
            }
            adapter.updateData(filter)
        }
    }

    private fun updateTabUI(activeTab: TextView, inactiveTabs: List<TextView>) {
        activeTab.setBackgroundResource(R.drawable.bg_input_pill)
        activeTab.backgroundTintList = getColorStateList(R.color.fafa_blue_primary)
        activeTab.setTextColor(getColor(R.color.white))

        for (tab in inactiveTabs) {
            tab.setBackgroundResource(R.drawable.bg_input_pill)
            tab.backgroundTintList = getColorStateList(R.color.text_grey_light)
            tab.setTextColor(getColor(R.color.text_grey))
        }
    }
}