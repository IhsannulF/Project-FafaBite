package com.example.fafabite.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.fafabite.R
import com.example.fafabite.api.ApiConfig
import com.example.fafabite.api.DataDashboard
import com.example.fafabite.api.ResponseDashboard
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.switchmaterial.SwitchMaterial
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*

class RestoranMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restoran_main)

        // ========================================================
        // 1. TANGKAP ELEMEN DARI XML
        // ========================================================
        val btnTambahMakanan = findViewById<Button>(R.id.btnTambahMakanan)
        val switchStatusToko = findViewById<SwitchMaterial>(R.id.switchStatusToko)
        val bottomNavResto = findViewById<BottomNavigationView>(R.id.bottomNavResto)
        val tvNamaTokoHeader = findViewById<TextView>(R.id.tvNamaTokoHeader)

        // ========================================================
        // 2. TAMPILKAN NAMA TOKO SEMENTARA (DARI SHARED PREF)
        // ========================================================
        val sharedPref = getSharedPreferences("FafaBitePrefs", Context.MODE_PRIVATE)
        val namaToko = sharedPref.getString("NAMA_USER", "Mitra FafaBite")
        tvNamaTokoHeader.text = namaToko // Tampilkan nama lokal dulu sambil nunggu loading API

        // ========================================================
        // 3. AKSI TOMBOL & SWITCH
        // ========================================================
        btnTambahMakanan.setOnClickListener {
            val intent = Intent(this, TambahMakananActivity::class.java)
            startActivity(intent)
        }

        switchStatusToko.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switchStatusToko.text = "Buka "
                Toast.makeText(this, "Toko Anda sekarang BUKA", Toast.LENGTH_SHORT).show()
            } else {
                switchStatusToko.text = "Tutup "
                Toast.makeText(this, "Toko Anda sekarang TUTUP", Toast.LENGTH_SHORT).show()
            }
        }

        // ========================================================
        // 4. AKSI NAVIGASI BAWAH
        // ========================================================
        bottomNavResto.selectedItemId = R.id.nav_home_resto

        bottomNavResto.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home_resto -> true
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
                R.id.nav_profil_resto -> {
                    startActivity(Intent(this, ProfilRestoranActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }

        // ========================================================
        // 5. PANGGIL API UNTUK MENGAMBIL DATA ANGKA DASHBOARD
        // ========================================================
        fetchDashboardData()
    }

    // Biar data selalu up-to-date (otomatis ter-refresh) saat Mas kembali dari halaman Tambah Makanan
    override fun onResume() {
        super.onResume()
        fetchDashboardData()
    }

    private fun fetchDashboardData() {
        val sharedPref = getSharedPreferences("FafaBitePrefs", Context.MODE_PRIVATE)

        // Ambil ID_USER sebagai Int (angka), lalu ubah menjadi String agar Retrofit tidak bingung
        val idToko = sharedPref.getInt("ID_USER", 1).toString()

        ApiConfig.getApiService().getDashboardPenjual(idToko).enqueue(object : Callback<ResponseDashboard> {
            override fun onResponse(call: Call<ResponseDashboard>, response: Response<ResponseDashboard>) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!.data
                    if (data != null) {
                        updateTampilan(data)
                    }
                } else {
                    Toast.makeText(this@RestoranMainActivity, "Gagal memuat angka dashboard", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseDashboard>, t: Throwable) {
                Toast.makeText(this@RestoranMainActivity, "Error Server: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateTampilan(data: DataDashboard) {
        // Tangkap elemen yang mau diubah angkanya
        val tvNamaToko = findViewById<TextView>(R.id.tvNamaTokoHeader)
        val tvPendapatan = findViewById<TextView>(R.id.tvPendapatanHariIni)
        val tvPesananBaru = findViewById<TextView>(R.id.tvPesananBaru)
        val tvStokTerjual = findViewById<TextView>(R.id.tvStokTerjual)
        val tvSisaStok = findViewById<TextView>(R.id.tvSisaStok)
        val progressBarStok = findViewById<ProgressBar>(R.id.progressBarStok)

        val cvOrderTerbaru = findViewById<CardView>(R.id.cvOrderTerbaru)
        val tvNomorOrder = findViewById<TextView>(R.id.tvNomorOrder)
        val tvStatusOrder = findViewById<TextView>(R.id.tvStatusOrder)
        val tvDetailOrder = findViewById<TextView>(R.id.tvDetailOrder)

        // 1. Teks Dasar (Ganti nama toko dengan data paling resmi dari Database)
        tvNamaToko.text = data.namaToko
        tvPesananBaru.text = "${data.pesananBaru} Order"
        tvStokTerjual.text = "${data.stokTerjual} Terjual"
        tvSisaStok.text = "${data.sisaStok} Sisa (Belum Laku)"

        // 2. Format Uang ke Rupiah (Biar rapi ada titiknya)
        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        tvPendapatan.text = formatRupiah.format(data.pendapatanHariIni).replace("Rp", "Rp ")

        // 3. Logika Progress Bar
        val totalStokKeseluruhan = data.stokTerjual + data.sisaStok
        progressBarStok.max = if (totalStokKeseluruhan > 0) totalStokKeseluruhan else 100
        progressBarStok.progress = data.stokTerjual

        // 4. Munculkan Kotak Pesanan (Hanya kalau ada orderan masuk)
        if (data.orderButuhPerhatian.isNotEmpty()) {
            cvOrderTerbaru.visibility = View.VISIBLE
            val orderPertama = data.orderButuhPerhatian[0]

            tvNomorOrder.text = "Order #${orderPertama.nomorOrder}"
            tvStatusOrder.text = orderPertama.status.replaceFirstChar { it.uppercase() }
            tvDetailOrder.text = "Pemesan: ${orderPertama.namaPemesan} • ${orderPertama.detailPesanan}"
        } else {
            cvOrderTerbaru.visibility = View.GONE
        }
    }
}