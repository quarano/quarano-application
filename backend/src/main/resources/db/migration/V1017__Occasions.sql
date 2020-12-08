CREATE TABLE occasions (
	occasion_id uuid NOT NULL,
	created timestamp NULL,
	created_by uuid NULL,
	last_modified timestamp NULL,
	last_modified_by uuid NULL,
	start timestamp NULL,
	end timestamp NULL,
	title varchar(255) NULL,
	occasion_code varchar(8) NULL,
    tracked_case_id uuid NOT NULL,
	CONSTRAINT occasions_pkey PRIMARY KEY (occasion_id),
    CONSTRAINT occasions_tracked_case_fk FOREIGN KEY (tracked_case_id) REFERENCES tracked_cases(tracked_case_id)
);

CREATE TABLE visitor_groups (
	visitor_group_id uuid NOT NULL,
	created timestamp NULL,
	created_by uuid NULL,
	last_modified timestamp NULL,
	last_modified_by uuid NULL,
	start timestamp NULL,
	end timestamp NULL,
	comment varchar(255) NULL,
	location_name varchar(255) NULL,
	occasion_code varchar(8) NULL,
	CONSTRAINT visitor_groups_pkey PRIMARY KEY (visitor_group_id),
	CONSTRAINT visitor_groups_occasion_fk FOREIGN KEY (occasion_code) REFERENCES occasions(occasion_code)
);

CREATE TABLE visitors (
	visitor_id uuid NOT NULL,
	created timestamp NULL,
	created_by uuid NULL,
	last_modified timestamp NULL,
	last_modified_by uuid NULL,
	city varchar(255) NULL,
	house_number varchar(255) NULL,
	street varchar(255) NULL,
	zipcode varchar(255) NULL,
	email varchar(255) NULL,
	first_name varchar(255) NULL,
	last_name varchar(255) NULL,
	phone_number varchar(255) NULL,
	checkin timestamp NULL,
	checkout timestamp NULL,
	negative_test_date date NULL,
	positive_test_date date NULL,
	verified bool NULL,
	visitor_group_id uuid NULL,
	CONSTRAINT visitors_pkey PRIMARY KEY (visitor_id),
	CONSTRAINT visitors_visitor_group_fk FOREIGN KEY (visitor_group_id) REFERENCES visitor_groups(visitor_group_id)
);
