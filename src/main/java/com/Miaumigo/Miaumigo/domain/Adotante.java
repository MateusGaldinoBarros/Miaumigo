package com.Miaumigo.Miaumigo.domain;

import com.Miaumigo.Miaumigo.domain.enums.Especie;
import com.Miaumigo.Miaumigo.domain.enums.ExperienciaAnimais;
import com.Miaumigo.Miaumigo.domain.enums.Porte;
import com.Miaumigo.Miaumigo.domain.enums.Tag;
import com.Miaumigo.Miaumigo.domain.enums.TempoDisponivel;
import com.Miaumigo.Miaumigo.domain.enums.TipoMoradia;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.AssociationOverride;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
//ultima versão
@Entity
@Table(name = "adotantes")
@AssociationOverride(
		name = "historico.logs",
		joinTable = @JoinTable(name = "adotante_logs", joinColumns = @JoinColumn(name = "adotante_id"))
)
public class Adotante extends Usuario {

	@ElementCollection
	@CollectionTable(name = "adotante_preferencias", joinColumns = @JoinColumn(name = "adotante_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "tag", nullable = false)
	@OrderColumn(name = "ordem")
	private List<Tag> preferencias = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "adotante_especies_preferidas", joinColumns = @JoinColumn(name = "adotante_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "especie", nullable = false)
	@OrderColumn(name = "ordem")
	private List<Especie> especiesPreferidas = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_moradia")
	private TipoMoradia tipoMoradia;

	@Enumerated(EnumType.STRING)
	@Column(name = "espaco_disponivel")
	private Porte espacoDisponivel;

	@Enumerated(EnumType.STRING)
	@Column(name = "tempo_disponivel")
	private TempoDisponivel tempoDisponivel;

	@Enumerated(EnumType.STRING)
	@Column(name = "experiencia_animais")
	private ExperienciaAnimais experienciaAnimais;

	@Column(name = "possui_criancas")
	private Boolean possuiCriancas;

	@Column(name = "possui_caes")
	private Boolean possuiCaes;

	@Column(name = "possui_gatos")
	private Boolean possuiGatos;

	@Column(name = "telefone")
	private String telefone;

	@Column(name = "cidade")
	private String cidade;

	@Embedded
	private Historico historico;

	protected Adotante() {
	}

	public Adotante(String nome, String endereco, String email, String senha, String cpf, List<Tag> preferencias) {
		super(nome, endereco, email, senha, cpf);
		this.preferencias = normalizarPreferencias(preferencias);
		this.historico = Historico.criar();
	}

	public List<Tag> getPreferencias() {
		return List.copyOf(preferencias);
	}

	public List<Especie> getEspeciesPreferidas() {
		return List.copyOf(especiesPreferidas);
	}

	public TipoMoradia getTipoMoradia() {
		return tipoMoradia;
	}

	public Porte getEspacoDisponivel() {
		return espacoDisponivel;
	}

	public TempoDisponivel getTempoDisponivel() {
		return tempoDisponivel;
	}

	public ExperienciaAnimais getExperienciaAnimais() {
		return experienciaAnimais;
	}

	public Boolean getPossuiCriancas() {
		return possuiCriancas;
	}

	public Boolean getPossuiCaes() {
		return possuiCaes;
	}

	public Boolean getPossuiGatos() {
		return possuiGatos;
	}

	public String getTelefone() {
		return telefone;
	}

	public String getCidade() {
		return cidade;
	}

	public boolean isPerfilCompleto() {
		return !especiesPreferidas.isEmpty()
				&& !preferencias.isEmpty()
				&& tipoMoradia != null
				&& espacoDisponivel != null
				&& tempoDisponivel != null
				&& experienciaAnimais != null
				&& possuiCriancas != null
				&& possuiCaes != null
				&& possuiGatos != null;
	}

	public void atualizarPerfil(
			List<Especie> especiesPreferidas,
			List<Tag> preferencias,
			TipoMoradia tipoMoradia,
			Porte espacoDisponivel,
			TempoDisponivel tempoDisponivel,
			ExperienciaAnimais experienciaAnimais,
			Boolean possuiCriancas,
			Boolean possuiCaes,
			Boolean possuiGatos,
			String telefone,
			String cidade
	) {
		this.especiesPreferidas = normalizarLista(especiesPreferidas);
		this.preferencias = normalizarPreferencias(preferencias);
		this.tipoMoradia = tipoMoradia;
		this.espacoDisponivel = espacoDisponivel;
		this.tempoDisponivel = tempoDisponivel;
		this.experienciaAnimais = experienciaAnimais;
		this.possuiCriancas = possuiCriancas;
		this.possuiCaes = possuiCaes;
		this.possuiGatos = possuiGatos;
		this.telefone = normalizarTextoOpcional(telefone);
		this.cidade = normalizarTextoOpcional(cidade);
		this.historico.marcarAtualizacao();
	}

	public List<String> getLogs() {
		return historico.getLogs();
	}

	public LocalDateTime getCriadoEm() {
		return historico.getCriadoEm();
	}

	public LocalDateTime getAtualizadoEm() {
		return historico.getAtualizadoEm();
	}

	public void adicionarLog(String mensagem) {
		this.historico.adicionarLog(mensagem);
	}

	private List<Tag> normalizarPreferencias(List<Tag> preferencias) {
		return normalizarLista(preferencias);
	}

	private <T> List<T> normalizarLista(List<T> valores) {
		if (valores == null) {
			return new ArrayList<>();
		}
		List<T> valoresNormalizados = valores.stream()
				.filter(Objects::nonNull)
				.distinct()
				.toList();
		return new ArrayList<>(valoresNormalizados);
	}

	private String normalizarTextoOpcional(String texto) {
		if (texto == null || texto.isBlank()) {
			return null;
		}
		return texto.trim();
	}
}
