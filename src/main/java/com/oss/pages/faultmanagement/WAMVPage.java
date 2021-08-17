package com.oss.pages.faultmanagement;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.IaaTable;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class WAMVPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(FMDashboardPage.class);
    private static final String TABLE_WIDGET_ID = "_AREA2AlarmListTable";
    private static final String BUTTON_ACK_TEST_ID = "Acknowledge";
    private static final String BUTTON_DEACK_TEST_ID = "Deacknowledge";



    public WAMVPage(WebDriver driver) {
        super(driver);
    }

    public WAMVPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        PageFactory.initElements(driver, this);
    }

    @Step("I initialize new WAMV")
    public static WAMVPage ceateWAMV(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 90);
        return new WAMVPage(driver, wait);
    }

    @Step("I create an IaaTable")
    private IaaTable createIaaTableElement() {
        return IaaTable.createById(driver, wait, TABLE_WIDGET_ID);
    }

    @Step("I create an button")
    private Button createButton(String buttonId) {
        return Button.createById(driver, buttonId);
    }

    @Step("I choose a selected row from a list")
    public void selectSpecificRow(int row) { // zakładam numerowanie od zera
        createIaaTableElement().selectRow(row);
    }

    @Step("I click on acknowledge button")
    public void clickOnAckButton() {
        createButton(BUTTON_ACK_TEST_ID).click();
        DelayUtils.sleep(5000);
    }

    @Step("I click on deacknowledge button")
    public void clickOnDeackButton() {
        createButton(BUTTON_DEACK_TEST_ID).click();
        DelayUtils.sleep(5000);
    }

    @Step("I return a cell text from ack. status column")
    public String getTextFromAckStatusCell(int row, String columnName) {
        return createIaaTableElement().getListOfCells(columnName).get(row).getAttribute("title").toString();
    }


}
