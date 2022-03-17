#!/usr/bin/env bash

# klay describe --output=<FORMAT> <KLAY-ID>

FORMAT=$([[ "$2" == *"-o"* ]] || [[ "$2" == *"--output"* ]] && echo -n $2 | cut -d'=' -f 2 || echo -n 'PLAIN')
KLAY_ID="${@: -1}"

# TODO: Search KLAY_ID in processes
FOUND=$(grep "$KLAY_ID" "$KLAY_HOME/processes")

if [[ ! -z "$FOUND" ]]; then
  if [ "$FORMAT" == "PLAIN" ]; then

    while IFS=';' read -r klayId pidSupplier typeSupplier versionSupplier created statusSupplier portsSupplier args; do
      echo ''
      echo "   KLAY-ID:$klayId"
      echo "       PID:$(eval $pidSupplier)"
      echo "    status:$(eval $statusSupplier)"
      echo "      type:$(eval $typeSupplier)"
      echo "   version:$(eval $versionSupplier)"
      echo "     ports:$(eval $portsSupplier)"
      echo "      args:$args"
      echo ''
    done <<< "$FOUND"

  elif [ "$FORMAT" == "CSV" ]; then

    # TODO: echo the evaluted result
    while IFS=';' read -r klayId pidSupplier typeSupplier versionSupplier created statusSupplier portsSupplier args; do
      echo "KLAY-ID;PID;status;type;version;ports;args"
      echo "$klayId;$(eval $pidSupplier);$(eval $statusSupplier);$(eval $typeSupplier);$(eval $versionSupplier);$(eval $portsSupplier);$args"
    done <<< "$FOUND"
  else
    echo "[ERROR] Unknown output format $FORMAT" >&2
    exit 6
  fi
else
  echo "[ERROR] No such process: $KLAY_ID" >&2
  exit 1
fi
