package com.gl4tp.chatzy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.gl4tp.chatzy.R
import com.gl4tp.chatzy.adapters.ChatAdapater
import com.gl4tp.chatzy.models.Chat
import com.gl4tp.chatzy.viewModels.ChatViewModel
import java.util.Date

class ChatScreenFragment : Fragment() {

    private val chatList = arrayListOf(
        Chat(
            "1",
            "Hi, how can I integrate Retrofit in Android development?",
            "sender",
            Date()
        ),
        Chat(
            "2",
            "You can integrate Retrofit by adding it as a dependency in your app's build.gradle file.",
            "receiver",
            Date()
        ),
        Chat(
            "3",
            "What is the purpose of ViewModel in MVVM architecture?",
            "sender",
            Date()
        ),
        Chat(
            "4",
            "ViewModel is used to store and manage UI-related data, separating it from the UI components.",
            "receiver",
            Date()
        ),
        Chat(
            "5",
            "What is the purpose of ViewModel in MVVM architecture?",
            "sender",
            Date()
        ),
        Chat(
            "6",
            "ViewModel is used to store and manage UI-related data, separating it from the UI components.",
            "receiver",
            Date()
        ),
        Chat(
            "7",
            "How do I implement a RecyclerView in Android?",
            "sender",
            Date()
        ),
        Chat(
            "8",
            "You can create a RecyclerView by defining a layout, adapter, and connecting it to your data source.",
            "receiver",
            Date()
        ),
        Chat(
            "9",
            "Can you suggest a good Android IDE?",
            "sender",
            Date()
        ),
        Chat(
            "10",
            "Android Studio is the official IDE for Android app development and is highly recommended.",
            "receiver",
            Date()
        ),
        Chat(
            "11",
            "How can I implement a swipe-to-refresh feature in my Android app?",
            "sender",
            Date()
        ),
        Chat(
            "12",
            "You can implement swipe-to-refresh by using the SwipeRefreshLayout widget in your layout XML and handling refresh events in your code.",
            "receiver",
            Date()
        )
    )

    private val chatViewModel : ChatViewModel by lazy {
        ViewModelProvider(this)[ChatViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_chat_screen, container, false)

        val chatRV = view.findViewById<RecyclerView>(R.id.chatRV)
        val chatAdapter = ChatAdapater()
        chatRV.adapter = chatAdapter
        //chatAdapter.submitList(chatList)

        val sendIBtn = view.findViewById<ImageButton>(R.id.sendImage)
        var counter = -1
        sendIBtn.setOnClickListener{
            counter += 1
            if(counter >= chatList.size){
                return@setOnClickListener
            }
            chatViewModel.insertChat(chatList[counter])
        }
        chatViewModel.chatList.observe(viewLifecycleOwner){
            chatAdapter.submitList(it)
            chatRV.smoothScrollToPosition(it.size)
        }



        return view
    }



}