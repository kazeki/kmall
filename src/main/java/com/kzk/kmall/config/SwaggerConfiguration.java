package com.kzk.kmall.config;

import com.fasterxml.classmate.TypeResolver;
import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.config.apidoc.PageableParameterBuilderPlugin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

@Configuration
public class SwaggerConfiguration {
    private final JHipsterProperties.Swagger properties;

    public SwaggerConfiguration(JHipsterProperties jHipsterProperties) {
        this.properties = jHipsterProperties.getSwagger();
    }

    @Bean
    public Docket swaggerSpringfoxManagementDocket2(@Value("${spring.application.name}") String appName, @Value("${management.context-path}") String managementContextPath, @Value("${info.project.version}") String appVersion) {
        return this.createDocket()
            .apiInfo(new ApiInfo(appName + " " + "kzk API", "Management endpoints documentation", appVersion, "", ApiInfo.DEFAULT_CONTACT, "", "", new ArrayList()))
            .groupName("kzkGroup").host(this.properties.getHost()).protocols(new HashSet(Arrays.asList(this.properties.getProtocols()))).forCodeGeneration(true).directModelSubstitute(ByteBuffer.class, String.class).genericModelSubstitutes(new Class[]{ResponseEntity.class}).select().paths(PathSelectors.any()).build();
    }

    protected Docket createDocket() {
        return new Docket(DocumentationType.SWAGGER_2);
    }
}
