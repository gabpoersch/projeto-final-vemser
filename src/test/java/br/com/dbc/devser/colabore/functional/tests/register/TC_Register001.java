package br.com.dbc.devser.colabore.functional.tests.register;

import br.com.dbc.devser.colabore.functional.actions.HomeAction;
import br.com.dbc.devser.colabore.functional.actions.LoginAction;
import br.com.dbc.devser.colabore.functional.actions.NewFundraiserAction;
import br.com.dbc.devser.colabore.functional.actions.RegisterAction;
import br.com.dbc.devser.colabore.functional.config.ConfigEnvironment;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class TC_Register001 extends ConfigEnvironment {

    WebDriverWait wait = new WebDriverWait(driver, 8);
    LoginAction actLogin = new LoginAction(driver, wait);
    RegisterAction actRegister = new RegisterAction(driver, wait);
    HomeAction homeAction = new HomeAction(driver, wait);
    NewFundraiserAction newFundraiser = new NewFundraiserAction(driver, wait);

    @Test
    public void successfullyRegistration() throws InterruptedException {
        driver.get("https://projeto-final-vem-ser-dbc-colabore-new.vercel.app");

        driver.manage().window().maximize();

        actLogin.clickLinkRegister();

        actRegister.writeInputEmail("54321@dbccompany.com.br")
                .writeInputName("Stephen Wells-O'Shaugnessy Marcus")
                .writeInputPassword("@!1234Aa123")
                .btnPhoto().btnRegister();

        wait.until(ExpectedConditions.urlToBe("https://projeto-final-vem-ser-dbc-colabore-new.vercel.app/campanhas"));

        //1º Verificação
        Assertions.assertEquals("https://projeto-final-vem-ser-dbc-colabore-new.vercel.app/campanhas", driver.getCurrentUrl());

        homeAction.createFundraiser();

        wait.until(ExpectedConditions.urlToBe("https://projeto-final-vem-ser-dbc-colabore-new.vercel.app/create-campanhas"));

        newFundraiser.writeInputTitle("Teste automatizado")
                .writeInputGoal("2500")
                .clickAutomaticClose()
                .writeInputDate("12/03/2023")
                .clickBtnCoverPhoto()
                .writeInputCategories(List.of("automatiza", "selenium"))
                .writeDescription("Descrição")
                .clickBtnSave();

        wait.until(ExpectedConditions.urlToBe("https://projeto-final-vem-ser-dbc-colabore-new.vercel.app/campanhas"));

        homeAction.logout();
//
    }

}
