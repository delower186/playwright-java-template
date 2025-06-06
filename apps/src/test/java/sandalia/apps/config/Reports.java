package sandalia.apps.config;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class Reports {
	static String path = "reports/index.html";
	private static ExtentReports extentReports = new ExtentReports();
	
	public static ExtentReports sparkReporter() {
		
		
		ExtentSparkReporter spark = new ExtentSparkReporter(path);
		spark.config().setTheme(Theme.DARK);
		spark.config().setDocumentTitle(EnvVariables.ReportDocumentTitle);
		spark.config().setReportName(EnvVariables.ReportName);
		extentReports.attachReporter(spark);
		extentReports.setSystemInfo("Automation Engineer","Md Delower Hossain");
		
		return extentReports;

	}
	
	public static void generateReport() throws IOException {
		
		extentReports.flush();
		Desktop.getDesktop().browse(new File(path).toURI());
		
	}
}
