package com.example.fafabite.api

import com.google.gson.annotations.SerializedName

data class ResponseUser(
    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("data")
    val data: DataUser? = null
)

data class DataUser(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("role")
    val role: String? = null,

    @field:SerializedName("saldo")
    val saldo: Int? = null,

    @field:SerializedName("foto_profil")
    val fotoProfil: String? = null
)