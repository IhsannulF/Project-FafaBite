package com.example.fafabite.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    // Base URL untuk tembak API
    private const val BASE_URL = "http://192.168.1.13:8000/api/"

    // URL untuk foto menu makanan toko
    const val IMAGE_URL = "http://192.168.1.13:8000/file-makanan/"

    // URL BARU untuk tarik foto profil pengguna
    const val URL_FOTO = "http://192.168.1.13:8000/storage/profil/"

    fun getApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }
}