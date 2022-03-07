// stream is free-terminals

// KTable with teminals in use
def busyTerminals = Stores.inMemoryKeyValueStore('busy-terminals-store')
def busy = builder.table('busy-terminals', Materialized.as(busyTerminals).withCachingDisabled())

// Repartition using terminal number as key, spreading values to another instances
def freeTerminals = Stores.inMemoryKeyValueStore('free-terminals-store')
stream.map {k,v -> KeyValue.pair(v.number, [ terminal: v.number, good: k ]) }
  .peek {k,v -> println v}
  .toTable(Materialized.as(freeTerminals).withCachingDisabled())
