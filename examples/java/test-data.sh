#!/bin/bash

klay kafka-cli producer \
--broker-list "$1" \
--topic orders \
--property "parse.key=true" \
--property "key.separator=|" <<EOF
2002|{"id": "2002", "value": 44.3}
2003|{"id": "2003", "value": 2.99}
2004|{"id": "2004", "value": 23.0}
2005|{"id": "2005", "value": 199.99}
EOF

klay kafka-cli producer \
--broker-list "$1" \
--topic payments \
--property "parse.key=true" \
--property "key.separator=|" <<EOF
2002|{"code": "y300"}
2003|{"code": "t900"}
2004|{"code": "r500"}
2005|{"code": "p560"}
EOF
