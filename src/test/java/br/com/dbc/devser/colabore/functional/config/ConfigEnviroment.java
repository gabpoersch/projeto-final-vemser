package br.com.dbc.devser.colabore.functional.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class ConfigEnviroment {

    public static WebDriver driver;

    @BeforeClass
    public static void initConfigClass() {
        String os = System.getProperty("os.name");
        if (os.equals("Linux")){
            WebDriverManager.chromedriver().setup();
        }else{
            WebDriverManager.edgedriver().setup();
        }
        driver = new EdgeDriver();
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }

}
