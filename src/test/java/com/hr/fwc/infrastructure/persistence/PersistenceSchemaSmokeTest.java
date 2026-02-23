package com.hr.fwc.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional(readOnly = true)
class PersistenceSchemaSmokeTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void foreignWorkerTableShouldContainEmbeddedAndAuditColumns() {
        List<String> columns = columnsOf("FOREIGN_WORKERS");

        assertThat(columns).contains(
            "ID",
            "NAME",
            "PASSPORT_NUMBER",
            "CONTACT_PHONE",
            "CONTACT_EMAIL",
            "VISA_TYPE",
            "VISA_EXPIRY_DATE",
            "ENTRY_DATE",
            "REGISTRATION_NUMBER",
            "CONTRACT_START_DATE",
            "CONTRACT_END_DATE",
            "WORKPLACE_ID",
            "NATIONALITY",
            "CREATED_AT",
            "UPDATED_AT"
        );
    }

    @Test
    void complianceDeadlineTableShouldContainCoreColumns() {
        List<String> columns = columnsOf("COMPLIANCE_DEADLINES");

        assertThat(columns).contains(
            "ID",
            "WORKER_ID",
            "DEADLINE_TYPE",
            "DUE_DATE",
            "STATUS",
            "DESCRIPTION"
        );
    }

    @Test
    void workplaceTableShouldContainBusinessColumns() {
        List<String> columns = columnsOf("WORKPLACES");

        assertThat(columns).contains(
            "ID",
            "NAME",
            "BUSINESS_NUMBER",
            "ADDRESS",
            "CONTACT_PHONE",
            "CREATED_AT"
        );
    }

    private List<String> columnsOf(String tableName) {
        return jdbcTemplate.queryForList(
            "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ?",
            String.class,
            tableName
        );
    }
}
