#!/usr/bin/env bash

# klay prune

while IFS=';' read -r klayId pidSupplier typeSupplier versionSupplier created statusSupplier portsSupplier args; do

  status=$(eval $statusSupplier) 2>/dev/null
  if [ "$status" == "Stopped" ]; then

    # remove logs
    rm "$KLAY_HOME/logs/${klayId}.log" "$KLAY_HOME/logs/${klayId}-errors.log" 2>/dev/null

    # remove metadada directory
    rm -r "$PROCESSES_DIR/$klayId" 2>/dev/null

  else
    # Save Running processes to create new processes file
    echo "$klayId;$pidSupplier;$typeSupplier;$versionSupplier;$created;$statusSupplier;$portsSupplier;$args" >> "$KLAY_PROCESSES.new"

  fi
done < "$KLAY_PROCESSES"

rm "$KLAY_PROCESSES" && touch "$KLAY_PROCESSES"

if [ -f "$KLAY_PROCESSES.new" ]; then
  mv -f "$KLAY_PROCESSES.new" "$KLAY_PROCESSES"
fi

