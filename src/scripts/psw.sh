#!/bin/bash

# To check pid and return "Running" or "Stopped"
ps -p $1 >> /dev/null
[ $? -eq 0 ] && echo -n "Running" || echo -n "Stopped"
