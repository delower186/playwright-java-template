package sandalia.apps.helpers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;

import com.microsoft.playwright.Page;

import sandalia.apps.config.Init;





public class Helper extends Init{
	
	
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
	
	
	/**
	 * 
	 * @param getScreenshotPath
	 * @return
	 * @throws IOException
	 */
	public String getScreenshotPath(String testCaseName) throws IOException {
		
		// set location of the screenshot
		String destinationFile = "reports/" + testCaseName + ".png";
		// Assign screenshot location to save
		Page.ScreenshotOptions options = new Page.ScreenshotOptions();
		options.setPath(Paths.get(destinationFile));
		// take screenshot
		page.screenshot(options);
		
	    // Return just the filename for the report
	    return testCaseName + ".png";
		
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
}
