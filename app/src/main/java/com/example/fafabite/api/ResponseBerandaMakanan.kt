package com.example.fafabite.api

import com.google.gson.annotations.SerializedName

data class ResponseBerandaMakanan(
    @field:SerializedName("sukses") val sukses: Boolean,
    @field:SerializedName("pesan") val pesan: String,
    @field:SerializedName("data") val data: DataBeranda? // Data sekarang berupa Object, bukan List
)

// Wadah untuk memisahkan 2 list
data class DataBeranda(
    @field:SerializedName("flash_sale") val flashSale: List<MakananBeranda>,
    @field:SerializedName("sekitar_kita") val sekitarKita: List<MakananBeranda>
)

data class MakananBeranda(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("nama_makanan") val namaMakanan: String,
    @field:SerializedName("harga_asli") val hargaAsli: Int,
    @field:SerializedName("harga_diskon") val hargaDiskon: Int,
    @field:SerializedName("stok") val stok: Int,
    @field:SerializedName("nama_toko") val namaToko: String,
    @field:SerializedName("waktu_pickup") val waktuPickup: String?,
    @field:SerializedName("status") val status: String,
    @field:SerializedName("foto_makanan") val fotoMakanan: String?
)