package com.example.fafabite.api

import com.example.fafabite.models.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    // --- Fitur Login ---
    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") pass: String
    ): Call<LoginResponse>

    // --- Fitur Register ---
    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("nama_lengkap") namaLengkap: String,
        @Field("email") email: String,
        @Field("password") pass: String,
        @Field("role") role: String,
        @Field("nama_toko") namaToko: String,
        @Field("alamat") alamat: String
    ): Call<LoginResponse>

    // --- Fitur Tambah Produk (Yang baru kita buat) ---
    @Multipart
    @POST("tambah-produk") // Nama rute pintu gerbang di Laravel
    fun uploadProduk(
        @Part("id_toko") idToko: RequestBody,
        @Part("nama_makanan") namaMakanan: RequestBody,
        @Part("harga_asli") hargaAsli: RequestBody,
        @Part("harga_diskon") hargaDiskon: RequestBody,
        @Part("stok") stok: RequestBody,
        @Part("waktu_pickup") waktuPickup: RequestBody,
        @Part("status") status: RequestBody,
        @Part foto_makanan: MultipartBody.Part // Khusus untuk file foto
    ): Call<ResponseProduk>
}