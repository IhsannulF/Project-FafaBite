package com.example.fafabite.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.fafabite.R

class PilihPeranActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_peran)

        val btnBack = findViewById<ImageView>(R.id.btnBackPilihPeran)
        val cvPembeli = findViewById<CardView>(R.id.cvPeranPembeli)
        val cvRestoran = findViewById<CardView>(R.id.cvPeranRestoran)

        // Tombol Kembali
        btnBack.setOnClickListener {
            finish()
        }

        // Jika Klik Kartu Pembeli
        cvPembeli.setOnClickListener {
            // Kita arahkan ke RegisterActivity yang sudah kamu buat sebelumnya
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Jika Klik Kartu Restoran
        cvRestoran.setOnClickListener {
            // Jika Klik Kartu Restoran

                // Arahkan ke halaman Register Restoran
                val intent = Intent(this, RegisterRestoranActivity::class.java)
                startActivity(intent)
        }
    }
}