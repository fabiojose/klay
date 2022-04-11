package __namespace_;

import com.github.fabiojose.klay.core.StreamSourceType;
import com.github.fabiojose.klay.core.TopologyBuilder;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.apache.kafka.streams.Topology;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class TopologyProducer {

  @ConfigProperty(name = "source.type", defaultValue = "GROOVY")
  StreamSourceType type;

  @Produces
  public Topology produce() {
    var builder = new TopologyBuilder();

    if (StreamSourceType.GROOVY.equals(type)) {

      return builder.topologyOf(
        getClass().getResourceAsStream("/my-stream.groovy"),
        type
      );

    } else {
      return builder.topologyOf(
        getClass().getResourceAsStream("/my-stream.java"),
        type
      );
    }
  }
}
