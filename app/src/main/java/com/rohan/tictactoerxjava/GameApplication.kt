package com.rohan.tictactoerxjava

import android.app.Application
import com.rohan.tictactoerxjava.networkservice.GameNetworkService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class GameApplication : Application() {

    var networkService: GameNetworkService = Retrofit.Builder()
        .baseUrl("https://www.google.com")
        .addConverterFactory(MoshiConverterFactory.create())
        .build().create(GameNetworkService::class.java)

    companion object {
        @JvmStatic
        private var instance: GameApplication? = null

        @JvmStatic
        fun getInstance(): GameApplication {
            return instance!!
        }
    }


    override fun onCreate() {
        super.onCreate()
        instance = this

    }

}