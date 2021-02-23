package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;
	private static String username = "Darren";
	private static String password = "password";

	public void signupandlogin(){
		String username = "Darren";
		String password = "pw123!";

		driver.get("http://localhost:" + this.port + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.createUser("Darren", "G", username, password);

		driver.get("http://localhost:" + this.port + "/login");
		LoginPage loginPage = new LoginPage(driver);
		Assertions.assertEquals("Login", driver.getTitle());
		loginPage.login(username,password);
	}

	private WebDriver driver;
//	private HomePage homePage = new HomePage(driver);
//	private WebDriverWait wait = new WebDriverWait(driver,5);
//
//	public void webdriver (){
//		WebDriverWait wait = new WebDriverWait(driver, 5);
//		driver.get("http://localhost:" + this.port + "/home");
//		HomePage homePage = new HomePage(driver);
//		Assertions.assertEquals("Home", driver.getTitle());
//	}

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(1)
	public void getSignupPage(){
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	@Order(2)
	public void getHomePage(){
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}

	@Test
	@Order(3)
	public void signupTest() {

		this.signupandlogin();


		driver.get("http://localhost:" + this.port + "/home");
		NotePage notePage = new NotePage(driver);
		Assertions.assertEquals("Home", driver.getTitle());
		notePage.logout();

		Assertions.assertEquals("http://localhost:" + port + "/login", driver.getCurrentUrl());
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}

	@Test
	@Order(4)
	public void createNoteTest() throws InterruptedException {
		String title = "title";
		String description = "description";

		this.signupandlogin();


		//Wait Driver
		WebDriverWait wait = new WebDriverWait(driver, 5);
		driver.get("http://localhost:" + this.port + "/home");
		NotePage notePage = new NotePage(driver);
		Assertions.assertEquals("Home", driver.getTitle());


		//open notes tab
		WebElement noteTab = notePage.getNotesTab();
		wait.until(ExpectedConditions.elementToBeClickable(noteTab)).click();

		//create a note
		wait.until(ExpectedConditions.elementToBeClickable( By.id("addNoteButton") )).click();
		wait.until(ExpectedConditions.elementToBeClickable( By.id("note-title") )).sendKeys(title);
		wait.until(ExpectedConditions.elementToBeClickable( By.id("note-description") )).sendKeys(description);
		wait.until(ExpectedConditions.elementToBeClickable( By.id("saveButton") )).click();

		//check result page working
		Assertions.assertEquals("http://localhost:" + port + "/notes", driver.getCurrentUrl());
		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable( By.id("success-go-home") )).click();
		Thread.sleep(1000);
		Assertions.assertEquals("http://localhost:" + port + "/home", driver.getCurrentUrl());

		//check if note created properly
		driver.get("http://localhost:" + this.port + "/home");
		wait.until(ExpectedConditions.elementToBeClickable(noteTab)).click();
		String noteTitle = wait.until(ExpectedConditions.elementToBeClickable(By.id("table-noteTitle"))).getText();
		String noteDescription = wait.until(ExpectedConditions.elementToBeClickable(By.id("table-noteDescription"))).getText();
		Assertions.assertEquals(title, noteTitle);
		Assertions.assertEquals(description, noteDescription);
	}

	@Test
	@Order(5)
	public void editNoteTest() throws InterruptedException {
		String editedTitle = "edited title";
		String editedDescription = "edited description";
		this.signupandlogin();
		this.createNoteTest();

		//Wait Driver
		WebDriverWait wait = new WebDriverWait(driver, 5);
		driver.get("http://localhost:" + this.port + "/home");
		NotePage notePage = new NotePage(driver);
		Assertions.assertEquals("Home", driver.getTitle());

		//open notes tab
		WebElement noteTab = notePage.getNotesTab();
		wait.until(ExpectedConditions.elementToBeClickable(noteTab)).click();

		//edit note
		wait.until(ExpectedConditions.elementToBeClickable(By.id("editNoteButton"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).clear();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).sendKeys(editedTitle);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("note-description"))).clear();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("note-description"))).sendKeys(editedDescription);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("saveButton"))).click();

		//check result page to home page flow
		Assertions.assertEquals("http://localhost:" + port + "/notes", driver.getCurrentUrl());
		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable( By.id("success-go-home") )).click();
		Thread.sleep(1000);
		Assertions.assertEquals("http://localhost:" + port + "/home", driver.getCurrentUrl());

		//check if note edited as expected
		driver.get("http://localhost:" + this.port + "/home");
		wait.until(ExpectedConditions.elementToBeClickable(noteTab)).click();
		String noteTitle = wait.until(ExpectedConditions.elementToBeClickable(By.id("table-noteTitle"))).getText();
		String noteDescription = wait.until(ExpectedConditions.elementToBeClickable(By.id("table-noteDescription"))).getText();
		Assertions.assertEquals(editedTitle, noteTitle);
		Assertions.assertEquals(editedDescription, noteDescription);
	}

	@Test
	@Order(6)
	public void deleteNoteTest() throws InterruptedException {
		this.signupandlogin();
		//this.createNoteTest();

		//Wait Driver
		WebDriverWait wait = new WebDriverWait(driver, 5);
		driver.get("http://localhost:" + this.port + "/home");
		NotePage notePage = new NotePage(driver);
		Assertions.assertEquals("Home", driver.getTitle());

		//open notes tab
		WebElement noteTab = notePage.getNotesTab();
		wait.until(ExpectedConditions.elementToBeClickable(noteTab)).click();

		//delete note
		wait.until(ExpectedConditions.elementToBeClickable(By.id("deleteNoteButton"))).click();

		//check result page working
		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable( By.id("success-go-home") )).click();
		Thread.sleep(1000);
		Assertions.assertEquals("http://localhost:" + port + "/home", driver.getCurrentUrl());

		//check if note is deleted
		driver.get("http://localhost:" + this.port + "/home");
		wait.until(ExpectedConditions.elementToBeClickable(noteTab)).click();

		Assertions.assertThrows(NoSuchElementException.class, () -> notePage.getHomeNoteTitle().getText());
		Assertions.assertThrows(NoSuchElementException.class, () -> notePage.getHomeNoteDescription().getText());
	}

	@Test
	@Order(7)
	public void createCredentialTest() throws InterruptedException {
		String url = "url.com";
		String credusername = "credential";
		String credpassword = "passcredential";


		this.signupandlogin();

		//Wait Driver
		WebDriverWait waitDriver = new WebDriverWait(driver, 5);
		driver.get("http://localhost:" + this.port + "/home");
		CredentialPage credentialPage = new CredentialPage(driver);
		Assertions.assertEquals("Home", driver.getTitle());

		//open credential tab
		WebElement credentialTab = credentialPage.getCredentialsTab();
		waitDriver.until(ExpectedConditions.elementToBeClickable(credentialTab)).click();

		//add new credential
		waitDriver.until(ExpectedConditions.elementToBeClickable( By.id("addCredentialButton") )).click();
		waitDriver.until(ExpectedConditions.elementToBeClickable( By.id("credential-url") )).sendKeys(url);
		waitDriver.until(ExpectedConditions.elementToBeClickable( By.id("credential-username") )).sendKeys(credusername);
		waitDriver.until(ExpectedConditions.elementToBeClickable( By.id("credential-password") )).sendKeys(credpassword);
		waitDriver.until(ExpectedConditions.elementToBeClickable( By.id("saveCredentialChanges") )).click();

		//check result page working
		Assertions.assertEquals("http://localhost:" + port + "/credentials", driver.getCurrentUrl());
		Thread.sleep(2000);
		waitDriver.until(ExpectedConditions.elementToBeClickable( By.id("success-go-home") )).click();
		Thread.sleep(1000);
		Assertions.assertEquals("http://localhost:" + port + "/home", driver.getCurrentUrl());

		//check if credential created as expected
		driver.get("http://localhost:" + this.port + "/home");
		waitDriver.until(ExpectedConditions.elementToBeClickable(credentialTab)).click();
		String displayedUrl = waitDriver.until(ExpectedConditions.elementToBeClickable(By.id("CredentialUrl"))).getText();
		String displayedUsername = waitDriver.until(ExpectedConditions.elementToBeClickable(By.id("CredentialUsername"))).getText();
		String displayedPassword = waitDriver.until(ExpectedConditions.elementToBeClickable(By.id("CredentialPassword"))).getText();
		Assertions.assertEquals(url, displayedUrl);
		Assertions.assertEquals(credusername, displayedUsername);
		Assertions.assertNotEquals(credpassword, displayedPassword);
	}

	@Test
	@Order(8)
	public void editCredentialTest() throws InterruptedException {
		String editedUrl = "edited.com";
		String editedcredusername = "edit_credential";
		String editedcredpassword = "edit_passcred";

		this.signupandlogin();

		//Wait Driver
		WebDriverWait waitDriver = new WebDriverWait(driver, 5);
		driver.get("http://localhost:" + this.port + "/home");
		CredentialPage credentialPage = new CredentialPage(driver);
		Assertions.assertEquals("Home", driver.getTitle());

		//open credential tab
		WebElement credentialTab = credentialPage.getCredentialsTab();
		waitDriver.until(ExpectedConditions.elementToBeClickable(credentialTab)).click();

		//edit existing credential
		waitDriver.until(ExpectedConditions.elementToBeClickable(By.id("editCredentialButton"))).click();
		waitDriver.until(ExpectedConditions.elementToBeClickable( By.id("credential-url") )).clear();
		waitDriver.until(ExpectedConditions.elementToBeClickable( By.id("credential-username") )).clear();
		waitDriver.until(ExpectedConditions.elementToBeClickable( By.id("credential-password") )).clear();
		waitDriver.until(ExpectedConditions.elementToBeClickable( By.id("credential-url") )).sendKeys(editedUrl);
		waitDriver.until(ExpectedConditions.elementToBeClickable( By.id("credential-username") )).sendKeys(editedcredusername);
		waitDriver.until(ExpectedConditions.elementToBeClickable( By.id("credential-password") )).sendKeys(editedcredpassword);
		waitDriver.until(ExpectedConditions.elementToBeClickable(By.id("saveCredentialChanges"))).click();

		//check result page working
		Assertions.assertEquals("http://localhost:" + port + "/credentials", driver.getCurrentUrl());
		Thread.sleep(2000);
		waitDriver.until(ExpectedConditions.elementToBeClickable( By.id("success-go-home") )).click();
		Thread.sleep(1000);
		Assertions.assertEquals("http://localhost:" + port + "/home", driver.getCurrentUrl());

		//check if credential edited properly
		driver.get("http://localhost:" + this.port + "/home");
		waitDriver.until(ExpectedConditions.elementToBeClickable(credentialTab)).click();
		String displayedUrl = waitDriver.until(ExpectedConditions.elementToBeClickable(By.id("CredentialUrl"))).getText();
		String displayedUsername = waitDriver.until(ExpectedConditions.elementToBeClickable(By.id("CredentialUsername"))).getText();
		String displayedPassword = waitDriver.until(ExpectedConditions.elementToBeClickable(By.id("CredentialPassword"))).getText();
		Assertions.assertEquals(editedUrl, displayedUrl);
		Assertions.assertEquals(editedcredusername, displayedUsername);
		Assertions.assertNotEquals(editedcredpassword, displayedPassword);
	}

	@Test
	@Order(9)
	public void deleteCredentialTest() throws InterruptedException {

		this.signupandlogin();
		//this.createCredentialTest();

		//Wait Driver
		WebDriverWait waitDriver = new WebDriverWait(driver, 5);
		driver.get("http://localhost:" + this.port + "/home");
		CredentialPage credentialPage = new CredentialPage(driver);
		Assertions.assertEquals("Home", driver.getTitle());

		//open credential tab
		WebElement credentialTab = credentialPage.getCredentialsTab();
		waitDriver.until(ExpectedConditions.elementToBeClickable(credentialTab)).click();

		//delete credential
		driver.get("http://localhost:" + this.port + "/home");
		waitDriver.until(ExpectedConditions.elementToBeClickable(credentialTab)).click();
		waitDriver.until(ExpectedConditions.elementToBeClickable(By.id("deleteCredentialButton"))).click();

		//check result page working
		Thread.sleep(2000);
		waitDriver.until(ExpectedConditions.elementToBeClickable( By.id("success-go-home") )).click();
		Thread.sleep(1000);
		Assertions.assertEquals("http://localhost:" + port + "/home", driver.getCurrentUrl());

		//check if credential is deleted
		driver.get("http://localhost:" + this.port + "/home");
		waitDriver.until(ExpectedConditions.elementToBeClickable(credentialTab)).click();

		Assertions.assertThrows(NoSuchElementException.class, () -> credentialPage.getHomeCredentialUrl().getText());
		Assertions.assertThrows(NoSuchElementException.class, () -> credentialPage.getHomeCredentialUsername().getText());
		Assertions.assertThrows(NoSuchElementException.class, () -> credentialPage.getHomeCredentialPassword().getText());
	}

	//https://www.edureka.co/blog/uploading-file-usiing-selenium/ used as a guide for file upload test
	@Test
	@Order(10)
	public void fileUploadTest() throws InterruptedException {
		//Change to selected file name that will be uploaded
		String file = "FDCV_KarenOSullivan_22Jan21 (1).docx";

		this.signupandlogin();

		//Wait Driver
		WebDriverWait wait = new WebDriverWait(driver, 5);
		driver.get("http://localhost:" + this.port + "/home");
		FilePage filePage = new FilePage(driver);
		Assertions.assertEquals("Home", driver.getTitle());

		//open files tab
		WebElement filesTab = filePage.getFilesTab();
		wait.until(ExpectedConditions.elementToBeClickable(filesTab)).click();

		//add new file
		wait.until(ExpectedConditions.elementToBeClickable( By.id("fileUpload") )).sendKeys("C:\\Users\\green\\Downloads\\FDCV_KarenOSullivan_22Jan21 (1).docx");
		wait.until(ExpectedConditions.elementToBeClickable( By.id("fileUploadButton") )).click();

		//check result page working
		Assertions.assertEquals("http://localhost:" + port + "/file-upload", driver.getCurrentUrl());
		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable( By.id("success-go-home") )).click();
		Thread.sleep(1000);
		Assertions.assertEquals("http://localhost:" + port + "/home", driver.getCurrentUrl());

		//check if file uploaded as expected
		driver.get("http://localhost:" + this.port + "/home");
		wait.until(ExpectedConditions.elementToBeClickable(filesTab)).click();
		String displayedFile = wait.until(ExpectedConditions.elementToBeClickable(By.id("FileNames"))).getText();
		Assertions.assertEquals(file, displayedFile);

	}

}
