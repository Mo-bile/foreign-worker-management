package com.hr.fwc.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SqsConfig {

    public static final String ALERT_QUEUE = "compliance-alert-queue";
    public static final String ALERT_DLQ = "compliance-alert-dlq";

    public ObjectMapper sqsObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

}
