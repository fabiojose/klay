#!/bin/bash

if ! [ -x "$(command -v klay)" ]; then
  echo 'ERROR: Klay not installed.' >&2
  exit 5
fi

if ! [ -x "$(command -v kafka-topics.sh)" ]; then
  echo 'ERROR: command kafka-topics.sh not found.' >&2
  exit 5
fi

# klay start broker (broker + zookeeper)
KLAY_BROKER_ID=$(klay -d start broker | grep 'Klay ID' | cut -d':' -f 2 | cut -d' ' -f 2)

# wait to start
echo 'Waiting 8s for Apache Kafka® startup . . .'
sleep 8

# get broker port
BROKER_PORT=$(klay describe -o=CSV $KLAY_BROKER_ID | tail -1 | cut -d';' -f 6 | cut -d',' -f 1 | cut -d':' -f 3)
BOOTSTRAP_SERVER="localhost:$BROKER_PORT"

# create topics
kafka-topics.sh --bootstrap-server $BOOTSTRAP_SERVER \
--create \
--partitions 3 \
--topic orders

kafka-topics.sh --bootstrap-server $BOOTSTRAP_SERVER \
--create \
--partitions 3 \
--topic payments

kafka-topics.sh --bootstrap-server $BOOTSTRAP_SERVER \
--create \
--partitions 3 \
--topic paid

klay start streams \
--from='orders' \
--to='paid' \
--application-id='demo-join' \
--bootstrap-servers=$BOOTSTRAP_SERVER \
./join.java

klay stop $KLAY_BROKER_ID
