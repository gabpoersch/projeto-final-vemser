package br.com.dbc.devser.colabore.functional.pageObjects;

import org.openqa.selenium.By;

public class LoginColabore {

    public static By inputEmail = By.id("login");
    public static By inputPassword = By.id("password");
    public static By buttonLogin = By.cssSelector("button[type='submit']");
    public static By linkRegister = By.linkText("Não possuo cadastro");

}
