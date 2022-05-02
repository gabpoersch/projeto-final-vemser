package br.com.dbc.devser.colabore.functional.tests.login;

import br.com.dbc.devser.colabore.functional.actions.LoginAction;
import br.com.dbc.devser.colabore.functional.config.ConfigEnvironment;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class ScenarioLogin extends ConfigEnvironment {

    LoginAction loginAction = new LoginAction(driver, wait);

    @Test
    public void shouldNotPassWithoutEmail() {
        driver.get("https://final-vemser.vercel.app");

        loginAction.writeInputPassword("321").clickLoginButton();

        Assertions.assertEquals("https://final-vemser.vercel.app/"
                , driver.getCurrentUrl());
    }

    @Test
    public void shouldNotPassWithoutPassword() {
        driver.get("https://final-vemser.vercel.app");

        loginAction.writeInputEmail("321@mail.com").clickLoginButton();

        Assertions.assertEquals("https://final-vemser.vercel.app/"
                , driver.getCurrentUrl());
    }
}
