#!/usr/bin/env bash
# fail if any commands fails
set -e
# debug log
set -x

gcloud firebase test android run \
  --type robo \
  --app "$BITRISE_APK_PATH" \
  --device model=walleye \
  --timeout 90s

