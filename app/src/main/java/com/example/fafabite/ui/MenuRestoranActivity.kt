package com.example.fafabite.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fafabite.EditMakananActivity
import com.example.fafabite.R
import com.example.fafabite.adapter.MenuRestoranAdapter
import com.example.fafabite.api.ApiConfig
import com.example.fafabite.api.ResponseListProduk
import com.example.fafabite.models.ResponseCheckout
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

        rvMakanan.layoutManager = LinearLayoutManager(this)
        bottomNav.selectedItemId = R.id.nav_makanan_resto

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home_resto -> {
                    startActivity(Intent(this, RestoranMainActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
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

        fabTambahMenu.setOnClickListener {
            startActivity(Intent(this, TambahMakananActivity::class.java))
        }

        fetchMenuData()
    }

    override fun onResume() {
        super.onResume()
        fetchMenuData()
    }

    private fun fetchMenuData() {
        progressBar.visibility = View.VISIBLE
        rvMakanan.visibility = View.GONE
        tvMenuKosong.visibility = View.GONE

        val sharedPref = getSharedPreferences("FafaBitePrefs", Context.MODE_PRIVATE)
        val idToko = sharedPref.getInt("ID_USER", 1).toString()

        ApiConfig.getApiService().getProdukByToko(idToko).enqueue(object : Callback<ResponseListProduk> {
            override fun onResponse(call: Call<ResponseListProduk>, response: Response<ResponseListProduk>) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body() != null) {
                    val listMakanan = response.body()!!.data

                    if (listMakanan.isNotEmpty()) {
                        rvMakanan.visibility = View.VISIBLE
                        val adapter = MenuRestoranAdapter(listMakanan)
                        rvMakanan.adapter = adapter
                    } else {
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

    // ==========================================
    // FUNGSI UNTUK DIALOG & HAPUS MAKANAN
    // ==========================================
    fun tampilkanDialogOpsi(idMakanan: Int, namaMakanan: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_edit_menu) // Pastikan nama layout XML-nya sesuai
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnEdit = dialog.findViewById<Button>(R.id.btnDialogEdit)
        val btnHapus = dialog.findViewById<Button>(R.id.btnDialogHapus)

        // Aksi Tombol Edit
        btnEdit.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, EditMakananActivity::class.java)
            // Ngirim data ID makanan ke halaman edit
            intent.putExtra("ID_MAKANAN", idMakanan)
            startActivity(intent)
        }

        // Aksi Tombol Hapus
        btnHapus.setOnClickListener {
            dialog.dismiss()
            konfirmasiHapusMakanan(idMakanan, namaMakanan)
        }

        dialog.show()
    }

    fun konfirmasiHapusMakanan(idMakanan: Int, namaMakanan: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Hapus Menu?")
        builder.setMessage("Yakin ingin hapus menu '$namaMakanan'?, Data yang dihapus tidak bisa dikembalikan.")

        builder.setPositiveButton("Iya, Hapus") { dialog, _ ->
            prosesHapusKeServer(idMakanan)
            dialog.dismiss()
        }

        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun prosesHapusKeServer(idMakanan: Int) {
        ApiConfig.getApiService().hapusMakanan(idMakanan).enqueue(object : Callback<ResponseCheckout> {
            override fun onResponse(call: Call<ResponseCheckout>, response: Response<ResponseCheckout>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MenuRestoranActivity, "Sip, Menu telah terhapus!", Toast.LENGTH_SHORT).show()
                    fetchMenuData() // Refresh data otomatis setelah dihapus
                } else {
                    Toast.makeText(this@MenuRestoranActivity, "Waduh, Gagal menghapus menu", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseCheckout>, t: Throwable) {
                Toast.makeText(this@MenuRestoranActivity, "Error jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}