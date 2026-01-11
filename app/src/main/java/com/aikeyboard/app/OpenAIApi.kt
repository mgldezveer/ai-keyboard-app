package com.aikeyboard.app

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class OpenAIRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<ChatMessage>,
    val max_tokens: Int = 150
)

data class OpenAIResponse(
    val id: String,
    val choices: List<Choice>
)

data class Choice(
    val message: ChatMessage
)

data class ChatMessage(
    val role: String,
    val content: String
)

interface OpenAIApiService {
    @POST("chat/completions")
    suspend fun chatCompletions(
        @Header("Authorization") authorization: String,
        @Body request: OpenAIRequest
    ): OpenAIResponse

    companion object {
        fun create(): OpenAIApiService {
            return Retrofit.Builder()
                .baseUrl("https://api.openai.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenAIApiService::class.java)
        }
    }
}
