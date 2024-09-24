package com.xantrix.webapp;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Resilience4JConfiguration
{

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfiguration() {

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                //soglia in percentuali di fallimentiche che determina l'apertura del circuito
                .failureRateThreshold(50)
                //numero di chiamate di riferimento
                .slidingWindowSize(5)
                //tempo di durata dello stato di circuito aperto
                .waitDurationInOpenState(Duration.ofMillis(20000))
                //numero di chiamate in stato half open
                .permittedNumberOfCallsInHalfOpenState(5)
                //Se impiega più di un secondo viene conteggiato come lento
                .slowCallDurationThreshold(Duration.ofMillis(1000))
                //soglia in percentuale di
                .slowCallRateThreshold(50.0F)

                .build();

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofMillis(3000))
                .cancelRunningFuture(true)
                .build();

        return factory -> factory.configure(builder -> builder.circuitBreakerConfig(circuitBreakerConfig)
                        .timeLimiterConfig(timeLimiterConfig).build(),
                "circuitbreaker");
    }



}