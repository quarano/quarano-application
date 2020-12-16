ALTER TABLE tracked_people ADD sormas_id varchar(50) NULL;

CREATE INDEX tracked_cases_sormas_case_id ON tracked_cases (sormas_case_id);
CREATE INDEX tracked_people_sormas_id ON tracked_people (sormas_id);

CREATE TABLE changes_for_sync (
    id uuid NOT NULL,
    created timestamp NOT NULL,
    source_table varchar(50) NOT NULL,
    ref_id uuid NOT NULL,
    CONSTRAINT changes_for_sync_pkey PRIMARY KEY (id)
);

CREATE TABLE sync_times (
    data_type varchar(50) NOT NULL,    
    last_sync long NOT NULL DEFAULT 0,
    CONSTRAINT sync_times_pkey PRIMARY KEY (data_type)
);

CREATE TRIGGER tracked_people_changes_for_sync_trigger
AFTER INSERT, UPDATE
ON tracked_people
FOR EACH ROW
CALL "quarano.sormas_integration.ChangesForSyncTrigger";

CREATE TRIGGER tracked_cases_changes_for_sync_trigger
AFTER INSERT, UPDATE
ON tracked_cases
FOR EACH ROW
CALL "quarano.sormas_integration.ChangesForSyncTrigger";
