package br.com.dbc.devser.colabore.functional.tests.register;

import br.com.dbc.devser.colabore.functional.actions.RegisterAction;
import br.com.dbc.devser.colabore.functional.config.ConfigEnvironment;
import org.junit.Test;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TC_Register001 extends ConfigEnvironment {

    RegisterAction act = new RegisterAction(driver, new WebDriverWait(driver, 8));

    @Test
    public void registerPass () throws InterruptedException {
        driver.get("https://projeto-final-vem-ser-dbc-colabore-new.vercel.app/register");
        driver.manage().window().maximize();
        act.writeInputEmail("joao@dbccompany.com.br")
                .writeInputName("joao")
                .writeInputPassword("ahdsagjdahgdj@#1212")
                .btnPhoto()
                .btnRegister();
        Thread.sleep(4000);
    }
}
