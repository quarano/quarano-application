CREATE TABLE frontend_texts (
    frontend_text_id uuid NOT NULL,
	text_key varchar(255) NOT NULL,
	locale varchar(20) NOT NULL,
	text text NULL,
	created timestamp NULL,
	created_by uuid NULL,
	last_modified timestamp NULL,
	last_modified_by uuid NULL,
	CONSTRAINT frontend_texts_pkey PRIMARY KEY (frontend_text_id),
	CONSTRAINT uk_frontend_text_key_locale UNIQUE (text_key, locale)
);

CREATE TABLE email_texts (
    email_text_id uuid NOT NULL,
	text_key varchar(255) NOT NULL,
	locale varchar(20) NOT NULL,
	text text NULL,
	created timestamp NULL,
	created_by uuid NULL,
	last_modified timestamp NULL,
	last_modified_by uuid NULL,
	CONSTRAINT email_texts_pkey PRIMARY KEY (email_text_id),
	CONSTRAINT uk_email_text_key_locale UNIQUE (text_key, locale)
);
