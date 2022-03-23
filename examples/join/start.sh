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
klay kafka-cli topic --bootstrap-server $BOOTSTRAP_SERVER \
--create \
--partitions 3 \
--topic orders

klay kafka-cli topic --bootstrap-server $BOOTSTRAP_SERVER \
--create \
--partitions 3 \
--topic payments

klay kafka-cli topic --bootstrap-server $BOOTSTRAP_SERVER \
--create \
--partitions 3 \
--topic paid

read -rsn1 -p"Press any key to start the Kafka Streams using Klay";echo

KLAY_STREAMS_ID=$(klay --detach start streams \
--from='orders' \
--to='paid' \
--application-id='demo-join-groovy' \
--bootstrap-servers=$BOOTSTRAP_SERVER \
./join.groovy | grep 'Klay ID' | cut -d':' -f 2 | cut -d' ' -f 2)

read -rsn1 -p"Press any key to produce test data";echo

source ./test-data.sh $BOOTSTRAP_SERVER

read -rsn1 -p"Press any key to start a console consumer and see the results";echo

kafka-console-consumer.sh \
--bootstrap-server $BOOTSTRAP_SERVER \
--from-beginning  \
--topic paid \
--property print.partition=true \
--property print.offset=true \
--property print.headers=true \
--property print.key=true

klay stop $KLAY_BROKER_ID
klay stop $KLAY_STREAMS_ID
