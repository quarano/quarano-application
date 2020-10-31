CREATE INDEX action_items_tracked_person_id ON action_items (tracked_person_id);
CREATE INDEX action_items_tracked_case_id ON action_items (tracked_case_id);
CREATE INDEX action_items_diary_entry_id ON action_items (entry_diary_entry_id);

CREATE INDEX tracked_cases_tracked_person_id ON tracked_cases (tracked_person_id);
CREATE INDEX tracked_cases_department_id ON tracked_cases (department_id);
CREATE INDEX tracked_cases_questionnaire_id ON tracked_cases (questionnaire_id);

CREATE INDEX tracked_cases_origin_cases_child ON tracked_cases_origin_cases (tracked_case_tracked_case_id);
CREATE INDEX tracked_cases_origin_cases_parents ON tracked_cases_origin_cases (origin_cases_tracked_case_id);

CREATE INDEX tracked_people_account_id ON tracked_people (account_id);

CREATE INDEX diary_entries_tracked_person_id ON diary_entries (tracked_person_id);

CREATE INDEX contact_people_tracked_person_id ON contact_people (tracked_person_id);

CREATE INDEX comments_tracked_case_id ON comments (tracked_case_id);

CREATE INDEX encounters_tracked_person_id ON encounters (tracked_person_id);
CREATE INDEX encounters_contact_person_id ON encounters (contact_person_id);

CREATE INDEX activation_codes_tracked_person_id ON activation_codes (tracked_person_id);
CREATE INDEX activation_codes_department_id ON activation_codes (department_id);

CREATE INDEX accounts_department_id ON accounts (department_id);

CREATE INDEX accounts_roles_role_id ON accounts_roles (roles_role_id);
CREATE INDEX accounts_roles_account_id ON accounts_roles (account_account_id);

CREATE INDEX departments_contacts_department_id ON departments_contacts (department_id);

CREATE INDEX diary_entries_contacts_contact_person_id ON diary_entries_contacts (contacts_contact_person_id);
CREATE INDEX diary_entries_contacts_diary_entry_id ON diary_entries_contacts (diary_entry_diary_entry_id);

CREATE INDEX diary_entries_symptoms_diary_entry_id ON diary_entries_symptoms (diary_entry_diary_entry_id);
CREATE INDEX diary_entries_symptoms_symptom_id ON diary_entries_symptoms (symptoms_symptom_id);

CREATE INDEX questionnaires_symptoms_diary_entry_id ON questionnaires_symptoms (questionnaire_questionnaire_id);
CREATE INDEX questionnaires_symptoms_symptom_id ON questionnaires_symptoms (symptoms_symptom_id);

CREATE INDEX tracked_cases_origin_contacts_contact_person_id ON tracked_cases_origin_contacts (origin_contacts_contact_person_id);
CREATE INDEX tracked_cases_origin_contacts_tracked_case_id ON tracked_cases_origin_contacts (tracked_case_tracked_case_id);
