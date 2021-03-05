ALTER TABLE tracked_cases ADD sormas_uuid varchar(50);
ALTER TABLE tracked_people ADD sormas_uuid varchar(50);
ALTER TABLE encounters ADD sormas_uuid varchar(50);
ALTER TABLE contact_people ADD sormas_uuid varchar(50);

CREATE INDEX tracked_cases_sormas_case_id ON tracked_cases (sormas_uuid);
CREATE INDEX tracked_people_sormas_id ON tracked_people (sormas_uuid);
CREATE INDEX encounters_sormas_id ON encounters (sormas_uuid);
CREATE INDEX contact_people_sormas_id ON contact_people (sormas_uuid);

CREATE TABLE index_sync_report (
    persons_number int NOT NULL,
    cases_number int NOT NULL,
    sync_date datetime NOT NULL,
    sync_time long NOT NULL,
    status int NOT NULL,
    CONSTRAINT changes_for_index_sync_pkey PRIMARY KEY (sync_date)
);

CREATE TABLE contacts_sync_report (
    persons_number int NOT NULL,
    sync_date datetime NOT NULL,
    sync_time long NOT NULL,
    status int NOT NULL,
    CONSTRAINT changes_for_contacts_sync_pkey PRIMARY KEY (sync_date)
);

CREATE TABLE contacts_synch_backlog (
    id uuid NOT NULL,
    sync_date datetime NOT NULL
);

CREATE TABLE index_synch_backlog (
    id uuid NOT NULL,
    sync_date datetime NOT NULL
);

CREATE INDEX index_sync_report_date ON index_sync_report (sync_date);
CREATE INDEX contacts_sync_report_date ON contacts_sync_report (sync_date);