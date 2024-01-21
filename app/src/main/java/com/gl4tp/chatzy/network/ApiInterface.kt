package com.gl4tp.chatzy.network

import com.gl4tp.chatzy.response.ChatRequest
import com.gl4tp.chatzy.response.ChatResponse
import com.gl4tp.chatzy.utils.OPENAI_API_KEY
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/*interface ApiInterface {

    @POST("chat/completions?Content-Type=application/json&Authorization=Bearer $OPENAI_API_KEY")
    fun createChatCompletion(
        @Body chatRequest : ChatRequest,
        @Header("Content-Type") contentTypes: String = "application/json",
        @Header("Authorization") Authorization: String = "Bearer $OPENAI_API_KEY"


        ) : Call<ChatResponse>




}*/
interface ApiInterface {

    @POST("chat/completions")
    fun createChatCompletion(
        @Header("Authorization") authorization: String ,
        @Body chatRequest: ChatRequest
    ): Call<ChatResponse>
}