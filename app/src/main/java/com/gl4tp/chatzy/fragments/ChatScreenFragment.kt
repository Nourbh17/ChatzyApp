package com.gl4tp.chatzy.fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.gl4tp.chatzy.R
import com.gl4tp.chatzy.adapters.ChatAdapater
import com.gl4tp.chatzy.utils.Status
import com.gl4tp.chatzy.utils.copyToClipBoard
import com.gl4tp.chatzy.utils.hideKeyBoard
import com.gl4tp.chatzy.utils.longToastShow
import com.gl4tp.chatzy.utils.robotImageList
import com.gl4tp.chatzy.utils.shareMsg
import com.gl4tp.chatzy.viewModels.ChatViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale


class ChatScreenFragment : Fragment() {



    private val chatViewModel : ChatViewModel by lazy {
        ViewModelProvider(this)[ChatViewModel::class.java]
    }

    private lateinit var edMessage : EditText

    private val chatArgs : ChatScreenFragmentArgs by navArgs()

    private lateinit var textToSpeech: TextToSpeech
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_chat_screen, container, false)

        val toolbarview = view.findViewById<View>(R.id.toolbarLayout)

        val closeimage =toolbarview.findViewById<ImageView>(R.id.backImg)
        val robotimage =toolbarview.findViewById<ImageView>(R.id.robotImage)

        robotimage.setImageResource(robotImageList[chatArgs.robotImg])
        closeimage.setOnClickListener {
            findNavController().navigateUp()
        }
        //text to speech
        textToSpeech = TextToSpeech(view.context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale.getDefault())
                if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    view.context.longToastShow("language is not supported")
                }
            }
        }

        val titleTxt= toolbarview.findViewById<TextView>(R.id.titleTxt)
        titleTxt.text= chatArgs.robotName

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
                        textToSpeech.stop()
                        view.context.copyToClipBoard(message)
                        return@setOnMenuItemClickListener true
                    }
                    R.id.selectTxtMenu -> {
                        textToSpeech.stop()
                        val action = ChatScreenFragmentDirections.actionChatScreenFragmentToSelectTextFragment(message)
                        findNavController().navigate(action)

                        return@setOnMenuItemClickListener true
                    }
                    R.id.TextToVoiceMenu ->{
                        textToSpeech.speak(
                            message,
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            null
                        )

                        return@setOnMenuItemClickListener true
                    }
                    R.id.shareTxtMenu -> {
                        textToSpeech.stop()
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


         edMessage= view.findViewById(R.id.edMessage)
        //var counter = -1
        sendIBtn.setOnClickListener{
            textToSpeech.stop()
            view.context.hideKeyBoard(it)
            if(edMessage.text.toString().trim().isNotEmpty()) {

                chatViewModel.createChatCompletion(edMessage.text.toString().trim(),chatArgs.robotId)
                edMessage.text=null

            }
            else {
                view.context.longToastShow("message is required")

            }
        }

        val speechToTextBtn = view.findViewById<ImageButton>(R.id.voiceToTextBtn)
        speechToTextBtn.setOnClickListener {
            textToSpeech.stop()
            edMessage.text = null
            try {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE,
                    Locale.getDefault()
                )
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say something")
                result.launch(intent)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        callGetChatList(chatRV, chatAdapter)
        chatViewModel.getChatList(chatArgs.robotId)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        textToSpeech.stop()
    }
    private val result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        if (result.resultCode == Activity.RESULT_OK){
            val results = result.data?.getStringArrayListExtra(
                RecognizerIntent.EXTRA_RESULTS
            ) as ArrayList<String>

            edMessage.setText(results[0])
        }
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

