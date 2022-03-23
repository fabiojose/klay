// no package at all //

// your imports here
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;

import java.time.Duration;
import java.util.Map;

/*
 * - the class must be called t
 * - and implements the K interface
 */
class t implements K {

  /**
   * the class must have one method with this signature and named as build.
   * @param fromStream Will be {@code null} when {@code --from} option has no value
   */
  public KStream build(KStream fromStream, StreamsBuilder builder) {

    var payments = builder.stream("payments");
    payments.to("payments-copy");

    return payments;

  }

}
