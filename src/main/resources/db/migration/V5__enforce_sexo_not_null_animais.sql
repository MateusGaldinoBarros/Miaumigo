DO $$
BEGIN
	IF EXISTS (
		SELECT 1
		FROM information_schema.tables
		WHERE table_schema = 'public'
			AND table_name = 'animais'
	) THEN
		ALTER TABLE animais ADD COLUMN IF NOT EXISTS sexo VARCHAR(20);
		UPDATE animais SET sexo = 'DESCONHECIDO' WHERE sexo IS NULL;
		ALTER TABLE animais ALTER COLUMN sexo SET NOT NULL;
	END IF;
END $$;
