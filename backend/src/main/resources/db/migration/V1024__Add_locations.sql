CREATE TABLE locations (
     location_id uuid NOT NULL,
     created timestamp NULL,
     created_by uuid NULL,
     last_modified timestamp NULL,
     last_modified_by uuid NULL,
     name varchar(255) NOT NULL,
     city varchar(255) NOT NULL,
     house_number varchar(255) NULL,
     street varchar(255) NOT NULL,
     zipcode varchar(255) NOT NULL,
     contact_person_name varchar(255) NULL,
     phone_number varchar(255) NULL,
     email varchar(255) NULL,
     comment varchar(255) NULL,
     tracked_person_id uuid NULL,
     CONSTRAINT locations_pkey PRIMARY KEY (location_id),
     CONSTRAINT locations_tracked_person_fk FOREIGN KEY (tracked_person_id) REFERENCES tracked_people(tracked_person_id)
);

ALTER TABLE public.encounters ADD location_id uuid NULL DEFAULT NULL;
ALTER TABLE public.encounters ADD CONSTRAINT encounters_locations_fk FOREIGN KEY (location_id) REFERENCES locations (location_id);

