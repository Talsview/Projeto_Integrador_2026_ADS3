package br.com.avcar.oficina;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação da Oficina Mecânica AV CAR AUTO CENTER.
 *
 * Arquitetura adotada: monólito modular em camadas.
 * Camadas previstas: Model, DTO, Repository, Validation, Service, Controller,
 * Response e View. A camada View será atendida pelo frontend Angular,
 * consumindo esta API REST.
 */
@SpringBootApplication
public class OficinaApplication {

    public static void main(String[] args) {
        SpringApplication.run(OficinaApplication.class, args);
    }
}
