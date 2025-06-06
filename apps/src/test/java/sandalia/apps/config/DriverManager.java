package sandalia.apps.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverManager {
    
	private static WebDriver driver;

    private DriverManager() {}

    /**
     *  Generate Chrome Driver
     * @return
     */
    public static WebDriver getDriver() {
        if (driver == null) {
            driver = new ChromeDriver();
            driver.manage().window().maximize();
        }
        return driver;
    }
    
    /**
     * Destroy Driver
     */
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
