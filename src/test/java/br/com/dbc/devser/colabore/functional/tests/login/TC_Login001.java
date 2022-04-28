package br.com.dbc.devser.colabore.functional.tests.login;

import br.com.dbc.devser.colabore.functional.actions.LoginAction;
import br.com.dbc.devser.colabore.functional.config.ConfigEnvironment;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TC_Login001 extends ConfigEnvironment {

    LoginAction act = new LoginAction(driver, new WebDriverWait(driver, 8));

    @Test
    public void shouldNotPass() throws InterruptedException {
        driver.get("https://projeto-final-vem-ser-dbc-colabore-new.vercel.app");
        driver.manage().window().maximize();
        Thread.sleep(3500);
        act.writeInputEmail("123@odjksdhkahdsakom").writeInputPassword("123")
                .clickLoginButton();
        Thread.sleep(3500);

        Assertions.assertEquals("https://projeto-final-vem-ser-dbc-colabore-new.vercel.app/",
                driver.getCurrentUrl());
    }

}
