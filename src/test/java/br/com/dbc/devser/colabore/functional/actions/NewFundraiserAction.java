package br.com.dbc.devser.colabore.functional.actions;

import br.com.dbc.devser.colabore.functional.pageObjects.NewFundraiser;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static br.com.dbc.devser.colabore.functional.pageObjects.NewFundraiser.*;

@RequiredArgsConstructor
public class NewFundraiserAction {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public NewFundraiserAction writeInputTitle(String text) {
        CommomActions.write(driver, wait, text, inputTitle);
        return this;
    }

    public NewFundraiserAction writeInputGoal(String text) {
        CommomActions.write(driver, wait, text, inputGoal);
        return this;
    }

    public NewFundraiserAction clickAutomaticClose() {
        WebElement ele = driver.findElement(checkAutomaticClose);
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("arguments[0].click()", ele);
//        driver.findElement(checkAutomaticClose).click();
        return this;
    }

    public NewFundraiserAction writeInputDate(String textDate) {
        WebElement ele = driver.findElement(inputDate);
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("arguments[0].setAttribute('value', '25/05/2022')", ele);
        return this;
    }

    public NewFundraiserAction clickBtnCoverPhoto() {
        WebElement btnPhotoElement = wait.until(ExpectedConditions
                .elementToBeClickable(driver.findElement(btnCoverPhoto)));
        btnPhotoElement.sendKeys(System.getProperty("user.dir") + "/src/main/resources/img/doacao-foto.png");
        return this;
    }

    public NewFundraiserAction writeInputCategories(List<String> categories) {
        WebElement inputCreateCategories = wait.until(ExpectedConditions
                .visibilityOf(driver.findElement(inputCategories)));
        Actions act = new Actions(driver);
        for (String category : categories) {
            act.sendKeys(inputCreateCategories, category).sendKeys(Keys.ENTER);
        }
        return this;
    }

    public NewFundraiserAction writeDescription(String text) {
        CommomActions.write(driver, wait, text, textArea);
        return this;
    }

    public NewFundraiserAction clickBtnBack() {
        driver.findElement(buttonBackFundraiser).click();
        return this;
    }

    public NewFundraiserAction clickBtnSave() {
        driver.findElement(buttonSaveFundraiser).click();
        return this;
    }

}
