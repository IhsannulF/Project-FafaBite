package com.example.fafabite.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fafabite.R
import com.example.fafabite.api.RiwayatPesananItem

class PesananAktifAdapter(private var listPesanan: List<RiwayatPesananItem>) :
    RecyclerView.Adapter<PesananAktifAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvBatasWaktu: TextView = view.findViewById(R.id.tvBatasWaktu)
        val tvNamaToko: TextView = view.findViewById(R.id.tvNamaToko)
        val tvDetailPesanan: TextView = view.findViewById(R.id.tvDetailPesanan)
        val tvPinPengambilan: TextView = view.findViewById(R.id.tvPinPengambilan)
        val btnTampilQR: Button = view.findViewById(R.id.btnTampilQR)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pesanan_aktif, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listPesanan[position]

        holder.tvNamaToko.text = item.namaToko ?: "Toko"
        holder.tvDetailPesanan.text = "${item.jumlahPesan}x ${item.namaMakanan}"
        holder.tvPinPengambilan.text = item.nomorOrder ?: "----"

        // Mengatur teks dan warna banner atas sesuai status pesanan
        when (item.statusPesanan.lowercase()) {
            "menunggu" -> {
                holder.tvBatasWaktu.text = "MENUNGGU KONFIRMASI RESTORAN"
                holder.tvBatasWaktu.setBackgroundColor(Color.parseColor("#F57C00")) // Warna Oranye
            }
            "disiapkan", "diproses" -> {
                holder.tvBatasWaktu.text = "MAKANAN SEDANG DISIAPKAN"
                holder.tvBatasWaktu.setBackgroundColor(Color.parseColor("#1976D2")) // Warna Biru
            }
            "siap_diambil" -> {
                holder.tvBatasWaktu.text = "SIAP DIAMBIL SEKARANG!"
                holder.tvBatasWaktu.setBackgroundColor(Color.parseColor("#388E3C")) // Warna Hijau
            }
            else -> {
                holder.tvBatasWaktu.text = "STATUS: ${item.statusPesanan.uppercase()}"
                holder.tvBatasWaktu.setBackgroundColor(Color.GRAY)
            }
        }

        // Aksi ketika tombol QR diklik
        holder.btnTampilQR.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Menampilkan QR Code untuk pesanan ${item.nomorOrder}", Toast.LENGTH_SHORT).show()
            // Nantinya di sini bisa diisi kodingan untuk membuka pop-up gambar QR
        }
    }

    override fun getItemCount(): Int = listPesanan.size

    fun perbaruiData(newList: List<RiwayatPesananItem>) {
        this.listPesanan = newList
        notifyDataSetChanged()
    }
}