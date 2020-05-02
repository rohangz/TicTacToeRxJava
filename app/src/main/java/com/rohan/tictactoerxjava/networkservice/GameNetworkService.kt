package com.rohan.tictactoerxjava.networkservice

import com.rohan.tictactoerxjava.model.GameUserModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface GameNetworkService  {

    @GET
    suspend fun getUserData(@Url url: String): Response<GameUserModel>
}