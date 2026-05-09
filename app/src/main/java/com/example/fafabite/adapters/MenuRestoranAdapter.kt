package com.example.fafabite.adapter

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fafabite.R
import com.example.fafabite.api.ProdukItem
import java.text.NumberFormat
import java.util.*

class MenuRestoranAdapter(private val listMakanan: List<ProdukItem>) : RecyclerView.Adapter<MenuRestoranAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tvNamaMakanan)
        val tvStatus: TextView = view.findViewById(R.id.tvStatusMakanan)
        val tvHargaAsli: TextView = view.findViewById(R.id.tvHargaAsli)
        val tvHargaDiskon: TextView = view.findViewById(R.id.tvHargaDiskon)
        val tvInfo: TextView = view.findViewById(R.id.tvInfoStokPickup)
        val ivFoto: ImageView = view.findViewById(R.id.ivFotoMakanan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_makanan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val makanan = listMakanan[position]
        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

        holder.tvNama.text = makanan.namaMakanan

        holder.tvHargaAsli.text = formatRupiah.format(makanan.hargaAsli).replace("Rp", "Rp ")
        holder.tvHargaAsli.paintFlags = holder.tvHargaAsli.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.tvHargaDiskon.text = formatRupiah.format(makanan.hargaDiskon).replace("Rp", "Rp ")

        // Penjual tidak butuh melihat "Nama Toko" di daftar menunya sendiri
        val jamPickup = makanan.waktuPickup?.substringAfter(" ")?.substringBeforeLast(":") ?: "00:00"
        holder.tvInfo.text = "Stok: ${makanan.stok} Porsi • Pickup: $jamPickup"

        if (makanan.stok > 0 && makanan.status.equals("tersedia", ignoreCase = true)) {
            holder.tvStatus.text = "Tersedia"
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"))
            holder.tvStatus.setBackgroundColor(Color.parseColor("#E8F5E9"))
        } else {
            holder.tvStatus.text = "Habis"
            holder.tvStatus.setTextColor(Color.parseColor("#F44336"))
            holder.tvStatus.setBackgroundColor(Color.parseColor("#FFEBEE"))
        }

        if (!makanan.fotoMakanan.isNullOrEmpty()) {
            val baseUrl = "http://192.168.1.61:8000/file-makanan/" // Pastikan IP ini masih sama dengan IP laptopmu saat ini
            Glide.with(holder.itemView.context)
                .load(baseUrl + makanan.fotoMakanan)
                .skipMemoryCache(true)
                .placeholder(R.drawable.bg_input_pill)
                .error(android.R.drawable.ic_menu_report_image)
                .centerCrop()
                .into(holder.ivFoto)
        }
    }

    override fun getItemCount(): Int = listMakanan.size
}