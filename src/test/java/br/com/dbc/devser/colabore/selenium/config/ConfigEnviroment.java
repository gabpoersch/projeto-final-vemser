package br.com.dbc.devser.colabore.selenium.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

import java.util.concurrent.TimeUnit;

public class ConfigEnviroment {

    public static WebDriver driver;

    @BeforeClass
    public static void initConfigClass() {
        WebDriverManager.edgedriver().setup();
        System.out.println("Download path: " + WebDriverManager.edgedriver().getDownloadedDriverPath());
        System.out.println("Version: " + WebDriverManager.edgedriver().getDownloadedDriverVersion());
        driver = new EdgeDriver();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }

}
