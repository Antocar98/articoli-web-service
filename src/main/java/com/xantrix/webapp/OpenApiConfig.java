package com.xantrix.webapp;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig
{
    //http://localhost:8080/swagger-ui.html

    @Bean
    public OpenAPI customOpenAPI()
    {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("ARTICOLI WEB SERVICE API")
                        .description("Spring Boot REST API per la gestione articoli AlphaShop")
                        .termsOfService("terms")
                        .contact(new Contact().email("antonio.carnevale@cap4lab.com").name("Antonio Carnevale").url("https://xantrix.it/info"))
                        .license(new License().name("Apache License Version 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                        .version("1.0")
                );
    }

    @Bean
    public GroupedOpenApi excludeDtoApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/api/**")
                .packagesToExclude("com.xantrix.webapp.dtos")  // Escludi i DTO dalla documentazione
                .build();
    }
}