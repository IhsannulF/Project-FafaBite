package com.example.fafabite

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {

    private var kuantitas = 1
    private var hargaSatuan = 0
    private val biayaLayanan = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // 1. Tangkap data dari halaman Home
        val idMakanan = intent.getIntExtra("ID_MAKANAN", 0)
        val namaMakanan = intent.getStringExtra("NAMA_MAKANAN") ?: "Makanan"
        hargaSatuan = intent.getIntExtra("HARGA_MAKANAN", 0)
        val fotoMakanan = intent.getStringExtra("FOTO_MAKANAN") ?: ""
        val namaToko = intent.getStringExtra("NAMA_TOKO") ?: ""

        // TANGKAP BATAS STOK DARI ADAPTER
        val batasStok = intent.getIntExtra("STOK_MAKANAN", 1)

        // 2. Hubungkan dengan UI
        val tvNama = findViewById<TextView>(R.id.tvNamaCheckout)
        val tvHarga = findViewById<TextView>(R.id.tvHargaCheckout)
        val ivFoto = findViewById<ImageView>(R.id.ivFotoCheckout)
        val tvQty = findViewById<TextView>(R.id.tvQty)
        val btnPlus = findViewById<TextView>(R.id.btnPlus)
        val btnMinus = findViewById<TextView>(R.id.btnMinus)
        val btnBayar = findViewById<Button>(R.id.btnBayar)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        // 3. Set Data ke Layar
        tvNama.text = namaMakanan
        tvHarga.text = formatRupiah(hargaSatuan)

        val baseUrl = "http://192.168.1.61:8000/file-makanan/"
        Glide.with(this).load(baseUrl + fotoMakanan).centerCrop().into(ivFoto)

        updateTotal() // Hitung total awal

        // 4. Tombol Kembali
        btnBack.setOnClickListener { finish() }

        // 5. Logika Plus Minus (SUDAH DIKUNCI SESUAI STOK RESTORAN)
        btnPlus.setOnClickListener {
            if (kuantitas < batasStok) {
                kuantitas++
                tvQty.text = kuantitas.toString()
                updateTotal()
            } else {
                Toast.makeText(this, "Maksimal pemesanan $batasStok porsi!", Toast.LENGTH_SHORT).show()
            }
        }

        btnMinus.setOnClickListener {
            if (kuantitas > 1) {
                kuantitas--
                tvQty.text = kuantitas.toString()
                updateTotal()
            }
        }

        // 6. Tombol Bayar -> Munculkan Pop-up VA Dummy
        btnBayar.setOnClickListener {
            munculkanVADummy(namaToko)
        }
    }

    private fun updateTotal() {
        val subtotal = hargaSatuan * kuantitas
        val totalAkhir = subtotal + biayaLayanan

        findViewById<TextView>(R.id.tvSubtotal).text = formatRupiah(subtotal)
        findViewById<TextView>(R.id.tvTotalAkhir).text = formatRupiah(totalAkhir)
    }

    private fun munculkanVADummy(namaToko: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pembayaran Virtual Account")

        val totalBayar = findViewById<TextView>(R.id.tvTotalAkhir).text.toString()
        val nomorVA = "8800" + (10000000..99999999).random() // Bikin nomor VA Acak

        builder.setMessage("Selesaikan pembayaran untuk toko $namaToko.\n\nBank BCA\nNomor VA: $nomorVA\nTotal: $totalBayar")

        builder.setPositiveButton("Sudah Bayar") { dialog, _ ->
            Toast.makeText(this, "Pembayaran Berhasil Diverifikasi!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            // TODO: Di sinilah nanti kita lempar dia ke Halaman QR Code
        }

        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }

        builder.setCancelable(false)
        builder.show()
    }

    private fun formatRupiah(angka: Int): String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return format.format(angka).replace("Rp", "Rp ")
    }
}