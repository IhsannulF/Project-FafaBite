package com.example.fafabite.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    // GANTI ANGKA INI DENGAN IP LAPTOP KAMU! (Contoh: 192.168.1.5)
    // Jangan lupa akhiri dengan garis miring (/)
    private const val BASE_URL = "http://192.168.1.6/fafabite_api/"

    fun getApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }
}