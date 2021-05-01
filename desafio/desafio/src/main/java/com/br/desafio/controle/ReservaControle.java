package com.br.desafio.controle;

import com.br.desafio.entity.Reserva;
import com.br.desafio.service.ReservaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reserva")
public class ReservaControle {

    @Autowired
    ReservaDao reservaDao;

    @GetMapping("/{cpf}")
    public @ResponseBody List<Reserva> listar(@PathVariable Long cpf){
        return reservaDao.listarCpf(cpf);
    }

    @PostMapping("/checkin")
    public ResponseEntity cadastrar(@RequestBody Reserva reserva){
        return new ResponseEntity(reservaDao.checkin(reserva), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity listarTodos(){
        return ResponseEntity.ok(reservaDao.listarTodos());
    }

    @DeleteMapping("/checkin")
    public ResponseEntity deleteReserva(@RequestBody Reserva reserva){
        return reservaDao.removerCheckin(reserva);
    }

    @PutMapping("/checkin")
    public ResponseEntity alterarReserva(@RequestBody Reserva reserva){
        return reservaDao.alterarCheckin(reserva);
    }

    @PostMapping("/checkout")
    public ResponseEntity realizarCheckout(@RequestBody Reserva reserva){
        return reservaDao.checkout(reserva);
    }

}
