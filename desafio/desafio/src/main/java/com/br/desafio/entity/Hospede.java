package com.br.desafio.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.springframework.data.jpa.repository.query.JpaQueryExecution;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "hospede")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Hospede {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @SequenceGenerator(name="hospede_id_seq")
    private int id;

    private String nome;

    private Date dataNascimento;

    private long telefone;

    private long cpf;

    private boolean ativo;
}
