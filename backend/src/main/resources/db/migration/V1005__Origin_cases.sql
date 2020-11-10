CREATE TABLE tracked_cases_origin_cases (
	tracked_case_tracked_case_id uuid NULL,
	origin_cases_tracked_case_id uuid NULL,
	CONSTRAINT tracked_cases_fk FOREIGN KEY (tracked_case_tracked_case_id) REFERENCES tracked_cases(tracked_case_id),
	CONSTRAINT origin_cases_fk FOREIGN KEY (origin_cases_tracked_case_id) REFERENCES tracked_cases(tracked_case_id)
);
