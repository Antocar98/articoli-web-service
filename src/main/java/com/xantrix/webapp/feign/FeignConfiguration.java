package com.xantrix.webapp.feign;

import feign.Logger;
import feign.Retryer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
public class FeignConfiguration {

    @Bean
    public FeignErrorDecoder getFeignErrorDecoder()
    {
        return new FeignErrorDecoder();
    }

    @Bean
    public Retryer retryer() {
        return new CustomRetryer();
    }

    @Bean
    Logger.Level feingLoggerLevel(){
        return Logger.Level.FULL;
    }
}
