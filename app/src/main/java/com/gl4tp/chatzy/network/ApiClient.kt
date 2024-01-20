package com.gl4tp.chatzy.network

import com.gl4tp.chatzy.utils.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiClient {

    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        // we need to add converter factory to
        // convert JSON object to Java object
        .build()
    /**
     * A public Api object that exposes the lazy-initialized Retrofit service
     */
    val retrofitService : ApiInterface by lazy { retrofit.create(ApiInterface::class.java) }

    /*fun getInstance (): ApiInterface{
        synchronized(this){
            return retrofitService
        }*/
    }


