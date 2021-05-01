package com.br.desafio.repository;

import com.br.desafio.entity.Reserva;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ReservaRepository extends CrudRepository<Reserva, Integer> {

    @Query("select r from Reserva r where r.checkin = :checkin and r.cpfHospede = :cpfHospede")
    public Reserva pesquisarCheckin(@Param("checkin") Date checkin, @Param("cpfHospede") Long cpfHospede);

    List<Reserva> findAll();

    Reserva findById(int id);

    List<Reserva> findByCpfHospede(long cpfHospede);

    <tipReserva extends Reserva> tipReserva save(tipReserva reserva);

    void delete(Reserva reserva);
}
