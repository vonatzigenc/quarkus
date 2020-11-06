package io.quarkus.micrometer.runtime.binder.kafkastream;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.kafka.streams.KafkaStreams;

import io.micrometer.core.instrument.binder.kafka.KafkaStreamsMetrics;
import io.quarkus.arc.Unremovable;
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
    @Unremovable
    @Startup
    public KafkaStreamsMetrics getKafkaStreamsMetrics() {
        return kafkaStreamsMetrics;
    }
}
