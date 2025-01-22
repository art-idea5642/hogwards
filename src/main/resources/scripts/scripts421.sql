ALTER TABLE student
ADD CONSTRAINT age_constraint CHECK (age >= 16),
ADD CONSTRAINT name_unique UNIQUE (name);

ALTER TABLE student
ALTER COLUMN name SET NOT NULL;

ALTER TABLE faculty ADD CONSTRAINT name_colour_unique UNIQUE (name, colour);

ALTER TABLE student ALTER COLUMN age SET DEFAULT 20;

