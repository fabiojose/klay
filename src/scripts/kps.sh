#!/usr/bin/env bash

# klay ps

if ! [ -x "$(command -v column)" ]; then
  echo 'ERROR: column command not installed.' >&2
  exit 5
fi

fmt="%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n"
(printf "$fmt" 'KLAY-ID' "PID" "TYPE" "VERSION" "PORTS"
while IFS=';' read -r klayId linuxId typeSupplier versionSupplier created statusSupplier portsSupplier args; do
  #TODO: Print out just the Running processes

  status=$(eval $statusSupplier)
  if [ "$status" == "Running" ]; then
    #TODO: Get the type, version, status and ports
    type=$(eval $typeSupplier)
    version=$(eval $versionSupplier)
    ports=$(eval $portsSupplier)
    printf "$fmt" "$klayId" "$linuxId" "$type" "$version" "$ports"
  fi
done < "$KLAY_PROCESSES") | column -t
