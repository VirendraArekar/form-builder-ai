-- Migration V4: Timely Forms AI features - favorite status and indexes
ALTER TABLE forms ADD COLUMN IF NOT EXISTS is_favorite BOOLEAN NOT NULL DEFAULT FALSE;
CREATE INDEX IF NOT EXISTS idx_forms_is_favorite ON forms(is_favorite);
