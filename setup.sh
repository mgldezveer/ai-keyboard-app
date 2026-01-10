#!/bin/bash

# AI Keyboard App - Initialization Script

echo "Setting up AI Keyboard App..."

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Java is not installed. Please install Java JDK first."
    exit 1
fi

# Create necessary directories
mkdir -p gradle/wrapper

echo "Project structure created successfully!"
echo ""
echo "Next steps:"
echo "1. Open the project in Android Studio"
echo "2. Sync Gradle"
echo "3. Add your OpenAI API key in the app settings"
echo "4. Build and run on Android device (min SDK 26)"
echo ""
echo "To enable the keyboard:"
echo "1. Go to Settings > System > Languages & input > Virtual keyboard"
echo "2. Enable 'AI Keyboard'"
echo "3. Select 'AI Keyboard' as your input method in any app"
