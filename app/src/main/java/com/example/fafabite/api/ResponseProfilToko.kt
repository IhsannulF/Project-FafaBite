package com.example.fafabite.api

import com.google.gson.annotations.SerializedName

data class ResponseProfilToko(
    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("data")
    val data: DataToko? = null
)

data class DataToko(
    @field:SerializedName("id_toko")
    val idToko: Int? = null,

    @field:SerializedName("id_user")
    val idUser: Int? = null,

    @field:SerializedName("nama_toko")
    val namaToko: String? = null,

    @field:SerializedName("alamat")
    val alamat: String? = null
)