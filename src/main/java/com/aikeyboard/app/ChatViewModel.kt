package com.aikeyboard.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    
    private val apiKeyManager = ApiKeyManager(application)
    private val apiService = OpenAIApiService.create()
    
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState
    
    private val _chatHistory = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatHistory: StateFlow<List<ChatMessage>> = _chatHistory
    
    fun sendMessage(content: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                val apiKey = apiKeyManager.getApiKey()
                if (apiKey.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Please set your API key first"
                    )
                    return@launch
                }
                
                val userMessage = ChatMessage(role = "user", content = content)
                val updatedHistory = _chatHistory.value + userMessage
                _chatHistory.value = updatedHistory
                
                val response = apiService.chatCompletions(
                    authorization = "Bearer $apiKey",
                    request = OpenAIRequest(
                        model = "gpt-3.5-turbo",
                        messages = updatedHistory
                    )
                )
                
                if (response.choices.isNotEmpty()) {
                    val assistantMessage = response.choices[0].message
                    _chatHistory.value = updatedHistory + assistantMessage
                }
                
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "An error occurred"
                )
            }
        }
    }
    
    fun clearHistory() {
        _chatHistory.value = emptyList()
    }
}
