CREATE TABLE shedlock(
    name varchar(64) NOT NULL,
    lock_until timestamp NOT NULL,
    locked_at timestamp NOT NULL,
    locked_by varchar(255) NOT NULL,
    CONSTRAINT shedlock_pkey PRIMARY KEY (name)
);