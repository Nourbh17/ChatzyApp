package com.gl4tp.chatzy.repository

import android.util.Log
import com.gl4tp.chatzy.network.ApiClient
import com.gl4tp.chatzy.response.ChatRequest
import com.gl4tp.chatzy.response.ChatResponse
import com.gl4tp.chatzy.response.Message
import com.gl4tp.chatzy.utils.CHAT_GPT_MODEL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ChatRepository {


    private val apiClient = ApiClient.retrofitService

    fun createChatCompletion(message : String){

       /* CoroutineScope(Dispatchers.IO)
            .launch {*/
                try{
                    val chatRequest = ChatRequest(arrayListOf(
                        Message("","system"),
                        Message(message,"user"),
                    ),
                        CHAT_GPT_MODEL

                    )
                    apiClient.createChatCompletion(chatRequest).enqueue(object : Callback<ChatResponse>{
                            override fun onResponse(
                                call: Call<ChatResponse>,
                                response: Response<ChatResponse>
                            ) {
                                val code = response.code()
                                Log.d("chat request","$chatRequest")
                                Log.d("chat resp","$response")
                                if(response.isSuccessful){
                                    val message = response.body()?.choices?.get(0)?.message

                                    Log.d("message", message?.toString() ?: "Message is null")


                                }
                                else {
                                    Log.d("Error",response.errorBody().toString())
                                }
                            }

                            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                                t.printStackTrace()
                            }

                        })


                }
                catch (e: Exception){
                    e.printStackTrace()
                }

    }

}