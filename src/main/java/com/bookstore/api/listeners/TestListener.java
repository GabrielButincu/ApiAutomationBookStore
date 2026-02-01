package com.bookstore.api.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListener implements ITestListener {
    
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    
    @Override
    public void onStart(ITestContext context) {
        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String reportPath = "test-output/extent-reports/ExtentReport_" + timestamp + ".html";
        
        File reportDir = new File("test-output/extent-reports");
        if (!reportDir.exists()) {
            reportDir.mkdirs();
        }
        
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setDocumentTitle("Bookstore API Test Automation Report");
        sparkReporter.config().setReportName("API Test Results");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
        
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("API Base URL", "https://fakerestapi.azurewebsites.net");
        extent.setSystemInfo("Tester", "Automation Framework");
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(
                result.getMethod().getMethodName(),
                result.getMethod().getDescription()
        );
        extentTest.set(test);
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS,
                MarkupHelper.createLabel(result.getName() + " PASSED", ExtentColor.GREEN));
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        extentTest.get().log(Status.FAIL,
                MarkupHelper.createLabel(result.getName() + " FAILED", ExtentColor.RED));
        extentTest.get().log(Status.FAIL, result.getThrowable());
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().log(Status.SKIP,
                MarkupHelper.createLabel(result.getName() + " SKIPPED", ExtentColor.ORANGE));
    }
    
    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }
}
