package com.br.desafio.service;

import com.br.desafio.entity.Reserva;
import com.br.desafio.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ReservaDao {

    @Autowired
    ReservaRepository repository;

    public @ResponseBody List<Reserva> listarCpf(Long cpf){
        return repository.findByCpfHospede(cpf);
    }

    public ResponseEntity checkin(Reserva reserva){
        return new ResponseEntity(repository.save(reserva), HttpStatus.CREATED);
    }

    public List<Reserva> listarTodos(){
        return repository.findAll();
    }

    public ResponseEntity removerCheckin(Reserva reserva){
        Reserva existReserva = this.validaReserva(reserva);
        if(existReserva != null) {
            repository.delete(reserva);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity alterarCheckin(Reserva reserva){
        Reserva existReserva = this.validaReserva(reserva);
        if(existReserva != null){
            repository.save(reserva);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity checkout(Reserva reserva){
        Reserva existReserva = this.validaReserva(reserva);
        if (existReserva != null){

            double valor = this.valorSerPago(reserva.getCheckin(), reserva.getCheckout(), reserva.isGaragem());
            this.checkin(reserva);
            return ResponseEntity.ok(valor);
        }else
            return ResponseEntity.notFound().build();
    }


    public double valorSerPago(Date checkinReserva, Date checkoutReserva, boolean garagem){
        double valor = 0;
        LocalDateTime checkin = checkinReserva.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime checkout = checkoutReserva.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        long data =   Duration.between(checkin, checkout).toDays();
        Calendar c = Calendar.getInstance();
        c.setTime(checkinReserva);
        for (int i = 0; i <= data; i++) {
            int diaSemana = c.get(Calendar.DAY_OF_WEEK);
            if(diaSemana == 1 || diaSemana == 7) {
                if(garagem)
                    valor+=20;
                valor += 150;
            }else {
                if(garagem)
                    valor+=15;
                valor += 120;
            }
            c.add(Calendar.DATE, 1);
        }
        Date novaData = new Date();
        novaData.setTime(checkoutReserva.getTime());
        novaData.setHours(16);
        novaData.setMinutes(30);
        if(checkoutReserva.getTime() > novaData.getTime()){
            c.setTime(checkoutReserva);
            int diaSemana = c.get(Calendar.DAY_OF_WEEK);
            if(diaSemana == 1 || diaSemana == 7)
                valor += 150;
            else
                valor += 120;
        }

        return valor;
    }

    public Reserva validaReserva(Reserva reserva){
        return repository.pesquisarCheckin(reserva.getCheckin(), reserva.getCpfHospede());
    }

}
