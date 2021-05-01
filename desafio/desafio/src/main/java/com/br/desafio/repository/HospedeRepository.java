package com.br.desafio.repository;

import com.br.desafio.entity.Hospede;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HospedeRepository extends CrudRepository<Hospede, Integer> {

    List<Hospede> findByAtivo(boolean ativo);

    Hospede findByCpf(long cpf);

    <tipHospede extends Hospede> tipHospede save(tipHospede hospede);

}
