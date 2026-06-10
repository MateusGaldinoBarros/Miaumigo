package com.Miaumigo.Miaumigo.repository;

import com.Miaumigo.Miaumigo.domain.Adotante;
import com.Miaumigo.Miaumigo.domain.Animal;
import com.Miaumigo.Miaumigo.domain.DadosAnimal;
import com.Miaumigo.Miaumigo.domain.enums.Especie;
import com.Miaumigo.Miaumigo.domain.Lar;
import com.Miaumigo.Miaumigo.domain.enums.Porte;
import com.Miaumigo.Miaumigo.domain.enums.SexoAnimal;
import com.Miaumigo.Miaumigo.domain.SolicitacaoAdocao;
import com.Miaumigo.Miaumigo.domain.enums.SolicitacaoStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class SolicitacaoAdocaoRepositoryTest {

	@Autowired
	private SolicitacaoAdocaoRepository solicitacaoRepository;

	@Autowired
	private AnimalRepository animalRepository;

	@Autowired
	private AdotanteRepository adotanteRepository;

	@Autowired
	private LarRepository larRepository;

	@Test
	void deveConsultarSolicitacoesPendentes_quandoLarEAdotantesValidos() {
		Lar lar = larRepository.save(new Lar("Lar Amigo"));
		Animal animal = animalRepository.save(new Animal(
				new DadosAnimal("Luna", Especie.GATO, Porte.PEQUENO, SexoAnimal.FEMEA, 2, "Calma"),
				lar.getId(),
				List.of(),
				null
		));
		Adotante maria = adotanteRepository.save(novoAdotante("Maria", "maria@email.com", "12345678901"));
		Adotante joao = adotanteRepository.save(novoAdotante("Joao", "joao@email.com", "98765432109"));
		solicitacaoRepository.save(new SolicitacaoAdocao(animal, maria));
		solicitacaoRepository.save(new SolicitacaoAdocao(animal, joao));

		assertTrue(solicitacaoRepository.existsByAnimalIdAndAdotanteIdAndStatus(
				animal.getId(), maria.getId(), SolicitacaoStatus.PENDENTE));
		assertEquals(2, solicitacaoRepository.findByAnimalLarIdAndStatus(
				lar.getId(), SolicitacaoStatus.PENDENTE).size());
		assertEquals(1, solicitacaoRepository.findByAdotanteIdOrderByCriadoEmDesc(maria.getId()).size());
	}

	private Adotante novoAdotante(String nome, String email, String cpf) {
		return new Adotante(nome, "Rua A", email, "senha", cpf, List.of());
	}
}
