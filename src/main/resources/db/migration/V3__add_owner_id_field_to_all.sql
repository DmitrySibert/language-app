ALTER TABLE training ADD COLUMN owner_id varchar(50);
UPDATE training SET owner_id = 'legacy';
ALTER TABLE training ALTER COLUMN owner_id SET NOT NULL;

ALTER TABLE word_entity ADD COLUMN owner_id varchar(50);
UPDATE word_entity SET owner_id = 'legacy';
ALTER TABLE word_entity ALTER COLUMN owner_id SET NOT NULL;
ALTER TABLE word_entity DROP CONSTRAINT word_entity_pkey;
ALTER TABLE word_entity ADD PRIMARY KEY (owner_id, origin);

ALTER TABLE word_progress ADD COLUMN owner_id varchar(50);
UPDATE word_progress SET owner_id = 'legacy';
ALTER TABLE word_progress ALTER COLUMN owner_id SET NOT NULL;
ALTER TABLE word_progress DROP CONSTRAINT word_progress_pkey;
ALTER TABLE word_progress ADD PRIMARY KEY (owner_id, origin);
