package br.com.dbc.devser.colabore.functional.actions;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static br.com.dbc.devser.colabore.functional.pageObjects.LoginColabore.*;

@RequiredArgsConstructor
public class LoginAction {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginAction writeInputEmail(String text) {
        CommomActions.write(driver, wait, text, inputEmail);
        return this;
    }

    public LoginAction writeInputPassword(String text) {
        CommomActions.write(driver, wait, text, inputPassword);
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
}
