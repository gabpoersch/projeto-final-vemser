package br.com.dbc.devser.colabore.functional.tests.login;

import br.com.dbc.devser.colabore.functional.actions.LoginAction;
import br.com.dbc.devser.colabore.functional.config.ConfigEnvironment;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TC_Login001 extends ConfigEnvironment {

    WebDriverWait wait = new WebDriverWait(driver, 8);
    LoginAction loginAction = new LoginAction(driver, wait);

    @Test
    public void shouldNotPassWithoutEmail() {
        driver.get("https://projeto-final-vem-ser-dbc-colabore-new.vercel.app");

        loginAction.writeInputPassword("321").clickLoginButton();

        Assertions.assertEquals("https://projeto-final-vem-ser-dbc-colabore-new.vercel.app"
                , driver.getCurrentUrl());
    }

    @Test
    public void shouldNotPassWithoutPassword() {
        driver.get("https://projeto-final-vem-ser-dbc-colabore-new.vercel.app");


    }
}
