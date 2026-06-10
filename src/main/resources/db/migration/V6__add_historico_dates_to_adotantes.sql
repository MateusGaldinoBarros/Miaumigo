ALTER TABLE adotantes
	ADD COLUMN IF NOT EXISTS criado_em TIMESTAMP,
	ADD COLUMN IF NOT EXISTS atualizado_em TIMESTAMP;

UPDATE adotantes
SET criado_em = COALESCE(criado_em, CURRENT_TIMESTAMP),
	atualizado_em = COALESCE(atualizado_em, CURRENT_TIMESTAMP);

ALTER TABLE adotantes
	ALTER COLUMN criado_em SET NOT NULL,
	ALTER COLUMN atualizado_em SET NOT NULL;
