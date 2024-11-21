//package com.example.virtuecase.network
//
//import okhttp3.OkHttpClient
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//object RetrofitInstance {
//    private const val BASE_URL = "https://your.api.base.url/" // Replace with your actual base URL
//
//    private val retrofit: Retrofit by lazy {
//        val client = OkHttpClient.Builder().build()
//
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(client)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    val apiService: ApiService by lazy {
//        retrofit.create(ApiService::class.java)
//    }
//}

////THIS FILE IS VERY USELESS IF YOU NOT USING ANY API'S !!!!!!!!!!!!!!