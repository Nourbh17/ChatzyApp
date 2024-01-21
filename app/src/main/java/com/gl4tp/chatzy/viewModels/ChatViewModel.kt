package com.gl4tp.chatzy.viewModels


import ChatRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gl4tp.chatzy.models.Chat

import com.gl4tp.chatzy.response.Message

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val chatRepository = ChatRepository(application)
    val  chatStateFlow get() = chatRepository.chatStateFlow




    fun createChatCompletion(message: String,robotId:String){

        chatRepository.createChatCompletion(message,robotId)

    }
    fun getChatList(robotId:String){

        chatRepository.getChatList(robotId)

    }


}

