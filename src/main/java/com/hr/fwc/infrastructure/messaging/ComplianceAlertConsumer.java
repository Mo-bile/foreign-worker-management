package com.hr.fwc.infrastructure.messaging;

import static com.hr.fwc.infrastructure.config.SqsConfig.ALERT_QUEUE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean(SqsTemplate.class)
public class ComplianceAlertConsumer {

    private static final Logger log = LoggerFactory.getLogger(ComplianceAlertConsumer.class);

    private final ObjectMapper objectMapper;

    public ComplianceAlertConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SqsListener(ALERT_QUEUE)
    public void handleAlert(String payload) {
        try {
            ComplianceAlertMessage message = objectMapper.readValue(payload, ComplianceAlertMessage.class);

            log.info("=== 알림 수신 ===");
            log.info("근로자 ID: {}", message.workerId());
            log.info("데드라인: {} ({})", message.deadlineType(), message.status());
            log.info("마감일: {}", message.dueDate());
            log.info("설명: {}", message.description());

            processNotification(message);

        }catch (JsonProcessingException e) {
            log.error("메시지 역직렬화 실패, 메시지 폐기: {}", payload, e);
        }
    }

    private void processNotification(ComplianceAlertMessage message) {
        // 실제 알림 발송 로직
        log.info("알림 처리 완료 - messageId : {}", message.messageId());
    }

}
