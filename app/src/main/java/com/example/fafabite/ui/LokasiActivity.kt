package com.example.fafabite.ui

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.fafabite.R

class LokasiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lokasi)

        // Tangkap tombol kembali (Back)
        val btnBack = findViewById<ImageView>(R.id.btnBackLokasi)

        // Jika diklik, tutup halaman ini dan kembali ke halaman sebelumnya
        btnBack.setOnClickListener {
            finish()
        }
    }
}