package com.example.fafabite.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fafabite.R
import com.example.fafabite.api.ApiConfig
import com.example.fafabite.api.ResponseProfilToko
import com.example.fafabite.api.ResponseUser
import com.example.fafabite.models.ResponseCheckout // Pastikan di-import
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class ProfilRestoranActivity : AppCompatActivity() {

    private lateinit var tvNamaToko: TextView
    private lateinit var tvAlamatToko: TextView
    private lateinit var tvSaldoPenjual: TextView
    private lateinit var ivProfilFoto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_restoran)

        tvNamaToko = findViewById(R.id.tvNamaToko)
        tvAlamatToko = findViewById(R.id.tvEmailToko)
        tvSaldoPenjual = findViewById(R.id.tvSaldoPenjual)
        ivProfilFoto = findViewById(R.id.ivProfilFoto)

        val btnTarikDana = findViewById<Button>(R.id.btnTarikDana)
        val btnLogout = findViewById<TextView>(R.id.tvLogoutResto)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavResto)

        bottomNav.selectedItemId = R.id.nav_profil_resto

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home_resto -> {
                    startActivity(Intent(this, RestoranMainActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_makanan_resto -> {
                    startActivity(Intent(this, MenuRestoranActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_pesanan_resto -> {
                    startActivity(Intent(this, PesananRestoranActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_profil_resto -> true
                else -> false
            }
        }

        val menuInformasiToko = findViewById<TextView>(R.id.menuInformasiToko)
        menuInformasiToko.setOnClickListener {
            val intent = Intent(this, EditProfilActivity::class.java)
            startActivity(intent)
        }

        // ==========================================
        // TOMBOL TARIK DANA DIKLIK
        // ==========================================
        btnTarikDana.setOnClickListener {
            val intent = Intent(this, TarikDanaActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            tampilkanDialogLogout()
        }
    }



    // ==========================================
    // LOGIKA LOGOUT
    // ==========================================
    private fun tampilkanDialogLogout() {
        val dialog = android.app.Dialog(this)
        dialog.setContentView(R.layout.layout_dialog_notif)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val ivIcon = dialog.findViewById<ImageView>(R.id.ivDialogIcon)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvDialogTitle)
        val tvMessage = dialog.findViewById<TextView>(R.id.tvDialogMessage)

        val btnOkBiasa = dialog.findViewById<Button>(R.id.btnDialogOk)
        val layoutDuaTombol = dialog.findViewById<android.widget.LinearLayout>(R.id.layoutDialogDuaTombol)
        val btnBatal = dialog.findViewById<Button>(R.id.btnDialogBatal)
        val btnKeluar = dialog.findViewById<Button>(R.id.btnDialogKeluar)

        btnOkBiasa.visibility = View.GONE
        layoutDuaTombol.visibility = View.VISIBLE

        tvTitle.text = "Konfirmasi Keluar"
        tvMessage.text = "Apakah Anda yakin ingin menutup sesi toko ini?"

        ivIcon.setImageResource(android.R.drawable.ic_lock_power_off)
        ivIcon.setColorFilter(android.graphics.Color.parseColor("#F44336"))
        ivIcon.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#FFEBEE"))

        btnBatal.setOnClickListener {
            dialog.dismiss()
        }

        btnKeluar.setOnClickListener {
            dialog.dismiss()
            prosesLogout()
        }

        dialog.show()
    }

    private fun prosesLogout() {
        val sharedPref = getSharedPreferences("FafaBitePrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()

        Toast.makeText(this, "Berhasil Keluar Akun", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    // =========================================================
    // DINAMISASI: Panggil API setiap kali halaman dibuka
    // =========================================================
    override fun onResume() {
        super.onResume()

        val sharedPref = getSharedPreferences("FafaBitePrefs", Context.MODE_PRIVATE)
        val idUserLogin = sharedPref.getInt("ID_USER", 0)

        if (idUserLogin != 0) {
            getDataProfil(idUserLogin)
            getDataSaldo(idUserLogin)
        } else {
            Toast.makeText(this, "Sesi berakhir, silakan login kembali", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun getDataProfil(idToko: Int) {
        ApiConfig.getApiService().getProfilToko(idToko).enqueue(object : Callback<ResponseProfilToko> {
            override fun onResponse(call: Call<ResponseProfilToko>, response: Response<ResponseProfilToko>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    val dataToko = response.body()?.data

                    if (dataToko != null) {
                        tvNamaToko.text = dataToko.namaToko
                        tvAlamatToko.text = dataToko.alamat
                    }
                }
            }

            override fun onFailure(call: Call<ResponseProfilToko>, t: Throwable) {
                // Biarkan kosong
            }
        })
    }

    private fun getDataSaldo(idUser: Int) {
        ApiConfig.getApiService().getProfilUser(idUser).enqueue(object : Callback<ResponseUser> {
            override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!.data

                    val formatRupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
                    val saldoTerformat = formatRupiah.format(user?.saldo ?: 0).replace("Rp", "Rp ")

                    tvSaldoPenjual.text = saldoTerformat

                    if (!user?.fotoProfil.isNullOrEmpty()) {
                        val urlFoto = ApiConfig.URL_FOTO + user?.fotoProfil

                        Glide.with(this@ProfilRestoranActivity)
                            .load(urlFoto)
                            .circleCrop()
                            .placeholder(R.drawable.bg_input_pill)
                            .error(android.R.drawable.ic_menu_camera)
                            .into(ivProfilFoto)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                // Biarkan kosong
            }
        })
    }
}