package br.com.dbc.devser.colabore.functional.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommomActions {

    public static void write (WebDriver driver, WebDriverWait wait, String text, By inputBy){
        WebElement inputElement = wait.until(ExpectedConditions.visibilityOf(driver.findElement(inputBy)));
        inputElement.clear();
        inputElement.sendKeys(text);
    }
}
