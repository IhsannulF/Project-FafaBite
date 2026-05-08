package com.example.fafabite.adapter

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fafabite.R
import com.example.fafabite.api.ProdukItem
import java.text.NumberFormat
import java.util.*
import com.bumptech.glide.Glide

class MakananAdapter(private val listMakanan: List<ProdukItem>) : RecyclerView.Adapter<MakananAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tvNamaMakanan)
        val tvStatus: TextView = view.findViewById(R.id.tvStatusMakanan)
        val tvHargaAsli: TextView = view.findViewById(R.id.tvHargaAsli)
        val tvHargaDiskon: TextView = view.findViewById(R.id.tvHargaDiskon)
        val tvInfo: TextView = view.findViewById(R.id.tvInfoStokPickup)
        val ivFoto: ImageView = view.findViewById(R.id.ivFotoMakanan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Mengambil cetakan XML yang tadi kita buat
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_makanan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val makanan = listMakanan[position]
        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

        // 1. Set Nama Makanan
        holder.tvNama.text = makanan.namaMakanan

        // 2. Set Harga (Dengan efek coret pada harga asli)
        holder.tvHargaAsli.text = formatRupiah.format(makanan.hargaAsli).replace("Rp", "Rp ")
        holder.tvHargaAsli.paintFlags = holder.tvHargaAsli.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG // Efek coret

        holder.tvHargaDiskon.text = formatRupiah.format(makanan.hargaDiskon).replace("Rp", "Rp ")

        // 3. Set Info Stok dan Pickup
        // Kita ambil jamnya saja dari format datetime (misal "2026-05-07 19:00:00" -> "19:00")
        val jamPickup = makanan.waktuPickup.substringAfter(" ").substringBeforeLast(":")
        holder.tvInfo.text = "Stok: ${makanan.stok} Porsi • Pickup: $jamPickup"

        // 4. Logika Badge Status (Tersedia vs Habis)
        if (makanan.stok > 0 && makanan.status.equals("tersedia", ignoreCase = true)) {
            holder.tvStatus.text = "Tersedia"
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")) // Hijau
            holder.tvStatus.setBackgroundColor(Color.parseColor("#E8F5E9"))
        } else {
            holder.tvStatus.text = "Habis"
            holder.tvStatus.setTextColor(Color.parseColor("#F44336")) // Merah
            holder.tvStatus.setBackgroundColor(Color.parseColor("#FFEBEE"))
        }

        // 5. Foto Makanan
        if (!makanan.fotoMakanan.isNullOrEmpty()) {
            // Menggunakan IP Adress laptop
            val baseUrl = "http://192.168.1.82:8000/file-makanan/"
            val urlFoto = baseUrl + makanan.fotoMakanan

            Glide.with(holder.itemView.context)
                .load(urlFoto)
                .skipMemoryCache(true)
                .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.NONE) // PAKSA JANGAN SIMPAN DI HP
                .placeholder(R.drawable.bg_input_pill)
                .error(android.R.drawable.ic_menu_report_image)
                .centerCrop()
                .into(holder.ivFoto)
        }
    }

    override fun getItemCount(): Int {
        return listMakanan.size
    }
}