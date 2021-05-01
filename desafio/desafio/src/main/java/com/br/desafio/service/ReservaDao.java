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

    public ResponseEntity listarCpf(Long cpf){
        List<Reserva> existReserva = repository.findByCpfHospede(cpf);
        if(!existReserva.isEmpty())
            return ResponseEntity.ok(existReserva);
        else
            return ResponseEntity.notFound().build();
    }

    public Reserva salvarReserva(Reserva reserva){
        return repository.save(reserva);
    }

    public List<Reserva> listarTodos(){
        return repository.findAll();
    }

    public ResponseEntity removerCheckin(Reserva reserva){
        Reserva existReserva = this.validaReserva(reserva);
        if(existReserva != null) {
            repository.delete(existReserva);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity alterarCheckin(Reserva reserva, int id){
        Reserva existReserva = repository.findById(id);
        if(existReserva != null){
            reserva.setId(id);
            return ResponseEntity.ok(this.salvarReserva(reserva));
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity removerCheckout(Reserva reserva){
        Reserva existReserva = this.validaReserva(reserva);
        if(existReserva != null){
            existReserva.setCheckout(null);
            return ResponseEntity.ok(this.salvarReserva(existReserva));
        }else
            return ResponseEntity.notFound().build();
    }

    public ResponseEntity checkout(Reserva reserva){
        Reserva existReserva = this.validaReserva(reserva);
        if (existReserva != null){
            double valor = this.valorSerPago(reserva.getCheckin(), reserva.getCheckout(), reserva.isGaragem());
            reserva.setId(existReserva.getId());
            this.salvarReserva(reserva);
            return ResponseEntity.ok(valor);
        }else
            return ResponseEntity.notFound().build();
    }

    public Reserva validaReserva(Reserva reserva){
        return repository.pesquisarCheckin(reserva.getCheckin(), reserva.getCpfHospede());
    }

    private double valorSerPago(Date checkinReserva, Date checkoutReserva, boolean garagem){
        double valor = 0;
        long data = this.qntDiasDiferenca(checkinReserva, checkoutReserva);
        Calendar c = Calendar.getInstance();
        c.setTime(checkinReserva);
        for (int i = 0; i <= data; i++) {
            int diaSemana = c.get(Calendar.DAY_OF_WEEK);
            valor+= this.somaDiaria(diaSemana,garagem);
            c.add(Calendar.DATE, 1);
        }
        Date comparaDataSaida = this.comparaDataSaida(checkoutReserva);
        if(checkoutReserva.getTime() > comparaDataSaida.getTime()){
            c.setTime(checkoutReserva);
            System.out.println("esta entrando aqui?");
            int diaSemana = c.get(Calendar.DAY_OF_WEEK);
            valor+=this.valorDiaSemana(diaSemana);
        }
        return valor;
    }

    private Date comparaDataSaida(Date checkoutReserva){
        Date novaData = new Date();
        novaData.setTime(checkoutReserva.getTime());
        novaData.setHours(16);
        novaData.setMinutes(30);
        return novaData;
    }

    private double valorDiaSemana(int diaSemana){
        double valor = 0;
        if(diaSemana == 1 || diaSemana == 7)
            valor += 150;
        else
            valor += 120;
        return valor;
    }

    private double somaDiaria(int diaSemana, boolean garagem){
        double valor = 0;
        if(diaSemana == 1 || diaSemana == 7) {
            if(garagem)
                valor+=20;
            valor += 150;
        }else {
            if(garagem)
                valor+=15;
            valor += 120;
        }
        return valor;
    }

    private long qntDiasDiferenca(Date checkinReserva, Date checkoutReserva){
        LocalDateTime checkin = checkinReserva.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime checkout = checkoutReserva.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        long data =   Duration.between(checkin, checkout).toDays();
        return data;
    }


}
