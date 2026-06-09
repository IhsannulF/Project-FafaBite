package com.example.fafabite.api

import com.google.gson.annotations.SerializedName

data class ResponseRiwayatPesanan(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<RiwayatPesananItem>
)

data class RiwayatPesananItem(
    @SerializedName("id") val id: Int,
    @SerializedName("id_toko") val idToko: Int,
    @SerializedName("id_user") val idUser: Int,
    @SerializedName("id_produk") val idProduk: Int,
    @SerializedName("nomor_order") val nomorOrder: String?,
    @SerializedName("jumlah_pesan") val jumlahPesan: Int,
    @SerializedName("total_harga") val totalHarga: Int,
    @SerializedName("status_pesanan") val statusPesanan: String,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("nama_toko") val namaToko: String,
    @SerializedName("nama_makanan") val namaMakanan: String,
    @SerializedName("foto_makanan") val fotoMakanan: String?
)