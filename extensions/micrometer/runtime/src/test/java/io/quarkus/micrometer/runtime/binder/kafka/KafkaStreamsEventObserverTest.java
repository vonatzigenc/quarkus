package io.quarkus.micrometer.runtime.binder.kafka;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.micrometer.core.instrument.binder.kafka.KafkaStreamsMetrics;
import io.quarkus.runtime.ShutdownEvent;

class KafkaStreamsEventObserverTest {

    @Test
    void testKafkaStreamsMetricsClosedAfterShutdownEvent() {
        KafkaStreamsEventObserver sut = new KafkaStreamsEventObserver();

        KafkaStreamsMetrics firstClientMetrics = Mockito.mock(KafkaStreamsMetrics.class);
        KafkaStreamsMetrics secondClientMetrics = Mockito.mock(KafkaStreamsMetrics.class);
        sut.kafkaStreamsMetrics.put(firstClientMetrics, firstClientMetrics);
        sut.kafkaStreamsMetrics.put(secondClientMetrics, secondClientMetrics);

        sut.onStop(new ShutdownEvent());

        Mockito.verify(firstClientMetrics).close();
        Mockito.verify(secondClientMetrics).close();
    }

}