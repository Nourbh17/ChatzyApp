package com.gl4tp.chatzy.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gl4tp.chatzy.models.Chat
import com.gl4tp.chatzy.repository.ChatRepository
import com.gl4tp.chatzy.response.Message

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val chatRepository = ChatRepository()


    var chatList = MutableLiveData<List<Chat>> (arrayListOf())
    // private set

    fun insertChat (chat: Chat){
        val modifiedChatList = ArrayList<Chat>().apply {
            addAll(chatList.value!!)
        }
        modifiedChatList.add(chat)
        chatList.postValue(modifiedChatList)
    }

    fun createChatCompletion(message: String){

        chatRepository.createChatCompletion(message)

    }

}

