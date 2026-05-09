package com.example.fafabite.adapter

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fafabite.CheckoutActivity
import com.example.fafabite.R
import com.example.fafabite.api.MakananBeranda
import java.text.NumberFormat
import java.util.*

class MakananSekitarAdapter(private val listMakanan: List<MakananBeranda>) : RecyclerView.Adapter<MakananSekitarAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tvNamaMakanan)
        val tvStatus: TextView = view.findViewById(R.id.tvStatusMakanan)
        val tvHargaAsli: TextView = view.findViewById(R.id.tvHargaAsli)
        val tvHargaDiskon: TextView = view.findViewById(R.id.tvHargaDiskon)
        val tvInfo: TextView = view.findViewById(R.id.tvInfoStokPickup)
        val ivFoto: ImageView = view.findViewById(R.id.ivFotoMakanan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // INI YANG BEDA: Memanggil layout vertikal
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_makanan_sekitar, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val makanan = listMakanan[position]
        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

        holder.tvNama.text = makanan.namaMakanan
        holder.tvHargaAsli.text = formatRupiah.format(makanan.hargaAsli).replace("Rp", "Rp ")
        holder.tvHargaAsli.paintFlags = holder.tvHargaAsli.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.tvHargaDiskon.text = formatRupiah.format(makanan.hargaDiskon).replace("Rp", "Rp ")

        val jamPickup = makanan.waktuPickup?.substringAfter(" ")?.substringBeforeLast(":") ?: "00:00"
        holder.tvInfo.text = "${makanan.namaToko} • Stok: ${makanan.stok} • Pickup: $jamPickup"

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
            val baseUrl = "http://192.168.1.61:8000/file-makanan/"
            Glide.with(holder.itemView.context)
                .load(baseUrl + makanan.fotoMakanan)
                .skipMemoryCache(true)
                .placeholder(R.drawable.bg_input_pill)
                .error(android.R.drawable.ic_menu_report_image)
                .centerCrop()
                .into(holder.ivFoto)
        }

        // Logika saat kartu makanan diklik
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, CheckoutActivity::class.java)
            intent.putExtra("ID_MAKANAN", makanan.id)
            intent.putExtra("NAMA_MAKANAN", makanan.namaMakanan)
            intent.putExtra("HARGA_MAKANAN", makanan.hargaDiskon) // Kita pakai harga diskon
            intent.putExtra("FOTO_MAKANAN", makanan.fotoMakanan)
            intent.putExtra("NAMA_TOKO", makanan.namaToko)
            intent.putExtra("STOK_MAKANAN", makanan.stok)
            holder.itemView.context.startActivity(intent)
        }    }

    override fun getItemCount(): Int = listMakanan.size
}