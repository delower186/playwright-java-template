package sandalia.apps.helpers;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import sandalia.apps.config.DriverManager;

public class SwiperCarousel {

	static WebDriver driver = DriverManager.getDriver();
	
    public static void verifyActiveSlide(String expectedTextContains) {
        // Locate the active slide
        WebElement activeSlide = Helper.select(10, By.cssSelector("swiper-slide.swiper-slide-active"));
        String activeSlideText = activeSlide.getText();
        System.out.println("Active Slide Text: " + activeSlideText);

        // Assert that the active slide contains some expected text
        Assert.assertTrue(activeSlideText.contains(expectedTextContains), "Active slide content mismatch!");
    }
    
    public static void verifyCarouselSlideCount(int expectedSlideCount) {
        // Get the list of all slides
        List<WebElement> allSlides = driver.findElements(By.cssSelector("swiper-slide"));
        int slideCount = allSlides.size();

        System.out.println("Total Slides: " + slideCount);
        Assert.assertEquals(slideCount, expectedSlideCount, "Slide count mismatch!");
    }
    
    public static void validateSpecificSlideContent(String expectedTextContains) {
        // Locate all the slides and print their content
        List<WebElement> allSlides = driver.findElements(By.cssSelector("swiper-slide"));

        for (WebElement slide : allSlides) {
            String slideContent = slide.getText();
            System.out.println("Slide Content: " + slideContent);
            // Check if any slide contains a specific testimonial
            if (slideContent.contains(expectedTextContains)) {
                Assert.assertTrue(true, "Found expected slide content!");
                return;
            }
        }
        Assert.fail("Expected slide content not found!");
    }
	
}
