package com.example.perpuletask.DI.Network

import com.example.perpuletask.DI.Models.AudioModel
import com.example.perpuletask.DI.Models.DataModel
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET


interface ApiService {

    @GET("audio")
    fun getAudio(): Single<Response<DataModel>>


}

