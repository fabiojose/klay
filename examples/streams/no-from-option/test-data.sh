#!/bin/bash

klay kafka-cli producer \
--broker-list "$1" \
--topic payments \
--property "parse.key=true" \
--property "key.separator=|" <<EOF
2002|{"code": "y300"}
2003|{"code": "t900"}
2004|{"code": "r500"}
2005|{"code": "p560"}
2006|{"code": "p780"}
EOF
