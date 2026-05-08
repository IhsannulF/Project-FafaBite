package com.example.fafabite.api

import com.google.gson.annotations.SerializedName

data class ResponseProfilToko(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("data")
    val data: ProfilToko?
)

data class ProfilToko(
    @field:SerializedName("nama_toko")
    val namaToko: String,

    @field:SerializedName("alamat")
    val alamat: String
)