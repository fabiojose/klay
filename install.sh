#!/usr/bin/env bash

# if JAVA_HOME, check it
JAVA=java
JAVAC=javac
if [[ ! -z "$JAVA_HOME" ]]; then
  JAVA=$JAVA_HOME/bin/java
  JAVAC=$JAVA_HOME/bin/javac
fi

# TODO: Check JDK
JAVAC_VERSION=$("$JAVAC" -version 2>&1)
if ! [[ $? -eq 0 ]]; then
  echo "[ERROR] Klay needs JDK, that wasn't found in the current system." >&2
  exit 5
fi

# TODO: Check java version
if ! [[ $JAVAC_VERSION == *"javac 17"* ]]; then
  echo "[ERROR] Klay needs Java 17, but the current installation is: $JAVAC_VERSION" >&2
  exit 5
fi

# TODO: Check existence of tar command
if ! [ -x "$(command -v tar)" ]; then
  echo '[ERROR] tar command not installed.' >&2
  exit 5
fi

# TODO: Get the latest release from github api and download
LATEST_RELEASE=$(curl -s https://api.github.com/repos/fabiojose/klay/releases/latest \
| grep "browser_download_url.*tgz" \
| cut -d : -f 2,3 \
| tr -d \")

# TODO: Extract the version number from LATEST_RELEASE
NEW_KLAY_VERSION=$(echo $LATEST_RELEASE | cut -d'/' -f 8)
NEW_KLAY_VERSION_NO=$(echo $NEW_KLAY_VERSION | cut -d'v' -f 2)

DOWNLOAD_LOCATION='/tmp/klay.tgz'
if [ -x "$(command -v wget)" ]; then
  wget -q --show-progress -O "$DOWNLOAD_LOCATION" $LATEST_RELEASE
else
  curl --connect-timeout 5 \
    --max-time 10 \
    --retry 5 \
    --retry-delay 0 \
    --retry-max-time 40 \
    -o "$DOWNLOAD_LOCATION" \
    "$LATEST_RELEASE"
fi

if ! [[ $? -eq 0 ]]; then
  echo "[ERROR] Could not download the latest Klay release." >&2
  exit 6
fi

KLAY_HOME="$HOME/.klay"
KLAY_CLI_HOME="$KLAY_HOME/cli"
# Check if $HOME/.klay/cli exists, if not, create it
if [ ! -d "$KLAY_CLI_HOME" ]; then
  mkdir -p $KLAY_CLI_HOME
  if ! [[ $? -eq 0 ]]; then
    echo "[ERROR] Unable to create directory $KLAY_CLI_HOME" >&2
    exit 6
  fi
fi

LIB_BAK="/tmp/klay-$NEW_KLAY_VERSION_NO-bak/lib"
if [ -d "$KLAY_CLI_HOME/lib" ]; then
  mv "$KLAY_CLI_HOME/lib/*" "$LIB_BAK"
fi

BIN_BAK="/tmp/klay-$NEW_KLAY_VERSION_NO-bak/bin"
if [ -d "$KLAY_CLI_HOME/bin" ]; then
  mv "$KLAY_CLI_HOME/bin/*" "$BIN_BAK"
fi

tar -xzf "$DOWNLOAD_LOCATION" --directory "$KLAY_HOME"
rm -rf "$KLAY_CLI_HOME"
mv --force "$KLAY_HOME/klay-$NEW_KLAY_VERSION_NO" "$KLAY_CLI_HOME"

if [[ $? -eq 0 ]]; then
  rm -rf "$LIB_BAK"
  rm -rf "$BIN_BAK"

  # TODO: Add to PATH if not presente: .bashrc and .zshrc

  if [[ -z "$OLD_KLAY_VERSION" ]]; then
    echo "Klay version $NEW_KLAY_VERSION installed with success."
    exit 0
  else
    echo "Klay updated from $OLD_KLAY_VERSION to version $NEW_KLAY_VERSION with success."
    exit 0
  fi
else
  echo "[ERROR] Unable to install klay version $NEW_KLAY_VERSION"

  mv "$KLAY_CLI_HOME/lib/bak/*" "$KLAY_CLI_HOME/lib"
  mv "$KLAY_CLI_HOME/bin/bak/*" "$KLAY_CLI_HOME/bin"
  exit 6
fi

