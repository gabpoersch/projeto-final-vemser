package br.com.dbc.devser.colabore.functional.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static br.com.dbc.devser.colabore.functional.pageObjects.RegisterColabore.*;

public class RegisterAction {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public RegisterAction(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 8);
    }

    public RegisterAction writeInputEmail(String text) {
        writeInput(inputEmail, text);
        return this;
    }

    public RegisterAction writeInputName(String text) {
        writeInput(inputName, text);
        return this;
    }

    public RegisterAction writeInputPassword(String text) {
        writeInput(inputPassword, text);
        writeInput(confirmPassword, text);
        return this;
    }

    public RegisterAction btnPhoto() {
        WebElement btnPhotoElement = wait.until(ExpectedConditions
                .elementToBeClickable(driver.findElement(btnPhoto)));
        btnPhotoElement.sendKeys(System.getProperty("user.dir")+"/src/main/resources/img/home.jpg");
        return this;
    }

    public RegisterAction btnRegister() {
        wait.until(ExpectedConditions
                .elementToBeClickable(driver.findElement(btnRegister))).click();
        return this;
    }

    private void writeInput(By inputBy, String text) {
        WebElement inputElement = wait.until(ExpectedConditions.visibilityOf(driver.findElement(inputBy)));
        inputElement.clear();
        inputElement.sendKeys(text);
    }
}
