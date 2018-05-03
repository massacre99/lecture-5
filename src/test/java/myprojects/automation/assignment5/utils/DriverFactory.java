package myprojects.automation.assignment5.utils;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.SkipException;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {
    /**
     *
     * @param browser Driver type to use in tests.
     * @return New instance of {@link WebDriver} object.
     */
    public static WebDriver initDriver(String browser) {
        MutableCapabilities options = getOptions(browser);

        switch (browser) {
            case "firefox":
                System.setProperty(
                        "webdriver.gecko.driver",
                        new File(DriverFactory.class.getResource("/geckodriver.exe").getFile()).getPath());
                return new FirefoxDriver(options);
            case "ie":
            case "internet explorer":
                System.setProperty(
                        "webdriver.ie.driver",
                        new File(DriverFactory.class.getResource("/IEDriverServer.exe").getFile()).getPath());;
                return new InternetExplorerDriver(options);
            case "android":
            case "headless-chrome":
            case "chrome":
            default:
                System.setProperty(
                        "webdriver.chrome.driver",
                        new File(DriverFactory.class.getResource("/chromedriver.exe").getFile()).getPath());
                return new ChromeDriver(options);
        }
    }


    public static MutableCapabilities getOptions(String browser) {
        MutableCapabilities options;

        switch (browser) {
            case "firefox":
                options = new FirefoxOptions();
                return options;
            case "ie":
            case "internet explorer":
                options = new InternetExplorerOptions().destructivelyEnsureCleanSession();
                options.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
                options.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                return options;
            case "android":
                Map<String, String> mobileEmulation = new HashMap<>();
                mobileEmulation.put("deviceName", "Galaxy S5");
                options = new ChromeOptions().setExperimentalOption("mobileEmulation", mobileEmulation);
                return options;
            case "headless-chrome":
                options = new ChromeOptions().addArguments("headless")
                        .addArguments("window-size=800x600");
                return options;
            case "chrome":
            default:
                options = new ChromeOptions();
                return options;
        }
    }

    /**
     *
     * @param browser Remote driver type to use in tests.
     * @param gridUrl URL to Grid.
     * @return New instance of {@link RemoteWebDriver} object.
     */
    public static WebDriver initDriver(String browser, String gridUrl) {
        WebDriver driver;
        try {
            driver = new RemoteWebDriver(new URL(gridUrl), getOptions(browser));
        } catch (Exception e) {
            e.printStackTrace();
            throw new SkipException("Unable to create RemoteWebDriver instance!");
        }
        return driver;
    }

}
