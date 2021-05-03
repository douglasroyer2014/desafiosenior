package com.br.desafio;

import com.br.desafio.entity.Reserva;
import com.br.desafio.service.ReservaDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

@SpringBootTest(classes = DesafioApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservaTests {

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private ReservaDao reservaDao;

    @Test
    void Teste01() {
        Reserva reserva = new Reserva();
        reserva.setGaragem(true);
        reserva.setCpfHospede(110);
        reserva.setCheckin(this.criarUmaData(20,03,2021,10,20));

        ResponseEntity response =  rest.postForEntity("/reserva/checkin", reserva, null);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        List<Reserva> lista = rest.exchange("/reserva/110", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Reserva>>() {}).getBody();

        Reserva reserva1 = lista.get(0);
        assertEquals(reserva.getCheckin(), reserva1.getCheckin());
        assertEquals(reserva.getCpfHospede(), reserva1.getCpfHospede());
        assertEquals(reserva.isGaragem(), reserva1.isGaragem());

    }

    @Test
    void Teste02() {
        Reserva reserva = new Reserva();
        reserva.setGaragem(true);
        reserva.setCpfHospede(455);
        reserva.setCheckin(this.criarUmaData(20,03,2021,10,20));

        ResponseEntity response =  rest.postForEntity("/reserva/checkin", reserva, null);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        reservaDao.removerCheckin(reserva);

        response = reservaDao.listarCpf(reserva.getCpfHospede());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }
    @Test
    void Teste03() {
        Reserva reserva = new Reserva();
        reserva.setGaragem(true);
        reserva.setCpfHospede(654);
        reserva.setCheckin(this.criarUmaData(20,03,2021,10,20));

        ResponseEntity response =  rest.postForEntity("/reserva/checkin", reserva, null);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        List<Reserva> lista = rest.exchange("/reserva/654", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Reserva>>() {}).getBody();

        Reserva reserva1 = lista.get(0);
        assertEquals(reserva.getCheckin(), reserva1.getCheckin());
        assertEquals(reserva.isGaragem(), reserva1.isGaragem());

        reserva1.setGaragem(false);
        reserva1.setCheckin(this.criarUmaData(25, 03, 2021, 15, 30));
        rest.put("/reserva/checkin/"+ reserva1.getId(), reserva1);

         lista = rest.exchange("/reserva/654", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Reserva>>() {}).getBody();

         reserva = lista.get(0);
         assertEquals(reserva.getCpfHospede(), reserva1.getCpfHospede());
         assertEquals(reserva.isGaragem(), reserva1.isGaragem());
         assertEquals(reserva.getCheckin(), reserva1.getCheckin());

    }

    @Test
    void Teste04() {
        Reserva reserva = new Reserva();
        reserva.setGaragem(true);
        reserva.setCpfHospede(987);
        reserva.setCheckin(this.criarUmaData(01,04,2021,9,20));

        ResponseEntity response =  rest.postForEntity("/reserva/checkin", reserva, null);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        List<Reserva> lista = rest.exchange("/reserva/987", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Reserva>>() {}).getBody();

        Reserva reserva1 = lista.get(0);
        assertEquals(reserva1.getCheckout(), null);
        assertEquals(reserva1.getCpfHospede(), 987);

        reserva1.setCheckout(this.criarUmaData(05,04,2021,16, 30));

        rest.put("/reserva/checkout", reserva1);

        lista = rest.exchange("/reserva/987", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Reserva>>() {}).getBody();

        reserva = lista.get(0);

        assertEquals(reserva.getCheckout(), reserva1.getCheckout());
        assertEquals(reserva.getCpfHospede(), reserva1.getCpfHospede());

        reservaDao.removerCheckout(reserva);

        lista = rest.exchange("/reserva/987", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Reserva>>() {}).getBody();

        reserva = lista.get(0);
        assertEquals(reserva.getCheckout(), null);

    }


    private Date criarUmaData(int dia, int mes, int ano, int hora, int minuto){
        Date data = new Date();
        data.setDate(dia);
        data.setMonth(mes);
        data.setYear(ano);
        data.setHours(hora);
        data.setMinutes(minuto);
        return data;
    }
}
