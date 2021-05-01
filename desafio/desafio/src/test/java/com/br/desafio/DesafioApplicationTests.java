package com.br.desafio;

import com.br.desafio.entity.Hospede;
import com.br.desafio.service.HospedeDao;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DesafioApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DesafioApplicationTests {

	@Autowired
	private TestRestTemplate rest;

	@Test
	void Teste01() {
		Date dataNascimento = criarUmaData(14,03,1999,0,0);
		Hospede hospede = this.criaNovoHospede(110455,"Douglas",99101,dataNascimento);
		ResponseEntity response = rest.postForEntity("/hospede", hospede, null);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		Hospede hospede2 = rest.getForEntity("/hospede/110455", Hospede.class).getBody();

		assertNotNull(hospede2);
		assertEquals(hospede.getCpf(), hospede2.getCpf());
		assertEquals(hospede.getDataNascimento(), hospede2.getDataNascimento());
		assertEquals(hospede.getNome(), hospede2.getNome());
	}

	@Test
	void Teste02() {
		Date dataNascimento = this.criarUmaData(10,10,1995,0,0);
		Hospede hospede = this.criaNovoHospede(110455,"Janio", 4799,dataNascimento);

		ResponseEntity response = rest.postForEntity("/hospede", hospede, null);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		assertEquals(1, rest.exchange("/hospede", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Hospede>>() {}).getBody().size());

		dataNascimento = this.criarUmaData(10,12,2000,0,0);
		Hospede hospede2 = this.criaNovoHospede(2222,"Jaime",9999,dataNascimento);

		 response = rest.postForEntity("/hospede", hospede2, null);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		assertEquals(2, rest.exchange("/hospede", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Hospede>>() {}).getBody().size());

	}

	@Test
	void Teste03() {

		Date dataNascimento = this.criarUmaData(05,01,2001,0,0);
		Hospede hospede = this.criaNovoHospede(852,"Alan",9952,dataNascimento);

		ResponseEntity response = rest.postForEntity("/hospede", hospede, null);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		rest.delete("/hospede/1");

		assertEquals(0, rest.exchange("/hospede", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Hospede>>() {}).getBody().size());
	}

	@Test
	void Test04(){
		Date dataNascimento = this.criarUmaData(05,01,2001,0,0);
		Hospede hospede = this.criaNovoHospede(852,"Alan",9952,dataNascimento);

		ResponseEntity response = rest.postForEntity("/hospede", hospede, null);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		hospede.setNome("Willian");
		hospede.setTelefone(3344);

		rest.put("/hospede", hospede);

		Hospede hospede2 = rest.getForEntity("/hospede/852", Hospede.class).getBody();

		assertEquals(hospede.getCpf(), hospede2.getCpf());
		assertEquals(hospede.getNome(), hospede2.getNome());
		assertEquals(hospede.getTelefone(), hospede2.getTelefone());

	}

	private Hospede criaNovoHospede(int cpf, String nome, int telefone, Date dataNasc){
		Hospede hospede = new Hospede();
		hospede.setCpf(cpf);
		hospede.setNome(nome);
		hospede.setTelefone(telefone);
		hospede.setDataNascimento(dataNasc);
		return hospede;
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
