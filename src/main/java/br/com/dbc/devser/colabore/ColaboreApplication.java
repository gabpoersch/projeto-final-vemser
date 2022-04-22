package br.com.dbc.devser.colabore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ColaboreApplication {
    /*
        TODO:
            - Trocar lógica de categorias
            - Trocar lógica da foto
            - No método de salvar fundraiser, adicionar um save para categorias (verificar se já existe)
            - Verificar se já existe um email no cadastro de usuário
            - Retirar a senha do UserDto, pois ele retorna na listagem das campanhas
            - Logs
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
