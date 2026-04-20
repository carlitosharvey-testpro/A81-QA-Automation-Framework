import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

public class BaseTest {

    public static WebDriver driver = null;
    public static String url = null;
    public static WebDriverWait wait = null;

    public static Actions actions = null;

    private static final ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();

    public static WebDriver getDriver(){
        return threadDriver.get();
    }

    @DataProvider(name="IncorrectLoginData")
    public Object[][] getDataFromDataProviders() {

        return new Object[][] {
                {"invalid@testpro.io", "invalidPass"},
                {"demo@testpro.io", ""},
                {"", ""}
        };
    }

    /*@BeforeSuite
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
        WebDriverManager.firefoxdriver().setup();
        WebDriverManager.safaridriver().setup();
    }*/

    @BeforeMethod
    @Parameters({"BaseURL"})
    public void launchBrowser(String BaseURL, Method method) throws MalformedURLException {
        //      Added ChromeOptions argument below to fix websocket error
        //ChromeOptions options = new ChromeOptions();
        //options.addArguments("--remote-allow-origins=*");

        //driver = new ChromeDriver(options);
        //driver = new FirefoxDriver();
        //driver = new SafariDriver();
        threadDriver.set(pickBrowser(System.getProperty("browser"), method));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        getDriver().manage().window().maximize();

        wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        actions = new Actions(getDriver());

        url = BaseURL;
        navigateToPage();
    }

    @AfterMethod
    public void tearDown(){
        threadDriver.get().close();
        threadDriver.remove();
    }

    //public void closeBrowser() {
    //    getDriver().quit();
    //}
    public  void navigateToPage() {
        getDriver().get(url);
    }

    public WebDriver pickBrowser(String browser, Method method) throws MalformedURLException {
        //DesiredCapabilities caps = new DesiredCapabilities();
        String gridURL = "http://localhost:4444";

        switch(browser){
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                return driver = new FirefoxDriver();
            case "safari":
                WebDriverManager.safaridriver().setup();
                return driver = new SafariDriver();
            // Grid Capable Browsers
            case "grid-firefox":
                //caps.setCapability("browserName", "firefox");
                //return driver = new RemoteWebDriver(URI.create(gridURL).toURL(), caps);
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                return new RemoteWebDriver(URI.create(gridURL).toURL(), firefoxOptions);
            case "grid-safari":
                //caps.setCapability("browserName", "safari");
                //return driver = new RemoteWebDriver(URI.create(gridURL).toURL(), caps);
                SafariOptions safariOptions = new SafariOptions();
                return driver = new RemoteWebDriver(URI.create(gridURL).toURL(), safariOptions);
            case "grid-chrome":
                //caps.setCapability("browserName", "chrome");
                //return driver = new RemoteWebDriver(URI.create(gridURL).toURL(), caps);
                ChromeOptions chromeOptions = new ChromeOptions();
                return driver = new RemoteWebDriver(URI.create(gridURL).toURL(), chromeOptions);
            // Cloud Execution
            case "cloud-chrome":
                return lambdaTest(method);
            case "cloud-firefox":
                return lambdaTestFireFox(method);
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions browserOptions = new ChromeOptions();
                browserOptions.addArguments("--remote-allow-origins=*");
                return driver = new ChromeDriver(browserOptions);
        }
    }

    public WebDriver lambdaTest(Method method) throws MalformedURLException{
        String hubURL = "https://hub.lambdatest.com/wd/hub";

        ChromeOptions browserOptions = new ChromeOptions();
        browserOptions.setPlatformName("Windows 10");
        browserOptions.setBrowserVersion("dev");
        HashMap<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("username", "carlitostestpro");
        ltOptions.put("accessKey", "LT_KnKGZEFkqN0iV2xtebmkp7CZcb5XENrzCNvgOk2WK2dA5FV");
        ltOptions.put("project", "Untitled");
        // Build Name
        ltOptions.put("build", "TestPro Demo Suite - " + LocalDate.now());
        // Test Names inside the Class
        String testName = method.getDeclaringClass().getSimpleName() + " - " + method.getName();
        ltOptions.put("name", testName);

        ltOptions.put("selenium_version", "4.0.0");
        ltOptions.put("w3c", true);
        browserOptions.setCapability("LT:Options", ltOptions);

        return new RemoteWebDriver(new URL(hubURL), browserOptions);
    }

    public WebDriver lambdaTestFireFox(Method method) throws MalformedURLException{
        String hubURL = "https://hub.lambdatest.com/wd/hub";

        FirefoxOptions browserOptions = new FirefoxOptions();
        browserOptions.setPlatformName("Windows 10");
        browserOptions.setBrowserVersion("dev");
        HashMap<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("username", "carlitostestpro");
        ltOptions.put("accessKey", "LT_KnKGZEFkqN0iV2xtebmkp7CZcb5XENrzCNvgOk2WK2dA5FV");
        ltOptions.put("project", "Untitled");

        // Build Name
        ltOptions.put("build", "TestPro Demo Suite - " + LocalDate.now());
        // Test Names inside the Class
        String testName = method.getDeclaringClass().getSimpleName() + " - " + method.getName();
        ltOptions.put("name", testName);

        ltOptions.put("w3c", true);
        browserOptions.setCapability("LT:Options", ltOptions);

        return new RemoteWebDriver(new URL(hubURL), browserOptions);
    }

}