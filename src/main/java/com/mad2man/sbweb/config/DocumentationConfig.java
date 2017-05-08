package com.mad2man.sbweb.config;

import com.google.common.base.Predicates;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@ConfigurationProperties(prefix = "documentation")
@Getter
@Setter
public class DocumentationConfig {

    private String title;
    private String description;
    private String license;
    private String licenseUrl;
    private String version;

    @Bean
    public Docket swaggerSpringMvcPlugin() {

        return new Docket(DocumentationType.SWAGGER_2)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            .select()
            .paths(Predicates.not(PathSelectors.regex("/error.*")))
            .build();
    }

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
            .title(title)
            .description(description)
            .license(license)
            .licenseUrl(licenseUrl)
            .version(version)
            .build();
    }
}
