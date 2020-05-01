CREATE TABLE accounts (
	id uuid NOT NULL,
	created timestamp NULL,
	created_by uuid NULL,
	last_modified timestamp NULL,
	last_modified_by uuid NULL,
	departement_id uuid NULL,
	firstname varchar(255) NULL,
	lastname varchar(255) NULL,
	value varchar(255) NULL,
	tracked_person_id uuid NULL,
	username varchar(255) NULL,
	CONSTRAINT accounts_pkey PRIMARY KEY (id)
);

CREATE TABLE activation_codes (
	id uuid NOT NULL,
	created timestamp NULL,
	created_by uuid NULL,
	last_modified timestamp NULL,
	last_modified_by uuid NULL,
	activation_tries integer NOT NULL,
	departement_id uuid NULL,
	expiration_time timestamp NULL,
	status integer NULL,
	tracked_person_id uuid NULL,
	CONSTRAINT activation_codes_pkey PRIMARY KEY (id)
);

CREATE TABLE comments (
	id uuid NOT NULL,
	author varchar(255) NULL,
	comment_text varchar(255) NULL,
	comment_date timestamp NULL,
	CONSTRAINT comments_pkey PRIMARY KEY (id)
);

CREATE TABLE departments (
	id uuid NOT NULL,
	created timestamp NULL,
	created_by uuid NULL,
	last_modified timestamp NULL,
	last_modified_by uuid NULL,
	department_name varchar(255) NULL,
	CONSTRAINT departments_pkey PRIMARY KEY (id),
	CONSTRAINT uk_qyf2ekbfpnddm6f3rkgt39i9o UNIQUE (department_name)
);


CREATE TABLE diary_entries (
	id uuid NOT NULL,
	created timestamp NULL,
	created_by uuid NULL,
	last_modified timestamp NULL,
	last_modified_by uuid NULL,
	temperature numeric(5,3) null,
	note varchar(255) NULL,
	reported_at timestamp NULL,
	date_of_slot date NULL,
	time_of_day integer NULL,
	tracked_person_id uuid NULL,
	CONSTRAINT diary_entries_pkey PRIMARY KEY (id)
);


CREATE TABLE initial_reports (
	id uuid NOT NULL,
	belong_to_laboratory_staff bool NULL,
	belong_to_medical_staff bool NULL,
	belong_to_nursing_staff bool NULL,
	day_of_first_symptoms date NULL,
	direct_contact_with_liquids_ofc19pat bool NULL,
	family_member bool NULL,
	flight_crew_member_withc19pat bool NULL,
	flight_passenger_close_rowc19pat bool NULL,
	has_symptoms bool NULL,
	min15minutes_contact_withc19pat bool NULL,
	nursing_action_onc19pat bool NULL,
	other_contact_type varchar(255) NULL,
	CONSTRAINT initial_reports_pkey PRIMARY KEY (id)
);

CREATE TABLE new_contact_people (
	id uuid NOT NULL,
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
	has_pre_existing_conditions bool NULL,
	identification_hint varchar(255) NULL,
	is_health_staff bool NULL,
	is_senior bool NULL,
	last_name varchar(255) NULL,
	mobile_phone_number varchar(255) NULL,
	tracked_person_id uuid NULL,
	phone_number varchar(255) NULL,
	remark varchar(255) NULL,
	type_of_contract integer NULL,
	CONSTRAINT new_contact_people_pkey PRIMARY KEY (id)
);


CREATE TABLE roles (
	id serial NOT NULL,
	role_name varchar(255) NULL,
	CONSTRAINT roles_pkey PRIMARY KEY (id)
);


CREATE TABLE symptoms (
	id uuid NOT NULL,
	is_characteristic bool NOT NULL,
	symptom_name varchar(255) NULL,
	CONSTRAINT symptoms_pkey PRIMARY KEY (id)
);


CREATE TABLE tracked_people (
	id uuid NOT NULL,
	created timestamp NULL,
	created_by uuid NULL,
	last_modified timestamp NULL,
	last_modified_by uuid NULL,
	account_registered_at timestamp NULL,
	city varchar(255) NULL,
	house_number varchar(255) NULL,
	street varchar(255) NULL,
	zipcode varchar(255) NULL,
	date_of_birth date NULL,
	email varchar(255) NULL,
	first_name varchar(255) NULL,
	last_name varchar(255) NULL,
	mobile_phone_number varchar(255) NULL,
	phone_number varchar(255) NULL,
	CONSTRAINT tracked_people_pkey PRIMARY KEY (id)
);

CREATE TABLE accounts_roles (
	account_id uuid NOT NULL,
	roles_id integer NOT NULL,
	CONSTRAINT accounts_roles_account_fk FOREIGN KEY (roles_id) REFERENCES roles(id),
	CONSTRAINT accounts_roles_role_fk FOREIGN KEY (account_id) REFERENCES accounts(id)
);

CREATE TABLE action_items (
	dtype varchar(31) NOT NULL,
	ifd uuid NOT NULL,
	created timestamp NULL,
	created_by uuid NULL,
	last_modified timestamp NULL,
	last_modified_by uuid NULL,
	arguments varchar(255) NULL,
	code integer NULL,
	tracked_person_id uuid NULL,
	resolved bool NOT NULL,
	item_type integer NULL,
	date_of_slot date NULL,
	time_of_day integer NULL,
	id uuid NULL,
	entry_id uuid NULL,
	CONSTRAINT action_items_pkey PRIMARY KEY (ifd),
	CONSTRAINT action_items_diary_entries_fk FOREIGN KEY (entry_id) REFERENCES diary_entries(id)
);


CREATE TABLE diary_entries_contacts (
	diary_entry_id uuid NOT NULL,
	contacts_id uuid NOT NULL,
	CONSTRAINT diary_entries_contacts_contact_person_fk FOREIGN KEY (contacts_id) REFERENCES new_contact_people(id),
	CONSTRAINT diary_entries_contacts_diary_entry_fk FOREIGN KEY (diary_entry_id) REFERENCES diary_entries(id)
);

CREATE TABLE diary_entries_symptoms (
	diary_entry_id uuid NOT NULL,
	symptoms_id uuid NOT NULL,
	CONSTRAINT diary_entries_symptoms_diary_entries_fk FOREIGN KEY (diary_entry_id) REFERENCES diary_entries(id),
	CONSTRAINT diary_entries_symptoms_symptons_fk FOREIGN KEY (symptoms_id) REFERENCES symptoms(id)
);

CREATE TABLE encounters (
	id uuid NOT NULL,
	encounter_date date NULL,
	contact_id uuid NULL,
	CONSTRAINT encounters_pkey PRIMARY KEY (id),
	CONSTRAINT encounters_contact_person_fk FOREIGN KEY (contact_id) REFERENCES new_contact_people(id)
);

CREATE TABLE tracked_cases (
	id uuid NOT NULL,
	created timestamp NULL,
	created_by uuid NULL,
	last_modified timestamp NULL,
	last_modified_by uuid NULL,
	concluded bool NOT NULL,
	completed_contact_retro bool NOT NULL,
	completed_personal_data bool NOT NULL,
	completed_questionnaire bool NOT NULL,
	infected bool NOT NULL,
	quarantine_from date NULL,
	quarantine_to date NULL,
	test_date date NULL,
	case_type integer NULL,
	department_id uuid NULL,
	initial_report_id uuid NULL,
	tracked_person_id uuid NULL,
	CONSTRAINT tracked_cases_pkey PRIMARY KEY (id),
	CONSTRAINT tracked_cases_department_fk FOREIGN KEY (department_id) REFERENCES departments(id),
	CONSTRAINT tracked_cases_initial_report_fk FOREIGN KEY (initial_report_id) REFERENCES initial_reports(id),
	CONSTRAINT tracked_cases_tracked_person_fk FOREIGN KEY (tracked_person_id) REFERENCES tracked_people(id)
);

CREATE TABLE tracked_cases_comments (
	tracked_case_id uuid NOT NULL,
	comments_id uuid NOT NULL,
	CONSTRAINT uk_flu8g0pcj8opppsxwlxpp8a8h UNIQUE (comments_id),
	CONSTRAINT tracked_cases_comments_tracked_case_id FOREIGN KEY (tracked_case_id) REFERENCES tracked_cases(id),
	CONSTRAINT tracked_cases_comments_comment_id FOREIGN KEY (comments_id) REFERENCES comments(id)
);

CREATE TABLE tracked_cases_origin_contacts (
	tracked_case_id uuid NOT NULL,
	origin_contacts_id uuid NOT NULL,
	CONSTRAINT uk_5w3qfxyw7sumuhsejtngstp6u UNIQUE (origin_contacts_id),
	CONSTRAINT tracked_cases_origin_contacts_contact_person_fk FOREIGN KEY (origin_contacts_id) REFERENCES new_contact_people(id),
	CONSTRAINT tracked_cases_origin_contacts_tracked_case_fk FOREIGN KEY (tracked_case_id) REFERENCES tracked_cases(id)
);

CREATE TABLE tracked_people_encounters (
	tracked_person_id uuid NOT NULL,
	encounters_id uuid NOT NULL,
	CONSTRAINT uk_hhc907fk7ogq8emu5jjtr43rd UNIQUE (encounters_id),
	CONSTRAINT tracked_people_encounters_encounter_fk FOREIGN KEY (encounters_id) REFERENCES encounters(id),
	CONSTRAINT tracked_people_encounters_tracked_person_fk FOREIGN KEY (tracked_person_id) REFERENCES tracked_people(id)
);