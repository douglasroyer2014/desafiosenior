package com.br.desafio.service;

import com.br.desafio.entity.Hospede;
import com.br.desafio.repository.HospedeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HospedeDao {

    @Autowired
    HospedeRepository repository;

    public List<Hospede> listar(boolean ativo){
        return repository.findByAtivo(ativo);
    }

    public Hospede cadastrar(Hospede hospede){
        hospede.setAtivo(true);
        return repository.save(hospede);
    }

    public ResponseEntity filtrar(Long cpf){
        Hospede hospede = repository.findByCpf(cpf);
        if(hospede != null)
            return ResponseEntity.ok(hospede);
        else
            return ResponseEntity.notFound().build();
    }

    public ResponseEntity alterar(Hospede hospede){
        Hospede existHospede = repository.findByCpf(hospede.getCpf());
        if (existHospede != null)
            return ResponseEntity.ok(repository.save(hospede));
        else
            return ResponseEntity.notFound().build();
    }

    public ResponseEntity ativarHospede(Integer id){
        Optional<Hospede> existHospede = repository.findById(id);
        if (existHospede.isPresent()) {
            Hospede hospede = existHospede.get();
            hospede.setAtivo(true);
            repository.save(hospede);
            return ResponseEntity.ok("Reativado!");
        }else
            return ResponseEntity.notFound().build();
    }

    public ResponseEntity desativarHospede(Integer id){
        Optional<Hospede> hospede = repository.findById(id);
        if(hospede.isPresent()){
            Hospede existHospede = hospede.get();
            existHospede.setAtivo(false);
            repository.save(existHospede);
            return ResponseEntity.ok("Desativado!");
        }else{
            return ResponseEntity.notFound().build();
        }
    }

}
