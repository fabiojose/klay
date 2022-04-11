import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;

import java.time.Duration;
import java.util.Map;

// Do the same as my-stream.groovy
class t implements K {

  public KStream build(KStream fromStream, StreamsBuilder builder) {

    // Create new stream from payments topic
    var payments = builder.stream("payments");

    // Print the record value
    payments.peek((k,v) -> System.out.println(v));

    return payments;

  }

}
