package com.mad2man.sbweb;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Predicates;
import com.mad2man.sbweb.config.ApplicationProperties;
import com.mad2man.sbweb.config.Profiles;
import com.mad2man.sbweb.util.DefaultProfileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

@SpringBootApplication
@EnableSwagger2
@ComponentScan
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class})
public class Application extends WebMvcConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private final Environment env;
    private ApplicationProperties applicationProperties;

    public static void main(String[] args) throws UnknownHostException {

        SpringApplication app = new SpringApplication(Application.class);
        DefaultProfileUtil.addDefaultProfile(app);

        Environment env = app.run(args).getEnvironment();

        String protocol = "http";

        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }

        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}\n\t" +
                        "External: \t{}://{}:{}\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                env.getProperty("server.port"),
                protocol,
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                env.getActiveProfiles());
    }

    public Application(Environment env, ApplicationProperties applicationProperties) {

        this.env = env;
        this.applicationProperties = applicationProperties;
    }

    @PostConstruct
    public void initApplication() {

        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());

        if (activeProfiles.contains(Profiles.PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(Profiles.PROFILE_PRODUCTION)) {

            log.error("You have mis-configured your application! It should not run " +
                    "with both the 'dev' and 'prod' profiles at the same time.");
        }
    }
    @Bean
    public Docket swaggerSpringMvcPlugin() {

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();
    }

    @Component
    @Primary
    public class CustomObjectMapper extends ObjectMapper {
        public CustomObjectMapper() {

            setSerializationInclusion(JsonInclude.Include.NON_NULL);
            configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            enable(SerializationFeature.INDENT_OUTPUT);
        }
    }

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                .title(applicationProperties.getProject().getName() + " REST-API")
                .description(String.format("%s<br/> visit us on <a href=\"%s\">Github</a> (<a href=\"%s\">%s</a>)",
                    applicationProperties.getProject().getDescription(),
                    applicationProperties.getProject().getUrl(),
                    applicationProperties.getProject().getIssueManagement().getUrl(),
                    applicationProperties.getProject().getIssueManagement().getSystem()))
                .license(applicationProperties.getProject().getLicense().getName())
                .licenseUrl(applicationProperties.getProject().getLicense().getUrl())
                .version(applicationProperties.getProject().getVersion())
                .build();
    }

}
