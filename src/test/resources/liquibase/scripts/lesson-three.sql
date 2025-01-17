-- liquibase formatted sql

-- changeset artakopian:1
CREATE INDEX student_name_index ON student (name);

-- changeset artakopian:2
CREATE INDEX faculties_nc_index ON faculty (name, colour);