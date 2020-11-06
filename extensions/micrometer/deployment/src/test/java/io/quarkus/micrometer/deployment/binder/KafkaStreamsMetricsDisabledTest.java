package io.quarkus.micrometer.deployment.binder;

import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.micrometer.runtime.binder.kafkastream.KafkaStreamsMetricsProducer;
import io.quarkus.test.QuarkusUnitTest;

public class KafkaStreamsMetricsDisabledTest {

    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest()
            .withConfigurationResource("test-logging.properties")
            .overrideConfigKey("quarkus.micrometer.binder.kafka.enabled", "true")
            .overrideConfigKey("quarkus.micrometer.binder-enabled-default", "false")
            .overrideConfigKey("quarkus.micrometer.registry-enabled-default", "false")
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class));

    @Inject
    Instance<KafkaStreamsMetricsProducer> instance;

    @Test
    void testNoInstancePresentIfNoKafkaStreamsClass() {
        assertTrue(instance.isUnsatisfied(),
                "No KafkaStreamsMetricsProducer expected, because we don't have dependency to kafka-streams");
    }

}
