package com.bravostudio.humansinspace

import android.app.Application
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private lateinit var mRetrofit: Retrofit

    private const val baseUrl = "http://api.open-notify.org/"
    private const val cacheSize = (1 * 1024 * 1024).toLong() // 1MB

    fun init(application: Application) {
        val myCache = Cache(application.applicationContext.cacheDir, cacheSize)
        val mOkHttpClient = OkHttpClient.Builder()
            .cache(myCache)
            .build()

        mRetrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(mOkHttpClient)
            .build()
    }

    fun getInstance(): Retrofit {
        return mRetrofit
    }

}