package com.hr.fwc.domain.workplace;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;



import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WorkplaceTest {

    @Test
    @DisplayName("사업장 생성 시 필드가 올바르게 설정됨")
    void createShouldSetFieldsCorrectly() {
        Workplace workplace = Workplace.create("테스트 회사", "123-45-67890", "서울시 강남구", "02-1234-5678");

        assertThat(workplace.name()).isEqualTo("테스트 회사");
        assertThat(workplace.businessNumber()).isEqualTo("123-45-67890");
        assertThat(workplace.address()).isEqualTo("서울시 강남구");
        assertThat(workplace.contactPhone()).isEqualTo("02-1234-5678");
    }

    @Test
    @DisplayName("사업장명이 null이면 예외 발생")
    void createShouldRejectNullName() {
        assertThatThrownBy(() -> Workplace.create(null, "123-45-67890", "서울", "02-1234-5678"))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("사업자등록번호가 null이면 예외 발생")
    void createShouldRejectNullBusinessNumber() {
        assertThatThrownBy(() -> Workplace.create("테스트 회사", null, "서울", "02-1234-5678"))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("주소와 연락처는 null 허용")
    void createShouldAllowNullOptionalFields() {
        Workplace workplace = Workplace.create("테스트 회사", "123-45-67890", null, null);

        assertThat(workplace.address()).isNull();
        assertThat(workplace.contactPhone()).isNull();
    }
}
