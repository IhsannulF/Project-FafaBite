package com.example.fafabite.api

import com.google.gson.annotations.SerializedName

data class ResponseListProduk(
    @SerializedName("sukses") val sukses: Boolean,
    @SerializedName("pesan") val pesan: String,
    @SerializedName("data") val data: List<ProdukItem>
)

data class ProdukItem(
    @SerializedName("id") val id: Int,
    @SerializedName("nama_makanan") val namaMakanan: String,
    @SerializedName("harga_asli") val hargaAsli: Int,
    @SerializedName("harga_diskon") val hargaDiskon: Int,
    @SerializedName("stok") val stok: Int,
    @SerializedName("waktu_pickup") val waktuPickup: String,
    @SerializedName("status") val status: String,
    @SerializedName("foto_makanan") val fotoMakanan: String?
)