package team.pacify.bookeet.ui.messaging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import team.pacify.bookeet.data.models.messaging.Chat
import team.pacify.bookeet.domain.repository.messaging.ChatRepository
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@HiltViewModel
class ChatViewModel
@Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _chats = MutableLiveData<Resource<List<Chat>>>()
    val chats: LiveData<Resource<List<Chat>>> = _chats

    fun getChats(userId: String) {
        viewModelScope.launch {
            _chats.postValue(Resource.Loading())
            chatRepository.getAllChats(userId, 0, 10).collect { resource ->
                _chats.postValue(resource)
            }
        }
    }

    fun addChat(chat: Chat) {
        viewModelScope.launch {
            chatRepository.addChat(chat)
        }
    }

    fun deleteChat(chat: Chat) {
        viewModelScope.launch {
            chatRepository.deleteChat(chat)
        }
    }

}