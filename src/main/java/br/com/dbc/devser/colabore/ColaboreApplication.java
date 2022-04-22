package br.com.dbc.devser.colabore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ColaboreApplication {
    /*
        TODO:
            Retirar a senha do UserDto, pois ele retorna na listagem das campanhas
            Logs
            Subir app
            Adequar classes as exceções personalizadas
            Adicionar validaçõs
            Refatorar e deixar perfomático
            Testar api no swagger
            Testes unitários

        IDEIAS
            Testes automatizados
     */
    public static void main(String[] args) {
        SpringApplication.run(ColaboreApplication.class, args);
    }

}
