#!/usr/bin/env bash

# klay logs [-f --follow] <KLAY-ID>

if ! [ -x "$(command -v tail)" ]; then
  echo 'ERROR: tail command not installed.' >&2
  exit 5
fi

if ! [ -x "$(command -v cat)" ]; then
  echo 'ERROR: cat command not installed.' >&2
  exit 5
fi

FOLLOW=$([[ "$1" == "-f" ]] || [[ "$1" == "--follow" ]] && echo -n 'y' || echo -n 'n')

if [ "$FOLLOW" == "y" ]; then
  tail -v -F $LOGS_DIR/$2.log $LOGS_DIR/$2-errors.log
else
  echo ''
  echo "==> $LOGS_DIR/$1.log <=="
  cat $LOGS_DIR/$1.log
  echo ''
  echo "==> $LOGS_DIR/$1-errors.log <=="
  cat $LOGS_DIR/$1-errors.log
fi
