package com.github.kafousis.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    // Springdoc-OpenAPI generates the documentation based on our application REST APIs
    // OpenAPI generated document: http://localhost:8080/v3/api-docs

    // Swagger-UI: collection of HTML, Javascript, and CSS files, generates a user interface based on the OpenAPI specification
    // default-ui-url = http://localhost:8080/swagger-ui/index.html
    // custom-ui-url = http://localhost:8080/swagger-ui

    // https://www.baeldung.com/openapi-jwt-authentication

    @Bean
    public OpenAPI openAPI() {

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info().title("Spring Entity-Dto Rest with JWT")
                        .description("Spring Boot project that shows how to build and secure a RESTful API using Spring Data JPA and Spring Security.")
                        .version("v0.0.1")
                        .contact(new Contact().name("Giannis Kafousis").email("g.kafousis@gmail.com").url("https://www.linkedin.com/in/gkafousis/")))

                .externalDocs(new ExternalDocumentation()
                        .description("Spring Entity-Dto Rest with JWT documentation")
                        .url("https://github.com/kafousis/spring-entity-dto-rest-with-jwt"))

                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
