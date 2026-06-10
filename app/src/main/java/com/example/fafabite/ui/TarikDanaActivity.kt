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

class TarikDanaActivity : AppCompatActivity() {

    private lateinit var etNominalTarik: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarik_dana)

        val btnBack = findViewById<ImageView>(R.id.btnBackTarikDana)
        etNominalTarik = findViewById(R.id.etNominalTarikDana)
        val btn20k = findViewById<Button>(R.id.btnNominalTarik20k)
        val btn50k = findViewById<Button>(R.id.btnNominalTarik50k)
        val btn100k = findViewById<Button>(R.id.btnNominalTarik100k)
        val btnProses = findViewById<Button>(R.id.btnProsesTarikDana)

        // Tombol Kembali
        btnBack.setOnClickListener { finish() }

        // Pilihan Nominal Cepat
        btn20k.setOnClickListener { etNominalTarik.setText("20000") }
        btn50k.setOnClickListener { etNominalTarik.setText("50000") }
        btn100k.setOnClickListener { etNominalTarik.setText("100000") }

        // Proses Klik Tarik Dana
        btnProses.setOnClickListener {
            val nominalStr = etNominalTarik.text.toString().trim()

            if (nominalStr.isEmpty()) {
                Toast.makeText(this, "Nominal penarikan tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nominalInt = nominalStr.toInt()
            if (nominalInt < 10000) {
                Toast.makeText(this, "Minimal penarikan dana adalah Rp 10.000", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            konfirmasiTarikDana(nominalInt)
        }
    }

    private fun konfirmasiTarikDana(nominal: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi Penarikan")
        builder.setMessage("Apakah Anda yakin ingin menarik dana sebesar Rp $nominal ke rekening Anda?")
        builder.setPositiveButton("Ya, Tarik") { dialog, _ ->
            prosesTarikDanaKeServer(nominal)
            dialog.dismiss()
        }
        builder.setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun prosesTarikDanaKeServer(nominal: Int) {
        val sharedPref = getSharedPreferences("FafaBitePrefs", Context.MODE_PRIVATE)
        val idUserSaatIni = sharedPref.getInt("ID_USER", 0)

        if (idUserSaatIni == 0) {
            Toast.makeText(this, "Sesi tidak valid, silakan login ulang.", Toast.LENGTH_SHORT).show()
            return
        }

        // Memanggil endpoint tarikDana di ApiService
        ApiConfig.getApiService().tarikDana(idUserSaatIni, nominal).enqueue(object : Callback<ResponseCheckout> {
            override fun onResponse(call: Call<ResponseCheckout>, response: Response<ResponseCheckout>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    tampilkanDialogSukses()
                } else {
                    // Menangani jika saldo tidak cukup (Error dari backend Laravel)
                    Toast.makeText(this@TarikDanaActivity, "Gagal: Saldo toko tidak mencukupi", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseCheckout>, t: Throwable) {
                Toast.makeText(this@TarikDanaActivity, "Error jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun tampilkanDialogSukses() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Penarikan Berhasil!")
        builder.setMessage("Dana berhasil ditarik. Saldo toko Anda telah dikurangi, silakan cek berkala.")
        builder.setCancelable(false)
        builder.setPositiveButton("Kembali") { dialog, _ ->
            dialog.dismiss()
            finish() // Menutup halaman, otomatis kembali ke ProfilRestoranActivity
        }
        builder.show()
    }
}