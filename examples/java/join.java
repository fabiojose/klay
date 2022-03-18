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

  // the class must have one method with this signature and named as apply
  public KStream build(KStream fromStream, StreamsBuilder builder) {

    fromStream.filter((k,v) -> k.equals("2002"))
      .peek((k,v) -> System.out.println(v));

    var payments = builder.stream("payments");

    // must return an instance of KStream
    return fromStream.join(
      payments,
      (order, payment) -> Map.of("order", order, "payment", payment),
      JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofMinutes(5))
    )
    .peek((k, v) -> System.out.println(v));

  }

}
