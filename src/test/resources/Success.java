// no package at all //

// your imports here
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;

import java.time.Duration;

/*
 * - the class must be called t
 * - and implements the K interface
 */
class t implements K {

  // the class must have one method with this signature and named as apply
  public KStream build(KStream fromStream, StreamsBuilder builder) {

    // must return an instance of KStream
    return fromStream;
  }

}
