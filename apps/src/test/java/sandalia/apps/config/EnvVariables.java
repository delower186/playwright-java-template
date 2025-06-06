package sandalia.apps.config;

public class EnvVariables {
	

	/***
	 * env value should be "prod" or "dev" or "staging"
	 */
	public static String envType = "staging";
	
	/***
	 * At least one URL is required for envType ("prod" or "dev" or "staging")
	 * prod URL
	 */
	public static String prodURL = "";
    
    /***
     * dev URL
     */
	public static String devURL = "";
	
    /***
     * staging URL
     */
	public static String stagingURL = "https://dishio-admin-staging.vercel.app/signin";
	
	
    /***
     * Report Document Title
     */
	public static String ReportDocumentTitle = "Dishio";
	
    /***
     * Report Name
     */
	public static String ReportName = "Dishio Automation Testing Report";
}
