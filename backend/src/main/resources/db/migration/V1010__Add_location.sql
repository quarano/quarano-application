CREATE TABLE locations (
	location_id uuid NOT NULL,
	created timestamp NULL,
	created_by uuid NULL,
	last_modified timestamp NULL,
	last_modified_by uuid NULL,
	city varchar(255) NULL,
	house_number varchar(255) NULL,
	street varchar(255) NULL,
	zipcode varchar(255) NULL,
    contact_info varchar(512) NULL,
    organization varchar(512) NULL,
    latitude double precision,
    longitude double precision,
    plus_code varchar(10) NULL,
	CONSTRAINT locations_pkey PRIMARY KEY (location_id)
);

CREATE TABLE location_visits (
	visit_id uuid NOT NULL,
	created timestamp NULL,
	created_by uuid NULL,
	last_modified timestamp NULL,
	last_modified_by uuid NULL,
	location_id uuid NOT NULL,
    tracked_person_id uuid NULL,
    from date NULL,
    to date NULL,
	CONSTRAINT location_visits_pkey PRIMARY KEY (visit_id),
	CONSTRAINT location_visits_fk FOREIGN KEY (location_id) REFERENCES locations(location_id),
	CONSTRAINT person_visits_fk FOREIGN KEY (tracked_person_id) REFERENCES tracked_people(tracked_person_id)
);