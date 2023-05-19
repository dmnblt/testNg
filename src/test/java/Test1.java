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
    private static final String TASK_URL = "https://acmp.ru/index.asp?main=task&id_task=1";
    private static final String USERNAME  = "duman_";
    private static final String PASSWORD = "87053570703d";
    private static final String PROBLEM_SOLUTION = "import java.io.*;\n" +
            "import java.util.*;\n" +
            "public class Summ {\n" +
            "    public static void main(String[] args) throws IOException {\n" +
            "        ArrayList<Integer> numeric = new ArrayList<>();\n" +
            "        int s = 0;\n" +
            "        String str;\n" +
            "        FileReader fin = new FileReader(\"input.txt\"); // Получение чисел из файла\n" +
            "        Scanner sc = new Scanner(fin);\n" +
            "        str = sc.nextLine();\n" +
            "        StringTokenizer st = new StringTokenizer(str, \" \");\n" +
            "        while(st.hasMoreTokens()){\n" +
            "            int a = Integer.valueOf(st.nextToken());\n" +
            "            numeric.add(a);\n" +
            "        }\n" +
            "        for (Integer aNumeric : numeric) {\n" +
            "            s += aNumeric;\n" +
            "        }\n" +
            "        FileWriter countStepFinish = new FileWriter(\"output.txt\");// Запись числа в файл\n" +
            "        countStepFinish.write(String.valueOf(s));\n" +
            "        countStepFinish.close();\n" +
            "    }\n" +
            "}";

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

        driver.navigate().to(TASK_URL);

        WebElement solutionInput = driver.findElement(By.cssSelector("span"));
        solutionInput.sendKeys(PROBLEM_SOLUTION);
        WebElement submitButton = driver.findElement(By.cssSelector("input[type='submit'][value='Submit']"));
        submitButton.click();

        WebElement submissionFeedback = driver.findElement(By.cssSelector(".p-user-outbox p"));
        String feedbackText = submissionFeedback.getText();
        Assert.assertTrue(feedbackText.contains("Submission successful"), "Submission was not successful or feedback is incorrect.");
    }
}
