package sandalia.apps.config;

import java.io.IOException;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import sandalia.apps.helpers.Helper;

public class Listeners extends Helper implements ITestListener{
	public ExtentTest test;
	ExtentReports extent = Reports.sparkReporter();

	@Override
	public void onTestStart(ITestResult result) {
		
		test = extent.createTest(result.getMethod().getMethodName());
		
	}
	
	@Override
	public void onTestSuccess(ITestResult result) {
		
		if(progressInfos.size()>0) {
			for(int i=0; i < progressInfos.size(); i++) {
				test.info(progressInfos.get(i));
			}
		}
		
		test.log(Status.PASS, result.getMethod().getMethodName() + " Passed");
		
	}
	
	@Override
	public void onTestFailure(ITestResult result) {
		
		test.fail(result.getThrowable());
		try {
			test.addScreenCaptureFromPath(getScreenshotPath(result.getMethod().getMethodName()),result.getMethod().getMethodName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onTestSkipped(ITestResult result) {}
	
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
	
	@Override
	public void onStart(ITestContext context) {}
	
	@Override
	public void onFinish(ITestContext context) {
		
		extent.flush();
		
	}
}
