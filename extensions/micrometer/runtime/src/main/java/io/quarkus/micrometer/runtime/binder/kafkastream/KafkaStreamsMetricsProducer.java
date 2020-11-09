package io.quarkus.micrometer.runtime.binder.kafkastream;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.kafka.streams.KafkaStreams;

import io.micrometer.core.instrument.binder.kafka.KafkaStreamsMetrics;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;

@ApplicationScoped
public class KafkaStreamsMetricsProducer {

    KafkaStreamsMetrics kafkaStreamsMetrics;

    @Inject
    public KafkaStreamsMetricsProducer(Instance<KafkaStreams> kafkaStreams) {
        if (!kafkaStreams.isUnsatisfied()) {
            kafkaStreamsMetrics = new KafkaStreamsMetrics(kafkaStreams.get());
        }
    }

    @Produces
    @Singleton
    @Startup
    public KafkaStreamsMetrics getKafkaStreamsMetrics() {
        return kafkaStreamsMetrics;
    }

    void onStop(@Observes ShutdownEvent event) {
        if (kafkaStreamsMetrics != null) {
            kafkaStreamsMetrics.close();
        }
    }

}
