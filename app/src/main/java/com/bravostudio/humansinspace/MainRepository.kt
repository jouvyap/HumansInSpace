package com.bravostudio.humansinspace

import retrofit2.Response
import retrofit2.http.GET

interface MainRepository {

    @GET("astros.json")
    suspend fun getAstronauts() : Response<MainDataModel>

}