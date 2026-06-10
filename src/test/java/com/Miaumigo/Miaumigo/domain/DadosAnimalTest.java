package com.Miaumigo.Miaumigo.domain;

import com.Miaumigo.Miaumigo.domain.enums.Especie;
import com.Miaumigo.Miaumigo.domain.enums.Porte;
import com.Miaumigo.Miaumigo.domain.enums.SexoAnimal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DadosAnimalTest {

	@Test
	void deveCriarDadosAnimal_quandoDadosValidos() {
		DadosAnimal dados = new DadosAnimal(
				"Luna",
				Especie.GATO,
				Porte.PEQUENO,
				SexoAnimal.FEMEA,
				2,
				"Dócil"
		);

		assertEquals("Luna", dados.getNome());
		assertEquals(Especie.GATO, dados.getEspecie());
		assertEquals(Porte.PEQUENO, dados.getPorte());
		assertEquals(SexoAnimal.FEMEA, dados.getSexo());
		assertEquals(2, dados.getIdade());
		assertEquals("Dócil", dados.getDescricao());
	}

	@Test
	void deveLancarExcecao_quandoNomeVazio() {
		assertThrows(IllegalArgumentException.class, () ->
				new DadosAnimal(" ", Especie.GATO, Porte.PEQUENO, SexoAnimal.FEMEA, 2, "Dócil"));
	}

	@Test
	void deveLancarExcecao_quandoEspecieNula() {
		assertThrows(NullPointerException.class, () ->
				new DadosAnimal("Luna", null, Porte.PEQUENO, SexoAnimal.FEMEA, 2, "Dócil"));
	}

	@Test
	void deveLancarExcecao_quandoPorteNulo() {
		assertThrows(NullPointerException.class, () ->
				new DadosAnimal("Luna", Especie.GATO, null, SexoAnimal.FEMEA, 2, "Dócil"));
	}

	@Test
	void deveLancarExcecao_quandoSexoNulo() {
		assertThrows(NullPointerException.class, () ->
				new DadosAnimal("Luna", Especie.GATO, Porte.PEQUENO, null, 2, "Dócil"));
	}

	@Test
	void deveLancarExcecao_quandoIdadeNegativa() {
		assertThrows(IllegalArgumentException.class, () ->
				new DadosAnimal("Luna", Especie.GATO, Porte.PEQUENO, SexoAnimal.FEMEA, -1, "Dócil"));
	}
}
