package com.Miaumigo.Miaumigo.domain;

import com.Miaumigo.Miaumigo.domain.enums.AnimalStatus;
import com.Miaumigo.Miaumigo.domain.enums.Especie;
import com.Miaumigo.Miaumigo.domain.enums.Porte;
import com.Miaumigo.Miaumigo.domain.enums.SexoAnimal;
import com.Miaumigo.Miaumigo.domain.enums.Tag;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "animais")
@AssociationOverride(
		name = "historico.logs",
		joinTable = @JoinTable(name = "animal_logs", joinColumns = @JoinColumn(name = "animal_id"))
)
public class Animal {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Embedded
	private DadosAnimal dados;

	@ElementCollection
	@CollectionTable(name = "animal_tags", joinColumns = @JoinColumn(name = "animal_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "tag", nullable = false)
	@OrderColumn(name = "ordem")
	private List<Tag> tags = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "animal_logs", joinColumns = @JoinColumn(name = "animal_id"))
	@Column(name = "mensagem", nullable = false, length = 1000)
	@OrderColumn(name = "ordem")
	private List<String> logs = new ArrayList<>();

	@Column(name = "cloudinary_public_id")
	private String cloudinaryPublicId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AnimalStatus status;

	@ManyToMany
	@JoinTable(
			name = "animal_adotantes",
			joinColumns = @JoinColumn(name = "animal_id"),
			inverseJoinColumns = @JoinColumn(name = "adotante_id")
	)
	private Set<Adotante> adotantes = new LinkedHashSet<>();

	@ManyToOne
	@JoinColumn(name = "adotante_atual_id")
	private Adotante adotanteAtual;

	@Column(name = "lar_id", nullable = false)
	private UUID larId;

	@Embedded
	private Historico historico;

	protected Animal() {
	}

	public Animal(DadosAnimal dados, UUID larId, List<Tag> tags, String cloudinaryPublicId) {
		this.dados = Objects.requireNonNull(dados, "Dados do animal são obrigatórios.");
		validarLar(larId);
		this.larId = larId;
		this.tags = normalizarTags(tags);
		this.cloudinaryPublicId = normalizarTextoOpcional(cloudinaryPublicId);
		this.status = AnimalStatus.DISPONIVEL;
		this.historico = Historico.criarComLogInicial("Animal cadastrado.");
	}
	public UUID getId() {
		return id;
	}

	public String getNome() {
		return dados.getNome();
	}

	public void transferirParaLar(UUID larId) {
		if (this.status != AnimalStatus.DISPONIVEL) {
			throw new IllegalStateException("Animal só pode ser transferido quando estiver disponível.");
		}
		validarLar(larId);
		this.larId = larId;
		this.historico.adicionarLog("Animal transferido para outro lar.");
	}

	public void disponibilizar() {
		this.status = AnimalStatus.DISPONIVEL;
		this.historico.adicionarLog("Animal disponibilizado para adoção.");
	}

	public void iniciarProcessoAdocao() {
		if (this.status != AnimalStatus.DISPONIVEL) {
			throw new IllegalStateException("Apenas animais disponíveis podem iniciar processo de adoção.");
		}
		this.status = AnimalStatus.EM_PROCESSO;
		this.historico.adicionarLog("Processo de adoção iniciado.");
	}
	//Marca como adotado quando está em processo de adoção
	public void marcarComoAdotado() {
		if (this.status != AnimalStatus.EM_PROCESSO) {
			throw new IllegalStateException("Apenas animais em processo de adoção podem ser marcados como adotados.");
		}
		this.status = AnimalStatus.ADOTADO;
		this.historico.adicionarLog("Animal marcado como adotado.");
	}

	//Marca como adotado quando o animal ainda está disponivel
	public void realizarAdocao(Adotante adotante) {
		if (this.status != AnimalStatus.DISPONIVEL) {
			throw new IllegalStateException("Apenas animais disponíveis podem ser adotados.");
		}
		if (adotante == null) {
			throw new IllegalArgumentException("Adotante é obrigatório.");
		}

		this.adotanteAtual = adotante;
		this.adotantes.add(adotante);
		this.status = AnimalStatus.ADOTADO;
		this.historico.adicionarLog("Adotado por " + adotante.getNome() + ".");
	}

	//Verbo do metodo da a indicar que o animal se devolve quando ele apenas registra a devolução e muda o estado
	public Adotante registrarDevolucao(String motivo) {
		if (this.status != AnimalStatus.ADOTADO || this.adotanteAtual == null) {
			throw new IllegalStateException("Apenas animais adotados podem ser devolvidos.");
		}
		Adotante adotanteAnterior = this.adotanteAtual;
		this.adotanteAtual = null;
		this.status = AnimalStatus.DISPONIVEL;
		String motivoNormalizado = normalizarTextoOpcional(motivo);
		if (motivoNormalizado == null) {
			this.historico.adicionarLog("Animal devolvido por " + adotanteAnterior.getNome() + ".");
		} else {
			this.historico.adicionarLog("Animal devolvido por " + adotanteAnterior.getNome() + ". Motivo: " + motivoNormalizado + ".");
		}
		return adotanteAnterior;
	}

	public Especie getEspecie() {
		return dados.getEspecie();
	}

	public Porte getPorte() {
		return dados.getPorte();
	}

	public SexoAnimal getSexo() {
		return dados.getSexo();
	}

	public Integer getIdade() {
		return dados.getIdade();
	}

	public String getDescricao() {
		return dados.getDescricao();
	}

	public List<Tag> getTags() {
		return List.copyOf(tags);
	}

	public List<String> getLogs() {
		return historico.getLogs();
	}

	public String getCloudinaryPublicId() {
		return cloudinaryPublicId;
	}

	public AnimalStatus getStatus() {
		return status;
	}

	public Set<Adotante> getAdotantes() {
		return Set.copyOf(adotantes);
	}

	public Adotante getAdotanteAtual() {
		return adotanteAtual;
	}

	public UUID getLarId() {
		return larId;
	}

	public LocalDateTime getCriadoEm() {
		return historico.getCriadoEm();
	}

	public LocalDateTime getAtualizadoEm() {
		return historico.getAtualizadoEm();
	}

	//Realmente precisa ser um metodo proprio?
	private void validarLar(UUID larId) {
		if (larId == null) {
			throw new IllegalArgumentException("Lar do animal é obrigatório.");
		}
	}

	private List<Tag> normalizarTags(List<Tag> tags) {
		if (tags == null) {
			return new ArrayList<>();
		}
		List<Tag> tagsNormalizadas = tags.stream()
				.filter(Objects::nonNull)
				.distinct()
				.toList();
		return new ArrayList<>(tagsNormalizadas);
	}

	private String normalizarTextoOpcional(String texto) {
		if (texto == null || texto.isBlank()) {
			return null;
		}
		return texto.trim();
	}
}
