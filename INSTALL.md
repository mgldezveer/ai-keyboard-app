# Installation Guide - AI Keyboard

## Prerequisites

### For Developers
- Android Studio Hedgehog (2023.1.1) or later
- Android SDK with API level 34
- Java JDK 17 or higher
- Gradle 8.4 or higher
- Android device or emulator (min SDK 26)

### For Users
- Android device running Android 8.0 (API 26) or higher
- OpenAI API account and API key
- Internet connection

## Developer Setup

### 1. Clone/Download Project

```bash
cd /home/exedev
cd ai-keyboard-app
```

### 2. Open in Android Studio

1. Launch Android Studio
2. Click "Open"
3. Navigate to `ai-keyboard-app` directory
4. Click "OK"

### 3. Configure SDK Location

1. Android Studio will prompt for SDK location
2. Click "Next"
3. Wait for Gradle sync to complete (first time may take a few minutes)

### 4. Create local.properties

If needed, create `local.properties`:

```bash
cp local.properties.example local.properties
```

Edit and add your SDK path:
```
sdk.dir=/path/to/your/Android/Sdk
```

### 5. Sync Gradle

1. Click "Sync Now" in the top-right corner
2. Wait for sync to complete
3. All dependencies should download automatically

### 6. Build Project

```bash
./gradlew build
```

Or in Android Studio:
- Click Build > Make Project
- Or press Ctrl+F9 (Windows/Linux) or Cmd+F9 (Mac)

### 7. Run on Device/Emulator

**Using Android Studio:**
1. Connect Android device via USB (with USB debugging enabled)
2. Or start Android Emulator
3. Click the Run button (▶️) in the toolbar
4. Select target device
5. Wait for build and installation

**Using Command Line:**

List connected devices:
```bash
adb devices
```

Build and install:
```bash
./gradlew installDebug
```

## User Installation (APK)

### Option 1: From Android Studio

1. Build the project as described above
2. Build APK:
   - Build > Build Bundle(s) / APK(s) > Build APK(s)
3. APK location: `app/build/outputs/apk/debug/app-debug.apk`
4. Transfer APK to device
5. On device: Enable "Install from Unknown Sources"
6. Tap APK file to install

### Option 2: Using Command Line

```bash
./gradlew assembleDebug
```

APK will be at:
```
app/build/outputs/apk/debug/app-debug.apk
```

Transfer to device and install.

### Option 3: Using ADB

```bash
# Build APK
./gradlew assembleDebug

# Install directly via ADB
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Getting OpenAI API Key

1. Go to https://platform.openai.com/
2. Sign up or log in
3. Navigate to API keys section
4. Click "Create new secret key"
5. Copy the key (starts with `sk-`)
6. Keep it safe - don't share it!

## First-Time Setup

### 1. Open the App

After installation:
- Open "AI Keyboard" app from app drawer

### 2. Enter API Key

1. You'll see a settings screen
2. Paste your OpenAI API key in the input field
3. Tap "Save API Key"
4. You'll see a confirmation message

### 3. Enable the Keyboard

1. Tap "Enable Keyboard in Settings" button
2. You'll be taken to system keyboard settings
3. Find "AI Keyboard" in the list
4. Toggle it on
5. You may see a warning - tap "OK"

### 4. Select Keyboard

1. Go to any app with text input (Messages, WhatsApp, Notes)
2. Tap in text field
3. You should see a keyboard selector icon (usually ⌨️)
4. Tap it and select "AI Keyboard"
5. Alternatively, change default keyboard in system settings

## Verification

### Test the Keyboard

1. Open Notes or any messaging app
2. Tap in text field
3. You should see AI Keyboard with:
   - QWERTY layout
   - Quick action buttons at top
   - Suggestions row above keys
   - Dark theme

### Test AI Chat

1. Tap 💬 Chat button
2. Chat panel should open
3. Type: "Hello, how are you?"
4. Tap send (➤)
5. Wait for AI response
6. Response should appear in chat and be inserted into text field

### Test Quick Actions

1. Tap ✉️ Email button
2. "Dear " should be inserted
3. Tap 🔄 Rewrite button
4. "Rewrite: " should be inserted

## Troubleshooting

### Keyboard Not Appearing

**Problem**: Keyboard doesn't show up when tapping text field

**Solutions**:
- Make sure keyboard is enabled in Settings > System > Languages & input
- Check if "AI Keyboard" is selected as active keyboard
- Restart the app
- Restart device

### API Key Not Working

**Problem**: AI responses fail or error message appears

**Solutions**:
- Verify API key is correct (starts with `sk-`)
- Check you have internet connection
- Make sure your OpenAI account has API credits
- Try re-entering API key in settings
- Check OpenAI API status page

### Build Errors

**Problem**: Gradle sync fails or build errors

**Solutions**:
- Check Java version (need JDK 17+)
- Clear Gradle cache: `./gradlew clean`
- Delete `.gradle` folder and resync
- Update Android SDK to latest
- Check Android SDK location in local.properties

### App Not Installing

**Problem**: APK won't install on device

**Solutions**:
- Enable "Install from Unknown Sources" in settings
- Check device Android version (min 8.0)
- Uninstall previous version if exists
- Check available storage space

### Chat Panel Not Working

**Problem**: Chat panel doesn't open or send messages

**Solutions**:
- Check internet connection
- Verify API key is saved
- Restart keyboard (switch to another keyboard and back)
- Check app permissions
- Reopen app and re-save API key

## Uninstallation

### Remove from Device

1. Go to Settings > Apps
2. Find "AI Keyboard"
3. Tap "Uninstall"
4. Confirm

### Deactivate Keyboard

Before uninstalling:
1. Go to Settings > System > Languages & input
2. Toggle off "AI Keyboard"
3. Select another keyboard as default

## Updating

### Method 1: From Android Studio

1. Open project
2. Make changes
3. Build and install as before
4. New version will overwrite old one

### Method 2: New APK

1. Build new APK
2. Install on device
3. Will automatically update existing installation

## Production Build

For release APK (requires signing):

```bash
./gradlew assembleRelease
```

APK location:
```
app/build/outputs/apk/release/app-release.apk
```

Note: You need to configure signing in `app/build.gradle.kts` for release builds.

## System Requirements Summary

**Minimum**:
- Android 8.0 (API 26)
- 50MB free storage
- OpenAI API account

**Recommended**:
- Android 10+ (API 29)
- 100MB free storage
- Stable internet connection
- Modern device for smooth performance

## Support

For issues or questions:
1. Check Troubleshooting section above
2. Review TODO.md for known issues
3. Check FEATURES.md for feature documentation
4. Review ARCHITECTURE.md for technical details
