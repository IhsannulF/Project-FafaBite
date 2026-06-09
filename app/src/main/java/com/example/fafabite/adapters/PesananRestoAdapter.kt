package com.example.fafabite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fafabite.R
import com.example.fafabite.models.PesananRestoItem
import java.text.NumberFormat
import java.util.Locale

class PesananRestoAdapter(
    private var listPesanan: List<PesananRestoItem>,
    private val aksiListener: OnAksiPesananListener
) : RecyclerView.Adapter<PesananRestoAdapter.ViewHolder>() {

    interface OnAksiPesananListener {
        fun onTerima(pesanan: PesananRestoItem)
        fun onTolak(pesanan: PesananRestoItem)
        fun onSiapDiambil(pesanan: PesananRestoItem)
        fun onSelesai(pesanan: PesananRestoItem) // Tambahan aksi selesai
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvKodeOrder: TextView = view.findViewById(R.id.tvKodeOrderResto)
        val tvNamaPembeli: TextView = view.findViewById(R.id.tvNamaPembeli)
        val tvWaktu: TextView = view.findViewById(R.id.tvWaktuPesanResto)
        val tvMenu: TextView = view.findViewById(R.id.tvMenuDipesan)
        val tvTotal: TextView = view.findViewById(R.id.tvTotalPendapatan)

        val layoutAksiMenunggu: LinearLayout = view.findViewById(R.id.layoutAksiMenunggu)
        val btnTerima: Button = view.findViewById(R.id.btnTerimaPesanan)
        val btnTolak: Button = view.findViewById(R.id.btnTolakPesanan)
        val btnSiap: Button = view.findViewById(R.id.btnSiapDiambil)
        val btnSelesai: Button = view.findViewById(R.id.btnSelesaikanPesanan) // Tombol baru
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pesanan_resto, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listPesanan[position]
        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

        holder.tvKodeOrder.text = item.nomorOrder ?: "FAFA-XXXX"
        holder.tvNamaPembeli.text = item.user?.name ?: "Pembeli Tanpa Nama"
        holder.tvMenu.text = "${item.jumlahPesan}x ${item.produk?.namaMakanan}"
        holder.tvTotal.text = "Total Pendapatan: " + formatRupiah.format(item.totalHarga).replace("Rp", "Rp ")

        val tanggalInput = item.createdAt?.substringBefore("T") ?: "-"
        val jamInput = item.createdAt?.substringAfter("T")?.substringBeforeLast(".") ?: "-"
        holder.tvWaktu.text = "$tanggalInput • $jamInput"

        // LOGIKA PENAMPILAN TOMBOL BERDASARKAN STATUS
        when (item.statusPesanan.lowercase()) {
            "menunggu" -> {
                holder.layoutAksiMenunggu.visibility = View.VISIBLE
                holder.btnSiap.visibility = View.GONE
                holder.btnSelesai.visibility = View.GONE
            }
            "disiapkan", "diproses" -> {
                holder.layoutAksiMenunggu.visibility = View.GONE
                holder.btnSiap.visibility = View.VISIBLE
                holder.btnSelesai.visibility = View.GONE
            }
            "siap_diambil" -> {
                holder.layoutAksiMenunggu.visibility = View.GONE
                holder.btnSiap.visibility = View.GONE
                holder.btnSelesai.visibility = View.VISIBLE // Hanya tombol selesai yang muncul
            }
            else -> {
                holder.layoutAksiMenunggu.visibility = View.GONE
                holder.btnSiap.visibility = View.GONE
                holder.btnSelesai.visibility = View.GONE
            }
        }

        // Tangkap event klik tombol
        holder.btnTerima.setOnClickListener { aksiListener.onTerima(item) }
        holder.btnTolak.setOnClickListener { aksiListener.onTolak(item) }
        holder.btnSiap.setOnClickListener { aksiListener.onSiapDiambil(item) }
        holder.btnSelesai.setOnClickListener { aksiListener.onSelesai(item) } // Aksi klik selesai
    }

    override fun getItemCount(): Int = listPesanan.size

    fun perbaruiData(newList: List<PesananRestoItem>) {
        this.listPesanan = newList
        notifyDataSetChanged()
    }
}