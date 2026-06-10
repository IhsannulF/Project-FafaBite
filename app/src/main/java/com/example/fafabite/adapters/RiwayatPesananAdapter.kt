package com.example.fafabite.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fafabite.R
import com.example.fafabite.api.ApiConfig
import com.example.fafabite.api.RiwayatPesananItem
import java.text.NumberFormat
import java.util.*

class RiwayatPesananAdapter(private var listRiwayat: List<RiwayatPesananItem>) :
    RecyclerView.Adapter<RiwayatPesananAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivFoto: ImageView = view.findViewById(R.id.ivFotoTokoRiwayat)
        val tvNamaToko: TextView = view.findViewById(R.id.tvNamaTokoRiwayat)
        val tvTanggal: TextView = view.findViewById(R.id.tvTanggalRiwayat)
        val tvStatus: TextView = view.findViewById(R.id.tvStatusRiwayat)
        val tvDetail: TextView = view.findViewById(R.id.tvDetailPesananRiwayat)
        val tvTotal: TextView = view.findViewById(R.id.tvTotalHargaRiwayat)
        val btnKiri: Button = view.findViewById(R.id.btnAksiKiri)
        val btnKanan: Button = view.findViewById(R.id.btnAksiKanan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat_pesanan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listRiwayat[position]
        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

        holder.tvNamaToko.text = item.namaToko
        holder.tvDetail.text = "${item.jumlahPesan}x ${item.namaMakanan}"
        holder.tvTotal.text = "Total: " + formatRupiah.format(item.totalHarga).replace("Rp", "Rp ")

        // Format tampilan tanggal jam simpel (Mengambil potongan teks tanggal bawaan Laravel)
        val tanggalInput = item.createdAt?.substringBefore("T") ?: "-"
        val jamInput = item.createdAt?.substringAfter("T")?.substringBeforeLast(".") ?: "-"
        holder.tvTanggal.text = "$tanggalInput • $jamInput"

        // ===================================================
        // LOGIKA BADGE WARNA SESUAI STATUS PESANAN (UPDATE)
        // ===================================================
        when (item.statusPesanan?.lowercase()) {
            "selesai" -> {
                holder.tvStatus.text = "Selesai"
                holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")) // Teks Hijau
                holder.tvStatus.setBackgroundColor(Color.parseColor("#E8F5E9")) // Background Hijau Muda

                holder.btnKiri.visibility = View.VISIBLE // Tombol Beri Nilai muncul
                holder.btnKanan.text = "Beli Lagi"
            }
            "siap_diambil" -> {
                holder.tvStatus.text = "Siap Diambil"
                holder.tvStatus.setTextColor(Color.parseColor("#FF9800")) // Teks Orange
                holder.tvStatus.setBackgroundColor(Color.parseColor("#FFF3E0")) // Background Orange Muda

                holder.btnKiri.visibility = View.GONE
                holder.btnKanan.text = "Cek PIN"
            }
            "batal" -> {
                holder.tvStatus.text = "Dibatalkan"
                holder.tvStatus.setTextColor(Color.parseColor("#F44336")) // Teks Merah
                holder.tvStatus.setBackgroundColor(Color.parseColor("#FFEBEE")) // Background Merah Muda

                holder.btnKiri.visibility = View.GONE
                holder.btnKanan.text = "Beli Lagi"
            }
            else -> {
                // Digunakan untuk status "menunggu" atau "disiapkan"
                holder.tvStatus.text = "Diproses"
                holder.tvStatus.setTextColor(Color.parseColor("#2196F3")) // Teks Biru
                holder.tvStatus.setBackgroundColor(Color.parseColor("#E3F2FD")) // Background Biru Muda

                holder.btnKiri.visibility = View.GONE
                holder.btnKanan.text = "Cek PIN"
            }
        }

        // Tampilkan foto makanan pendukung di riwayat
        if (!item.fotoMakanan.isNullOrEmpty()) {
            val urlFoto = ApiConfig.IMAGE_URL + item.fotoMakanan
            Glide.with(holder.itemView.context)
                .load(urlFoto)
                .placeholder(R.drawable.bg_input_pill)
                .error(android.R.drawable.ic_menu_report_image)
                .centerCrop()
                .into(holder.ivFoto)
        }
    }

    override fun getItemCount(): Int = listRiwayat.size

    // FUNGSI UTAMA UNTUK FILTER TOMBOL PILL BUTTONS (Semua, Selesai, Diproses)
    fun perbaruiData(newList: List<RiwayatPesananItem>) {
        this.listRiwayat = newList
        notifyDataSetChanged() // Beritahu RecyclerView agar memperbarui tampilan layar secara kilat
    }
}