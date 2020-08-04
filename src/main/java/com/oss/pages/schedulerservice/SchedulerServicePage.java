package com.oss.pages.schedulerservice;

import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SchedulerServicePage extends BasePage {

    public SchedulerServicePage(WebDriver driver){super(driver);}

    @FindBy(xpath = "//div[@data-attributename ='search']//input")
    private WebElement searchField;
    @FindBy(xpath = "(//div[contains (@class, 'Cell Row') and contains(@class, 'radio')])[1]")
    private WebElement firstJob;
    @FindBy(id = "EDIT")
    private WebElement editContextAction;
    @FindBy(id = "schedulerServiceDeleteJobAct")
    private WebElement deleteJobAction;
    @FindBy(id = "schedulerServicePermanentlyDeleteJobAct")
    private WebElement permanentDeleteJobAction;
    @FindBy(xpath = "//button[contains(@class, 'actionButton') and contains (@class, 'danger')]")
    private WebElement confirmDeleteButton;

    @Step("Open Scheduler Service Page")
    public static SchedulerServicePage goToSchedulerServicePage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/scheduler-service-view/main/global" +
                "?perspective=LIVE", basicURL));
        return new SchedulerServicePage(driver);
    }

    private void typeInSearchField(String value){
        waitForVisibility(searchField);
        searchField.sendKeys(value);
    }

    private boolean isRowContainsTextVisible(String text){
        waitForVisibility(firstJob);
        return driver.findElements(By.xpath(getPathOfRowContainsText(text))).size() > 0;
    }

    private void clickOnRowContainsText(String text){
        waitForComponent(getPathOfRowContainsText(text));
        driver.findElement(By.xpath(getPathOfRowContainsText(text))).click();
    }

    private String getPathOfRowContainsText(String text){
        return "//div[contains(@class, 'Cell Row')]/div[contains(text(),'"+text+"')]";
    }

    public String getTextOfJob(String text){
        return driver.findElement(By.xpath(getPathOfRowContainsText(text))).getText();
    }

    @Step("Find created Job and click on it")
    public SchedulerServicePage findJobAndClickOnIt(String name){
        if(!isRowContainsTextVisible(name)) {
            typeInSearchField(name);
        }
        clickOnRowContainsText(name);
        return this;
    }

    @Step("Delete created Job")
    public SchedulerServicePage deleteSelectedJob(){
        waitForVisibility(editContextAction);
        editContextAction.click();
        deleteJobAction.click();
        waitForVisibility(confirmDeleteButton);
        confirmDeleteButton.click();
        DelayUtils.sleep(500);
        return this;
    }

    @Step("Permanently delete created Job")
    public SchedulerServicePage permanentlyRemoveJob(){
        waitForVisibility(editContextAction);
        editContextAction.click();
        permanentDeleteJobAction.click();
        waitForVisibility(confirmDeleteButton);
        confirmDeleteButton.click();
        DelayUtils.sleep(500);
        return this;
    }

    @Step("Select deleted Job")
    public SchedulerServicePage selectDeletedJob(String text){
        waitforclickability(driver.findElement(By.xpath("//div[contains(@class, 'Cell Row')]/div[contains(text(),'"+text+"')]")));
        clickOnRowContainsText(text);
        DelayUtils.sleep(500);
        clickOnRowContainsText(text);
        return this;
    }
}
