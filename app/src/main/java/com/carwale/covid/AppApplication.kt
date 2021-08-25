package com.carwale.covid

import android.app.Application
import android.content.Context
import com.carwale.covid.services.ApiClient

class AppApplication : Application() {
    companion object {

        lateinit var appContext: Context
        fun getApiClient(): ApiClient {
            return ApiClient()
        }

    }

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext
        getApiClient()
    }

}