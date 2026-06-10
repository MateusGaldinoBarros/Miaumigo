package com.Miaumigo.Miaumigo.dto;

import com.Miaumigo.Miaumigo.domain.enums.Especie;
import com.Miaumigo.Miaumigo.domain.enums.Porte;
import com.Miaumigo.Miaumigo.domain.enums.SexoAnimal;
import com.Miaumigo.Miaumigo.domain.enums.Tag;
import com.Miaumigo.Miaumigo.domain.enums.AnimalStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record AnimalResponse(
		UUID id,
			String nome,
			Integer idade,
			Porte porte,
			Especie especie,
			SexoAnimal sexo,
			String descricao,
			AnimalStatus status,
			List<Tag> tags,

		@JsonProperty("cloudinary_public_id")
		String cloudinaryPublicId
) {
}
