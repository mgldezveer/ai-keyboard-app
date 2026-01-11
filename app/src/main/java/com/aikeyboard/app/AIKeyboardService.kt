package com.aikeyboard.app

import android.inputmethodservice.InputMethodService
import android.inputmethodservice.KeyboardView
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputConnection
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ChatMessage(
    val id: String = "",
    val role: String,
    val content: String
)

class AIKeyboardService : InputMethodService() {

    private var inputConnection: InputConnection? = null
    private val chatHistory = MutableStateFlow<List<ChatMessage>>(emptyList())
    private var currentInput by mutableStateOf("")
    private var showChat by mutableStateOf(false)
    private val apiKeyManager by lazy { ApiKeyManager(this) }

    override fun onCreateInputView(): View {
        inputConnection = currentInputConnection
        return ComposeView(this).apply {
            setContent {
                KeyboardUI(
                    chatHistory = chatHistory.asStateFlow().collectAsState().value,
                    currentInput = currentInput,
                    onInputChange = { currentInput = it },
                    onSendMessage = { message ->
                        handleMessage(message)
                    },
                    onKeyPress = { key ->
                        handleKeyPress(key)
                    },
                    onDelete = { handleDelete() },
                    onSpace = { handleSpace() },
                    onEnter = { handleEnter() },
                    onChatToggle = { showChat = !showChat },
                    showChat = showChat
                )
            }
        }
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
    }

    private fun handleKeyPress(key: String) {
        inputConnection?.commitText(key, 1)
    }

    private fun handleDelete() {
        inputConnection?.deleteSurroundingText(1, 0)
    }

    private fun handleSpace() {
        inputConnection?.commitText(" ", 1)
    }

    private fun handleEnter() {
        inputConnection?.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
        inputConnection?.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
    }

    private suspend fun getAIResponse(messages: List<ChatMessage>): String {
        return try {
            val apiKey = apiKeyManager.getApiKey()
            if (apiKey.isEmpty()) {
                return "Please set your API key in the app settings"
            }
            
            val apiService = OpenAIApiService.create()
            val request = OpenAIRequest(
                model = "gpt-3.5-turbo",
                messages = messages.map { com.aikeyboard.app.ChatMessage(it.role, it.content) }
            )
            
            val response = apiService.chatCompletions(
                authorization = "Bearer $apiKey",
                request = request
            )
            
            if (response.choices.isNotEmpty()) {
                response.choices[0].message.content
            } else {
                "No response from AI"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    private fun handleMessage(message: String) {
        val newMessage = ChatMessage(
            id = System.currentTimeMillis().toString(),
            role = "user",
            content = message
        )
        
        val updatedHistory = chatHistory.value + newMessage
        chatHistory.value = updatedHistory
        
        kotlinx.coroutines.GlobalScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            val response = getAIResponse(updatedHistory)
            
            val aiMessage = ChatMessage(
                id = (System.currentTimeMillis() + 1).toString(),
                role = "assistant",
                content = response
            )
            
            chatHistory.value = updatedHistory + aiMessage
            
            inputConnection?.commitText(response, 1)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeyboardUI(
    chatHistory: List<ChatMessage>,
    currentInput: String,
    onInputChange: (String) -> Unit,
    onSendMessage: (String) -> Unit,
    onKeyPress: (String) -> Unit,
    onDelete: () -> Unit,
    onSpace: () -> Unit,
    onEnter: () -> Unit,
    onChatToggle: () -> Unit,
    showChat: Boolean
) {
    val suggestions = listOf("Hello", "How are you?", "Thank you", "Best regards", "Please")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1A1A))
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        QuickActionsBar(
            onChatToggle = onChatToggle,
            onEmail = { onKeyPress("Dear ") },
            onRewrite = { onKeyPress("Rewrite: ") },
            onSummarize = { onKeyPress("Summarize: ") },
            onTranslate = { onKeyPress("Translate: ") }
        )

        if (showChat) {
            ChatPanel(
                chatHistory = chatHistory,
                inputText = currentInput,
                onInputChange = onInputChange,
                onSend = {
                    if (currentInput.isNotBlank()) {
                        onSendMessage(currentInput)
                        onInputChange("")
                    }
                }
            )
        }

        SuggestionsRow(
            suggestions = suggestions,
            onSuggestionClick = { suggestion ->
                onKeyPress(suggestion + " ")
            }
        )

        StandardKeyboard(
            onKeyPress = onKeyPress,
            onDelete = onDelete,
            onSpace = onSpace,
            onEnter = onEnter
        )
    }
}

@Composable
fun QuickActionsBar(
    onChatToggle: () -> Unit,
    onEmail: () -> Unit,
    onRewrite: () -> Unit,
    onSummarize: () -> Unit,
    onTranslate: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ActionButton(icon = "💬", label = "Chat", onClick = onChatToggle)
        ActionButton(icon = "✉️", label = "Email", onClick = onEmail)
        ActionButton(icon = "🔄", label = "Rewrite", onClick = onRewrite)
        ActionButton(icon = "📝", label = "Summary", onClick = onSummarize)
        ActionButton(icon = "🌐", label = "Translate", onClick = onTranslate)
    }
}

@Composable
fun ActionButton(icon: String, label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(60.dp)
            .padding(2.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2D2D2D),
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = icon, fontSize = 20.sp)
            Text(text = label, fontSize = 8.sp, fontWeight = FontWeight.Light)
        }
    }
}

@Composable
fun ChatPanel(
    chatHistory: List<ChatMessage>,
    inputText: String,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit
) {
    val listState = rememberLazyListState()
    
    LaunchedEffect(chatHistory.size) {
        if (chatHistory.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color(0xFF252525), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = listState,
            reverseLayout = true
        ) {
            items(chatHistory.reversed()) { message ->
                ChatMessageItem(message)
            }
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = onInputChange,
                placeholder = { Text("Ask AI...", color = Color.Gray, fontSize = 12.sp) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color(0xFF1A1A1A),
                    textColor = Color.White,
                    cursorColor = Color(0xFF6200EE),
                    focusedBorderColor = Color(0xFF6200EE)
                )
            )
            
            Button(
                onClick = onSend,
                modifier = Modifier.size(36.dp),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE)
                )
            ) {
                Text("➤", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = if (message.role == "user") "You:" else "AI:",
            color = Color.Gray,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = message.content,
            color = if (message.role == "user") Color.White else Color(0xFFBB86FC),
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
fun SuggestionsRow(
    suggestions: List<String>,
    onSuggestionClick: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(suggestions) { suggestion ->
            SuggestionChip(
                onClick = { onSuggestionClick(suggestion) },
                label = { Text(suggestion, fontSize = 11.sp, color = Color.White) },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = Color(0xFF6200EE)
                )
            )
        }
    }
}

@Composable
fun StandardKeyboard(
    onKeyPress: (String) -> Unit,
    onDelete: () -> Unit,
    onSpace: () -> Unit,
    onEnter: () -> Unit
) {
    val rows = listOf(
        listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
        listOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
        listOf("Z", "X", "C", "V", "B", "N", "M", "⌫")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1A1A)),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        rows.forEach { row ->
            KeyboardRow(
                keys = row,
                onKeyPress = onKeyPress,
                onDelete = onDelete
            )
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            KeyButton(
                text = "123",
                modifier = Modifier.weight(1f),
                onClick = { }
            )
            KeyButton(
                text = "?123",
                modifier = Modifier.weight(1f),
                onClick = { }
            )
            KeyButton(
                text = "space",
                modifier = Modifier.weight(5f),
                onClick = onSpace
            )
            KeyButton(
                text = ".",
                modifier = Modifier.weight(1f),
                onClick = { onKeyPress(".") }
            )
            KeyButton(
                text = "↵",
                modifier = Modifier.weight(1.5f),
                onClick = onEnter
            )
        }
    }
}

@Composable
fun KeyboardRow(
    keys: List<String>,
    onKeyPress: (String) -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        keys.forEach { key ->
            val weight = when (key) {
                "⌫" -> 1.5f
                else -> 1f
            }
            
            KeyButton(
                text = key,
                modifier = Modifier.weight(weight),
                onClick = {
                    when (key) {
                        "⌫" -> onDelete()
                        else -> onKeyPress(key.lowercase())
                    }
                }
            )
        }
    }
}

@Composable
fun KeyButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(45.dp)
            .padding(horizontal = 1.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2D2D2D),
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
