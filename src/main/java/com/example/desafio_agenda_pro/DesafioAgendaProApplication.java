package com.example.desafio_agenda_pro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DesafioAgendaProApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioAgendaProApplication.class, args);
	}

}
