#!/usr/bin/env bash

# klay ps

if ! [ -x "$(command -v column)" ]; then
  echo 'ERROR: column command not installed.' >&2
  exit 5
fi

ALL=$([[ "$1" == "-a" ]] || [[ "$1" == "--all" ]] && echo -n 'y' || echo -n 'n')

fmt="%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n"
(printf "$fmt" 'KLAY-ID' "PID" "TYPE" "VERSION" "PORTS" "STATUS"
while IFS=';' read -r klayId pidSupplier typeSupplier versionSupplier created statusSupplier portsSupplier args; do

  pid=$(eval $pidSupplier)
  status=$(eval $statusSupplier) 2>/dev/null
  if [ "$status" == "Running" ] || [ "$ALL" == "y" ]; then
    # Get the type, version, status and ports
    type=$(eval $typeSupplier)
    version=$(eval $versionSupplier)
    ports=$(eval $portsSupplier)
    printf "$fmt" "$klayId" "$pid" "$type" "$version" "$ports" "$status"
  fi
done < "$KLAY_PROCESSES") | column -t
