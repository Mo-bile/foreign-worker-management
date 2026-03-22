package com.hr.fwc.infrastructure.loader;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvLoader {

    private static final Logger log = LoggerFactory.getLogger(CsvLoader.class);
    private static final CsvMapper CSV_MAPPER = new CsvMapper();
    private static final double MAX_FAILURE_RATIO = 0.1;

    /**
     * CSV 파일을 파싱하여 도메인 객체 리스트를 반환한다.
     * 파일 누락이나 I/O 오류 시 예외를 던진다 — 호출자가 정책을 결정한다.
     */
    public <T> List<T> load(String resourcePath, Class<T> type) {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        if (!resource.exists()) {
            throw new CsvFileNotFoundException(resourcePath);
        }

        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        List<T> results = new ArrayList<>();
        int failureCount = 0;
        int totalCount = 0;

        try (InputStream is = resource.getInputStream()) {
            com.fasterxml.jackson.databind.MappingIterator<T> iterator =
                CSV_MAPPER.readerFor(type).with(schema).readValues(is);
            // lineNum starts at 1 to account for the header row
            int lineNum = 1;
            while (iterator.hasNext()) {
                lineNum++;
                totalCount++;
                try {
                    results.add(iterator.next());
                } catch (Exception e) {
                    failureCount++;
                    log.warn("CSV 파싱 실패 ({}:{} 행)", resourcePath, lineNum, e);
                }
            }
        } catch (Exception e) {
            throw new UncheckedIOException(
                "CSV 파일 읽기 실패: " + resourcePath,
                new java.io.IOException(e));
        }

        if (totalCount > 0 && (double) failureCount / totalCount > MAX_FAILURE_RATIO) {
            throw new CsvParseException(
                String.format("CSV 파싱 실패율 초과: %s (%d/%d건 실패)", resourcePath, failureCount, totalCount));
        }

        log.info("CSV 적재 완료: {} → {}건 (실패 {}건)", resourcePath, results.size(), failureCount);
        return results;
    }

    public static class CsvFileNotFoundException extends RuntimeException {
        public CsvFileNotFoundException(String resourcePath) {
            super("CSV 파일을 찾을 수 없습니다: " + resourcePath);
        }
    }

    public static class CsvParseException extends RuntimeException {
        public CsvParseException(String message) {
            super(message);
        }
    }
}
