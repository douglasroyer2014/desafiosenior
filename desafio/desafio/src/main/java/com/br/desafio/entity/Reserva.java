package com.br.desafio.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name="reserva")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Reserva {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @SequenceGenerator(name="reserva_id_seq")
    private int id;

    private Date checkin;

    private Date checkout;

    private long cpfHospede;

    private boolean garagem;

}
