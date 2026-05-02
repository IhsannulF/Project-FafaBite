package com.example.fafabite.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("sukses")
    val sukses: Boolean,

    @field:SerializedName("pesan")
    val pesan: String,

    @field:SerializedName("data")
    val data: UserData? = null
)

data class UserData(
    @field:SerializedName("id_user")
    val idUser: Int,

    @field:SerializedName("nama_lengkap")
    val namaLengkap: String,

    @field:SerializedName("role")
    val role: String
)