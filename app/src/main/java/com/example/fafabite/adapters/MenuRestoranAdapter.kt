package com.example.fafabite.adapter

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fafabite.EditMakananActivity
import com.example.fafabite.R
import com.example.fafabite.api.ApiConfig
import com.example.fafabite.api.ProdukItem
import com.example.fafabite.ui.MenuRestoranActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu_penjual, parent, false)
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
            val urlFoto = ApiConfig.IMAGE_URL + makanan.fotoMakanan
            Glide.with(holder.itemView.context)
                .load(urlFoto)
                .skipMemoryCache(true)
                .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.NONE)
                .placeholder(R.drawable.bg_input_pill)
                .error(android.R.drawable.ic_menu_report_image)
                .centerCrop()
                .into(holder.ivFoto)
        }

        // --- BOTTOM SHEET DIALOG UNTUK EDIT/HAPUS ---
        holder.itemView.setOnClickListener {
            val dialog = BottomSheetDialog(holder.itemView.context)
            val view = LayoutInflater.from(holder.itemView.context).inflate(R.layout.dialog_edit_menu, null)

            val btnEdit = view.findViewById<Button>(R.id.btnDialogEdit)
            val btnHapus = view.findViewById<Button>(R.id.btnDialogHapus)

            // Aksi Edit
            btnEdit.setOnClickListener {
                val intent = Intent(holder.itemView.context, EditMakananActivity::class.java)
                intent.putExtra("ID_MAKANAN", makanan.id)
                intent.putExtra("NAMA_MAKANAN", makanan.namaMakanan)
                intent.putExtra("HARGA_ASLI", makanan.hargaAsli)
                intent.putExtra("HARGA_DISKON", makanan.hargaDiskon)
                intent.putExtra("STOK_MAKANAN", makanan.stok)
                intent.putExtra("WAKTU_PICKUP", makanan.waktuPickup)
                intent.putExtra("STATUS_MAKANAN", makanan.status)
                intent.putExtra("FOTO_MAKANAN", makanan.fotoMakanan)
                holder.itemView.context.startActivity(intent)
                dialog.dismiss()
            }

            // Aksi Hapus
            btnHapus.setOnClickListener {
                dialog.dismiss() // Tutup Bottom Sheet dulu

                // Panggil pop-up konfirmasi yang ada di MenuRestoranActivity
                if (holder.itemView.context is MenuRestoranActivity) {
                    (holder.itemView.context as MenuRestoranActivity).konfirmasiHapusMakanan(makanan.id, makanan.namaMakanan ?: "Menu ini")
                }
            }

            dialog.setContentView(view)
            dialog.show()
        }
    }

    override fun getItemCount(): Int = listMakanan.size
}