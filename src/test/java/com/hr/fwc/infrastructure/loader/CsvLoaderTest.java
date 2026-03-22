package com.hr.fwc.infrastructure.loader;

import com.hr.fwc.infrastructure.loader.csv.RegionalIndustryCsvRow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CsvLoaderTest {

    private final CsvLoader csvLoader = new CsvLoader();

    @Test
    @DisplayName("유효한 CSV 파일을 파싱하여 올바른 건수를 반환한다")
    void 유효한_CSV_파싱() {
        List<RegionalIndustryCsvRow> rows = csvLoader.load("data/regional_industry.csv", RegionalIndustryCsvRow.class);
        assertThat(rows).hasSize(5);
        assertThat(rows.get(0).getRegion()).isEqualTo("서울특별시");
        assertThat(rows.get(0).getWorkerCount()).isEqualTo(12500);
    }

    @Test
    @DisplayName("존재하지 않는 CSV 파일은 CsvFileNotFoundException을 던진다")
    void 존재하지_않는_파일() {
        assertThatThrownBy(() -> csvLoader.load("data/nonexistent.csv", RegionalIndustryCsvRow.class))
            .isInstanceOf(CsvLoader.CsvFileNotFoundException.class)
            .hasMessageContaining("nonexistent.csv");
    }
}
