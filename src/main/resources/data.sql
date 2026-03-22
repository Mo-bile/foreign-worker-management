-- Company seed data
-- 앱 시작 시 JPA 스키마 생성 후 자동 실행 (defer-datasource-initialization=true 필요)

INSERT INTO companies (name, business_number, region, sub_region, industry_category, industry_sub_category, employee_count, foreign_worker_count, address, contact_phone, created_at)
VALUES ('한국제조(주)', '123-45-67890', 'GYEONGGI', '안산시', 'MANUFACTURING', '금속가공제품제조업', 150, 45, '경기도 안산시 단원구 공단1로 10', '031-123-4567', NOW());

INSERT INTO companies (name, business_number, region, sub_region, industry_category, industry_sub_category, employee_count, foreign_worker_count, address, contact_phone, created_at)
VALUES ('그린농장', '234-56-78901', 'CHUNGNAM', '논산시', 'AGRICULTURE', NULL, 30, 12, '충남 논산시 연산면 농장길 25', '041-234-5678', NOW());

INSERT INTO companies (name, business_number, region, sub_region, industry_category, industry_sub_category, employee_count, foreign_worker_count, address, contact_phone, created_at)
VALUES ('대한건설(주)', '345-67-89012', 'SEOUL', '강남구', 'CONSTRUCTION', NULL, 200, 60, '서울 강남구 테헤란로 100', '02-345-6789', NOW());

INSERT INTO companies (name, business_number, region, sub_region, industry_category, industry_sub_category, employee_count, foreign_worker_count, address, contact_phone, created_at)
VALUES ('서울식품(주)', '456-78-90123', 'GYEONGGI', '화성시', 'MANUFACTURING', '식료품제조업', 80, 25, '경기도 화성시 동탄대로 50', '031-456-7890', NOW());

INSERT INTO companies (name, business_number, region, sub_region, industry_category, industry_sub_category, employee_count, foreign_worker_count, address, contact_phone, created_at)
VALUES ('글로벌IT(주)', '567-89-01234', 'SEOUL', '구로구', 'SERVICE', NULL, 50, 8, '서울 구로구 디지털로 300', '02-567-8901', NOW());

INSERT INTO companies (name, business_number, region, sub_region, industry_category, industry_sub_category, employee_count, foreign_worker_count, address, contact_phone, created_at)
VALUES ('해양수산(주)', '678-90-12345', 'BUSAN', '사하구', 'FISHING', NULL, 40, 15, '부산 사하구 낙동남로 200', '051-678-9012', NOW());

INSERT INTO companies (name, business_number, region, sub_region, industry_category, industry_sub_category, employee_count, foreign_worker_count, address, contact_phone, created_at)
VALUES ('스마트물류(주)', '789-01-23456', 'INCHEON', '서구', 'TRANSPORTATION', NULL, 120, 30, '인천 서구 청라대로 150', '032-789-0123', NOW());

INSERT INTO companies (name, business_number, region, sub_region, industry_category, industry_sub_category, employee_count, foreign_worker_count, address, contact_phone, created_at)
VALUES ('코리아호텔', '890-12-34567', 'JEJU', '제주시', 'ACCOMMODATION', NULL, 60, 18, '제주 제주시 관광로 80', '064-890-1234', NOW());

-- Quota seed data (HWP 수동추출 — 플레이스홀더 수치)
INSERT INTO quota ("year", industry, quota_count, source) VALUES (2025, '제조업', 36000, '도입계획');
INSERT INTO quota ("year", industry, quota_count, source) VALUES (2025, '농축산업', 8000, '도입계획');
INSERT INTO quota ("year", industry, quota_count, source) VALUES (2025, '어업', 5000, '도입계획');
INSERT INTO quota ("year", industry, quota_count, source) VALUES (2025, '건설업', 2000, '도입계획');
INSERT INTO quota ("year", industry, quota_count, source) VALUES (2025, '서비스업', 1500, '도입계획');
INSERT INTO quota ("year", industry, quota_count, source) VALUES (2024, '제조업', 34000, 'E-9현황');
INSERT INTO quota ("year", industry, quota_count, source) VALUES (2024, '농축산업', 7500, 'E-9현황');
INSERT INTO quota ("year", industry, quota_count, source) VALUES (2024, '어업', 4800, 'E-9현황');
INSERT INTO quota ("year", industry, quota_count, source) VALUES (2024, '건설업', 1800, 'E-9현황');
INSERT INTO quota ("year", industry, quota_count, source) VALUES (2024, '서비스업', 1200, 'E-9현황');
