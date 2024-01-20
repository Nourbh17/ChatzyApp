package com.gl4tp.chatzy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gl4tp.chatzy.databinding.ItemReceiverBinding
import com.gl4tp.chatzy.models.Chat
import com.gl4tp.chatzy.databinding.ItemSenderBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ChatAdapater (
    private var onClickCallback: (message : String, view: View) -> Unit
):
    ListAdapter<Chat, RecyclerView.ViewHolder>(DiffiCallback()){


    class DiffiCallback : DiffUtil.ItemCallback<Chat>(){

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            TODO("Not yet implemented")
        }

        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            TODO("Not yet implemented")
        }

    }
    class SenderViewHolder(private val itemSenderBinding: ItemSenderBinding) :
        RecyclerView.ViewHolder(itemSenderBinding.root){
            fun bind(chat: Chat){
                itemSenderBinding.txtMessage.text=chat.message
                val dataFormat = SimpleDateFormat("dd-MM-yyyy HH:mm a", Locale.getDefault())
                itemSenderBinding.txtDate.text = dataFormat.format(chat.date)
            }


    }

    class ReceiverViewHolder(private val itemReceiverBinding: ItemReceiverBinding) :
        RecyclerView.ViewHolder(itemReceiverBinding.root){
        fun bind(chat: Chat){
            itemReceiverBinding.txtMessage.text=chat.message
            val dataFormat = SimpleDateFormat("dd-MM-yyyy HH:mm a", Locale.getDefault())
            itemReceiverBinding.txtDate.text = dataFormat.format(chat.date)
        }


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  if (viewType==1){
            ReceiverViewHolder(
                ItemReceiverBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
        else {
            SenderViewHolder(
                ItemSenderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).messageType.equals("sender",true)){0}
        else {1}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = getItem(position)

        if (chat.messageType.equals("sender",true)){
            (holder as SenderViewHolder).bind(chat)
        }
        else{
            (holder as ReceiverViewHolder).bind(chat)
        }
        holder.itemView.setOnLongClickListener{
            if (holder.adapterPosition != -1){
                onClickCallback(chat.message, holder.itemView)
            }
            true
        }

    }
}