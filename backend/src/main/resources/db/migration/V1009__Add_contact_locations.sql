CREATE TABLE locations (
	/* aggregate metadata */
	created timestamp NULL,
	created_by uuid NULL,
	last_modified timestamp NULL,
	last_modified_by uuid NULL,

	location_id uuid NOT NULL,
	street varchar(255) NULL,
	house_number varchar(255) NULL,
	city varchar(255) NULL,
	zipcode varchar(255) NULL,
	/* ~1m precision */
	latitude numeric(8,5) NULL,
	longitude numeric(8,5) NULL,
	CONSTRAINT locations_pkey PRIMARY KEY (location_id)
);

CREATE TABLE contact_locations (
	contact_location_id uuid NOT NULL,
	location_id uuid NOT NULL,
	tracked_person_id uuid NOT NULL,
	name varchar(255) NULL,
	contact_person varchar(255) NULL,
	start_time timestamp NULL,
	end_time timestamp NULL,
	notes varchar(255) NULL,
	CONSTRAINT contact_locations_pkey PRIMARY KEY (contact_location_id),
	CONSTRAINT contact_locations_locations_fk FOREIGN KEY (location_id) REFERENCES locations(location_id),
	CONSTRAINT contact_locations_tracked_person_fk FOREIGN KEY (tracked_person_id) REFERENCES tracked_people(tracked_person_id)
);
