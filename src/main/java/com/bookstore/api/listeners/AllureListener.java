package com.bookstore.api.listeners;

import io.qameta.allure.Attachment;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class AllureListener implements ITestListener {
    
    @Override
    public void onTestFailure(ITestResult result) {
        saveTextLog("Test Failed: " + result.getName());
        saveTextLog("Error Message: " + result.getThrowable().getMessage());
    }
    
    @Attachment(value = "{0}", type = "text/plain")
    public String saveTextLog(String message) {
        return message;
    }
}
