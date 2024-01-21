package com.gl4tp.chatzy.network

import com.gl4tp.chatzy.utils.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object ApiClient {

    private val  okHttpClient = OkHttpClient
        .Builder()
        .connectTimeout(1,TimeUnit.MINUTES)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60,TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
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


