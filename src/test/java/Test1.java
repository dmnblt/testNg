import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class Test1 {
    private WebDriver driver;
    private static final String BASE_URL = "https://www.linkedin.com";

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void loginToLinkedIn() {
        driver.get(BASE_URL);

        WebElement signInButton = driver.findElement(By.linkText("Sign in"));
        signInButton.click();
        WebElement usernameInput = driver.findElement(By.id("username"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        usernameInput.sendKeys("dumanb");
        passwordInput.sendKeys("{password}");

        WebElement signInButton2 = driver.findElement(By.xpath("//button[@type='submit']"));
        signInButton2.click();
        WebElement profileDropdown = driver.findElement(By.id("profile-nav-item"));
        Assert.assertTrue(profileDropdown.isDisplayed(), "Login failed. Profile dropdown not found.");
    }

    @Test
    public void searchOnLinkedIn() {
        driver.get(BASE_URL);
        WebElement searchInput = driver.findElement(By.name("keywords"));
        searchInput.sendKeys("example search term");
        WebElement searchDropdown = driver.findElement(By.id("search-dropdown-select"));
        searchDropdown.click();
        WebElement peopleOption = driver.findElement(By.xpath("//option[@value='PEOPLE']"));
        peopleOption.click();
        WebElement searchButton = driver.findElement(By.xpath("//button[@aria-label='Search']"));
        searchButton.click();

        WebElement searchResults = driver.findElement(By.id("search-results-container"));
        Assert.assertTrue(searchResults.isDisplayed(), "Search results not found.");
    }

    @Test
    public void searchCustomerOnLinkedIn() {
        driver.get(BASE_URL);

        WebElement searchInput = driver.findElement(By.name("keywords"));
        searchInput.sendKeys("John Doe");

        WebElement searchDropdown = driver.findElement(By.cssSelector("button.search-global-typeahead__button"));
        searchDropdown.click();
        WebElement peopleOption = driver.findElement(By.xpath("//button[text()='People']"));
        peopleOption.click();

        WebElement searchButton = driver.findElement(By.cssSelector("button.search-global-typeahead__button"));
        searchButton.click();

        WebElement searchResults = driver.findElement(By.cssSelector("div.search-results__list"));
        Assert.assertTrue(searchResults.isDisplayed(), "Search results not found.");
    }

    @Test
    public void connectWithUserOnLinkedIn() {
        driver.get(BASE_URL);
        WebElement searchInput = driver.findElement(By.name("keywords"));
        searchInput.sendKeys("John Doe");
        WebElement searchButton = driver.findElement(By.cssSelector("button.search-typeahead-v2__button"));
        searchButton.click();
        WebElement userProfileLink = driver.findElement(By.xpath("//span[contains(text(), 'John Doe')]"));
        userProfileLink.click();

        WebElement connectButton = driver.findElement(By.xpath("//button[contains(text(), 'Connect')]"));
        connectButton.click();

        WebElement connectionTypeDropdown = driver.findElement(By.cssSelector("li.mn-connect-button__dropdown-item"));
        connectionTypeDropdown.click();
        WebElement personalNoteInput = driver.findElement(By.cssSelector("textarea.mn-connect-button__personalize-text"));
        personalNoteInput.sendKeys("Hello, I would like to connect with you.");

        WebElement sendButton = driver.findElement(By.xpath("//button[contains(text(), 'Send')]"));
        sendButton.click();

        WebElement sentConfirmationMessage = driver.findElement(By.xpath("//span[contains(text(), 'Invitation Sent')]"));
        Assert.assertTrue(sentConfirmationMessage.isDisplayed(), "Connection request failed.");
    }

    @Test
    public void editLinkedInProfile() {
        driver.get(BASE_URL);

        WebElement profilePicture = driver.findElement(By.cssSelector("button.nav-item__profile-member-photo.nav-item__icon"));
        profilePicture.click();

        WebElement viewProfileOption = driver.findElement(By.xpath("//span[text()='View profile']"));
        viewProfileOption.click();

        WebElement editButton = driver.findElement(By.xpath("//button[text()='Edit']"));
        editButton.click();

        WebElement headlineInput = driver.findElement(By.cssSelector("input#headline"));
        headlineInput.clear();
        headlineInput.sendKeys("Experienced Software Engineer");

        WebElement summaryInput = driver.findElement(By.cssSelector("textarea#summary"));
        summaryInput.clear();
        summaryInput.sendKeys("Passionate about developing innovative solutions.");

        WebElement saveButton = driver.findElement(By.xpath("//button[contains(text(), 'Save')]"));
        saveButton.click();

        WebElement successMessage = driver.findElement(By.xpath("//div[contains(text(), 'Profile updated')]"));
        Assert.assertTrue(successMessage.isDisplayed(), "Profile update failed.");
    }




}
