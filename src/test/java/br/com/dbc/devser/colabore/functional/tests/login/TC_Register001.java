package br.com.dbc.devser.colabore.functional.tests.login;

import br.com.dbc.devser.colabore.functional.actions.LoginAction;
import br.com.dbc.devser.colabore.functional.actions.RegisterAction;
import br.com.dbc.devser.colabore.functional.config.ConfigEnvironment;
import org.junit.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TC_Register001 extends ConfigEnvironment {

    WebDriverWait wait = new WebDriverWait(driver, 8);
    LoginAction actLogin = new LoginAction(driver, wait);
    RegisterAction actRegister = new RegisterAction(driver, wait);

    @Test
    public void successfullyRegistration() throws InterruptedException {
        driver.get("https://projeto-final-vem-ser-dbc-colabore-new.vercel.app");
        driver.manage().window().maximize();
        actLogin.clickLinkRegister();
        actRegister.writeInputEmail("54321@dbccompany.com.br")
                .writeInputName("Gab Poersch")
                .writeInputPassword("@12345Aa")
                .btnPhoto();
        Thread.sleep(1000);
        actRegister.btnRegister();
//        Thread.sleep(4000);
        wait.until(ExpectedConditions.urlToBe("https://projeto-final-vem-ser-dbc-colabore-new.vercel.app/"));

    }

}
