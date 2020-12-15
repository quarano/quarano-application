ALTER TABLE departments ADD federal_state varchar(100) NOT NULL;
ALTER TABLE departments ADD district varchar(100) NOT NULL;

ALTER TABLE tracked_cases ADD sormas_case_id varchar(50) NULL;
