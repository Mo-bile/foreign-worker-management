-- Workplace seed data
-- 앱 시작 시 JPA 스키마 생성 후 자동 실행 (defer-datasource-initialization=true 필요)
-- id는 IDENTITY 자동 생성 (1~8 순차 할당)

INSERT INTO workplaces (name, business_number, address, contact_phone, created_at)
VALUES ('한국제조(주)', '123-45-67890', '경기도 안산시 단원구 공단1로 10', '031-123-4567', NOW());

INSERT INTO workplaces (name, business_number, address, contact_phone, created_at)
VALUES ('그린농장', '234-56-78901', '충남 논산시 연산면 농장길 25', '041-234-5678', NOW());

INSERT INTO workplaces (name, business_number, address, contact_phone, created_at)
VALUES ('대한건설(주)', '345-67-89012', '서울 강남구 테헤란로 100', '02-345-6789', NOW());

INSERT INTO workplaces (name, business_number, address, contact_phone, created_at)
VALUES ('서울식품(주)', '456-78-90123', '경기도 화성시 동탄대로 50', '031-456-7890', NOW());

INSERT INTO workplaces (name, business_number, address, contact_phone, created_at)
VALUES ('글로벌IT(주)', '567-89-01234', '서울 구로구 디지털로 300', '02-567-8901', NOW());

INSERT INTO workplaces (name, business_number, address, contact_phone, created_at)
VALUES ('해양수산(주)', '678-90-12345', '부산 사하구 낙동남로 200', '051-678-9012', NOW());

INSERT INTO workplaces (name, business_number, address, contact_phone, created_at)
VALUES ('스마트물류(주)', '789-01-23456', '인천 서구 청라대로 150', '032-789-0123', NOW());

INSERT INTO workplaces (name, business_number, address, contact_phone, created_at)
VALUES ('코리아호텔', '890-12-34567', '제주 제주시 관광로 80', '064-890-1234', NOW());
