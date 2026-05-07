package com.example.fafabite.api

import com.google.gson.annotations.SerializedName

data class ResponseDashboard(
    @SerializedName("sukses") val sukses: Boolean,
    @SerializedName("pesan") val pesan: String? = null,
    @SerializedName("data") val data: DataDashboard? = null
)

data class DataDashboard(
    @SerializedName("nama_toko") val namaToko: String,
    @SerializedName("pendapatan_hari_ini") val pendapatanHariIni: Int,
    @SerializedName("pesanan_baru") val pesananBaru: Int,
    @SerializedName("stok_terjual") val stokTerjual: Int,
    @SerializedName("sisa_stok") val sisaStok: Int,
    @SerializedName("order_butuh_perhatian") val orderButuhPerhatian: List<OrderPerhatian>
)

data class OrderPerhatian(
    @SerializedName("nomor_order") val nomorOrder: String,
    @SerializedName("nama_pemesan") val namaPemesan: String,
    @SerializedName("detail_pesanan") val detailPesanan: String,
    @SerializedName("status") val status: String
)