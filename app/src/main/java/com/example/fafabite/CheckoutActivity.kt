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
import com.example.fafabite.api.ApiConfig
import com.example.fafabite.models.ResponseCheckout
import com.example.fafabite.ui.RiwayatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {

    private var kuantitas = 1
    private var hargaSatuan = 0

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

        val urlFoto = ApiConfig.IMAGE_URL + fotoMakanan
        Glide.with(this)
            .load(urlFoto)
            .placeholder(R.drawable.bg_input_pill)
            .error(android.R.drawable.ic_menu_report_image)
            .centerCrop()
            .into(ivFoto)

        updateTotal() // Hitung total awal

        // 4. Tombol Kembali
        btnBack.setOnClickListener { finish() }

        // 5. Logika Plus Minus (Terkunci Sesuai Stok)
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

        // 6. Tombol Bayar -> Munculkan Pop-up Konfirmasi FafaPay
        btnBayar.setOnClickListener {
            munculkanKonfirmasiFafaPay(idMakanan, namaToko)
        }
    }

    private fun updateTotal() {
        val totalAkhir = hargaSatuan * kuantitas

        findViewById<TextView>(R.id.tvSubtotal).text = formatRupiah(totalAkhir)
        findViewById<TextView>(R.id.tvTotalAkhir).text = formatRupiah(totalAkhir)
    }

    private fun munculkanKonfirmasiFafaPay(idMakanan: Int, namaToko: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Bayar pakai FafaPay?")

        val totalBayar = findViewById<TextView>(R.id.tvTotalAkhir).text.toString()

        builder.setMessage("Selesaikan pembayaran untuk toko $namaToko sebesar $totalBayar.\n\nSaldo FafaPay kamu akan otomatis terpotong.")

        builder.setPositiveButton("Bayar Sekarang") { dialog, _ ->
            dialog.dismiss()
            lakukanPembayaranKeServer(idMakanan, kuantitas)
        }

        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }

        builder.setCancelable(false)
        builder.show()
    }

    // FUNGSI UTAMA UNTUK NEMBAK API LARAVEL
    private fun lakukanPembayaranKeServer(idProduk: Int, jumlahPesan: Int) {
        val btnBayar = findViewById<Button>(R.id.btnBayar)

        // Hardcode ID User untuk testing sementara = 1 (Pastikan User ID 1 punya saldo di database)
        val idUserSaatIni = 1

        btnBayar.text = "Memproses..."
        btnBayar.isEnabled = false

        ApiConfig.getApiService().prosesCheckout(idUserSaatIni, idProduk, jumlahPesan)
            .enqueue(object : Callback<ResponseCheckout> {
                override fun onResponse(call: Call<ResponseCheckout>, response: Response<ResponseCheckout>) {
                    btnBayar.isEnabled = true
                    btnBayar.text = "Pesan Sekarang"

                    if (response.isSuccessful && response.body() != null) {
                        val body = response.body()!!
                        if (body.status == "success") {
                            Toast.makeText(this@CheckoutActivity, "Sukses! Pesanan diteruskan ke resto.", Toast.LENGTH_LONG).show()

                            // Arahkan user ke halaman Riwayat biar bisa ngecek status "Diproses"
                            val intent = Intent(this@CheckoutActivity, RiwayatActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // Saldo kurang atau stok habis
                            Toast.makeText(this@CheckoutActivity, body.message, Toast.LENGTH_LONG).show()
                        }
                    } else {
                        // TAMPILKAN ERROR ASLI DARI LARAVEL
                        val errorAsli = response.errorBody()?.string()
                        Toast.makeText(this@CheckoutActivity, "Gagal: $errorAsli", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseCheckout>, t: Throwable) {
                    btnBayar.isEnabled = true
                    btnBayar.text = "Pesan Sekarang"
                    Toast.makeText(this@CheckoutActivity, "Error Jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun formatRupiah(angka: Int): String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return format.format(angka).replace("Rp", "Rp ")
    }
}