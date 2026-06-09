package com.example.fafabite.models

import com.google.gson.annotations.SerializedName

data class ResponsePesananResto(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<PesananRestoItem>
)

data class PesananRestoItem(
    @SerializedName("id") val id: Int,
    @SerializedName("nomor_order") val nomorOrder: String?,
    @SerializedName("jumlah_pesan") val jumlahPesan: Int,
    @SerializedName("total_harga") val totalHarga: Int,
    @SerializedName("status_pesanan") val statusPesanan: String,
    @SerializedName("created_at") val createdAt: String?,
    // Relasi dari Laravel otomatis menjadi objek di Kotlin
    @SerializedName("user") val user: UserPembeli?,
    @SerializedName("produk") val produk: ProdukDipesan?
)

data class UserPembeli(
    @SerializedName("name") val name: String
)

data class ProdukDipesan(
    @SerializedName("nama_makanan") val namaMakanan: String
)