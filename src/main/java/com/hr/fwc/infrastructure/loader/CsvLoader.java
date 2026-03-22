package com.hr.fwc.infrastructure.loader;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvLoader {

    private static final Logger log = LoggerFactory.getLogger(CsvLoader.class);

    public <T> List<T> load(String resourcePath, Class<T> type) {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        if (!resource.exists()) {
            log.warn("CSV 파일을 찾을 수 없습니다: {}. 해당 테이블 적재를 스킵합니다.", resourcePath);
            return List.of();
        }

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        List<T> results = new ArrayList<>();

        try (InputStream is = resource.getInputStream()) {
            com.fasterxml.jackson.databind.MappingIterator<T> iterator =
                mapper.readerFor(type).with(schema).readValues(is);
            int lineNum = 1;
            while (iterator.hasNext()) {
                lineNum++;
                try {
                    results.add(iterator.next());
                } catch (Exception e) {
                    log.warn("CSV 파싱 실패 ({}:{} 행): {}", resourcePath, lineNum, e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("CSV 파일 읽기 실패: {}", resourcePath, e);
            return List.of();
        }

        log.info("CSV 적재 완료: {} → {}건", resourcePath, results.size());
        return results;
    }
}
