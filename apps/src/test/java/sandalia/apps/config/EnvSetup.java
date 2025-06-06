package sandalia.apps.config;

import org.testng.annotations.Optional;

public class EnvSetup {
	
	/**
	 * 
	 * @param env
	 * @param devURL
	 * @param stagingURL
	 * @param productionURL
	 * @return
	 */
	public static String setBaseUrl(@Optional("prod") String env, String devURL, String stagingURL, String productionURL) {
        String baseURL;
        
        switch (env.toLowerCase()) {
            case "dev":
            	baseURL = devURL;
                break;
            case "staging":
            	baseURL = stagingURL;
                break;
            case "prod":
            	baseURL = productionURL;
                break;
            default:
                throw new IllegalArgumentException("Invalid environment: " + env);
        }
        System.out.println("🌐 Environment: " + env);
        System.out.println("🔗 Base URL: " + baseURL);
        
        return baseURL;
	}
	
}
