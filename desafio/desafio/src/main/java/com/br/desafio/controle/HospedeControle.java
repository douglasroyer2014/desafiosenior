package com.br.desafio.controle;

import com.br.desafio.entity.Hospede;
import com.br.desafio.repository.HospedeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hospede")
public class HospedeControle {

    @Autowired
    HospedeRepository repository;

   @GetMapping
    public @ResponseBody List<Hospede> listar(){
        return repository.findByAtivo(true);
    }

    @PostMapping
    public ResponseEntity<Hospede> cadastrar(@RequestBody Hospede hospede){
        hospede.setAtivo(true);
        return new ResponseEntity<Hospede>(repository.save(hospede), HttpStatus.CREATED);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity filtrar(@PathVariable Long cpf){
        return ResponseEntity.ok(repository.findByCpf(cpf));
    }

    @PutMapping
    public ResponseEntity<Hospede> alterar(@RequestBody Hospede hospede) {
        Hospede existHospede = repository.findById(hospede.getId());
        if (existHospede != null)
            return ResponseEntity.ok(repository.save(hospede));
        else
            return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        Optional<Hospede> hospede = repository.findById(id);
        if(hospede.isPresent()){
            Hospede existHospede = hospede.get();
            existHospede.setAtivo(false);
            repository.save(existHospede);
           return ResponseEntity.ok("Removido com sucesso!");
        }else{
            return ResponseEntity.notFound().build();
        }
    }


}
