#!/usr/bin/env bash

# klay stop <KLAY-ID>

pid=$(cat $KLAY_HOME/processes.d/$1/pid)
kill $pid
