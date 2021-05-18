package com.oss.pages.schedulerservice;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class SchedulerServicePage extends BasePage {

    public SchedulerServicePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//div[@data-attributename ='search']//input")
    private WebElement searchField;
    @FindBy(xpath = "(//div[contains (@class, 'Cell Row') and contains(@class, 'radio')])[1]")
    private WebElement firstJob;
    @FindBy(id = "EDIT")
    private WebElement editContextAction;
    @FindBy(id = "schedulerServiceRetireJobAct")
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

    private void typeInSearchField(String value) {
        DelayUtils.waitForVisibility(wait, searchField);
        searchField.sendKeys(value);
    }

    private boolean isRowContainsTextVisible(String text) {
        DelayUtils.waitForVisibility(wait, firstJob);
        return driver.findElements(By.xpath(getPathOfRowContainsText(text))).size() > 0;
    }

    private void clickOnRowContainsText(String text) {
        DelayUtils.waitByXPath(wait, getPathOfRowContainsText(text));
        driver.findElement(By.xpath(getPathOfRowContainsText(text))).click();
    }

    private String getPathOfRowContainsText(String text) {
        return "//div[contains(@class, 'Cell Row')]/div[contains(text(),'" + text + "')]";
    }

    public String getTextOfJob(String text) {
        return driver.findElement(By.xpath(getPathOfRowContainsText(text))).getText();
    }

    @Step("Find created Job and click on it")
    public SchedulerServicePage findJobAndClickOnIt(String name) {
        if (!isRowContainsTextVisible(name)) {
            typeInSearchField(name);
        }
        clickOnRowContainsText(name);
        return this;
    }

    @Step("Delete created Job")
    public SchedulerServicePage deleteSelectedJob() {
        DelayUtils.waitForVisibility(wait, editContextAction);
        editContextAction.click();
        deleteJobAction.click();
        DelayUtils.waitForVisibility(wait, confirmDeleteButton);
        confirmDeleteButton.click();
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Permanently delete created Job")
    public SchedulerServicePage permanentlyRemoveJob() {
        DelayUtils.waitForVisibility(wait, editContextAction);
        editContextAction.click();
        permanentDeleteJobAction.click();
        DelayUtils.waitForVisibility(wait, confirmDeleteButton);
        confirmDeleteButton.click();
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Select deleted Job")
    public SchedulerServicePage selectDeletedJob(String text) {
        DelayUtils.waitForClickability(wait, driver.findElement(By.xpath("//div[contains(@class, 'Cell Row')]/div[contains(text(),'" + text + "')]")));
        clickOnRowContainsText(text);
        DelayUtils.waitForPageToLoad(driver, wait);
        clickOnRowContainsText(text);
        return this;
    }
}
