package com.hr.fwc.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DateSerializationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("LocalDate가_ISO_8601_문자열로_직렬화된다")
    void LocalDate가_ISO_8601_문자열로_직렬화된다() throws Exception {
        record DateHolder(LocalDate date) {}

        DateHolder holder = new DateHolder(LocalDate.of(2026, 12, 31));
        String json = objectMapper.writeValueAsString(holder);

        assertThat(json).contains("\"2026-12-31\"");
        assertThat(json).doesNotContain("[2026,12,31]");
    }

    @Test
    @DisplayName("ISO_8601_문자열이_LocalDate로_역직렬화된다")
    void ISO_8601_문자열이_LocalDate로_역직렬화된다() throws Exception {
        record DateHolder(LocalDate date) {}

        String json = "{\"date\":\"2026-12-31\"}";
        DateHolder holder = objectMapper.readValue(json, DateHolder.class);

        assertThat(holder.date()).isEqualTo(LocalDate.of(2026, 12, 31));
    }
}
