package sandalia.apps.config;

public class EnvVariables {
	

	/***
	 * env value should be "prod" or "dev" or "staging"
	 */
	public static String envType = "prod";
	
	/***
	 * At least one URL is required for envType ("prod" or "dev" or "staging")
	 * prod URL
	 */
	public static String prodURL = "https://dishio-admin-staging.vercel.app/signin";
    
    /***
     * dev URL
     */
	public static String devURL = "";
	
    /***
     * staging URL
     */
	public static String stagingURL = "";
    
    
    

}
