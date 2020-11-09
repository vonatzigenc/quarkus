package io.quarkus.micrometer.runtime.binder.kafka;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.apache.kafka.streams.KafkaStreams;
import org.jboss.logging.Logger;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.binder.kafka.KafkaStreamsMetrics;
import io.quarkus.runtime.ShutdownEvent;

/**
 * Observer to create and register KafkaStreamsMetrics.
 * 
 * Must be separated from KafkaEventObserver, because they use different dependencies and if only kafka-client is used, the
 * classes from kafka-streams aren't loaded.
 */
@ApplicationScoped
public class KafkaStreamsEventObserver {

    private static final Logger log = Logger.getLogger(KafkaStreamsEventObserver.class);
    MeterRegistry registry = Metrics.globalRegistry;
    Map<Object, KafkaStreamsMetrics> kafkaStreamsMetrics = new HashMap<>();

    /**
     * Manage bind/close of KafkaStreamsMetrics for the specified KafkaStreams client.
     * If kafkaStreams has not been seen before, it will be bound to the
     * Micrometer registry and instrumented using a Kafka MeterBinder.
     * If the producer has been seen before, the MeterBinder will be closed.
     *
     * @param kafkaStreams Observed KafkaStreams instance
     */
    public synchronized void kafkaStreamsCreated(@Observes KafkaStreams kafkaStreams) {
        KafkaStreamsMetrics metrics = kafkaStreamsMetrics.remove(kafkaStreams);
        if (metrics == null) {
            metrics = new KafkaStreamsMetrics(kafkaStreams);
            try {
                metrics.bindTo(registry);
                kafkaStreamsMetrics.put(kafkaStreams, metrics);
            } catch (Throwable t) {
                log.warnf(t, "Unable to register metrics for KafkaStreams %s", kafkaStreams);
                tryToClose(metrics);
            }
        } else {
            tryToClose(metrics);
        }
    }

    void onStop(@Observes ShutdownEvent event) {
        kafkaStreamsMetrics.values().forEach(this::tryToClose);
    }

    void tryToClose(AutoCloseable c) {
        try {
            c.close();
        } catch (Exception e) {
            // intentionally empty
        }
    }

}
