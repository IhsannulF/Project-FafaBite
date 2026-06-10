package com.example.fafabite.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fafabite.R
import com.example.fafabite.api.ApiConfig
import com.example.fafabite.models.ResponseCheckout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TopUpActivity : AppCompatActivity() {

    private lateinit var etNominal: EditText
    // Variabel idUserSaatIni = 1 (hardcode) SUDAH DIHAPUS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up)

        val btnBack = findViewById<ImageView>(R.id.btnBackTopUp)
        etNominal = findViewById(R.id.etNominalTopUp)
        val btn20k = findViewById<Button>(R.id.btnNominal20k)
        val btn50k = findViewById<Button>(R.id.btnNominal50k)
        val btn100k = findViewById<Button>(R.id.btnNominal100k)
        val btnProses = findViewById<Button>(R.id.btnProsesTopUp)

        // Tombol Kembali
        btnBack.setOnClickListener { finish() }

        // Tombol Nominal Cepat
        btn20k.setOnClickListener { etNominal.setText("20000") }
        btn50k.setOnClickListener { etNominal.setText("50000") }
        btn100k.setOnClickListener { etNominal.setText("100000") }

        // Eksekusi Top Up
        btnProses.setOnClickListener {
            val nominalStr = etNominal.text.toString().trim()

            if (nominalStr.isEmpty()) {
                Toast.makeText(this, "Nominal tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nominalInt = nominalStr.toInt()
            if (nominalInt < 10000) {
                Toast.makeText(this, "Minimal Top Up adalah Rp 10.000", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            konfirmasiTopUp(nominalInt)
        }
    }

    private fun konfirmasiTopUp(nominal: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Simulasi Pembayaran")
        builder.setMessage("Lanjutkan simulasi pembayaran instan sebesar Rp $nominal?")
        builder.setPositiveButton("Bayar Sekarang") { dialog, _ ->
            prosesTopUpKeServer(nominal)
            dialog.dismiss()
        }
        builder.setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun prosesTopUpKeServer(nominal: Int) {
        // AMBIL ID USER DARI SHAREDPREFERENCES
        val sharedPref = getSharedPreferences("FafaBitePrefs", Context.MODE_PRIVATE)
        val idUserSaatIni = sharedPref.getInt("ID_USER", 0)

        // Validasi keamanan jika ID gagal terbaca
        if (idUserSaatIni == 0) {
            Toast.makeText(this, "Sesi login tidak valid, silakan login ulang.", Toast.LENGTH_SHORT).show()
            return
        }

        ApiConfig.getApiService().topUpFafaPay(idUserSaatIni, nominal).enqueue(object : Callback<ResponseCheckout> {
            override fun onResponse(call: Call<ResponseCheckout>, response: Response<ResponseCheckout>) {
                if (response.isSuccessful) {
                    tampilkanDialogSukses()
                } else {
                    Toast.makeText(this@TopUpActivity, "Gagal melakukan top up", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseCheckout>, t: Throwable) {
                Toast.makeText(this@TopUpActivity, "Error jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun tampilkanDialogSukses() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Top Up Berhasil!")
        builder.setMessage("Saldo FafaPay kamu sudah ditambahkan. Silakan cek di halaman Profil.")
        builder.setCancelable(false)
        builder.setPositiveButton("Kembali ke Profil") { dialog, _ ->
            dialog.dismiss()
            finish() // Tutup halaman ini, kembali ke profil
        }
        builder.show()
    }
}