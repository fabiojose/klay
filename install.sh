#!/usr/bin/env bash

KLAY_HOME="$HOME/.klay"
KLAY_CLI_HOME="$KLAY_HOME/cli"

# if JAVA_HOME, check it
JAVA=java
JAVAC=javac
if [[ ! -z "$JAVA_HOME" ]]; then
  JAVA=$JAVA_HOME/bin/java
  JAVAC=$JAVA_HOME/bin/javac
fi

# Check JDK
JAVAC_VERSION=$("$JAVAC" -version 2>&1)
if ! [[ $? -eq 0 ]]; then
  echo "[ERROR] Klay needs JDK, that wasn't found in the current system." >&2
  exit 5
fi

# Check java version
if ! [[ $JAVAC_VERSION == *"javac 17"* ]]; then
  echo "[ERROR] Klay needs Java 17, but the current installation is: $JAVAC_VERSION" >&2
  exit 5
fi

# Check existence of tar command
if ! [ -x "$(command -v tar)" ]; then
  echo '[ERROR] tar command not installed.' >&2
  exit 5
fi

# Get the latest release from github api and download
LATEST_RELEASE=$(curl -s https://api.github.com/repos/fabiojose/klay/releases/latest \
| grep "browser_download_url.*tgz" \
| cut -d : -f 2,3 \
| tr -d \")

# Extract the version number from LATEST_RELEASE
NEW_KLAY_VERSION=$(echo $LATEST_RELEASE | cut -d'/' -f 8)
NEW_KLAY_VERSION_NO=$(echo $NEW_KLAY_VERSION | cut -d'v' -f 2)

# Compare the new and the old version and go ahead if they are not equals
if [ -f "$KLAY_CLI_HOME/version" ]; then
  OLD_KLAY_VERSION_NO=$(cat "$KLAY_CLI_HOME/version")

  if [ "$NEW_KLAY_VERSION_NO" == "$OLD_KLAY_VERSION_NO" ]; then
    echo ''
    echo "[INFO] Klay is already updated."
    exit 0
  fi
fi

DOWNLOAD_LOCATION='/tmp/klay.tgz'
if [ -x "$(command -v wget)" ]; then
  wget -q --show-progress -O "$DOWNLOAD_LOCATION" $LATEST_RELEASE
else
  curl -L --connect-timeout 5 \
    --retry 5 \
    --retry-delay 0 \
    --retry-max-time 40 \
    -o "$DOWNLOAD_LOCATION" \
    $LATEST_RELEASE
fi

if ! [[ $? -eq 0 ]]; then
  echo "[ERROR] Could not download the latest Klay release." >&2
  exit 6
fi

if [ ! -d "$KLAY_CLI_HOME" ]; then
  mkdir -p $KLAY_CLI_HOME
  if ! [[ $? -eq 0 ]]; then
    echo "[ERROR] Unable to create directory $KLAY_CLI_HOME" >&2
    exit 6
  fi
fi

LIB_BAK="/tmp/klay-$NEW_KLAY_VERSION_NO-bak/lib"
mkdir -p $LIB_BAK
if [ -d "$KLAY_CLI_HOME/lib" ]; then
  mv "$KLAY_CLI_HOME/lib" "$LIB_BAK"
fi

BIN_BAK="/tmp/klay-$NEW_KLAY_VERSION_NO-bak/bin"
mkdir -p $BIN_BAK
if [ -d "$KLAY_CLI_HOME/bin" ]; then
  mv "$KLAY_CLI_HOME/bin" "$BIN_BAK"
fi

tar -xzf "$DOWNLOAD_LOCATION" --directory "$KLAY_HOME"
rm -rf "$KLAY_CLI_HOME"
mv --force "$KLAY_HOME/klay-$NEW_KLAY_VERSION_NO" "$KLAY_CLI_HOME"

if [[ $? -eq 0 ]]; then
  rm -rf "$LIB_BAK"
  rm -rf "$BIN_BAK"

  # Register the new version
  echo -n $NEW_KLAY_VERSION_NO > "$KLAY_CLI_HOME/version"

  echo ''
  if [[ -z "$OLD_KLAY_VERSION_NO" ]]; then

    KLAY_CFG='n'
    KLAY_HOME_EXPORT="KLAY_HOME=$HOME/.klay"
    KLAY_CLI_HOME_EXPORT='KLAY_CLI_HOME=$KLAY_HOME/cli'
    JAR_EXPORT='KLAY_UBER_JAR_LOCATION=$KLAY_CLI_HOME/lib/klay.jar'
    PATH_EXPORT='PATH=$PATH:$KLAY_CLI_HOME/bin'
    CLASSPATH_EXPORT='CLASSPATH=$CLASSPATH:$KLAY_CLI_HOME/lib:'
    if [ -f "$HOME/.bashrc" ]; then
      echo '' >> "$HOME/.bashrc"
      echo '# Klay configurations' >> "$HOME/.bashrc"
      echo "export $KLAY_HOME_EXPORT" >> "$HOME/.bashrc"
      echo "export $KLAY_CLI_HOME_EXPORT" >> "$HOME/.bashrc"
      echo "export $JAR_EXPORT" >> "$HOME/.bashrc"
      echo "export $PATH_EXPORT" >> "$HOME/.bashrc"
      echo "export $CLASSPATH_EXPORT" >> "$HOME/.bashrc"
      KLAY_CFG='y'
    fi

    if [ -f "$HOME/.zshrc" ]; then
      echo '' >> "$HOME/.zshrc"
      echo '# Klay configurations' >> "$HOME/.zshrc"
      echo "export $KLAY_HOME_EXPORT" >> "$HOME/.zshrc"
      echo "export $KLAY_CLI_HOME_EXPORT" >> "$HOME/.zshrc"
      echo "export $JAR_EXPORT" >> "$HOME/.zshrc"
      echo "export $PATH_EXPORT" >> "$HOME/.zshrc"
      echo "export $CLASSPATH_EXPORT" >> "$HOME/.zshrc"
      KLAY_CFG='y'
    fi

    if [ "$KLAY_CFG" == "y" ]; then
      echo "🕹️ Klay version $NEW_KLAY_VERSION installed and configured with success."
      echo '   ✅ Installation'
      echo '   ✅ Configuration'
    else
      echo "🕹️ Klay version $NEW_KLAY_VERSION installed with success."
      echo '   ✅ Installation'
      echo '   ❌ Configuration: 😔 unable to configure Klay, may the current shell is not supported'
    fi
    exit 0
  else
    echo "🕹️ Klay updated from $OLD_KLAY_VERSION_NO to version $NEW_KLAY_VERSION_NO with success."
    exit 0
  fi
else
  echo "[ERROR] Unable to install klay version $NEW_KLAY_VERSION" >&2

  mv --force "$LIB_BAK" "$KLAY_CLI_HOME/lib"
  mv --force "$BIN_BAK" "$KLAY_CLI_HOME/bin"
  exit 6
fi

