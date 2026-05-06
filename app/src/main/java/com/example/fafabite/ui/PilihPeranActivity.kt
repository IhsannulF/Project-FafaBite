package com.example.fafabite.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
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

        // Tombol Kembali ke Landing Page
        btnBack.setOnClickListener {
            finish()
        }

        // 1. Jika Klik Kartu Pembeli -> Arahkan ke Register Pembeli
        cvPembeli.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // 2. Jika Klik Kartu Restoran -> Arahkan ke Register Restoran
        cvRestoran.setOnClickListener {
            val intent = Intent(this, RegisterRestoranActivity::class.java)
            startActivity(intent)
        }
    }
}