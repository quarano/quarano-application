CREATE TABLE events (
    event_id uuid NOT NULL,
    created timestamp NULL,
    created_by uuid NULL,
    last_modified timestamp NULL,
    last_modified_by uuid NULL,
    start timestamp NULL,
    end timestamp NULL,
    title varchar(255) NULL,
    event_code varchar(8) NULL,
    CONSTRAINT event_pkey PRIMARY KEY (event_id)
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
    event_code varchar(8) NULL,
    CONSTRAINT visitor_group_pkey PRIMARY KEY (visitor_group_id),
    CONSTRAINT visitor_group_event_fk FOREIGN KEY (event_code) REFERENCES events(event_code)
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
    visitor_group_id uuid NULL,
    CONSTRAINT visitor_pkey PRIMARY KEY (visitor_id),
    CONSTRAINT visitor_visitor_group_fk FOREIGN KEY (visitor_group_id) REFERENCES visitor_groups(visitor_group_id)
);


