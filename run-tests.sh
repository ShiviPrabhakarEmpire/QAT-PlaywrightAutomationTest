#!/usr/bin/env bash
set -e

# Attempt to automatically resolve JAVA_HOME if it is not set
if [ -z "$JAVA_HOME" ]; then
    # macOS: Try to use java_home utility or common Homebrew paths for Java 21
    if command -v /usr/libexec/java_home &> /dev/null; then
        export JAVA_HOME=$(/usr/libexec/java_home -v 21 2>/dev/null || echo "")
    fi
    
    if [ -z "$JAVA_HOME" ] && [ -d "/usr/local/opt/openjdk@21" ]; then
        export JAVA_HOME="/usr/local/opt/openjdk@21"
    elif [ -z "$JAVA_HOME" ] && [ -d "/opt/homebrew/opt/openjdk@21" ]; then
        export JAVA_HOME="/opt/homebrew/opt/openjdk@21"
    fi
fi

if [ -z "$JAVA_HOME" ]; then
    echo "❌ ERROR: JAVA_HOME is not set and Java 21 was not found automatically."
    echo "Please install Java 21 and set your JAVA_HOME."
    echo "  - macOS: brew install openjdk@21"
    echo "  - Ubuntu: sudo apt install openjdk-21-jdk"
    exit 1
fi

echo "☕ Using JAVA_HOME: $JAVA_HOME"

# Check if Playwright browsers need to be installed
if [ ! -d "$HOME/Library/Caches/ms-playwright" ] && [ ! -d "$HOME/.cache/ms-playwright" ]; then
    echo "🌐 Playwright browsers not found. Installing..."
    ./gradlew installBrowsers
fi

echo "🚀 Running tests with arguments: $@"
# Pass all CLI arguments directly to gradle test
./gradlew test "$@"
