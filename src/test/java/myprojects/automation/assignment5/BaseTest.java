package myprojects.automation.assignment5;

import myprojects.automation.assignment5.utils.DriverFactory;
import myprojects.automation.assignment5.utils.logging.EventHandler;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.util.concurrent.TimeUnit;

/**
 * Base script functionality, can be used for all Selenium scripts.
 * TODO environment : Chrome 64 (driver 2.35), FF 54 (driver 0.17.0), IE 11 (driver 3.3.0.0)
 */
public abstract class BaseTest {
    protected EventFiringWebDriver driver;
    protected GeneralActions actions;
    protected boolean isMobileTesting;

    /**
     * Prepares {@link WebDriver} instance with timeout and browser window configurations.
     *
     * Driver type is based on passed parameters to the automation project,
     * creates {@link ChromeDriver} instance by default.
     *
     */
    @BeforeClass
    @Parameters({"selenium.browser", "selenium.grid"})
    public void setUp(@Optional("headless-chrome") String browser, @Optional("") String gridUrl) {
        WebDriver tempDriver;
        if (gridUrl.equals("")) {
            tempDriver = DriverFactory.initDriver(browser);
        }
        else {
            tempDriver = DriverFactory.initDriver(browser,gridUrl);
        }

        driver = new EventFiringWebDriver(tempDriver);
        driver.register(new EventHandler());

        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        // unable to maximize window in mobile mode
        if (!isMobileTesting(browser))
            driver.manage().window().maximize();

        isMobileTesting = isMobileTesting(browser);

        actions = new GeneralActions(driver);
    }

    /**
     * Closes driver instance after test class execution.
     */
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     *
     * @return Whether required browser displays content in mobile mode.
     */
    private boolean isMobileTesting(String browser) {
        switch (browser) {
            case "android":
                return true;
            case "firefox":
            case "ie":
            case "internet explorer":
            case "headless-chrome":
            case "chrome":
            default:
                return false;
        }
    }
}
