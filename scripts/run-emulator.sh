#!/bin/bash
set -euo pipefail

export ANDROID_HOME=/opt/homebrew/share/android-commandlinetools
export ANDROID_AVD_HOME="/Users/volkerheidema/Projects/wait-a-sec/Untitled/.avd"
export PATH="$ANDROID_HOME/emulator:$ANDROID_HOME/platform-tools:$PATH"

mkdir -p /tmp
rm -f "$ANDROID_AVD_HOME"/WaitASec_Phone.avd/*.lock 2>/dev/null || true

exec emulator \
  -avd WaitASec_Phone \
  -ports 5554,5555 \
  -no-audio \
  -no-snapshot \
  -gpu host
