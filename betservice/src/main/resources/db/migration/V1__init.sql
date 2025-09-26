CREATE TABLE IF NOT EXISTS _bootstrap (
                                          id SERIAL PRIMARY KEY,
                                          created_at TIMESTAMPTZ DEFAULT NOW()
    );