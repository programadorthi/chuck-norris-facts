#!/bin/bash

# Export variables used to build when they don't exist
# if [ -z "$ANDROID_HOME" ]; then
#   export ANDROID_HOME="$HOME/.android/"
#   export PATH="$PATH:$HOME/.android/platform-tools/"
# fi

# Before each action run
./gradlew --stop

# Before build or test action
./gradlew clean ktlintCheck
./gradlew clean detekt

# Action to execute all tests
./gradlew clean test

# Action to build development version
./gradlew clean assembleProdRelease
