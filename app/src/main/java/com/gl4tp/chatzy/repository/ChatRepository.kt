// ChatRepository.kt
import android.app.Application
import android.util.Log
import com.gl4tp.chatzy.database.chatzyDatabase
import com.gl4tp.chatzy.models.Chat
import com.gl4tp.chatzy.network.ApiClient
import com.gl4tp.chatzy.response.ChatRequest
import com.gl4tp.chatzy.response.ChatResponse
import com.gl4tp.chatzy.response.Message
import com.gl4tp.chatzy.utils.Resource
import com.gl4tp.chatzy.utils.longToastShow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date
import java.util.UUID

class ChatRepository ( val application : Application) {
    private val chatDao = chatzyDatabase.getInstance(application).chatdao

    private val apiClient = ApiClient.retrofitService
    private val _chatStateFlow= MutableStateFlow<Resource<Flow<List<Chat>>>>(Resource.Loading())
    val chatStateFlow : StateFlow<Resource<Flow<List<Chat>>>>

        get() = _chatStateFlow
    fun getChatList(){
        CoroutineScope(Dispatchers.IO).launch {
            try{
                _chatStateFlow.emit(Resource.Loading())
                val result= async {
                    delay(300)
                    chatDao.getChatList()
                }
                    .await()
                _chatStateFlow.emit(Resource.Success(result))

            }
            catch (e:Exception ){
                _chatStateFlow.emit(Resource.Error(e.message.toString()))
            }
        }

    }

    fun createChatCompletion(message: String) {
        val receiverId = UUID.randomUUID().toString()

        CoroutineScope(Dispatchers.IO).launch {
            delay(200)
            val senderId= UUID.randomUUID().toString()
            try {
                async {
                    chatDao.insertChat(
                        Chat(
                            senderId,
                            Message(
                                message,"user"

                            ),
                            Date()
                        )
                    )

                }
                    .await()
                val messageList= chatDao.getChatListWithoutFlow().map {
                    it.message
                }.reversed().toMutableList()
                if(messageList.size==1){
                    messageList.add(0, Message("introduce yourself and let's ask me what topic i want to discuss ", "system"))
                }
                async {
                    chatDao.insertChat(
                        Chat(
                            receiverId,
                            Message(
                                "",
                                "assistant"

                            ),
                            Date()
                        )
                    )

                }
                    .await()
                val chatRequest = ChatRequest(
                     messageList,
                    "gpt-3.5-turbo-1106"
                )

                val apiKey = "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" // Replace with your actual API key
                val authorizationHeader = "Bearer $apiKey"

                val response = apiClient.createChatCompletion(authorizationHeader, chatRequest).execute()
                Log.d("chat request","$chatRequest")
                Log.d("chat resp","$response")

                if (response.isSuccessful) {

                    val msg = response.body()?.choices?.get(0)?.message
                    Log.d("message", msg?.toString() ?: "Message is null")
                    chatDao.updateChatParticulatField(
                        receiverId,
                        msg!!.content,
                        msg.role,
                        Date()
                    )
                } else {
                    Log.e("ChatResponse", "Error: ${response.errorBody()?.string()}")
                    deleteChatAPIFailure(receiverId, senderId)
                }

            } catch (e: Exception) {
                Log.e("ChatRequest", "Failed: ${e.message}")
                e.printStackTrace()
                deleteChatAPIFailure(receiverId, senderId)

            }
        }
    }

    private fun deleteChatAPIFailure(receiverId: String, senderId: String) {
        CoroutineScope(Dispatchers.IO).launch{
            listOf(
                async { chatDao.deleteChatUsingChatId(receiverId) },
                  async{  chatDao.deleteChatUsingChatId(senderId)}
            )
                .awaitAll()
            //_chatStateFlow.emit(Resource.Error("something went wrong"))
            withContext(Dispatchers.Main){
                application.longToastShow("something went wrong")
            }
        }
    }
}
