package com.example.fafabite.api

import com.google.gson.annotations.SerializedName

// Cetakan utama pembungkus respons JSON
data class ResponsePesanan(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: List<PesananItem>
)

// Cetakan rincian masing-masing pesanan
data class PesananItem(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("nomor_order")
    val nomorOrder: String,

    @field:SerializedName("jumlah_pesan")
    val jumlahPesan: Int,

    @field:SerializedName("total_harga")
    val totalHarga: Int,

    @field:SerializedName("status_pesanan")
    val statusPesanan: String,

    @field:SerializedName("user")
    val user: UserPesanan?,

    @field:SerializedName("produk")
    val produk: ProdukPesanan?
)

// Cetakan untuk mengambil nama pembeli dari tabel relasi
data class UserPesanan(
    @field:SerializedName("name")
    val name: String
)

// Cetakan untuk mengambil detail makanan dari tabel relasi
data class ProdukPesanan(
    @field:SerializedName("nama_makanan")
    val namaMakanan: String,

    @field:SerializedName("waktu_pickup")
    val waktuPickup: String,

    @field:SerializedName("harga_diskon")
    val hargaDiskon: Int
)

// Cetakan simpel untuk respons saat tombol Terima/Tolak ditekan
data class ResponseUpdateStatus(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String
)