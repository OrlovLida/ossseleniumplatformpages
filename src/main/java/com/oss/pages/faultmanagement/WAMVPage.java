package com.oss.pages.faultmanagement;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.iaa.widgets.list.ListApp;
import com.oss.framework.iaa.widgets.table.FMSMTable;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

/**
 * @author Bartosz Nowak
 */
public class WAMVPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(WAMVPage.class);
    private static final String TABLE_AREA2_WIDGET_ID = "area2-alarm-table";
    private static final String BUTTON_ACK_TEST_ID = "Acknowledge";
    private static final String BUTTON_DEACK_TEST_ID = "Deacknowledge";
    private static final String BUTTON_NOTE_TEST_ID = "Edit Note";
    private static final String ACKNOWLEDGE_COLUMN_ID = "cell-row-col-acknowledge";
    private static final String NOTIFICATION_IDENTIFIER_COLUMN_ID = "cell-row-col-notificationIdentifier";
    private static final String NOTE_COLUMN_ID = "cell-row-col-note";
    private static final String ADDITIONAL_TEXT_TAB_ID = "additional-text";
    private static final String ALARM_DETAILS_TAB_ID = "alarm-details";
    private static final String SAME_MO_DETAILS_TAB_ID = "mo-properties";
    private static final String SAME_MO_ALARMS_TAB_ID = "mo-alarms";
    private static final String SAME_MO_ALARMS_TABLE_ID = "area3-mo-alarms";
    private static final String ADAPTER_NAME_VALUE = "Adapter Name";
    private static final String NOTIFICATION_IDENTIFIER_VALUE = "Notification Identifier";
    private static final String PERCEIVED_SEVERITY_VALUE = "Perceived Severity";
    private static final String PROPERTY_PANEL_ID = "card-content_AREA3";

    private final FMSMTable fmsmTable = FMSMTable.createById(driver, wait, TABLE_AREA2_WIDGET_ID);

    public WAMVPage(WebDriver driver) {
        super(driver);
    }

    public WAMVPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        PageFactory.initElements(driver, this);
    }

    @Step("I initialize a new WAMV")
    public static WAMVPage createWAMV(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 90);
        log.info("Initialization of WAMV page");
        return new WAMVPage(driver, wait);
    }

    @Step("I create a SameMOAlarmsTable")
    private FMSMTable createSameMOAlarmsTable() {
        log.info("Creating Same MO Alarms Table");
        return FMSMTable.createById(driver, wait, SAME_MO_ALARMS_TABLE_ID);
    }

    @Step("I create a button")
    private Button createButton(String buttonId) {
        log.info("Creating a button");
        return Button.createById(driver, buttonId);
    }

    @Step("I choose a selected row from a list")
    public void selectSpecificRow(int row) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fmsmTable.selectRow(row);
        log.info("Selecting row {} on alarm list", row);
    }

    @Step("I click on acknowledge button")
    public void clickOnAckButton() {
        DelayUtils.sleep(1000);
        createButton(BUTTON_ACK_TEST_ID).click();
        log.info("Clicking on acknowledge button");
    }

    @Step("I click on deacknowledge button")
    public void clickOnDeackButton() {
        DelayUtils.sleep(1000);
        createButton(BUTTON_DEACK_TEST_ID).click();
        log.info("Clicking on deacknowledge button");
    }

    @Step("I add note to alarm")
    public void addNote(String note) {
        DelayUtils.sleep(1000);
        createButton(BUTTON_NOTE_TEST_ID).click();
        DelayUtils.sleep(1000);
        EditNoteWizardPage editNote = new EditNoteWizardPage(driver);
        editNote.typeNote(note);
        log.info("Adding note: {}", note);
    }

    @Step("I return a cell text from acknowledge state column")
    public String getTitleFromAckStatusCell(int row, String value) {
        fmsmTable.getCell(row, ACKNOWLEDGE_COLUMN_ID).waitForExpectedValue(wait, value);
        log.info("Returning cell text from acknowledge state column in row: {}", row);
        return fmsmTable.getCellValue(row, ACKNOWLEDGE_COLUMN_ID);
    }

    @Step("I return a cell text from notification identifier column")
    public String getTextFromNotificationIdentifierCell(int row) {
        DelayUtils.sleep(1000);
        log.info("Returning cell text from notification identifier column in row: {}", row);
        return fmsmTable.getCellValue(row, NOTIFICATION_IDENTIFIER_COLUMN_ID);
    }

    @Step("I return a cell text from note column")
    public String getTextFromNoteStatusCell(int row, String value) {
        fmsmTable.getCell(row, NOTE_COLUMN_ID).waitForExpectedValue(wait, value);
        log.info("Returning cell text from note column in row: {}", row);
        return fmsmTable.getCellValue(row, NOTE_COLUMN_ID);
    }

    @Step("I click on Additional Text tab")
    public void clickOnAdditionalTextTab() {
        createButton(ADDITIONAL_TEXT_TAB_ID).click();
        log.info("Clicking on Additional Text tab");
    }

    @Step("I click on Alarm Details tab")
    public void clickOnAlarmDetailsTab() {
        createButton(ALARM_DETAILS_TAB_ID).click();
        log.info("Clicking on Alarm Details tab");
    }

    @Step("I click on Same MO Details tab")
    public void clickOnSameMODetailsTab() {
        createButton(SAME_MO_DETAILS_TAB_ID).click();
        log.info("Clicking on Same Mo Details tab");
    }

    @Step("I click on Same MO Alarms tab")
    public void clickOnSameMOAlarmsTab() {
        createButton(SAME_MO_ALARMS_TAB_ID).click();
        log.info("Clicking on Same Mo Alarms tab");
    }

    @Step("I get adapter name from Alarms Details Tab")
    public String getAdapterNameValueFromAlarmDetailsTab() {
        log.info("Checking adapter name value from Alarm Details Tab");
        return getPropertyPanel().getPropertyValue(ADAPTER_NAME_VALUE);
    }

    @Step("I get notification identifier from Alarms Details Tab")
    public String getNotificationIdentifierValueFromAlarmDetailsTab() {
        log.info("Checking notification identifier value from Alarm Details Tab");
        return getPropertyPanel().getPropertyValue(NOTIFICATION_IDENTIFIER_VALUE);
    }

    @Step("I get perceived severity from Alarms Details Tab")
    public String getPerceivedSeverityValueFromAlarmDetailsTab() {
        log.info("Checking perceived severity value from Alarm Details Tab");
        return getPropertyPanel().getPropertyValue(PERCEIVED_SEVERITY_VALUE);
    }

    @Step("I check if Same MO Alarms Table is visible")
    public boolean checkVisibilityOfSameMOAlarmsTable() {
        log.info("Checking visibility of Same MO Alarms Table");
        DelayUtils.waitForPageToLoad(driver, wait);
        return driver.getPageSource().contains(SAME_MO_ALARMS_TABLE_ID);
    }

    @Step("I check page title")
    public boolean checkIfPageTitleIsCorrect(String pageTitleLabel) {
        DelayUtils.sleep(1000);
        log.info("Checking page title: {}", pageTitleLabel);
        for (int i = 0; i < 100; i++) {
            if (driver.getPageSource().contains(pageTitleLabel)) {
                return true;
            }
            DelayUtils.sleep(50);
        }
        return false;
    }

    @Step("I check if additional text is displayed")
    public boolean isAdditionalTextDisplayed(String expectedText, String windowId) {
        List<String> text = ListApp.createFromParent(driver, wait, windowId).getValues();
        if (text.contains(expectedText)) {
            log.info("Expected additional text {} is displayed", expectedText);
            return true;
        } else {
            log.info("Expected additional text {} is not displayed", expectedText);
            return false;
        }
    }

    @Step("Search in view for specific attribute")
    public void searchInView(String searchedAttribute, String widgetId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        AdvancedSearch.createByWidgetId(driver, wait, widgetId).fullTextSearch(searchedAttribute);
        log.info("Searching in {} for text {}", widgetId, searchedAttribute);
    }

    private OldPropertyPanel getPropertyPanel() {
        return OldPropertyPanel.createById(driver, wait, PROPERTY_PANEL_ID);
    }
}
