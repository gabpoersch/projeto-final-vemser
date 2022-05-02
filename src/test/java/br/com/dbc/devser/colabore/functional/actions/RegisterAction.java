package br.com.dbc.devser.colabore.functional.actions;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static br.com.dbc.devser.colabore.functional.pageObjects.RegisterColabore.*;

@RequiredArgsConstructor
public class RegisterAction {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public RegisterAction writeInputEmail(String text) {
        CommonActions.write(driver, wait, text, inputEmail);
        return this;
    }

    public RegisterAction writeInputName(String text) {
        CommonActions.write(driver, wait, text, inputName);
        return this;
    }

    public RegisterAction writeInputPassword(String text) {
        CommonActions.write(driver, wait, text, inputPassword);
        CommonActions.write(driver, wait, text, confirmPassword);
        return this;
    }

    public RegisterAction btnPhoto() {
        wait.until(ExpectedConditions
                        .elementToBeClickable(driver.findElement(btnPhoto)))
                .sendKeys(System.getProperty("user.dir") + "/src/main/resources/img/home.jpg");
        return this;
    }

    public RegisterAction btnRegister() {
        wait.until(ExpectedConditions
                .elementToBeClickable(driver.findElement(btnRegister))).click();
        return this;
    }
}
