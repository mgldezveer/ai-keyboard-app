# AI Keyboard

Android keyboard application with ChatGPT/AI integration.

## Features

- **AI Chat Integration**: Built-in ChatGPT integration for intelligent text generation
- **Quick Actions**: Pre-built actions for email, rewrite, summarize, translate
- **Smart Suggestions**: AI-powered text suggestions
- **Custom Themes**: Dark mode support
- **Multilingual**: Support for multiple languages

## Screens

1. **Welcome/Onboarding**
2. **API Key Setup**
3. **Settings**
4. **Chat Interface**
5. **Keyboard**

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Networking**: Retrofit + OkHttp
- **Storage**: DataStore Preferences
- **Coroutines**: Kotlin Coroutines

## Setup

1. Clone the repository
2. Open in Android Studio
3. Add your OpenAI API key in the app settings
4. Build and run on Android device (min SDK 26)

## Architecture

- `MainActivity.kt`: Main settings screen
- `AIKeyboardService.kt`: IME service implementation
- `ChatMessage.kt`: Data model for chat messages

## API Integration

The app integrates with OpenAI API for AI text generation. You need a valid API key from https://platform.openai.com/api-keys

## Enabling the Keyboard

1. Install the app on your device
2. Go to Settings > System > Languages & input > Virtual keyboard
3. Enable "AI Keyboard"
4. Select "AI Keyboard" as your input method in any app

## Future Enhancements

- [ ] Full OpenAI API integration
- [ ] Chat history persistence
- [ ] Custom prompts
- [ ] Voice input
- [ ] Swipe typing
- [ ] Emoji support
- [ ] More keyboard layouts
