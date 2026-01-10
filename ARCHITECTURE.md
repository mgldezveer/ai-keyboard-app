# AI Keyboard - Architecture Documentation

## Project Structure

```
ai-keyboard-app/
├── src/main/java/com/aikeyboard/app/
│   ├── MainActivity.kt              # Main settings screen
│   ├── AIKeyboardService.kt         # IME service implementation
│   ├── OpenAIApi.kt                 # OpenAI API integration
│   ├── ApiKeyManager.kt             # API key management
│   └── ChatViewModel.kt             # Chat state management
├── src/main/res/
│   ├── drawable/                    # Image resources
│   ├── layout/                      # XML layouts (if needed)
│   ├── values/                      # Resources (strings, colors, themes)
│   ├── xml/                         # XML configurations
│   └── mipmap/                      # App icons
├── app/build.gradle.kts             # App-level build configuration
├── build.gradle.kts                 # Project-level build configuration
└── settings.gradle.kts              # Gradle settings
```

## Core Components

### 1. MainActivity.kt
- **Purpose**: Main settings screen for the app
- **Key Functions**:
  - API key input and storage using DataStore
  - Keyboard enable/disable settings
  - Navigation to system keyboard settings

### 2. AIKeyboardService.kt
- **Purpose**: Input Method Editor (IME) service
- **Key Functions**:
  - Custom keyboard UI using Jetpack Compose
  - Input handling (key presses, delete, space, enter)
  - AI chat panel integration
  - Quick actions (email, rewrite, summarize, translate)
  - Smart suggestions

### 3. OpenAIApi.kt
- **Purpose**: OpenAI API integration using Retrofit
- **Key Functions**:
  - API service definition
  - Request/Response models
  - Configuration for GPT-3.5-turbo model

### 4. ApiKeyManager.kt
- **Purpose**: Secure API key storage
- **Key Functions**:
  - Save/Load API key using DataStore
  - Check if API key exists

### 5. ChatViewModel.kt
- **Purpose**: Chat state management
- **Key Functions**:
  - Manage chat history
  - Handle API calls
  - UI state (loading, error, messages)

## Key Features

### Keyboard Layout
- Standard QWERTY layout
- Custom key styling
- Special keys (delete, space, enter)
- Symbol/number switch

### AI Integration
- Real-time OpenAI API calls
- Chat history management
- Context-aware responses
- Error handling

### Quick Actions
- 💬 Chat: Toggle chat panel
- ✉️ Email: Quick email template
- 🔄 Rewrite: Rewrite selected text
- 📝 Summary: Summarize text
- 🌐 Translate: Translation action

### Smart Suggestions
- Contextual suggestions
- Auto-completion
- History-based predictions

## Data Flow

```
User Input -> AIKeyboardService
    |
    +-> Standard Keyboard -> InputConnection -> Active App
    |
    +-> Quick Actions -> Pre-defined Actions -> InputConnection
    |
    +-> Chat Panel -> ChatViewModel -> OpenAIApi -> AI Response -> InputConnection
```

## Storage

### DataStore Preferences
- API key storage (encrypted)
- Settings persistence
- Location: `/data/data/com.aikeyboard.app/files/datastore/settings`

### Chat History
- In-memory storage (session-based)
- Can be extended to use Room database

## Security

- API keys stored securely in DataStore
- No logging of sensitive data
- Secure API communication over HTTPS

## Future Enhancements

1. **Persistence**: Room database for chat history
2. **Custom Prompts**: User-defined prompts
3. **Voice Input**: Speech-to-text integration
4. **Swipe Typing**: Gesture-based input
5. **Emoji Support**: Full emoji picker
6. **More Languages**: International keyboard layouts
7. **Themes**: Custom themes and colors
8. **Analytics**: Usage tracking and improvements

## Dependencies

- **Jetpack Compose**: Modern UI toolkit
- **Retrofit**: HTTP client
- **OkHttp**: Networking
- **DataStore**: Data persistence
- **Coroutines**: Asynchronous programming
- **Material3**: Material Design components

## Minimum Requirements

- **Android SDK**: 26 (Android 8.0)
- **Kotlin**: 1.9.20
- **Gradle**: 8.4
- **Java**: 17

## Testing

### Unit Tests
- ViewModel logic
- API integration
- API key management

### UI Tests
- Keyboard interactions
- Chat panel
- Settings navigation

### Integration Tests
- End-to-end chat flow
- API key setup
- Keyboard enablement
