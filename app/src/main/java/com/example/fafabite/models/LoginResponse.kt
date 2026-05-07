package com.example.fafabite.models // Sesuaikan nama folder/package-nya jika beda

import com.google.gson.annotations.SerializedName

// Ini untuk menangkap bingkai utama (sukses, pesan, data)
data class LoginResponse(
    @field:SerializedName("sukses")
    val sukses: Boolean,

    @field:SerializedName("pesan")
    val pesan: String,

    @field:SerializedName("data")
    val data: UserData? // Memanggil data class di bawah
)

// Ini untuk menangkap rincian data user-nya
data class UserData(
    @field:SerializedName("id_user")
    val id_user: Int,

    @field:SerializedName("nama_lengkap")
    val nama_lengkap: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("role")
    val role: String
)