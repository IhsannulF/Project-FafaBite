package com.example.fafabite.adapter // Sesuaikan nama package-mu jika berbeda

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fafabite.R
import com.example.fafabite.api.PesananItem
import com.google.android.material.button.MaterialButton
import java.text.NumberFormat
import java.util.*

class PesananAdapter(
    private var listPesanan: List<PesananItem>,
    // Ini adalah 'kabel' untuk mengirim aksi klik tombol kembali ke Activity
    private val onUpdateStatus: (idPesanan: Int, statusBaru: String) -> Unit,
    private val onScanQR: (nomorOrder: String) -> Unit
) : RecyclerView.Adapter<PesananAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNomorOrder: TextView = view.findViewById(R.id.tvNomorOrder)
        val tvBadgeStatus: TextView = view.findViewById(R.id.tvBadgeStatus)
        val tvNamaPemesan: TextView = view.findViewById(R.id.tvNamaPemesan)
        val tvDetailMakanan: TextView = view.findViewById(R.id.tvDetailMakanan)
        val tvWaktuPickup: TextView = view.findViewById(R.id.tvWaktuPickup)

        val btnPesananSelesai: MaterialButton = view.findViewById(R.id.btnPesananSelesai)
        val btnScanQR: MaterialButton = view.findViewById(R.id.btnScanQR)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pesanan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pesanan = listPesanan[position]
        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        val hargaFormatted = formatRupiah.format(pesanan.totalHarga).replace("Rp", "Rp ")

        // 1. Set Data Teks
        holder.tvNomorOrder.text = "Order #${pesanan.nomorOrder}"
        holder.tvNamaPemesan.text = "Pemesan: ${pesanan.user?.name ?: "Tanpa Nama"}"

        // Contoh: "3x Udon Mentah (Rp 45.000)"
        val namaMakanan = pesanan.produk?.namaMakanan ?: "Makanan Dihapus"
        holder.tvDetailMakanan.text = "${pesanan.jumlahPesan}x $namaMakanan ($hargaFormatted)"

        // Ambil jam saja (Misal "2026-05-10 19:00:00" -> "19:00")
        val jamPickup = pesanan.produk?.waktuPickup?.substringAfter(" ")?.substringBeforeLast(":") ?: "-"
        holder.tvWaktuPickup.text = "Waktu Pickup: $jamPickup"

        // 2. Logika Status & Tombol Dinamis
        when (pesanan.statusPesanan.lowercase()) {
            "menunggu" -> {
                holder.tvBadgeStatus.text = "Pesanan Baru"
                holder.tvBadgeStatus.setTextColor(Color.parseColor("#1976D2"))
                holder.tvBadgeStatus.setBackgroundColor(Color.parseColor("#E3F2FD"))

                // Munculkan tombol Pesanan Selesai, Sembunyikan Scan QR
                holder.btnPesananSelesai.visibility = View.VISIBLE
                holder.btnScanQR.visibility = View.GONE
            }
            "disiapkan", "proses", "diproses" -> {
                holder.tvBadgeStatus.text = "Siap Diambil"
                holder.tvBadgeStatus.setTextColor(Color.parseColor("#4CAF50"))
                holder.tvBadgeStatus.setBackgroundColor(Color.parseColor("#E8F5E9"))

                // Sembunyikan tombol Pesanan Selesai, Munculkan Scan QR
                holder.btnPesananSelesai.visibility = View.GONE
                holder.btnScanQR.visibility = View.VISIBLE
            }
            else -> {
                holder.tvBadgeStatus.text = pesanan.statusPesanan.replaceFirstChar { it.uppercase() }
                holder.tvBadgeStatus.setTextColor(Color.parseColor("#757575"))
                holder.tvBadgeStatus.setBackgroundColor(Color.parseColor("#EEEEEE"))

                // Sembunyikan semua tombol aksi
                holder.btnPesananSelesai.visibility = View.GONE
                holder.btnScanQR.visibility = View.GONE
            }
        }

        // 3. Pasang Aksi Klik Tombol
        // HAPUS holder.btnTerima.setOnClickListener dan holder.btnTolak.setOnClickListener, GANTI DENGAN INI:
        holder.btnPesananSelesai.setOnClickListener {
            // Karena ini tombol "Selesai", kita asumsikan statusnya langsung berubah jadi 'selesai'
            // CATATAN: Kalau kamu butuh ordernya masuk ke tab "Diproses" dulu, ganti "selesai" menjadi "disiapkan"
            onUpdateStatus(pesanan.id, "selesai")
        }

        holder.btnScanQR.setOnClickListener {
            onScanQR(pesanan.nomorOrder)
        }



        holder.btnScanQR.setOnClickListener {
            onScanQR(pesanan.nomorOrder)
        }
    }

    override fun getItemCount(): Int = listPesanan.size

    // Fungsi untuk menyegarkan data adapter saat difilter
    fun updateData(newList: List<PesananItem>) {
        listPesanan = newList
        notifyDataSetChanged()
    }
}