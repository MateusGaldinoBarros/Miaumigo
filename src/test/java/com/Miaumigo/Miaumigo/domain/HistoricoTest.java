package com.Miaumigo.Miaumigo.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HistoricoTest {

	@Test
	void deveCriarHistoricoSemLogs_quandoNaoPossuiMensagemInicial() {
		Historico historico = Historico.criar();

		assertEquals(List.of(), historico.getLogs());
		assertTrue(historico.getCriadoEm() != null);
		assertEquals(historico.getCriadoEm(), historico.getAtualizadoEm());
	}

	@Test
	void deveCriarHistoricoComLogInicial_quandoMensagemValida() {
		Historico historico = Historico.criarComLogInicial("Criado.");

		assertEquals(List.of("Criado."), historico.getLogs());
		assertEquals(historico.getCriadoEm(), historico.getAtualizadoEm());
	}

	@Test
	void deveAdicionarLogNormalizado_quandoMensagemValida() {
		Historico historico = Historico.criar();

		historico.adicionarLog(" Recebeu vacina. ");

		assertEquals(List.of("Recebeu vacina."), historico.getLogs());
	}

	@Test
	void deveLancarExcecao_quandoMensagemNulaOuVazia() {
		Historico historico = Historico.criar();

		assertThrows(IllegalArgumentException.class, () -> historico.adicionarLog(null));
		assertThrows(IllegalArgumentException.class, () -> historico.adicionarLog(" "));
	}

	@Test
	void deveAtualizarDataAtualizadoEm_quandoAdicionarLog() {
		Historico historico = Historico.criar();
		LocalDateTime atualizadoEmAnterior = historico.getAtualizadoEm();
		aguardarProximoInstante();

		historico.adicionarLog("Recebeu vacina.");

		assertTrue(historico.getAtualizadoEm().isAfter(atualizadoEmAnterior));
	}

	@Test
	void deveRetornarLogsImutaveis_quandoSolicitarLogs() {
		Historico historico = Historico.criarComLogInicial("Criado.");

		List<String> logs = historico.getLogs();

		assertThrows(UnsupportedOperationException.class, () -> logs.add("Outro log."));
	}

	private void aguardarProximoInstante() {
		try {
			Thread.sleep(5);
		} catch (InterruptedException exception) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException("Teste interrompido.", exception);
		}
	}
}
