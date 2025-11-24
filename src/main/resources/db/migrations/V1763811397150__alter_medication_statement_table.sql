ALTER TABLE medication.medication_statements
    ALTER COLUMN effective_start_date TYPE DATE USING effective_start_date::DATE;

ALTER TABLE medication.medication_statements
    ALTER COLUMN effective_end_date TYPE DATE USING effective_end_date::DATE;
