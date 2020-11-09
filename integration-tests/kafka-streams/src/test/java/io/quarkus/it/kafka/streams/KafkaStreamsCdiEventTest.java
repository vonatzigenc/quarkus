package io.quarkus.it.kafka.streams;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTestResource(KafkaTestResource.class)
@QuarkusTest
public class KafkaStreamsCdiEventTest {

    @Inject
    KafkaStreamsEventCounter eventCounter;

    @Test
    void testEventShouldBePublished() {
        assertThat("There should be at least one event for creating KafakStreams in the producder.",
                eventCounter.getEventCount(), greaterThanOrEqualTo(1));
    }
}
