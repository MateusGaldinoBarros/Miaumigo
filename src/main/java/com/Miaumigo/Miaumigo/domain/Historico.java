package com.Miaumigo.Miaumigo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OrderColumn;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Historico {

	@ElementCollection
	@Column(name = "mensagem", nullable = false, length = 1000)
	@OrderColumn(name = "ordem")
	private List<String> logs = new ArrayList<>();

	@Column(name = "criado_em", nullable = false, updatable = false)
	private LocalDateTime criadoEm;

	@Column(name = "atualizado_em", nullable = false)
	private LocalDateTime atualizadoEm;

	protected Historico() {
	}

	private Historico(LocalDateTime criadoEm) {
		this.criadoEm = criadoEm;
		this.atualizadoEm = criadoEm;
	}

	public static Historico criar() {
		return new Historico(LocalDateTime.now());
	}

	public static Historico criarComLogInicial(String mensagem) {
		Historico historico = criar();
		historico.logs.add(normalizarMensagemObrigatoria(mensagem));
		return historico;
	}

	public void adicionarLog(String mensagem) {
		this.logs.add(normalizarMensagemObrigatoria(mensagem));
		marcarAtualizacao();
	}

	public void marcarAtualizacao() {
		this.atualizadoEm = LocalDateTime.now();
	}

	public List<String> getLogs() {
		return List.copyOf(logs);
	}

	public LocalDateTime getCriadoEm() {
		return criadoEm;
	}

	public LocalDateTime getAtualizadoEm() {
		return atualizadoEm;
	}

	private static String normalizarMensagemObrigatoria(String mensagem) {
		if (mensagem == null || mensagem.isBlank()) {
			throw new IllegalArgumentException("Mensagem do log é obrigatória.");
		}
		return mensagem.trim();
	}
}
