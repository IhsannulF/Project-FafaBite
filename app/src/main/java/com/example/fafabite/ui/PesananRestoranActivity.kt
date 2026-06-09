package com.example.fafabite.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fafabite.R
import com.example.fafabite.adapter.PesananRestoAdapter
import com.example.fafabite.api.ApiConfig
import com.example.fafabite.models.PesananRestoItem
import com.example.fafabite.models.ResponseCheckout
import com.example.fafabite.models.ResponsePesananResto
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PesananRestoranActivity : AppCompatActivity(), PesananRestoAdapter.OnAksiPesananListener {

    private lateinit var rvPesanan: RecyclerView
    private lateinit var adapter: PesananRestoAdapter
    private var listPesananAsli: List<PesananRestoItem> = listOf()

    private lateinit var tabSemua: TextView
    private lateinit var tabDiproses: TextView
    private lateinit var tabSiap: TextView

    private val idTokoSaatIni = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesanan_restoran)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavResto)
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
                R.id.nav_pesanan_resto -> true
                R.id.nav_profil_resto -> {
                    startActivity(Intent(this, ProfilRestoranActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }

        rvPesanan = findViewById(R.id.rvPesanan)
        rvPesanan.layoutManager = LinearLayoutManager(this)

        tabSemua = findViewById(R.id.tabSemua)
        tabDiproses = findViewById(R.id.tabDiproses)
        tabSiap = findViewById(R.id.tabSiap)

        adapter = PesananRestoAdapter(listPesananAsli, this)
        rvPesanan.adapter = adapter

        ambilDataPesananToko()
        setupFilter()
    }

    private fun ambilDataPesananToko() {
        ApiConfig.getApiService().getPesananToko(idTokoSaatIni).enqueue(object : Callback<ResponsePesananResto> {
            override fun onResponse(call: Call<ResponsePesananResto>, response: Response<ResponsePesananResto>) {
                if (response.isSuccessful && response.body() != null) {
                    listPesananAsli = response.body()!!.data

                    // Tampilkan semua kecuali yang batal dan selesai
                    val dataAktif = listPesananAsli.filter {
                        !it.statusPesanan.equals("batal", ignoreCase = true) &&
                                !it.statusPesanan.equals("selesai", ignoreCase = true)
                    }
                    adapter.perbaruiData(dataAktif)
                } else {
                    val errorAsli = response.errorBody()?.string()
                    Toast.makeText(this@PesananRestoranActivity, "Gagal API: $errorAsli", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponsePesananResto>, t: Throwable) {
                Toast.makeText(this@PesananRestoranActivity, "Error Jaringan: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setupFilter() {
        tabSemua.setOnClickListener {
            updateTabUI(tabSemua, listOf(tabDiproses, tabSiap))
            val dataFilter = listPesananAsli.filter {
                !it.statusPesanan.equals("batal", ignoreCase = true) &&
                        !it.statusPesanan.equals("selesai", ignoreCase = true)
            }
            adapter.perbaruiData(dataFilter)
        }

        tabDiproses.setOnClickListener {
            updateTabUI(tabDiproses, listOf(tabSemua, tabSiap))
            // Menggabungkan pesanan baru masuk (menunggu) dengan yang sedang dibuat (disiapkan)
            val dataFilter = listPesananAsli.filter {
                it.statusPesanan.equals("menunggu", ignoreCase = true) ||
                        it.statusPesanan.equals("disiapkan", ignoreCase = true)
            }
            adapter.perbaruiData(dataFilter)
        }

        tabSiap.setOnClickListener {
            updateTabUI(tabSiap, listOf(tabSemua, tabDiproses))
            val dataFilter = listPesananAsli.filter { it.statusPesanan.equals("siap_diambil", ignoreCase = true) }
            adapter.perbaruiData(dataFilter)
        }
    }

    private fun updateTabUI(activeTab: TextView, inactiveTabs: List<TextView>) {
        val warnaAktifBg = ContextCompat.getColor(this, R.color.fafa_blue_primary)
        val warnaAktifTeks = Color.WHITE
        val warnaTidakAktifBg = Color.parseColor("#E0E0E0")
        val warnaTidakAktifTeks = Color.parseColor("#757575")

        activeTab.backgroundTintList = android.content.res.ColorStateList.valueOf(warnaAktifBg)
        activeTab.setTextColor(warnaAktifTeks)

        for (tab in inactiveTabs) {
            tab.backgroundTintList = android.content.res.ColorStateList.valueOf(warnaTidakAktifBg)
            tab.setTextColor(warnaTidakAktifTeks)
        }
    }

    override fun onTerima(pesanan: PesananRestoItem) {
        kirimUpdateStatusKeServer(pesanan.id, "disiapkan")
    }

    override fun onTolak(pesanan: PesananRestoItem) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Batalkan Pesanan?")
        builder.setMessage("Apakah Anda yakin ingin menolak pesanan ini? Saldo akan dikembalikan ke pembeli.")
        builder.setPositiveButton("Ya, Tolak") { dialog, _ ->
            kirimUpdateStatusKeServer(pesanan.id, "batal")
            dialog.dismiss()
        }
        builder.setNegativeButton("Kembali") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    override fun onSiapDiambil(pesanan: PesananRestoItem) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pesanan Siap?")
        builder.setMessage("Tandai pesanan ini siap diambil oleh pembeli?")
        builder.setPositiveButton("Ya, Sudah Siap") { dialog, _ ->
            kirimUpdateStatusKeServer(pesanan.id, "siap_diambil")
            dialog.dismiss()
        }
        builder.setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    override fun onSelesai(pesanan: PesananRestoItem) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selesaikan Pesanan?")
        builder.setMessage("Pastikan pembeli sudah mengambil makanannya. Tandai transaksi sebagai selesai?")
        builder.setPositiveButton("Ya, Selesai") { dialog, _ ->
            kirimUpdateStatusKeServer(pesanan.id, "selesai")
            dialog.dismiss()
        }
        builder.setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun kirimUpdateStatusKeServer(idPesanan: Int, statusBaru: String) {
        ApiConfig.getApiService().updateStatusPesanan(idPesanan, statusBaru).enqueue(object : Callback<ResponseCheckout> {
            override fun onResponse(call: Call<ResponseCheckout>, response: Response<ResponseCheckout>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@PesananRestoranActivity, "Pesanan berhasil diupdate!", Toast.LENGTH_SHORT).show()
                    ambilDataPesananToko()
                } else {
                    Toast.makeText(this@PesananRestoranActivity, "Gagal memperbarui status", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseCheckout>, t: Throwable) {
                Toast.makeText(this@PesananRestoranActivity, "Error jaringan", Toast.LENGTH_SHORT).show()
            }
        })
    }
}