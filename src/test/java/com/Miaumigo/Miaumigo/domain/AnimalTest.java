package com.Miaumigo.Miaumigo.domain;

import com.Miaumigo.Miaumigo.domain.enums.AnimalStatus;
import com.Miaumigo.Miaumigo.domain.enums.Especie;
import com.Miaumigo.Miaumigo.domain.enums.Porte;
import com.Miaumigo.Miaumigo.domain.enums.SexoAnimal;
import com.Miaumigo.Miaumigo.domain.enums.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AnimalTest {

	@Test
	void deveCriarAnimalDisponivel_quandoDadosValidos() {
		UUID larId = UUID.randomUUID();

		Animal animal = novoAnimal(larId);

		assertEquals("Luna", animal.getNome());
		assertEquals(AnimalStatus.DISPONIVEL, animal.getStatus());
		assertEquals(larId, animal.getLarId());
		assertEquals(List.of(), animal.getTags());
		assertEquals(1, animal.getLogs().size());
		assertEquals("Animal cadastrado.", animal.getLogs().getFirst());
	}

	@Test
	void deveCriarAnimalComTagsEPublicIdCloudinary_quandoDadosValidos() {
		UUID larId = UUID.randomUUID();

		Animal animal = new Animal(
				dadosAnimal(),
				larId,
				Arrays.asList(Tag.DOCIL, Tag.CARINHOSO, Tag.DOCIL, null),
				" animais/luna "
		);

		assertEquals(List.of(Tag.DOCIL, Tag.CARINHOSO), animal.getTags());
		assertEquals("animais/luna", animal.getCloudinaryPublicId());
	}

	@Test
	void deveLancarExcecao_quandoNomeVazio() {
		assertThrows(IllegalArgumentException.class, () ->
				new DadosAnimal(" ", Especie.GATO, Porte.PEQUENO, SexoAnimal.FEMEA, 2, "Dócil"));
	}

	@Test
	void deveLancarExcecao_quandoIdadeNegativa() {
		assertThrows(IllegalArgumentException.class, () ->
				new DadosAnimal("Luna", Especie.GATO, Porte.PEQUENO, SexoAnimal.FEMEA, -1, "Dócil"));
	}

	@Test
	void deveLancarExcecao_quandoLarNulo() {
		assertThrows(IllegalArgumentException.class, () ->
				new Animal(dadosAnimal(), null, List.of(), null));
	}

	@Test
	void deveTransferirParaLar_quandoAnimalDisponivel() {
		Animal animal = novoAnimal();
		UUID novoLarId = UUID.randomUUID();

		animal.transferirParaLar(novoLarId);

		assertEquals(novoLarId, animal.getLarId());
	}

	@Test
	void deveLancarExcecao_quandoTransferirAnimalEmProcesso() {
		Animal animal = novoAnimal();
		animal.iniciarProcessoAdocao();

		assertThrows(IllegalStateException.class, () -> animal.transferirParaLar(UUID.randomUUID()));
	}

	@Test
	void devePermitirTransicaoParaEmProcesso_quandoAnimalDisponivel() {
		Animal animal = novoAnimal();

		animal.iniciarProcessoAdocao();

		assertEquals(AnimalStatus.EM_PROCESSO, animal.getStatus());
	}

	@Test
	void devePermitirTransicaoParaAdotado_quandoAnimalEmProcesso() {
		Animal animal = novoAnimal();
		animal.iniciarProcessoAdocao();

		animal.marcarComoAdotado();

		assertEquals(AnimalStatus.ADOTADO, animal.getStatus());
	}

	@Test
	void devePermitirRetornarParaDisponivel_quandoAnimalEmProcesso() {
		Animal animal = novoAnimal();
		animal.iniciarProcessoAdocao();

		animal.disponibilizar();

		assertEquals(AnimalStatus.DISPONIVEL, animal.getStatus());
	}

	@Test
	void devePermitirRetornarParaDisponivel_quandoAnimalAdotado() {
		Animal animal = novoAnimal();
		animal.iniciarProcessoAdocao();
		animal.marcarComoAdotado();

		animal.disponibilizar();

		assertEquals(AnimalStatus.DISPONIVEL, animal.getStatus());
	}

	@Test
	void deveLancarExcecao_quandoMarcarComoAdotadoAnimalDisponivel() {
		Animal animal = novoAnimal();

		assertThrows(IllegalStateException.class, animal::marcarComoAdotado);
	}

	@Test
	void deveRealizarAdocao_quandoAnimalDisponivel() {
		Animal animal = novoAnimal();
		Adotante adotante = novoAdotante("Maria Silva");

		animal.realizarAdocao(adotante);

		assertEquals(AnimalStatus.ADOTADO, animal.getStatus());
		assertEquals(adotante, animal.getAdotanteAtual());
		assertTrue(animal.getAdotantes().contains(adotante));
		assertEquals("Adotado por Maria Silva.", animal.getLogs().get(1));
	}

	@Test
	void deveLancarExcecao_quandoRealizarAdocaoAnimalIndisponivel() {
		Animal animal = novoAnimal();
		animal.iniciarProcessoAdocao();

		assertThrows(IllegalStateException.class, () -> animal.realizarAdocao(novoAdotante("Maria Silva")));
	}

	@Test
	void deveLancarExcecao_quandoRealizarAdocaoSemAdotante() {
		Animal animal = novoAnimal();

		assertThrows(IllegalArgumentException.class, () -> animal.realizarAdocao(null));
	}

	@Test
	void deveDevolverAnimal_quandoAnimalAdotado() {
		Animal animal = novoAnimal();
		Adotante adotante = novoAdotante("Maria Silva");
		animal.realizarAdocao(adotante);

		Adotante adotanteAnterior = animal.registrarDevolucao("Não se adaptou");

		assertEquals(adotante, adotanteAnterior);
		assertEquals(AnimalStatus.DISPONIVEL, animal.getStatus());
		assertEquals(null, animal.getAdotanteAtual());
		assertTrue(animal.getAdotantes().contains(adotante));
		assertEquals("Animal devolvido por Maria Silva. Motivo: Não se adaptou.", animal.getLogs().get(2));
	}

	@Test
	void devePermitirNovaAdocao_quandoAnimalFoiDevolvido() {
		Animal animal = novoAnimal();
		Adotante primeiroAdotante = novoAdotante("Maria Silva");
		Adotante segundoAdotante = novoAdotante("João Souza");
		animal.realizarAdocao(primeiroAdotante);
		animal.registrarDevolucao(null);

		animal.realizarAdocao(segundoAdotante);

		assertEquals(AnimalStatus.ADOTADO, animal.getStatus());
		assertEquals(segundoAdotante, animal.getAdotanteAtual());
		assertTrue(animal.getAdotantes().contains(primeiroAdotante));
		assertTrue(animal.getAdotantes().contains(segundoAdotante));
	}

	@Test
	void deveLancarExcecao_quandoDevolverAnimalNaoAdotado() {
		Animal animal = novoAnimal();

		assertThrows(IllegalStateException.class, () -> animal.registrarDevolucao("Não se adaptou"));
	}

	@Test
	void deveRegistrarLog_quandoTransferirParaLar() {
		Animal animal = novoAnimal();

		animal.transferirParaLar(UUID.randomUUID());

		assertEquals(2, animal.getLogs().size());
		assertEquals("Animal transferido para outro lar.", animal.getLogs().get(1));
	}

	@Test
	void deveAtualizarDataAtualizadoEm_quandoTransferirParaLar() {
		Animal animal = novoAnimal();

		assertAtualizadoEmFoiAtualizado(animal, a -> a.transferirParaLar(UUID.randomUUID()));
	}

	@Test
	void deveAtualizarDataAtualizadoEm_quandoDisponibilizar() {
		Animal animal = novoAnimal();
		animal.iniciarProcessoAdocao();

		assertAtualizadoEmFoiAtualizado(animal, Animal::disponibilizar);
	}

	@Test
	void deveAtualizarDataAtualizadoEm_quandoIniciarProcessoAdocao() {
		Animal animal = novoAnimal();

		assertAtualizadoEmFoiAtualizado(animal, Animal::iniciarProcessoAdocao);
	}

	@Test
	void deveAtualizarDataAtualizadoEm_quandoMarcarComoAdotado() {
		Animal animal = novoAnimal();
		animal.iniciarProcessoAdocao();

		assertAtualizadoEmFoiAtualizado(animal, Animal::marcarComoAdotado);
	}

	@Test
	void deveAtualizarDataAtualizadoEm_quandoRealizarAdocao() {
		Animal animal = novoAnimal();

		assertAtualizadoEmFoiAtualizado(animal, a -> a.realizarAdocao(novoAdotante("Maria Silva")));
	}

	@Test
	void deveAtualizarDataAtualizadoEm_quandoDevolver() {
		Animal animal = novoAnimal();
		animal.realizarAdocao(novoAdotante("Maria Silva"));

		assertAtualizadoEmFoiAtualizado(animal, a -> a.registrarDevolucao("Não se adaptou"));
	}

	private void assertAtualizadoEmFoiAtualizado(Animal animal, Consumer<Animal> acao) {
		LocalDateTime atualizadoEmAnterior = animal.getAtualizadoEm();
		aguardarProximoInstante();

		acao.accept(animal);

		assertTrue(animal.getAtualizadoEm().isAfter(atualizadoEmAnterior));
	}

	private void aguardarProximoInstante() {
		try {
			Thread.sleep(5);
		} catch (InterruptedException exception) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException("Teste interrompido.", exception);
		}
	}

	private Adotante novoAdotante(String nome) {
		return new Adotante(nome, "Rua das Flores, 123", nome.replace(" ", ".") + "@email.com", "senha123", "12345678901", List.of());
	}

	private Animal novoAnimal() {
		return novoAnimal(UUID.randomUUID());
	}

	private Animal novoAnimal(UUID larId) {
		return new Animal(dadosAnimal(), larId, List.of(), null);
	}

	private DadosAnimal dadosAnimal() {
		return new DadosAnimal("Luna", Especie.GATO, Porte.PEQUENO, SexoAnimal.FEMEA, 2, "Dócil");
	}
}
