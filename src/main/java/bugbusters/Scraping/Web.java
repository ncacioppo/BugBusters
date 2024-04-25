package bugbusters.Scraping;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Collection;

public class Web {

    private static final int LONG = 5;
    private static final int SHORT = 2;

    private Web(){}

    class xPath {

        private xPath(){

        }
        public static void click (WebDriver driver, String xPath){
            WebElement element = driver.findElement((By.xpath(xPath)));
            Web.click(driver, element);
        }

        public static void carefulClick (WebDriver driver, String xPath){
            WebElement element = driver.findElement((By.xpath(xPath)));
            Web.carefulClick(driver, element);
        }

        public static String getText (WebDriver driver, String xPath){
            WebElement element = driver.findElement((By.xpath(xPath)));
            return Web.getText(driver, element);
        }

        public static String getText (WebDriver driver, String xPath, Collection<String> letters){
            WebElement element = driver.findElement((By.xpath(xPath)));
            return Web.getText(driver, element, letters);
        }

        public static String getTextFast (WebDriver driver, String xPath){
            WebElement element = driver.findElement((By.xpath(xPath)));
            return Web.getTextFast(driver, element);
        }

        public static String getTextFast (WebDriver driver, String xPath, Collection<String> letters){
            WebElement element = driver.findElement((By.xpath(xPath)));
            return Web.getTextFast(driver, element, letters);
        }

        public static void clearTextBox (WebDriver driver, String xPath){
            WebElement element = driver.findElement((By.xpath(xPath)));
            Web.clearTextBox(driver, element);
        }

        public static boolean exists (WebDriver driver, String xPath){
            try {
                Web.xPath.getText(driver, xPath);
                return true;
            } catch (Exception e){
                return false;
            }
        }

        public static void type (WebDriver driver, String xPath, String content){
            WebElement element = driver.findElement((By.xpath(xPath)));
            Web.type(driver, element, content);
        }

        public static int findFrame(WebDriver driver, String xPath){
            boolean found = false;

            int size = driver.findElements(By.tagName("iframe")).size();

            int i = 0;
            while ((i<=size)&&(found == false)){
                driver.switchTo().frame(i);
                if ((driver.findElements(By.xpath(xPath)).size())>0){
                    found = true;
                }
                i++;
            }

            if (found == true){
                return i-1;
            } else{
                return -1;
            }
        }
    }

    class ID {
        private ID(){

        }

        public static void click (WebDriver driver, String ID){
            WebElement element = driver.findElement((By.id(ID)));
            Web.click(driver, element);
        }

        public static void carefulClick (WebDriver driver, String ID){
            WebElement element = driver.findElement((By.id(ID)));
            Web.carefulClick(driver, element);
        }

        public static String getText (WebDriver driver, String ID){
            WebElement element = driver.findElement((By.id(ID)));
            return Web.getText(driver, element);
        }

        public static String getText (WebDriver driver, String ID, Collection<String> letters){
            WebElement element = driver.findElement((By.id(ID)));
            return Web.getText(driver, element, letters);
        }

        public static String getTextFast (WebDriver driver, String ID){
            WebElement element = driver.findElement((By.id(ID)));
            return Web.getTextFast(driver, element);
        }

        public static String getTextFast (WebDriver driver, String ID, Collection<String> letters){
            WebElement element = driver.findElement((By.id(ID)));
            return Web.getTextFast(driver, element, letters);
        }

        public static void clearTextBox (WebDriver driver, String ID){
            WebElement element = driver.findElement((By.id(ID)));
            Web.clearTextBox(driver, element);
        }

        public static boolean exists (WebDriver driver, String ID){
            try {
                Web.ID.getText(driver, ID);
                return true;
            } catch (Exception e){
                return false;
            }
        }

        public static void type (WebDriver driver, String ID, String content){
            WebElement element = driver.findElement((By.id(ID)));
            Web.type(driver, element, content);
        }

        public static int findFrame(WebDriver driver, String ID){
            boolean found = false;

            int size = driver.findElements(By.tagName("iframe")).size();

            int i = 0;
            while ((i<=size)&&(found == false)){
                driver.switchTo().frame(i);
                if ((driver.findElements(By.id(ID)).size())>0){
                    found = true;
                }
                i++;
            }

            if (found == true){
                return i-1;
            } else{
                return -1;
            }
        }
    }

    public static RemoteWebDriver chrome(int implicitWait){
        WebDriverManager.chromedriver().setup();

        WebDriverManager.chromedriver().clearDriverCache().setup();
        WebDriverManager.chromedriver().clearResolutionCache().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*", "--headless", "--disable-gpu", "--window-size=1920,1200",
                "--ignore-certificate-errors","--disable-extensions","--no-sandbox","--disable-dev-shm-usage");

        RemoteWebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

        return driver;
    }

    public static RemoteWebDriver chromeTest(int implicitWait){
        WebDriverManager.chromedriver().setup();

        WebDriverManager.chromedriver().clearDriverCache().setup();
        WebDriverManager.chromedriver().clearResolutionCache().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        RemoteWebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

        return driver;
    }

    public static void hitEnter (WebDriver driver){
        Actions search = new Actions(driver);
        search.sendKeys(Keys.ENTER).perform();
    }

    public static boolean takeScreenshot (WebDriver driver, String savePath){
        try {
            TakesScreenshot screenShot = ((TakesScreenshot) driver);
            File sourceFile = screenShot.getScreenshotAs(OutputType.FILE);
            File saveIn = new File(savePath);
            FileUtils.copyFile(sourceFile, saveIn);
            return true;
        } catch (IOException e){
            return false;
        }
    }

    public static boolean takeScreenshot (WebDriver driver, String savePath, int zoom){
        String script = "document.body.style.zoom='"+ zoom + "%'";
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript(script);
        boolean out = Web.takeScreenshot(driver, savePath);
        js.executeScript("document.body.style.zoom='100%'");
        return out;
    }

    public static boolean takeScreenshot (WebDriver driver, String savePath, String zoom){
        String script = "document.body.style.zoom='"+ zoom + "%'";
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript(script);
        boolean out = Web.takeScreenshot(driver, savePath);
        js.executeScript("document.body.style.zoom='100%'");
        return out;
    }

    public static void toFrame (WebDriver driver, int frame){
        driver.switchTo().frame(frame);
    }

    public static void defaultContent(WebDriver driver){
        driver.switchTo().defaultContent();
    }

    public static void toWindow(WebDriver driver, int window){
        driver.switchTo().window(driver.getWindowHandles().toArray()[window].toString());
    }

    private static void click (WebDriver driver, WebElement element){
        WebDriverWait waitLong = new WebDriverWait(driver, Duration.ofSeconds(LONG));
        WebDriverWait waitShort = new WebDriverWait(driver, Duration.ofSeconds(SHORT));

        waitLong.until(ExpectedConditions.visibilityOf(element));
        waitShort.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    private static void carefulClick (WebDriver driver, WebElement element){
        WebDriverWait waitLong = new WebDriverWait(driver, Duration.ofSeconds(LONG));
        WebDriverWait waitShort = new WebDriverWait(driver, Duration.ofSeconds(SHORT));

        for (int i=0; i<10; i++){
            waitLong.until(ExpectedConditions.visibilityOf(element));
            waitShort.until(ExpectedConditions.elementToBeClickable(element));
        }

        element.click();
    }

    private static String getText (WebDriver driver, WebElement element){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT));

        wait.until(ExpectedConditions.visibilityOf(element));
        String out = element.getText();
        return out;
    }

    private static String getTextFast (WebDriver driver, WebElement element){
        String out = element.getText();
        return out;
    }

    private static String getText (WebDriver driver, WebElement element, Collection<String> letters){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT));

        wait.until(ExpectedConditions.visibilityOf(element));
        String out = element.getText();

        for (String x : letters){
            out.replace(x, "");
        }

        return out;
    }

    private static String getTextFast (WebDriver driver, WebElement element, Collection<String> letters) {
        String out = element.getText();

        for (String x : letters) {
            out.replace(x, "");
        }

        return out;
    }

    private static void clearTextBox (WebDriver driver, WebElement element){
        WebDriverWait waitLong = new WebDriverWait(driver, Duration.ofSeconds(LONG));
        WebDriverWait waitShort = new WebDriverWait(driver, Duration.ofSeconds(SHORT));

        waitLong.until(ExpectedConditions.visibilityOf(element));
        waitShort.until(ExpectedConditions.elementToBeClickable(element));

        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.BACK_SPACE);

        element.click();
    }

    private static void type (WebDriver driver, WebElement element, String content){
        WebDriverWait waitLong = new WebDriverWait(driver, Duration.ofSeconds(LONG));
        WebDriverWait waitShort = new WebDriverWait(driver, Duration.ofSeconds(SHORT));

        waitLong.until(ExpectedConditions.visibilityOf(element));
        waitShort.until(ExpectedConditions.elementToBeClickable(element));

        element.sendKeys(content);
    }
}
