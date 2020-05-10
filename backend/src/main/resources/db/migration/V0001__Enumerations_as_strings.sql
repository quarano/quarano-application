-- change status
ALTER TABLE tracked_cases add status_new varchar(50);
UPDATE tracked_cases set status_new = 'OPEN' where status = 1;
UPDATE tracked_cases set status_new = 'IN_REGISTRATION' where status = 2;
UPDATE tracked_cases set status_new = 'REGISTERED' where status = 3;
UPDATE tracked_cases set status_new = 'TRACKING' where status = 4;
UPDATE tracked_cases set status_new = 'CONCLUDED' where status = 5;
ALTER TABLE tracked_cases DROP COLUMN status;
ALTER TABLE tracked_cases RENAME COLUMN status_new TO status;

-- change type
ALTER TABLE tracked_cases add case_type_new varchar(50);
UPDATE tracked_cases set case_type_new = 'INDEX' where case_type = 1;
UPDATE tracked_cases set case_type_new = 'CONTACT' where case_type = 2;
UPDATE tracked_cases set case_type_new = 'CONTACT_MEDICAL' where case_type = 3;
ALTER TABLE tracked_cases DROP COLUMN case_type;
ALTER TABLE tracked_cases RENAME COLUMN case_type_new TO case_type;

-- item type
ALTER TABLE action_items add item_type_new varchar(50);
UPDATE action_items set item_type_new = 'MEDICAL_INCIDENT' where item_type = 1;
UPDATE action_items set item_type_new = 'PROCESS_INCIDENT' where item_type = 2;
ALTER TABLE action_items DROP COLUMN item_type;
ALTER TABLE action_items RENAME COLUMN item_type_new TO item_type;

-- code
ALTER TABLE action_items add code_new varchar(50);
UPDATE action_items set code_new = 'INCREASED_TEMPERATURE' where code = 1;
UPDATE action_items set code_new = 'FIRST_CHARACTERISTIC_SYMPTOM' where code = 2;
UPDATE action_items set code_new = 'DIARY_ENTRY_MISSING' where code = 3;
UPDATE action_items set code_new = 'MISSING_DETAILS_INDEX' where code = 4;
UPDATE action_items set code_new = 'MISSING_DETAILS_CONTACT' where code = 5;
UPDATE action_items set code_new = 'INITIAL_CALL_OPEN_INDEX' where code = 6;
UPDATE action_items set code_new = 'INITIAL_CALL_OPEN_CONTACT' where code = 7;
ALTER TABLE action_items DROP COLUMN code;
ALTER TABLE action_items RENAME COLUMN code_new TO code;

-- activation code status
ALTER TABLE activation_codes add status_new varchar(50);
UPDATE activation_codes set status_new = 'WAITING_FOR_ACTIVATION' where status = 1;
UPDATE activation_codes set status_new = 'REDEEMED' where status = 2;
UPDATE activation_codes set status_new = 'CANCELED' where status = 3;
ALTER TABLE activation_codes DROP COLUMN status;
ALTER TABLE activation_codes RENAME COLUMN status_new TO status;

-- contact_people type_of_contract
ALTER TABLE contact_people add type_of_contract_new varchar(50);
UPDATE contact_people set type_of_contract_new = 'O' where type_of_contract = 1;
UPDATE contact_people set type_of_contract_new = 'S' where type_of_contract = 2;
UPDATE contact_people set type_of_contract_new = 'P' where type_of_contract = 3;
UPDATE contact_people set type_of_contract_new = 'AE' where type_of_contract = 4;
UPDATE contact_people set type_of_contract_new = 'Aer' where type_of_contract = 5;
UPDATE contact_people set type_of_contract_new = 'Mat' where type_of_contract = 6;
UPDATE contact_people set type_of_contract_new = 'And' where type_of_contract = 7;
ALTER TABLE contact_people DROP COLUMN type_of_contract;
ALTER TABLE contact_people RENAME COLUMN type_of_contract_new TO type_of_contract;

-- diary_entries slot
ALTER TABLE diary_entries add time_of_day_new varchar(50);
UPDATE diary_entries set time_of_day_new = 'MORNING' where time_of_day = 1;
UPDATE diary_entries set time_of_day_new = 'EVENING' where time_of_day = 2;
ALTER TABLE diary_entries DROP COLUMN time_of_day;
ALTER TABLE diary_entries RENAME COLUMN time_of_day_new TO time_of_day;

-- diary_entries slot
ALTER TABLE action_items add time_of_day_new varchar(50);
UPDATE action_items set time_of_day_new = 'MORNING' where time_of_day = 1;
UPDATE action_items set time_of_day_new = 'EVENING' where time_of_day = 2;
ALTER TABLE action_items DROP COLUMN time_of_day;
ALTER TABLE action_items RENAME COLUMN time_of_day_new TO time_of_day;

