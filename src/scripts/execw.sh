#!/usr/bin/env bash

DETACH=$([[ "$1" == "-d" ]] || [[ "$1" == "--detach" ]] && echo -n 'y' || echo -n 'n')

if ! [ -f /proc/sys/kernel/random/uuid  ]; then
  echo 'ERROR: path /proc/sys/kernel/random/uuid not found.' >&2
  exit 5
fi
EXTERNAL_ID=$(cat /proc/sys/kernel/random/uuid)
PROCESS_DIR="$PROCESSES_DIR/$EXTERNAL_ID"
mkdir -p "$PROCESS_DIR"

if ! [ -x "$(command -v nohup)" ]; then
  echo 'ERROR: nohup is not installed.' >&2
  exit 5
fi

if [ "$DETACH" == "n" ]; then

  if [ "$KLAY_HELP_REQUESTED" == "n" ]; then
    # KLAY ID     LINUX PID    TYPE   VERSION    CREATED    STATUS    PORTS   ARGS
    echo "$EXTERNAL_ID;cat '$PROCESS_DIR/pid';cat '$PROCESS_DIR/type';cat '$PROCESS_DIR/version';$(date +"%Y-%m-%d %H:%M:%S");bash psw.sh \$(cat $PROCESS_DIR/pid);cat '$PROCESS_DIR/ports';'$@'" >> $KLAY_PROCESSES
  fi

  java $JAVA_OPTS -jar $KLAY_UBER_JAR_LOCATION --external-id=${EXTERNAL_ID} "$@"
else
  nohup java $JAVA_OPTS -jar $KLAY_UBER_JAR_LOCATION --external-id=${EXTERNAL_ID} "$@" >> "${LOGS_DIR}/${EXTERNAL_ID}.log" 2> "${LOGS_DIR}/${EXTERNAL_ID}-errors.log" &
  LINUX_PID=$!
  echo 'Process started'
  echo "   Klay ID..: $EXTERNAL_ID"
  echo "   Linux pID: $LINUX_PID"

  # KLAY ID     LINUX PID    TYPE   VERSION    CREATED    STATUS    PORTS   ARGS
  echo "$EXTERNAL_ID;$LINUX_PID;cat '$PROCESS_DIR/type';cat '$PROCESS_DIR/version';$(date +"%Y-%m-%d_%H:%M:%S");bash $SCRIPTS_HOME/psw.sh $LINUX_PID;cat '$PROCESS_DIR/ports';'$@'" >> $KLAY_PROCESSES

  # TYPE, VERSION and PORTS are written by the processes it self
fi
