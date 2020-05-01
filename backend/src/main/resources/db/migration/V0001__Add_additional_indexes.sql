ALTER TABLE accounts ADD CONSTRAINT accounts_department_fk FOREIGN KEY (departement_id) REFERENCES departments (id);
ALTER TABLE accounts ADD CONSTRAINT accounts_tracked_person_fk FOREIGN KEY (tracked_person_id) REFERENCES tracked_people (id);

ALTER TABLE activation_codes ADD CONSTRAINT activation_codes_department_fk FOREIGN KEY (departement_id) REFERENCES departments (id);
ALTER TABLE activation_codes ADD CONSTRAINT activation_codes_tracked_person_fk FOREIGN KEY (tracked_person_id) REFERENCES tracked_people (id);

ALTER TABLE diary_entries ADD CONSTRAINT diary_entries_tracked_person_fk FOREIGN KEY (tracked_person_id) REFERENCES tracked_people (id);

ALTER TABLE new_contact_people ADD CONSTRAINT new_contact_people_tracked_person_fk FOREIGN KEY (tracked_person_id) REFERENCES tracked_people (id);

ALTER TABLE action_items ADD CONSTRAINT action_items_tracked_person_fk FOREIGN KEY (tracked_person_id) REFERENCES tracked_people (id);
