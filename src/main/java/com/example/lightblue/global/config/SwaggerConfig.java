package com.example.lightblue.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenApiCustomizer sortTags() {
        return openApi -> {
            if (openApi.getTags() != null) {
                List<Tag> sortedTags = openApi.getTags().stream()
                        .sorted(Comparator.comparing(Tag::getName))
                        .toList();
                openApi.setTags(sortedTags);
            }
        };
    }


    @Bean
    public OpenAPI lightBlueAPI() {
        Info info = new Info()
                .title("LightBlue팀 백엔드")
                .description("LightBlue팀 백엔드 API 명세서")
                .version("1.0.0");

        String jwtSchemeName = "JWT TOKEN";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}