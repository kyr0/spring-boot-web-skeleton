package com.mad2man.sbweb.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;

@Configuration
public class ObjectMapperConfig {

    @Component
    @Primary
    public class CustomObjectMapper extends ObjectMapper {
        public CustomObjectMapper() {

            setSerializationInclusion(JsonInclude.Include.NON_NULL);
            configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            enable(SerializationFeature.INDENT_OUTPUT);

            // avoid having to annotate classes
            registerModule(new ParameterNamesModule());
            registerModule(new JavaTimeModule());
            registerModule(new Jdk8Module());

            // make private fields of Person visible to Jackson
            setVisibility(FIELD, ANY);
        }
    }
}
