package br.com.dbc.devser.colabore.functional.actions;

import static br.com.dbc.devser.colabore.functional.pageObjects.HomeColabore.*;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static br.com.dbc.devser.colabore.functional.pageObjects.LoginColabore.*;

@RequiredArgsConstructor
public class HomeAction {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public HomeAction fundraiserHome() {
        wait.until(ExpectedConditions
                .elementToBeClickable(driver.findElement(exploreButton))).click();
        return this;
    }

    public HomeAction writeFundraiserToBeFound (String text) {
        CommomActions.write(driver, wait, text, searchBar);
        return this;
    }

    public HomeAction createFundraiser() {
        wait.until(ExpectedConditions
                .elementToBeClickable(driver.findElement(createButton))).click();
        return this;
    }

    public HomeAction logout() {
        wait.until(ExpectedConditions
                .elementToBeClickable(driver.findElement(logoutButton))).click();
        return this;
    }
//
//    public HomeAction selectCategory() {
//        wait.until(ExpectedConditions
//                .elementToBeClickable(driver.findElement(categorySelectionButton))).click();
//    }



}
