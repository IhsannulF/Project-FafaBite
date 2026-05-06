package com.example.fafabite.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    // Pastikan persis seperti ini: ada http:// di depan dan diakhiri garis miring /
    private const val BASE_URL = "http://192.168.2.192:8000/api/"

    fun getApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }
}