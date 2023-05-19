import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class Test1 {
    private WebDriver driver;
    private static final String BASE_URL = "https://acmp.ru/";
    private static final String TASK_URL = "https://acmp.ru/index.asp?main=task&id_task=1234";
    private static final String USERNAME  = "duman_";
    private static final String PASSWORD = "87053570703d";

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get(BASE_URL);
        WebElement usernameInput = driver.findElement(By.name("lgn"));
        WebElement passwordInput = driver.findElement(By.name("password"));
        usernameInput.sendKeys(USERNAME);
        passwordInput.sendKeys(PASSWORD);
        WebElement loginButton = driver.findElement(By.cssSelector("input[type='submit'][value='Ok']"));
        loginButton.click();
        WebElement lang = driver.findElement(By.linkText("[English]"));
        lang.click();
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testRegistration() {
        driver.get(BASE_URL);
        WebElement registrationButton = driver.findElement(By.linkText("[регистрация]"));
        registrationButton.click();
        Assert.assertNotEquals(BASE_URL, driver.getCurrentUrl());
    }

    @Test(priority = 1)
    public void testLoginWithValidCredentials() {
        Assert.assertEquals(BASE_URL, driver.getCurrentUrl());
    }

    @Test(priority = 2)
    public void testProblemSearchByValidId() {
        driver.get(BASE_URL);

        WebElement searchInput = driver.findElement(By.name("str"));
        searchInput.sendKeys("Edinitsy");

        WebElement searchButton = driver.findElement(By.cssSelector("input[type='submit'][value='Ok']"));
        searchButton.click();

        WebElement problemTitle = driver.findElement(By.cssSelector(".problem-name a"));
        String actualProblemId = problemTitle.getText().trim();
        Assert.assertEquals(actualProblemId, "Edinitsy", "Search results do not display the correct problem based on the ID.");
    }

    @Test(priority = 3)
    public void testAccessProblemStatement() {
        driver.get(BASE_URL);

        driver.navigate().to(TASK_URL);

        WebElement problemStatement = driver.findElement(By.cssSelector(".problem-text"));
        String statementText = problemStatement.getText();
        Assert.assertTrue(statementText.contains("Problem statement"), "Problem statement is not displayed correctly.");
    }

    @Test(priority = 4)
    public void testSolutionSubmission() {
        driver.get(BASE_URL);
        WebElement usernameInput = driver.findElement(By.name("username"));
        WebElement passwordInput = driver.findElement(By.name("password"));
        usernameInput.sendKeys(USERNAME);
        passwordInput.sendKeys(PASSWORD);
        WebElement loginButton = driver.findElement(By.cssSelector("input[type='submit'][value='Log in']"));
        loginButton.click();

        driver.navigate().to(TASK_URL);

        WebElement solutionInput = driver.findElement(By.cssSelector(".sub a"));
        solutionInput.sendKeys("<>");
        WebElement submitButton = driver.findElement(By.cssSelector("input[type='submit'][value='Submit']"));
        submitButton.click();

        WebElement submissionFeedback = driver.findElement(By.cssSelector(".p-user-outbox p"));
        String feedbackText = submissionFeedback.getText();
        Assert.assertTrue(feedbackText.contains("Submission successful"), "Submission was not successful or feedback is incorrect.");
    }
}
