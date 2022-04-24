package br.com.dbc.devser.colabore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ColaboreApplication {
    /*
        TODO:
            - Logs
            - Validar e-mail e categoria (não ter duplicatas)
            - Adequar classes as exceções personalizadas
            - Adicionar validações
            - Testar api no swagger
            - Refatorar e deixar perfomático
            - Testes unitários

        IDEIAS
            - Testes automatizados
     */
    public static void main(String[] args) {
        SpringApplication.run(ColaboreApplication.class, args);
    }

}
