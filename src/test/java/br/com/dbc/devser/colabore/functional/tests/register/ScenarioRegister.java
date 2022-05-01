package br.com.dbc.devser.colabore.functional.tests.register;

import br.com.dbc.devser.colabore.functional.actions.HomeAction;
import br.com.dbc.devser.colabore.functional.actions.LoginAction;
import br.com.dbc.devser.colabore.functional.actions.NewFundraiserAction;
import br.com.dbc.devser.colabore.functional.actions.RegisterAction;
import br.com.dbc.devser.colabore.functional.config.ConfigEnvironment;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class ScenarioRegister extends ConfigEnvironment {

    LoginAction actLogin = new LoginAction(driver, wait);
    RegisterAction actRegister = new RegisterAction(driver, wait);
    HomeAction homeAction = new HomeAction(driver, wait);
    NewFundraiserAction newFundraiserAction = new NewFundraiserAction(driver, wait);

    @Test
    public void shouldRequireModelEmail() throws InterruptedException {
        driver.get("https://projeto-final-vem-ser-dbc-colabore-new.vercel.app");

        driver.manage().window().maximize();

        actLogin.clickLinkRegister();

        actRegister.writeInputEmail("testandodominio@gmail.com")
                .writeInputName("Teste");

        WebElement span = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/form/div[1]/span"))));

        Assertions.assertEquals("Email incorreto!", span.getText());
    }

    @Test
    public void shouldPassModelEmail() {
        driver.get("https://projeto-final-vem-ser-dbc-colabore-new.vercel.app");

        driver.manage().window().maximize();

        actLogin.clickLinkRegister();

        actRegister.writeInputEmail("testandodominio@dbccompany.com.br")
                .writeInputName("Teste");

        WebElement divInput = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/form/div[1]"))));

        Assertions.assertEquals("2",divInput.getAttribute("childElementCount"));
    }

    @Test
    public void successfullyRegistration() throws InterruptedException {
        driver.get("https://projeto-final-vem-ser-dbc-colabore-new.vercel.app");

        driver.manage().window().maximize();

        actLogin.clickLinkRegister();

        //Tempo de apresentação
        Thread.sleep(3000);

        actRegister.writeInputEmail("54321@dbccompany.com.br")
                .writeInputName("Stephen Wells")
                .writeInputPassword("@!1234Aa123")
                .btnPhoto()
                .btnRegister();

        wait.until(ExpectedConditions.urlToBe("https://projeto-final-vem-ser-dbc-colabore-new.vercel.app/campanhas"));

        //1º Verificação
        Assertions.assertEquals("https://projeto-final-vem-ser-dbc-colabore-new.vercel.app/campanhas", driver.getCurrentUrl());

        homeAction.clickButtonCreateFundraiser();

        wait.until(ExpectedConditions.urlToBe("https://projeto-final-vem-ser-dbc-colabore-new.vercel.app/create-campanhas"));

        //Tempo de apresentação
        Thread.sleep(3000);

        newFundraiserAction.writeInputTitle("Teste automatizado")
                .writeInputGoal("2500")
                .clickAutomaticClose()
                .writeInputDate("12/03/2023")
                .clickBtnCoverPhoto()
                .writeInputCategories(List.of("automatiza", "selenium"))
                .writeDescription("Descrição")
                .clickBtnSave();

        wait.until(ExpectedConditions.urlToBe("https://projeto-final-vem-ser-dbc-colabore-new.vercel.app/campanhas"));

        homeAction.clickMyFundraisers();

        //Tempo de apresentação
        Thread.sleep(8000);

        WebElement divMyFundraisers = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"root\"]/div[3]/div[1]"))));

        //Assegura de que criou a campanha
        Assertions.assertEquals("1", divMyFundraisers.getAttribute("childElementCount"));

        homeAction.logout();
    }

}
