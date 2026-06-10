package com.Miaumigo.Miaumigo.dto;

import com.Miaumigo.Miaumigo.domain.enums.Especie;
import com.Miaumigo.Miaumigo.domain.enums.ExperienciaAnimais;
import com.Miaumigo.Miaumigo.domain.enums.Porte;
import com.Miaumigo.Miaumigo.domain.enums.Tag;
import com.Miaumigo.Miaumigo.domain.enums.TempoDisponivel;
import com.Miaumigo.Miaumigo.domain.enums.TipoMoradia;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record AdotanteMeResponse(
		UUID id,
		String nome,
		String endereco,
		String email,
		String cpf,
		List<Tag> preferencias,

		@JsonProperty("especies_preferidas")
		List<Especie> especiesPreferidas,

		@JsonProperty("tipo_moradia")
		TipoMoradia tipoMoradia,

		@JsonProperty("espaco_disponivel")
		Porte espacoDisponivel,

		@JsonProperty("tempo_disponivel")
		TempoDisponivel tempoDisponivel,

		@JsonProperty("experiencia_animais")
		ExperienciaAnimais experienciaAnimais,

		@JsonProperty("possui_criancas")
		Boolean possuiCriancas,

		@JsonProperty("possui_caes")
		Boolean possuiCaes,

		@JsonProperty("possui_gatos")
		Boolean possuiGatos,

		String telefone,
		String cidade,

		@JsonProperty("perfil_completo")
		boolean perfilCompleto
) {
}
