CREATE TABLE IF NOT EXISTS word_entity(
    origin TEXT PRIMARY KEY,
    data JSONB
);

CREATE TABLE IF NOT EXISTS word_progress(
    origin TEXT PRIMARY KEY,
    approved INT NOT NULL,
    failed INT NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS training(
    id TEXT PRIMARY KEY,
    type TEXT NOT NULL,
    tags TEXT[],
    size INT NOT NULL,
    status TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP
);

