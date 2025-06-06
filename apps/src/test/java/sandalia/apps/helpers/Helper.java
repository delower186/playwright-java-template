package sandalia.apps.helpers;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v136.network.Network;
import org.openqa.selenium.devtools.v136.network.model.RequestId;
import org.openqa.selenium.devtools.v136.network.model.Response;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import sandalia.apps.config.DriverManager;


public class Helper{
	
	static WebDriver driver = DriverManager.getDriver();
	
	public static ArrayList<String> progressInfos = new ArrayList<String>();
	public static ArrayList<String> tabs;
	
	/**
	 * wait until SECONDS before performing any action
	 * @param seconds
	 * @throws InterruptedException
	 */
	public static void wait(int seconds) throws InterruptedException {
		
		Thread.sleep(Duration.ofSeconds(seconds).toMillis());
		
	}
	
	public static String capture_network_request(String apiUrlPattern) {
        
        // Use AtomicReference to store response data
        AtomicReference<String> responseJSON = new AtomicReference<>("");
        
		DevTools devTools = ((ChromeDriver) driver).getDevTools();

        // Create a new DevTools session
        devTools.createSession();

        // Enable network monitoring
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        // Capture network requests
        devTools.addListener(Network.requestWillBeSent(), request -> {
            String url = request.getRequest().getUrl();
            String method = request.getRequest().getMethod();

            // Check if the request URL matches the specified pattern
            if (url.contains(apiUrlPattern)) {
                System.out.println("🔵 Captured API Request:");
                System.out.println("🔵 Request URL: " + url);
                System.out.println("🔵 Request Method: " + method);
            }
        });

        // Capture network responses
        devTools.addListener(Network.responseReceived(), responseReceived -> {
            Response response = responseReceived.getResponse();
            String url = response.getUrl();
            int status = response.getStatus();
            String mimeType = response.getMimeType();
            RequestId requestId = responseReceived.getRequestId();

            // Check if the response URL matches the specified pattern
            if (url.contains(apiUrlPattern)) {
                System.out.println("🔴 Captured API Response:");
                System.out.println("🔴 Response URL: " + url);
                System.out.println("🔴 Status Code: " + status);
                System.out.println("🔴 MIME Type: " + mimeType);

             // Get the response body
                try {
                    Network.GetResponseBodyResponse responseBody = devTools.send(
                            Network.getResponseBody(requestId)
                    );
                    if (responseBody != null) {
                        responseJSON.set(responseBody.getBody());
                        System.out.println("🔴 Response Body: " + responseJSON.get());
                    } else {
                        System.out.println("❗ No response body available.");
                    }
                } catch (Exception e) {
                    System.out.println("❗ Error retrieving response body: " + e.getMessage());
                }

                System.out.println("====================================");
            }
        });
        
        return responseJSON.get();
	}
	
	/**
	 * 
	 * @param getScreenshotPath
	 * @return
	 * @throws IOException
	 */
	public String getScreenshotPath(String testCaseName) throws IOException {
		
		File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String destinationFile = "reports/" + testCaseName + ".png";
		FileHandler.copy(source, new File(destinationFile));
		
	    // Return just the filename for the report
	    return testCaseName + ".png";
		
	}
	
	//window full screen
	public static void maximizeWindow() {
		driver.manage().window().maximize();
	}
	
	// random integer generator
	public static int randomNumber(int min, int max) {
		Random rn = new Random();
		return rn.nextInt(max - min + 1) + min;
	}
	
	// select element
	public static void selectByText(By by, String selectorText) {
    	Select element = new Select(driver.findElement(by));
    	element.selectByVisibleText(selectorText);
	}
	
	//switch to new tab start from 0
	public static void switchTab(int tab) {
		
	    try {
	        // Get all open window handles
	        tabs = new ArrayList<>(driver.getWindowHandles());

	        // Check if the tab index is valid
	        if (tab < 0 || tab >= tabs.size()) {
	            System.out.println("Invalid tab index: " + tab);
	            return;
	        }

	        // Switch to the specified tab
	        driver.switchTo().window(tabs.get(tab));
	        System.out.println("Switched to tab: " + (tab + 1));
	    } catch (Exception e) {
	        System.err.println("Error while switching tabs: " + e.getMessage());
	    }
	}
	//close new tab and return to the previous one
	public static void closeTab(int tab) {
		
	    try {
	        // Get the list of window handles
	        tabs = new ArrayList<>(driver.getWindowHandles());

	        // Check if the tab index is valid
	        if (tab < 0 || tab >= tabs.size()) {
	            System.out.println("Invalid tab index: " + tab);
	            return;
	        }

	        // Switch to the specified tab
	        driver.switchTo().window(tabs.get(tab));
	        System.out.println("Switched to tab: " + (tab + 1));

	        // Close the current tab
	        driver.close();
	        System.out.println("Closed tab: " + (tab + 1));

	        // Update the list of handles after closing
	        tabs = new ArrayList<>(driver.getWindowHandles());

	        // Switch to another available tab (if any)
	        if (!tabs.isEmpty()) {
	            driver.switchTo().window(tabs.get(0));
	            System.out.println("Switched to the first available tab.");
	        }
	    } catch (Exception e) {
	        System.err.println("Error while closing the tab: " + e.getMessage());
	    }
	}
	
	public static void hoverOverElement(By locator) {

	    try {
	        WebElement element = select(5, locator);

	        // Create an Actions instance
	        Actions actions = new Actions(driver);

	        // Move to the element to hover
	        actions.moveToElement(element).perform();

	        System.out.println("Hovered over element: " + element.getText());
	    } catch (Exception e) {
	        System.err.println("Error hovering over element: " + e.getMessage());
	    }
	}	
	
	/**
	 * @param firstElement
	 * @param secondElement
	 * @throws InterruptedException 
	 */
	public static void selectWhichAvailable(By firstElement, By secondElement) throws InterruptedException {
		
        WebElement element;
        
        try {

            element = driver.findElement(firstElement);
            
        } catch (NoSuchElementException e) {
        	
        	element = driver.findElement(secondElement);
        }
        
        
        // Perform actions on the element
        element.click();
	}

	//dismiss or remove any popup
	public static void dismisIfAvailable(By selector) {
		
		try {
			
			driver.findElement(selector).click();
			
		}catch(NoSuchElementException e) {
			
			System.out.println(e.getMessage());
			
		}
	}
	
	public static String timestampGenerator() {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Define the format (e.g., "2025-05-06 14:30:00")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

        // Format the timestamp as string
        String formattedTimestamp = now.format(formatter);
        
        return formattedTimestamp;
	}
	
	public static void select_one_from_dropdown(By parent, By child) {

        // Locate the <ul> and then its <li> children
        WebElement ulElement = driver.findElement(parent); // Adjust selector
        List<WebElement> liElements = ulElement.findElements(child);

        // Pick a random <li>
        if (!liElements.isEmpty()) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(liElements.size());

            WebElement selectedLi = liElements.get(randomIndex);
            selectedLi.click(); // Select it

            System.out.println("Selected option: " + selectedLi.getText());
        } else {
            System.out.println("No options found in dropdown.");
        }
	}
	
	/**
	 * wait until TIMEOUT before visible the LOCATOR to perform any action
	 * @param timeout
	 * @param locator
	 * @return
	 */
	public static WebElement select(int timeout, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	/**
	 * verifyImageLink
	 * @param imgUrl
	 */
	public static int verifyImageLink(String imgUrl) {
		int responseCode = 0;
		try {
	        // Use URI to handle the image URL properly
	        URL url = URI.create(imgUrl).toURL();
	        HttpURLConnection http = (HttpURLConnection) url.openConnection();
	        http.setRequestMethod("GET");
	        http.connect();
	        responseCode = http.getResponseCode();

	    } catch (Exception e) {
	        System.err.println("Error checking image URL: " + e.getMessage());
	    }
		
        if (responseCode == 200) {
            System.out.println("Image loaded successfully: " + responseCode);
        } else {
            System.out.println("Image is broken: HTTP " + responseCode);
        }
		
		return responseCode;
	}
	
	/**
	 * scrollToElement
	 * @param locator
	 */
	public static void scrollToElement(By locator) {
		
	    try {
	        WebElement element = select(5, locator);

	        // Use JavaScript to scroll the element into view
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);

	        // Add a small wait to ensure scrolling is complete
	        wait(5);

	    } catch (Exception e) {
	        System.err.println("Error scrolling to element and clicking: " + e.getMessage());
	    }
	}
	
	/**
	 * scrollToElementByWebElement
	 * @param locator
	 */
	public static void scrollToElementByWebElement(WebElement element) {
		
	    try {

	        // Use JavaScript to scroll the element into view
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);

	        // Add a small wait to ensure scrolling is complete
	        wait(5);

	    } catch (Exception e) {
	        System.err.println("Error scrolling to element and clicking: " + e.getMessage());
	    }
	}
	
	public static void selectAllFromDropdown(By parent, By child) {
		boolean allClicked = true; // Track if all elements were clicked successfully
		try {
	        // Locate the <ul> element
	        WebElement ulElement = driver.findElement(parent);

	        // Get all <li> elements within the dropdown
	        List<WebElement> liElements = ulElement.findElements(child);

	        if (!liElements.isEmpty()) {
	            int totalOptions = liElements.size();
	            System.out.println("Total options: " + totalOptions);

	            // Scroll and select each option
	            for (int i = 0; i < totalOptions; i++) {
	                try {
	                    // Re-fetch the list to avoid StaleElementReferenceException
	                    liElements = ulElement.findElements(child);

	                    // Scroll element into view
	                    WebElement option = liElements.get(i);
	                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", option);

	                    // Click the option
	                    option.click();
	                    System.out.println("Selected option: " + option.getText());

	                    // Small delay to account for UI changes
	                    wait(1);
	                } catch (StaleElementReferenceException e) {
	                    System.out.println("Stale element, retrying...");
	                    // Refresh the list again if stale
	                    liElements = ulElement.findElements(child);
	                    i--; // Retry the same index
	                } catch (Exception e) {
	                	allClicked = false; // Mark as failed if any click fails
	                    System.err.println("Error selecting option: " + e.getMessage());
	                }
	            }
	            // Assert that all options were clicked
	            Assert.assertTrue(allClicked, "Not all dropdown options were clicked successfully.");
	            System.out.println("All options selected and asserted successfully.");
	        } else {
	            System.out.println("No options found in dropdown.");
	            Assert.fail("Dropdown is empty.");
	        }
	    } catch (Exception e) {
	        System.err.println("Dropdown not found: " + e.getMessage());
	        Assert.fail("Failed to locate the dropdown.");
	    }
	}
	
	public static String getRootURL() {
		String currentUrl = driver.getCurrentUrl();
		String rootURL = null;
		try {
		    URI uri = new URI(currentUrl);
		    rootURL = uri.getScheme() + "://" + uri.getHost();
		    System.out.println("Root URL: " + rootURL);
		} catch (URISyntaxException e) {
		    e.printStackTrace();
		}
		
		return rootURL;
	}
	
	public static void collapseExpand(String ceEl) throws InterruptedException {
		
    	List<WebElement> ceEls = driver.findElements(By.xpath(ceEl));
    	
    	if(!ceEls.isEmpty()) {
    	    for (int i = 0; i < ceEls.size(); i++) {
    	    	Helper.scrollToElementByWebElement(ceEls.get(i));
    	    	ceEls.get(i).click();
    	    	Helper.wait(2);
    	    }
    	}
		
	}

}
