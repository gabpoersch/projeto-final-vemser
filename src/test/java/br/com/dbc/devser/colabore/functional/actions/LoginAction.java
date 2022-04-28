package br.com.dbc.devser.colabore.functional.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static br.com.dbc.devser.colabore.functional.pageObjects.LoginColabore.*;
import static br.com.dbc.devser.colabore.functional.pageObjects.RegisterColabore.btnRegister;

public class LoginAction {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginAction(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 8);
    }

    public LoginAction writeInputEmail(String text) {
        writeInput(inputEmail, text);
        return this;
    }

    public LoginAction writeInputPassword(String text) {
        writeInput(inputPassword, text);
        return this;
    }

    public LoginAction clickLoginButton() {
        wait.until(ExpectedConditions
                .elementToBeClickable(driver.findElement(buttonLogin))).click();
        return this;
    }

    public LoginAction clickLinkRegister() {
        wait.until(ExpectedConditions
                .elementToBeClickable(driver.findElement(linkRegister))).click();
        return this;
    }

//    private void writeInput(By inputBy, String text) {
//        WebElement inputElement = driver.findElement(inputBy);
//        inputElement.clear();
//        inputElement.sendKeys(text);
//    }

    private void writeInput(By inputBy, String text) {
        WebElement inputElement = wait.until(ExpectedConditions.visibilityOf(driver.findElement(inputBy)));
        inputElement.clear();
        inputElement.sendKeys(text);
    }
}
