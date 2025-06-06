package sandalia.apps;

import org.testng.annotations.Test;

import sandalia.apps.config.Init;
import sandalia.apps.pages.AdminPage;


public class AppTest extends Init{
	@Test
	public void listActiveIntegrations() {
		AdminPage.listActiveIntegrations();
	}
	@Test
	public void listAllIntegrations() {
		AdminPage.listAllIntegrations();
	}
	

}
