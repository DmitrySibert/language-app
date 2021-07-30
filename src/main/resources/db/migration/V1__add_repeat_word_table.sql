CREATE TABLE IF NOT EXISTS word_entity(
    origin TEXT PRIMARY KEY,
    translate TEXT,
    word_info TEXT,
    tags TEXT
);

CREATE TABLE IF NOT EXISTS repeat_word(
    origin TEXT PRIMARY KEY
);