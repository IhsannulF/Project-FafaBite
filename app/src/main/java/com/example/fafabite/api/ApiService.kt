package com.example.fafabite.api

import com.example.fafabite.models.LoginResponse
import com.example.fafabite.models.ResponseCheckout
import com.example.fafabite.models.ResponsePesananResto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    // --- Fitur Login ---
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") pass: String
    ): Call<LoginResponse>

    // --- Fitur Register ---
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("register")
    fun registerUser(
        @Field("nama") nama: String?,             // Untuk pembeli
        @Field("email") email: String,
        @Field("password") pass: String,
        @Field("role") role: String,              // "pembeli" atau "penjual"
        @Field("nama_toko") namaToko: String?,    // Untuk penjual
        @Field("alamat") alamat: String?          // Untuk penjual
    ): Call<LoginResponse>

    // --- Fitur Tambah Produk ---
    @Headers("Accept: application/json")
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

    // --- Fitur Dashboard Penjual ---
    @Headers("Accept: application/json")
    @GET("dashboard-penjual/{id_toko}")
    fun getDashboardPenjual(
        @Path("id_toko") idToko: String
    ): Call<ResponseDashboard>

    // --- Fitur Ambil Daftar Menu Restoran ---
    @Headers("Accept: application/json")
    @GET("produk/toko/{id_toko}")
    fun getProdukByToko(
        @Path("id_toko") idToko: String
    ): Call<ResponseListProduk>

    // --- Fitur Ambil Pesanan Masuk (Sisi Restoran) ---
    @Headers("Accept: application/json")
    @GET("pesanan/toko/{id_toko}")
    fun getPesananToko(
        @Path("id_toko") idToko: Int
    ): Call<ResponsePesananResto>

    // --- Fitur Update Status Pesanan (Terima / Tolak) ---
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("pesanan/update-status/{id}")
    fun updateStatusPesanan(
        @Path("id") idPesanan: Int,
        @Field("status_pesanan") statusBaru: String
    ): Call<ResponseCheckout>

    // --- Ambil Data Makanan untuk Beranda Pembeli ---
    @GET("beranda/makanan") // Pastikan URL ini sesuai dengan route di Laravel-mu
    fun getBerandaMakanan(): Call<ResponseBerandaMakanan>

    @Multipart
    @POST("produk/update/{id}")
    fun updateProduk(
        @Path("id") id: Int,
        @Part("nama_makanan") namaMakanan: RequestBody,
        @Part("harga_asli") hargaAsli: RequestBody,
        @Part("harga_diskon") hargaDiskon: RequestBody,
        @Part("stok") stok: RequestBody,
        @Part("waktu_pickup") waktuPickup: RequestBody,
        @Part("status") status: RequestBody,
        @Part fotoMakanan: MultipartBody.Part? // Boleh kosong jika tidak ganti foto
    ): Call<ResponseProduk>

    // --- Fitur Ambil Riwayat Pesanan Pembeli ---
    @Headers("Accept: application/json")
    @GET("riwayat-pesanan/{id_user}")
    fun getRiwayatPesanan(
        @Path("id_user") idUser: Int
    ): Call<ResponseRiwayatPesanan>

    // --- Fitur Checkout & Potong Saldo ---
    @FormUrlEncoded
    @POST("checkout")
    fun prosesCheckout(
        @Field("id_user") idUser: Int,
        @Field("id_produk") idProduk: Int,
        @Field("jumlah_pesan") jumlahPesan: Int
    ): Call<ResponseCheckout>

    // --- Fitur Hapus Makanan (Sisi Restoran) ---
    @DELETE("produk/{id}")
    fun hapusMakanan(
        @Path("id") idMakanan: Int
    ): Call<ResponseCheckout>

    // --- Fitur Top Up FafaPay ---
    @FormUrlEncoded
    @POST("topup")
    fun topUpFafaPay(
        @Field("id_user") idUser: Int,
        @Field("nominal") nominal: Int
    ): Call<com.example.fafabite.models.ResponseCheckout>

    // --- Fitur Ambil Profil Toko ---
    @Headers("Accept: application/json")
    @GET("toko/profil/{id_toko}")
    fun getProfilToko(
        @Path("id_toko") idToko: Int
    ): Call<ResponseProfilToko>

    // --- Fitur Ambil Profil & Saldo User (Pembeli & Penjual) ---
    @Headers("Accept: application/json")
    @GET("user/{id}")
    fun getProfilUser(
        @Path("id") idUser: Int
    ): Call<ResponseUser>

    // --- Fitur Update Profil User ---
    @Multipart
    @POST("update-profil")
    fun updateProfilUser(
        @Part("id_user") idUser: RequestBody,
        @Part("nama") nama: RequestBody,
        @Part("email") email: RequestBody,
        @Part("alamat") alamat: RequestBody?,
        @Part foto: MultipartBody.Part?
    ): Call<ResponseProfilToko>

    // --- Fitur Tarik Dana Penjual ---
    @FormUrlEncoded
    @POST("tarik-dana")
    fun tarikDana(
        @Field("id_user") idUser: Int,
        @Field("nominal") nominal: Int
    ): Call<ResponseCheckout>
}