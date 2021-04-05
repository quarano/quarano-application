ALTER TABLE tracked_cases ADD sormas_uuid varchar(50);
ALTER TABLE tracked_people ADD sormas_uuid varchar(50);

CREATE INDEX tracked_cases_sormas_case_id ON tracked_cases (sormas_uuid);
CREATE INDEX tracked_people_sormas_id ON tracked_people (sormas_uuid);

CREATE TABLE index_sync_report (
    uuid uuid NOT NULL,
    persons_number varchar(255) NOT NULL,
    cases_number varchar(255) NOT NULL,
    sync_date timestamp NOT NULL,
    sync_time varchar(255) NOT NULL,
    status varchar(255) NOT NULL,
    CONSTRAINT changes_for_index_sync_pkey PRIMARY KEY (uuid)
);

CREATE INDEX index_sync_report_date ON index_sync_report (sync_date);

CREATE TABLE contacts_sync_report (
    uuid uuid NOT NULL,
    persons_number varchar(255) NOT NULL,
    sync_date timestamp NOT NULL,
    sync_time varchar(255) NOT NULL,
    status varchar(255) NOT NULL,
    CONSTRAINT changes_for_contacts_sync_pkey PRIMARY KEY (uuid)
);

CREATE INDEX contacts_sync_report_date ON contacts_sync_report (sync_date);

CREATE TABLE contacts_synch_backlog (
    id uuid NOT NULL,
    sync_date timestamp NOT NULL
);

CREATE TABLE index_synch_backlog (
    id uuid NOT NULL,
    sync_date timestamp NOT NULL
);