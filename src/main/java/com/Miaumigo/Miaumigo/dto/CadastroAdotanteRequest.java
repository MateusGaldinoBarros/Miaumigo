package com.Miaumigo.Miaumigo.dto;

import com.Miaumigo.Miaumigo.domain.enums.Especie;
import com.Miaumigo.Miaumigo.domain.enums.ExperienciaAnimais;
import com.Miaumigo.Miaumigo.domain.enums.Porte;
import com.Miaumigo.Miaumigo.domain.enums.Tag;
import com.Miaumigo.Miaumigo.domain.enums.TempoDisponivel;
import com.Miaumigo.Miaumigo.domain.enums.TipoMoradia;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CadastroAdotanteRequest(
		@NotBlank(message = "Nome do adotante é obrigatório")
		String nome,

		@NotBlank(message = "Endereço do adotante é obrigatório")
		String endereco,

		@NotBlank(message = "Email do adotante é obrigatório")
		String email,

		@NotBlank(message = "Senha do adotante é obrigatória")
		String senha,

		@NotBlank(message = "CPF do adotante é obrigatório")
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

		String cidade
) {
}
