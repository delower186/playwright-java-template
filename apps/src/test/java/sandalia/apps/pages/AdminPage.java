package sandalia.apps.pages;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

import com.microsoft.playwright.Locator;

import sandalia.apps.config.Init;
import sandalia.apps.helpers.Helper;

public class AdminPage extends Init{
	
	// List all integrations for a valid brand (with/without filters) when enableIntegrations is true.
	public static String brand = "(//div[contains(@class,'flex flex-row items-center gap-4')])[1]";
	public static String name = "";
	public static String integrationsBTN = "//button[@id='underline-button-integrations']";
	public static String enabledIntegrations = "//div[contains(@data-sentry-component,'IntegrationCardWrapper')]";
	
	public static String enableIntegrationRadioBTN = "//label[contains(@for,'enableIntegrations')]";
	public static String saveBTN = "//button[@type='submit']";
	
	// List integrations for a brand with no integrations.
	public static String brandWithNoIntegration = "(//div[contains(@class,'flex flex-row items-center gap-4')])[3]";
	public static String allIntegrations = "(//div[contains(@class,'flex flex-col border border-gray-200 rounded-2xl overflow-hidden transition-all duration-200 hover:border-gray-300 h-auto w-full max-w-full sm:w-[320px] sm:h-[320px]')])";
	public static String brandManager = "(//img[@class='my-3 cursor-pointer'])[1]";

	public static void listActiveIntegrations() {
		String brandName = page.locator(brand).textContent();
		page.locator(brand).click();
		
		Locator radioButton = page.locator(enableIntegrationRadioBTN);
		//radioButton.scrollIntoViewIfNeeded();

		if (radioButton.isChecked()) {
		  System.out.println("Radio button is already selected. Skipping.....");
		} else {
		  System.out.println("Radio button is not selected. Let's select it...");
		  radioButton.click();
		  page.locator(saveBTN).click();
		  System.out.println("Radio button is selected & saved!");
		}
		
		page.locator(integrationsBTN).click();
		page.waitForSelector(enabledIntegrations);
		Locator items = page.locator(enabledIntegrations);
		
		List<String> titles = new ArrayList<String>();
		
	      int count = items.count();
	      for (int i = 0; i < count; i++) {
	        String text = items.nth(i).locator("h4").textContent();
	        titles.add(text);
	        System.out.println("Integration Title " + (i + 1) + ": " + text);
	      }
	      
	      Assert.assertEquals(count, 4);
	      
		Helper.progressInfos.add("Brand: '"+ brandName+"', Total active integrations: " + count +" and They are '" + String.join("', '", titles) + "'");
	}
	
	public static void listAllIntegrations() {
		
		page.locator(brandManager).click();
		
		String brandName = page.locator(brandWithNoIntegration).textContent();
		page.locator(brandWithNoIntegration).click();
		
		Locator radioButton = page.locator(enableIntegrationRadioBTN);
		// radioButton.scrollIntoViewIfNeeded();

		if (radioButton.isChecked()) {
		  System.out.println("Radio button is already selected. Skipping.....");
		} else {
		  System.out.println("Radio button is not selected. Let's select it...");
		  radioButton.click();
		  page.locator(saveBTN).click();
		  System.out.println("Radio button is selected & saved!");
		}
		
		page.locator(integrationsBTN).click();
		page.waitForSelector(allIntegrations);
		Locator items = page.locator(allIntegrations);
		
		List<String> titles = new ArrayList<String>();
		
	      int count = items.count();
	      for (int i = 0; i < count; i++) {
	        String text = items.nth(i).locator("h4").textContent();
	        titles.add(text);
	        System.out.println("Integration Title " + (i + 1) + ": " + text);
	      }
	      
	      Assert.assertEquals(count, 23);
	      
		Helper.progressInfos.add("Brand: '"+ brandName+"', Total integrations: " + count +" and They are '" + String.join("', '", titles) + "'");
	}
	
}
