package sandalia.apps.pages;

import sandalia.apps.config.Init;

public class AccessControl extends Init{
	
	/**
	 * LoginForm
	 */
	public static String email = "//input[@placeholder='user@email.com']";
	public static String password = "//input[@name='password']";
	public static String signInBtn = "//button[normalize-space()='Sign In']";
	
	/**
	 * Logout
	 */
	public static String profileDropDownIcon = "(//button[@class=' relative rounded-full bg-white p-1 hover:bg-gray-200'])[1]";
	public static String logoutButton = "//button[normalize-space()='Log out']";
	
	
	public static void login() {
		
		page.locator(email).fill("testerbd365@gmail.com");
		page.locator(password).fill("A@12345b");
		page.locator(signInBtn).click();
	}
	
	public static void logout() {
		page.locator(profileDropDownIcon).click();
		page.locator(logoutButton).click();
	}
	
}