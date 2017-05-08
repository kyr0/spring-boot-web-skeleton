package com.mad2man.sbweb;

import com.mad2man.sbweb.common.DefaultProfile;
import com.mad2man.sbweb.common.Profiles;
import com.mad2man.sbweb.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

@SpringBootApplication
@EnableSwagger2
@ComponentScan
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationConfig.class})
public class Application extends WebMvcConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private final Environment env;

    public static void main(String[] args) throws UnknownHostException {

        SpringApplication app = new SpringApplication(Application.class);

        DefaultProfile.addDefaultProfile(app);

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

    public Application(Environment env) {
        this.env = env;
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
}
