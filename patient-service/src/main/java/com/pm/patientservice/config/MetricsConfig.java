package com.pm.patientservice.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Prometheus metrics.
 * This class configures various metrics collectors for the application.
 */
@Configuration
public class MetricsConfig {

    /**
     * Configures the TimedAspect for the application.
     * This allows using the @Timed annotation on methods to track their execution time.
     *
     * @param registry the meter registry
     * @return the timed aspect
     */
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    /**
     * Configures JVM memory metrics.
     *
     * @param registry the meter registry
     * @return the JVM memory metrics
     */
    @Bean
    public JvmMemoryMetrics jvmMemoryMetrics(MeterRegistry registry) {
        JvmMemoryMetrics memoryMetrics = new JvmMemoryMetrics();
        memoryMetrics.bindTo(registry);
        return memoryMetrics;
    }

    /**
     * Configures JVM garbage collection metrics.
     *
     * @param registry the meter registry
     * @return the JVM GC metrics
     */
    @Bean
    public JvmGcMetrics jvmGcMetrics(MeterRegistry registry) {
        JvmGcMetrics gcMetrics = new JvmGcMetrics();
        gcMetrics.bindTo(registry);
        return gcMetrics;
    }

    /**
     * Configures JVM thread metrics.
     *
     * @param registry the meter registry
     * @return the JVM thread metrics
     */
    @Bean
    public JvmThreadMetrics jvmThreadMetrics(MeterRegistry registry) {
        JvmThreadMetrics threadMetrics = new JvmThreadMetrics();
        threadMetrics.bindTo(registry);
        return threadMetrics;
    }

    /**
     * Configures processor metrics.
     *
     * @param registry the meter registry
     * @return the processor metrics
     */
    @Bean
    public ProcessorMetrics processorMetrics(MeterRegistry registry) {
        ProcessorMetrics processorMetrics = new ProcessorMetrics();
        processorMetrics.bindTo(registry);
        return processorMetrics;
    }

    /**
     * Configures uptime metrics.
     *
     * @param registry the meter registry
     * @return the uptime metrics
     */
    @Bean
    public UptimeMetrics uptimeMetrics(MeterRegistry registry) {
        UptimeMetrics uptimeMetrics = new UptimeMetrics();
        uptimeMetrics.bindTo(registry);
        return uptimeMetrics;
    }
}
