package com.gl4tp.chatzy.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.gl4tp.chatzy.R
import com.gl4tp.chatzy.adapters.ChatAdapater
import com.gl4tp.chatzy.utils.Status

import com.gl4tp.chatzy.utils.copyToClipBoard
import com.gl4tp.chatzy.utils.hideKeyBoard
import com.gl4tp.chatzy.utils.longToastShow
import com.gl4tp.chatzy.utils.shareMsg
import com.gl4tp.chatzy.viewModels.ChatViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ChatScreenFragment : Fragment() {



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

        val chatAdapter = ChatAdapater() { message, textView ->
            val popup = PopupMenu(context, textView)

            try {
                val fields = popup.javaClass.declaredFields
                for (field in fields) {
                    if ("mPopup" == field.name) {
                        field.isAccessible = true
                        val menuPopupHelper = field.get(popup)
                        val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                        val setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon",
                            Boolean::class.javaPrimitiveType
                        )
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            popup.menuInflater.inflate(R.menu.option_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when(item.itemId){
                    R.id.copyMenu -> {
                        view.context.copyToClipBoard(message)
                        return@setOnMenuItemClickListener true
                    }
                    R.id.selectTxtMenu -> {
                        val action = ChatScreenFragmentDirections.actionChatScreenFragmentToSelectTextFragment(message)
                        findNavController().navigate(action)

                        return@setOnMenuItemClickListener true
                    }
                    R.id.shareTxtMenu -> {
                        view.context.shareMsg(message)
                        return@setOnMenuItemClickListener true
                    }
                    else -> {
                        return@setOnMenuItemClickListener true

                    }

                }
            }
            popup.show()
        }

        chatRV.adapter = chatAdapter
        chatAdapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver()
        {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                chatRV.smoothScrollToPosition(positionStart)
            }
        })
        //chatAdapter.submitList(chatList)

        val sendIBtn = view.findViewById<ImageButton>(R.id.sendImage)


        val edmessage= view.findViewById<EditText>(R.id.edMessage)
        //var counter = -1
        sendIBtn.setOnClickListener{
            view.context.hideKeyBoard(it)
            if(edmessage.text.toString().trim().isNotEmpty()) {

                chatViewModel.createChatCompletion(edmessage.text.toString().trim())
                edmessage.text=null

            }
            else {
                view.context.longToastShow("message is required")
            }
        }


        callGetChatList(chatRV, chatAdapter)
        chatViewModel.getChatList()

        return view
    }

    private fun callGetChatList(chatRV: RecyclerView, chatAdapter: ChatAdapater) {
         CoroutineScope(Dispatchers.Main).launch {
             chatViewModel
                 .chatStateFlow
                 .collectLatest {
                     when(it.status){
                         Status.LOADING -> {}
                         Status.SUCCESS -> {
                             it.data?.collect{ chatList ->
                             chatAdapter.submitList(chatList)}
                         }
                         Status.ERROR -> {
                             it.message?.let { it1 -> chatRV.context.longToastShow(it1) }
                         }

                     }
                 }
         }
    }


}

