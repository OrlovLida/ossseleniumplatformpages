package com.oss.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;

import static com.oss.utils.AttachmentsManager.saveScreenshotPNG;
import static com.oss.utils.AttachmentsManager.saveTextLog;

public class TestListener extends BaseTestCase implements ITestListener {

    private static final Logger log = LoggerFactory.getLogger(TestListener.class);

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        log.info("I am in onStart method " + iTestContext.getName());
        iTestContext.setAttribute("WebDriver", this.driver);
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        log.info("I am in onFinish method " + iTestContext.getName());

    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        log.info("I am in onTestStart method " + getTestMethodName(iTestResult) + " start");
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        log.info("I am in onTestSuccess method " + getTestMethodName(iTestResult) + " succeed");
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        log.info("I am in onTestFailure method " + getTestMethodName(iTestResult) + " failed");
        //Get driver from BaseTest and assign to local webdriver variable.
        Object testClass = iTestResult.getInstance();
        WebDriver driver = ((BaseTestCase) testClass).driver;
        //Allure ScreenShotRobot and SaveTestLog
        if (driver != null) {
            log.info("Screenshot captured for test case:" + getTestMethodName(iTestResult));
            saveScreenshotPNG(driver);
            // attachConsoleLogs(driver);
            SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, new WebDriverWait(driver, 5));
            if (systemMessage.isErrorDisplayed(true)) {
                systemMessage.close();
            }
            //Take base64Screenshot screenshot for extent reports
            String base64Screenshot = "data:image/png;base64," + ((TakesScreenshot) driver).
                    getScreenshotAs(OutputType.BASE64);
        }
        //Save a log on allure.
        saveTextLog(getTestMethodName(iTestResult) + " failed and screenshot taken!");
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        log.info("I am in onTestSkipped method " + getTestMethodName(iTestResult) + " skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        log.info("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
    }
}
