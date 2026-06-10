package com.Miaumigo.Miaumigo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.Objects;

@Embeddable
public class DadosAnimal {

	@Column(nullable = false)
	private String nome;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Especie especie;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Porte porte;

	@Enumerated(EnumType.STRING)
	@Column(name = "sexo", nullable = false)
	private SexoAnimal sexo;

	private Integer idade;

	@Column(length = 1000)
	private String descricao;

	protected DadosAnimal() {
	}

	public DadosAnimal(String nome, Especie especie, Porte porte, SexoAnimal sexo, Integer idade, String descricao) {
		if (nome == null || nome.isBlank()) {
			throw new IllegalArgumentException("Nome do animal é obrigatório.");
		}
		Objects.requireNonNull(especie, "Espécie do animal é obrigatória.");
		Objects.requireNonNull(porte, "Porte do animal é obrigatório.");
		Objects.requireNonNull(sexo, "Sexo do animal é obrigatório.");
		if (idade != null && idade < 0) {
			throw new IllegalArgumentException("Idade do animal não pode ser negativa.");
		}
		this.nome = nome;
		this.especie = especie;
		this.porte = porte;
		this.sexo = sexo;
		this.idade = idade;
		this.descricao = descricao;
	}

	public String getNome() {
		return nome;
	}

	public Especie getEspecie() {
		return especie;
	}

	public Porte getPorte() {
		return porte;
	}

	public SexoAnimal getSexo() {
		return sexo;
	}

	public Integer getIdade() {
		return idade;
	}

	public String getDescricao() {
		return descricao;
	}
}
