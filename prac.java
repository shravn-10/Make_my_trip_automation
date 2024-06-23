import java.time.Duration;
//import java.util.List;
import java.util.Set;
//import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileReader;
import java.io.BufferedReader;

//import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.print.PrintOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
//import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Pdf;
import org.openqa.selenium.TakesScreenshot;

//Parallel execution

public class prac implements Runnable{

    WebDriver driver;
    int num = 0;
    int length;
    String fileName;
    Thread agent;
    prac() {}
    prac(String fileName) {
        this.fileName = fileName;
        agent = new Thread(this, "Testing Agent for "+fileName);
    }
    // Data driven

    public void run() {
        System.out.println(agent.getName());
        BufferedReader buff;
        try {
            buff = new BufferedReader(new FileReader(fileName));
            String record;
            while ((record = buff.readLine()) != null) {
                String source = record.split(",")[0];
                String destination = record.split(",")[1];
                String travelClass = record.split(",")[2];
                try {
                    launchBrowser(source, destination, travelClass);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            buff.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Window handle

    private void windowOperations() throws InterruptedException {
        // Get the WebDriver.Window interface
        WebDriver.Window window = driver.manage().window();

        // Maximize the browser window
        window.maximize();
        Thread.sleep(1000);
        // Set the browser window size
        window.setSize(new org.openqa.selenium.Dimension(800, 600));
        Thread.sleep(1000);
        // Get the browser window position
        System.out.println("Window position: " + window.getPosition());
        Thread.sleep(1000);
        window.maximize();
        Thread.sleep(2000);
    }
    // cookies

    private void cookiesOperations() throws InterruptedException {
        //Options
        WebDriver.Options options = driver.manage();

        // Perform operations using WebDriver.Options
        // Retrieve and print cookies
        Set<Cookie> cookies = options.getCookies();
        System.out.println("Number of cookies: " + cookies.size());

        for (Cookie cookie : cookies) {
            System.out.println("Cookie: " + cookie.getName() + " = " + cookie.getValue());
        }
        options.deleteAllCookies();
        options.timeouts().implicitlyWait(Duration.ofSeconds(10));
        System.err.println("Deleted all Cookies");
    }
    //ACTUAL TESTING
    private void enterCity(String cityName) throws InterruptedException {
        driver.findElement(By.xpath("//*[@id=\"top-banner\"]/div[2]/div/div/div/div[2]/div/div[1]/label")).click();

        Thread.sleep(2000);
        
        driver.findElement(
            By.xpath("//*[@id='root']/div/div[2]/div/div/div/div[2]/div/div[1]/div[1]/div/div/div/input"))
            .sendKeys(cityName);
        // wait until element is not visible
        Thread.sleep(2000);
        
        int length = driver.findElements(By.cssSelector("ul.react-autosuggest__suggestions-list > li")).size();
        System.out.println("Length of the list is: " + length);

        Thread.sleep(1000);

        // define a loop to iterate through the list
        for (int i = 0; i < length; i++) {
            // find the element using index
            String element = driver.findElements(By.cssSelector(
                            "ul.react-autosuggest__suggestions-list > li > div > div > p.searchedResult.font14.darkText > span"))
                    .get(i)
                    .getText();
            System.out.println("Element is: " + element);
            // check if the element is equal to the search term
            if (element.equals(cityName)) {
                // click on the element
                driver.findElements(By.cssSelector(
                                "ul.react-autosuggest__suggestions-list > li > div > div > p.searchedResult.font14.darkText > span"))
                        .get(i).click();
                break;
            }
        }
    }

    private void selectCity(String destination) throws InterruptedException {
        driver.findElement(By.xpath("//*[@id='root']/div/div[2]/div/div/div/div[2]/div/div[2]/label/span")).click();
        
        Thread.sleep(2000);

        driver.findElement(
                        By.xpath("//*[@id='root']/div/div[2]/div/div/div/div[2]/div/div[2]/div[1]/div/div/div/input"))
                .sendKeys(destination);
        // wait until element is not visible
        Thread.sleep(2000);


        length = driver.findElements(By.cssSelector("ul.react-autosuggest__suggestions-list > li")).size();
        System.out.println("Length of the list is: " + length);

        Thread.sleep(1000);

        for (int i = 0; i < length; i++) {
            // find the element using index
            String element = driver.findElements(By.cssSelector(
                            "ul.react-autosuggest__suggestions-list > li > div > div > p.searchedResult.font14.darkText > span"))
                    .get(i)
                    .getText();
            System.out.println("Element is: " + element);
            // check if the element is equal to the search term
            if (element.equals(destination)) {
                // click on the element
                driver.findElements(By.cssSelector(
                                "ul.react-autosuggest__suggestions-list > li > div > div > p.searchedResult.font14.darkText > span"))
                        .get(i).click();
                break;
            }
        }
    }

    private void selectDate() throws InterruptedException {
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@id='root']/div/div[2]/div/div/div/div[2]/div/div[3]")).click();

        Thread.sleep(1000);
        driver.findElement(By.xpath(
                "//*[@id='root']/div/div[2]/div/div/div/div[2]/div/div[3]/div[1]/div/div/div/div[2]/div/div[2]/div[1]/div[3]/div[3]/div[6]"))
                .click();

    }

    private void selectTravelClass(String travelClass) throws InterruptedException {
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@id='root']/div/div[2]/div/div/div/div[2]/div/div[4]")).click();
        length = driver.findElements(By.cssSelector("ul.travelForPopup > li")).size();
        System.out.println("Length of the list is: " + length);
        for (int i = 0; i < length; i++) {
            String element = driver.findElements(By.cssSelector("ul.travelForPopup > li")).get(i).getText();
            System.out.println("Element is: " + element);
            if (element.equals(travelClass)) {
                driver.findElements(By.cssSelector("ul.travelForPopup > li")).get(i).click();
                break;
            }
        }
    }

    private void clickSearchButton() throws InterruptedException {
        Thread.sleep(4000);
        driver.findElement(By.xpath("//*[@id=\"top-banner\"]/div[2]/div/div/div/div[2]/p/a")).click();
        Thread.sleep(4000);
    }

    private void navigateBack() throws InterruptedException {
        driver.navigate().to("https://www.makemytrip.com/railways/");
        Thread.sleep(4000);
    }

    //SCREENSHOT

    synchronized public void takeScreenshot(String fileName) throws IOException {
    
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);
            
            File destinationFile = new File(fileName);
            Files.copy(sourceFile.toPath(), destinationFile.toPath());
            
            System.out.println("Screenshot saved: " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error taking screenshot: " + e.getMessage());
            throw e; // Re-throw the exception after handling it
        }
    }

    public void quitDriver() {
        // Quit the driver when needed
        if (driver != null) {
            driver.quit();
        }
    }

    public void launchBrowser(String source, String destination, String travelClass) throws InterruptedException {
        
        driver = new ChromeDriver();
        driver.get("https://www.makemytrip.com/railways/");
		// driver.manage().window().maximize();
        
        // driver.navigate().refresh();
        // Thread.sleep(3000);
		
        // Check if you have landed in the correct page
        // Print the URL and Title of the Page
        String title = driver.getTitle();
        System.out.println("Title of the page is: " + title);
        String url = driver.getCurrentUrl();
        System.out.println("URL of the page is: " + url);

        windowOperations();
        Thread.sleep(2000);
        enterCity(source);
        selectCity(destination);
        selectDate();
        selectTravelClass(travelClass);
        clickSearchButton();
        Thread.sleep(2000);

        cookiesOperations();
        Thread.sleep(2000);
        navigateBack();
        
        try {
            
            takeScreenshot("screenshot_"+fileName.substring(4, 9)+"_"+num+".png");
            num++;
        } catch (IOException e) {
            System.err.println("Error taking screenshot: " + e.getMessage());
        }
        driver.quit();
    }

    //HIGHLIGHT

    private void highlightItems(WebDriver driver) throws InterruptedException, IOException {
        // driver = new ChromeDriver();
        driver.get("https://www.makemytrip.com/railways/");
        // driver.manage().window().maximize();

        Thread.sleep(20000);

        WebElement offers = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/main/main/div[2]/div[1]/h2"));
        WebElement ticket = driver.findElement((By.xpath("/html/body/div[1]/div/div[2]/div/div/div/div[1]/div/h1")));
        
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'background: orange; border: 2px solid red;');", ticket);
        js.executeScript("arguments[0].setAttribute('style', 'background: orange; border: 2px solid red;');", offers);

        System.out.println("Elemeaqwnts highlighted");
        Thread.sleep(20000);
        downloadPage(driver);
        System.out.println("Page downloaded");

        // throw new UnsupportedOperationException("Unimplemented method 'highlight'");
    }

    //DOWNLOAD PDF

    private void downloadPage(WebDriver driver) throws IOException {
        Path printPage = Paths.get("MMT_homepage.pdf");
        // driver.get("https://www.makemytrip.com/railways/");
        Pdf print = ((RemoteWebDriver) driver).print(new PrintOptions());
        Files.write(printPage, OutputType.BYTES.convertFromBase64Png(print.getContent()));
    }
    
    public static void main(String[] args) throws InterruptedException, FileNotFoundException, IOException {
        prac testerOne = new prac("src/test1.csv");
        prac testerTwo = new prac("src/test2.csv");
        prac testerThree = new prac("src/test3.csv");
        prac obj = new prac();
        System.setProperty("webdriver.chrome.driver", "chromedriver1.exe");
        
        
        testerOne.agent.start();
        testerTwo.agent.start();
        testerThree.agent.start();
        
        testerOne.agent.join();
        testerTwo.agent.join();
        testerThree.agent.join();

        System.out.println("Parallel Testing Execution completed, printing the homepage...");
        obj.driver = new ChromeDriver();
        Thread.sleep(2000);
        obj.highlightItems(obj.driver);
        
        Thread.sleep(2000);
        obj.downloadPage(obj.driver);

        Thread.sleep(2000);
        obj.quitDriver();
    }
}

