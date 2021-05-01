package com.br.desafio.controle;

import com.br.desafio.entity.Hospede;
import com.br.desafio.service.HospedeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hospede")
public class HospedeControle {

    @Autowired
    HospedeDao hospedeDao;

   @GetMapping
    public @ResponseBody List<Hospede> listar(){
        return hospedeDao.listar(true);
    }

    @PostMapping
    public ResponseEntity<Hospede> cadastrar(@RequestBody Hospede hospede){
        return hospedeDao.cadastrar(hospede);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity filtrar(@PathVariable Long cpf){
       return hospedeDao.filtrar(cpf);
    }

    @PutMapping
    public ResponseEntity alterar(@RequestBody Hospede hospede) {
        return hospedeDao.alterar(hospede);
    }

    @PutMapping("/{id}")
    public ResponseEntity ativarHospede(@PathVariable Integer id) {
        return hospedeDao.ativarHospede(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity desativarHospede(@PathVariable Integer id){
        return hospedeDao.desativarHospede(id);
    }

}
