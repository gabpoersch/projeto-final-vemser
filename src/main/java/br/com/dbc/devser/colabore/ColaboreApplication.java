package br.com.dbc.devser.colabore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ColaboreApplication {
    /*
        TODO:
            - Logs
            - Adequar classes as exceções personalizadas
            - Adicionar validações
            - Testar api no swagger
            - Refatorar e deixar perfomático
            - Testes unitários
            - User @PutMapping
            - Fundraiser @PutMapping
            - Fazer lógica de verificação do id nos updates e rquisições

        IDEIAS
            - Testes automatizados
     */
    public static void main(String[] args) {
        SpringApplication.run(ColaboreApplication.class, args);
    }

}
