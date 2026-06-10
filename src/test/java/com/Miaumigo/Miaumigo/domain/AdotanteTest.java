package com.Miaumigo.Miaumigo.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdotanteTest {

	@Test
	void deveCriarAdotante_quandoDadosValidos() {
		Adotante adotante = novoAdotante(List.of(Tag.DOCIL, Tag.CARINHOSO));

		assertEquals("Maria Silva", adotante.getNome());
		assertEquals(List.of(Tag.DOCIL, Tag.CARINHOSO), adotante.getPreferencias());
		assertEquals(List.of(), adotante.getLogs());
		assertTrue(adotante.getCriadoEm() != null);
		assertEquals(adotante.getCriadoEm(), adotante.getAtualizadoEm());
	}

	@Test
	void deveNormalizarPreferencias_quandoPossuiDuplicadasOuNulas() {
		Adotante adotante = novoAdotante(Arrays.asList(Tag.DOCIL, null, Tag.CARINHOSO, Tag.DOCIL));

		assertEquals(List.of(Tag.DOCIL, Tag.CARINHOSO), adotante.getPreferencias());
	}

	@Test
	void deveCriarAdotanteSemPreferencias_quandoPreferenciasNulas() {
		Adotante adotante = novoAdotante(null);

		assertEquals(List.of(), adotante.getPreferencias());
	}

	@Test
	void deveAdicionarLog_quandoMensagemValida() {
		Adotante adotante = novoAdotante(List.of());

		adotante.adicionarLog(" Adotou Luna. ");

		assertEquals(List.of("Adotou Luna."), adotante.getLogs());
	}

	@Test
	void deveAtualizarDataAtualizadoEm_quandoAdicionarLog() {
		Adotante adotante = novoAdotante(List.of());

		assertAtualizadoEmFoiAtualizado(adotante, a -> a.adicionarLog("Adotou Luna."));
	}

	@Test
	void deveAtualizarDataAtualizadoEm_quandoAtualizarPerfil() {
		Adotante adotante = novoAdotante(List.of());

		assertAtualizadoEmFoiAtualizado(adotante, a -> a.atualizarPerfil(
				List.of(Especie.GATO),
				List.of(Tag.CALMO),
				TipoMoradia.APARTAMENTO,
				Porte.PEQUENO,
				TempoDisponivel.UMA_HORA,
				ExperienciaAnimais.PRIMEIRA_ADOCAO,
				false,
				false,
				true,
				"81999999999",
				"Recife"
		));
	}

	@Test
	void deveLancarExcecao_quandoLogSemMensagem() {
		Adotante adotante = novoAdotante(List.of());

		assertThrows(IllegalArgumentException.class, () -> adotante.adicionarLog(" "));
	}

	private void assertAtualizadoEmFoiAtualizado(Adotante adotante, Consumer<Adotante> acao) {
		LocalDateTime atualizadoEmAnterior = adotante.getAtualizadoEm();
		aguardarProximoInstante();

		acao.accept(adotante);

		assertTrue(adotante.getAtualizadoEm().isAfter(atualizadoEmAnterior));
	}

	private void aguardarProximoInstante() {
		try {
			Thread.sleep(5);
		} catch (InterruptedException exception) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException("Teste interrompido.", exception);
		}
	}

	private Adotante novoAdotante(List<Tag> preferencias) {
		return new Adotante(
				"Maria Silva",
				"Rua das Flores, 123",
				"maria@email.com",
				"senha123",
				"12345678901",
				preferencias
		);
	}
}
