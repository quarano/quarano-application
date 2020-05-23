CREATE TABLE departments_contacts (
     department_contact_id uuid NOT NULL,
     created timestamp NULL,
     created_by uuid NULL,
     last_modified timestamp NULL,
     last_modified_by uuid NULL,
     type varchar(255) NOT NULL,
     email varchar(255) NOT NULL,
     phone_number varchar(255) NOT NULL,
     department_id uuid NULL,
     CONSTRAINT department_contacts_pkey PRIMARY KEY (department_contact_id),
     CONSTRAINT uk_dep_cont UNIQUE (department_id, type),
     CONSTRAINT contact_department_fk FOREIGN KEY (department_id) REFERENCES departments(department_id)
);

-- we use the department_id also as this id, because there is no single uuid-generate method on h2 and postgres
INSERT INTO departments_contacts (department_contact_id, type, email, phone_number, department_id)
    SELECT department_id, 'INDEX', email, phone_number, department_id FROM departments;

ALTER TABLE departments DROP COLUMN email;
ALTER TABLE departments DROP COLUMN phone_number;
