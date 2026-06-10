package com.example.fafabite.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fafabite.MainActivity
import com.example.fafabite.R
import com.example.fafabite.api.ApiConfig
import com.example.fafabite.api.ResponseUser
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class AkunActivity : AppCompatActivity() {

    private lateinit var tvSaldo: TextView
    private lateinit var tvNamaProfil: TextView
    private lateinit var tvDetailProfil: TextView

    private lateinit var ivProfilFoto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_akun)

        tvSaldo = findViewById(R.id.tvSaldoFafaPay)
        tvNamaProfil = findViewById(R.id.tvProfilNama)
        tvDetailProfil = findViewById(R.id.tvProfilDetail)
        ivProfilFoto = findViewById(R.id.ivProfilFoto)
        val btnTopUp = findViewById<Button>(R.id.btnTopUpFafa)

        btnTopUp.setOnClickListener {
            startActivity(Intent(this, TopUpActivity::class.java))
        }

        // Cari tombol Informasi Akun di XML Pembeli
        val btnInfoAkun = findViewById<TextView>(R.id.InfoAkun)

        btnInfoAkun.setOnClickListener {
            val intent = Intent(this, EditProfilActivity::class.java)
            startActivity(intent)
        }

        // ==========================================
        // LOGIKA BOTTOM NAVIGATION & FAB
        // ==========================================
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.nav_akun

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_riwayat -> {
                    startActivity(Intent(this, RiwayatActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_akun -> true
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
        // LOGIKA LOGOUT DENGAN DIALOG KONFIRMASI
        // ==========================================
        val btnLogout = findViewById<View>(R.id.tvLogout)
        btnLogout.setOnClickListener {
            tampilkanDialogLogout()
        }
    }

    // FUNGSI MENAMPILKAN DIALOG
    private fun tampilkanDialogLogout() {
        val dialog = android.app.Dialog(this)
        dialog.setContentView(R.layout.layout_dialog_notif)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val ivIcon = dialog.findViewById<android.widget.ImageView>(R.id.ivDialogIcon)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvDialogTitle)
        val tvMessage = dialog.findViewById<TextView>(R.id.tvDialogMessage)

        val btnOkBiasa = dialog.findViewById<Button>(R.id.btnDialogOk)
        val layoutDuaTombol = dialog.findViewById<android.widget.LinearLayout>(R.id.layoutDialogDuaTombol)
        val btnBatal = dialog.findViewById<Button>(R.id.btnDialogBatal)
        val btnKeluar = dialog.findViewById<Button>(R.id.btnDialogKeluar)

        // Sembunyikan tombol Oke, munculkan dua tombol (Batal & Keluar)
        btnOkBiasa.visibility = View.GONE
        layoutDuaTombol.visibility = View.VISIBLE

        tvTitle.text = "Konfirmasi Keluar"
        tvMessage.text = "Apakah kamu yakin ingin keluar dari akun FafaBite?"

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

    // FUNGSI EKSEKUSI PENGHAPUSAN SESI
    private fun prosesLogout() {
        val sharedPref = getSharedPreferences("FafaBitePrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()

        Toast.makeText(this, "Berhasil Keluar dari Akun", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    // ==========================================
    // LOGIKA TARIK DATA PROFIL & SALDO
    // ==========================================
    override fun onResume() {
        super.onResume()

        val sharedPref = getSharedPreferences("FafaBitePrefs", Context.MODE_PRIVATE)
        // Variabel ini bernama idUser
        val idUser = sharedPref.getInt("ID_USER", 0)

        // Panggil variabel idUser di sini
        android.util.Log.d("CEK_KUNCI_ID", "ID User yang terbaca di HP: $idUser")

        if (idUser == 0) {
            Toast.makeText(this, "Peringatan: ID User di HP terbaca 0 (Kosong)!", Toast.LENGTH_LONG).show()
        }

        if (idUser != 0) {
            tarikDataProfil(idUser)
        }
    }

    private fun tarikDataProfil(idUser: Int) {
        ApiConfig.getApiService().getProfilUser(idUser).enqueue(object : Callback<ResponseUser> {
            override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!.data

                    tvNamaProfil.text = user?.name ?: "Nama Pengguna"

                    val sharedPref = getSharedPreferences("FafaBitePrefs", Context.MODE_PRIVATE)
                    val role = sharedPref.getString("ROLE_USER", "Member")?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                    tvDetailProfil.text = "${user?.email ?: "email@domain.com"} • $role"

                    val formatRupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
                    val saldoTerformat = formatRupiah.format(user?.saldo ?: 0).replace("Rp", "Rp ")
                    tvSaldo.text = saldoTerformat

                    // ==========================================
                    // LOGIKA MENAMPILKAN FOTO PROFIL DENGAN GLIDE
                    // ==========================================
                    if (!user?.fotoProfil.isNullOrEmpty()) {
                        val urlFoto = ApiConfig.URL_FOTO + user?.fotoProfil

                        Glide.with(this@AkunActivity)
                            .load(urlFoto)
                            .circleCrop() // Bikin gambar jadi bulat
                            .placeholder(R.drawable.bg_input_pill)
                            .error(android.R.drawable.ic_menu_camera) // Tampilkan ikon kamera jika foto gagal dimuat
                            .into(ivProfilFoto)
                    }

                } else {
                    Toast.makeText(this@AkunActivity, "Gagal mengambil data akun", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                Toast.makeText(this@AkunActivity, "Error jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}