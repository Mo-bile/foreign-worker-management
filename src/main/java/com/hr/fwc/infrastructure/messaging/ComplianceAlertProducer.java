package com.hr.fwc.infrastructure.messaging;

import static com.hr.fwc.infrastructure.config.SqsConfig.ALERT_QUEUE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean(SqsTemplate.class)
public class ComplianceAlertProducer {

    private static final Logger log = LoggerFactory.getLogger(ComplianceAlertProducer.class);

    private final SqsTemplate sqsTemplate;
    private final ObjectMapper objectMapper;

    public ComplianceAlertProducer(SqsTemplate sqsTemplate, ObjectMapper objectMapper) {
        this.sqsTemplate = sqsTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendAlert(ComplianceAlertMessage message) {
        try {
            String payload = objectMapper.writeValueAsString(message);
            sqsTemplate.send(ALERT_QUEUE, payload);
            log.info("Compliance alert sent to SQS: {} / deadlineId : {}, status : {}", payload, message.deadlineId(), message.status());
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize compliance alert message", e);
        }
    }

}
