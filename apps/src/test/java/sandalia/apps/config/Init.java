package sandalia.apps.config;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import sandalia.apps.helpers.Helper;
import sandalia.apps.pages.AccessControl;

import com.microsoft.playwright.BrowserType.LaunchOptions;

public class Init extends EnvSetup{
	
	protected static Playwright playwright;
	protected static Browser browser;
	protected static Page page;
	
	
    // Define a set of method names to skip
    private static final Set<String> SKIP_METHODS = new HashSet<>();

    // Initialize the set of methods to skip
    static {
//        SKIP_METHODS.add("TC001");
//        SKIP_METHODS.add("TC038");
//        SKIP_METHODS.add("TC039");
//        SKIP_METHODS.add("TC040");
//        SKIP_METHODS.add("TC041");
//        SKIP_METHODS.add("TC042");
    }
	
	@BeforeSuite
	public void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new LaunchOptions().setHeadless(true));
        // Page mean a browser tab
        page = browser.newPage();
        
        /**
         * set base url
         * environment (prod, dev or staging) include separate base url for each and activate environment using first parameter dev, staging or prod
         */
		String baseURL = setBaseUrl(EnvVariables.envType,EnvVariables.devURL,EnvVariables.stagingURL,EnvVariables.prodURL);
		
		page.navigate(baseURL);
		AccessControl.login();
	}
	
	//clear arraylist before each test case
	@BeforeMethod
	public void emptyProgressInfos() {
		Helper.progressInfos.clear();
	}
	
	@BeforeMethod
	public void methodSetup(Method method) {
		
        // Check if the current method name is in the skip list
        if (SKIP_METHODS.contains(method.getName())) {
            System.out.println("🚫 Skipping @BeforeMethod for: " + method.getName());
            return;  // Skip setup
        }
	}

	@AfterSuite
	public void tearDown() {
		AccessControl.logout();
		page.close();
		browser.close();
		playwright.close();
	}
}