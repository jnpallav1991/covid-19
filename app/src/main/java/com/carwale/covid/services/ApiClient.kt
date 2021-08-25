package com.carwale.covid.services

import com.pjtech.covid.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient {

    companion object {

        private const val BASE_URL = "https://api.covid19api.com/"
    }

    private var apiService: ApiInterface

    private val httpClient = OkHttpClient.Builder()

    private val interceptor = HttpLoggingInterceptor()

    constructor() {
        //.readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        }
        httpClient.addInterceptor(interceptor).connectTimeout(100, TimeUnit.SECONDS)

        httpClient.addInterceptor { chain ->
            val request =
                chain.request().newBuilder().build()
            chain.proceed(request)
        }

        val retrofit by lazy {
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build()).build()
        }

        apiService = retrofit.create(ApiInterface::class.java)
    }


    fun getRestInterface(): ApiInterface {
        return apiService
    }
}