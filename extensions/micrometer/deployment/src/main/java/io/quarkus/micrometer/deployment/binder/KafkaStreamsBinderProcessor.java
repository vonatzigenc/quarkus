package io.quarkus.micrometer.deployment.binder;

import java.util.function.BooleanSupplier;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.micrometer.runtime.MicrometerRecorder;
import io.quarkus.micrometer.runtime.config.MicrometerConfig;

public class KafkaStreamsBinderProcessor {

    static final String KAFKA_STREAMS_CLASS_NAME = "org.apache.kafka.streams.KafkaStreams";
    static final Class<?> KAFKA_STREAMS_CLASS_CLASS = MicrometerRecorder.getClassForName(KAFKA_STREAMS_CLASS_NAME);

    static final String KAFKA_STREAMS_METRICS_PRODUCER_CLASS_NAME = "io.quarkus.micrometer.runtime.binder.kafkastream.KafkaStreamsMetricsProducer";

    static class KafkaStreamsSupportEnabled implements BooleanSupplier {
        MicrometerConfig mConfig;

        public boolean getAsBoolean() {
            return KAFKA_STREAMS_CLASS_CLASS != null && mConfig.checkBinderEnabledWithDefault(mConfig.binder.kafka);
        }
    }

    @BuildStep(onlyIf = KafkaStreamsBinderProcessor.KafkaStreamsSupportEnabled.class)
    AdditionalBeanBuildItem createKafkaStreamsMetricsProducer() {
        return AdditionalBeanBuildItem.builder()
                .addBeanClass(KAFKA_STREAMS_METRICS_PRODUCER_CLASS_NAME)
                .setUnremovable().build();
    }

}
