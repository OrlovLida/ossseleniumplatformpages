package com.oss.pages.faultmanagement;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.framework.widgets.tablewidget.FMSMTable;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Bartosz Nowak
 */
public class WAMVPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(WAMVPage.class);
    private static final String TABLE_AREA2_WIDGET_ID = "_AREA2AlarmListTable";
    private static final String BUTTON_ACK_TEST_ID = "Acknowledge";
    private static final String BUTTON_DEACK_TEST_ID = "Deacknowledge";
    private static final String BUTTON_NOTE_TEST_ID = "Edit Note";
    private static final String ACKNOWLEDGE_COLUMN_ID = "cell-row-col-acknowledge";
    private static final String NOTIFICATION_IDENTIFIER_COLUMN_ID = "cell-row-col-notificationIdentifier";
    private static final String NOTE_COLUMN_ID = "cell-row-col-note";
    private static final String ADDITIONAL_TEXT_TAB_ID = "tab__AREA3AdditionalTextTab";
    private static final String ALARM_DETAILS_TAB_ID = "tab__AREA3AlarmDetailsTab";
    private static final String SAME_MO_DETAILS_TAB_ID = "tab__AREA3SameMODetailsTab";
    private static final String SAME_MO_ALARMS_TAB_ID = "tab__AREA3SameMOAlarmsTab";
    private static final String SAME_MO_ALARMS_TABLE_ID = "_AREA3SameMOAlarms";

    private final FMSMTable fmsmTable = FMSMTable.createById(driver, wait, TABLE_AREA2_WIDGET_ID);

    public WAMVPage(WebDriver driver) {
        super(driver);
    }

    public WAMVPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        PageFactory.initElements(driver, this);
    }

    @Step("I initialize new WAMV")
    public static WAMVPage createWAMV(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 90);
        log.info("Initialization of WAMVPage");
        return new WAMVPage(driver, wait);
    }

//    @Step("I create an FMSMTable")
//    private FMSMTable createArea2Table() {
//        return FMSMTable.createById(driver, wait, TABLE_AREA2_WIDGET_ID);
//    }

    @Step("I create an SameMOAlarmsTable")
    private FMSMTable createSameMOAlarmsTable() {
        log.info("Creating Same MO Alarms Table");
        return FMSMTable.createById(driver, wait, SAME_MO_ALARMS_TABLE_ID);
    }

    @Step("I create an button")
    private Button createButton(String buttonId) {
        log.info("Creating a button");
        return Button.createById(driver, buttonId);
    }

    @Step("I choose a selected row from a list")
    public void selectSpecificRow(int row) {
        fmsmTable.selectRow(row);
        log.info("Selecting row {} on alarm list", row);
    }

    @Step("I click on acknowledge button")
    public void clickOnAckButton() {
        createButton(BUTTON_ACK_TEST_ID).click();
        log.info("Clicking on acknowledge button");
    }

    @Step("I click on deacknowledge button")
    public void clickOnDeackButton() {
        createButton(BUTTON_DEACK_TEST_ID).click();
        log.info("Clicking on deacknowledge button");
    }

    @Step("I add note to alarm")
    public void addNote(String note) {
        createButton(BUTTON_NOTE_TEST_ID).click();
        EditNoteWizardPage editNote = new EditNoteWizardPage(driver);
        editNote.typeNote(note);
        log.info("Adding note: {}", note);
    }

    @Step("I return a cell text from acknowledge state column")
    public String getTitleFromAckStatusCell(int row) {
        log.info("Returning cell text from acknowledge state column in row: {}", row);
        return fmsmTable.getCellValueById(row, ACKNOWLEDGE_COLUMN_ID);
    }

    @Step("I return a cell text from notification identifier column")
    public String getTextFromNotificationIdentifierCell(int row) {
        log.info("Returning cell text from notification identifier column in row: {}", row);
        return fmsmTable.getCellValueById(row, NOTIFICATION_IDENTIFIER_COLUMN_ID);
    }

    @Step("I return a cell text from note column")
    public String getTextFromNoteStatusCell(int row) {
        log.info("Returning cell text from note column in row: {}", row);
        return fmsmTable.getCellValueById(row, NOTE_COLUMN_ID);
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
        OldPropertyPanel propertyPanel = OldPropertyPanel.create(driver, wait);
        log.info("Checking adapter name value from Alarm Details Tab");
        return propertyPanel.getPropertyValue("Adapter Name");
    }

    @Step("I get notification identifier from Alarms Details Tab")
    public String getNotificationIdentifierValueFromAlarmDetailsTab() {
        OldPropertyPanel propertyPanel = OldPropertyPanel.create(driver, wait);
        log.info("Checking notification identifier value from Alarm Details Tab");
        return propertyPanel.getPropertyValue("Notification Identifier");
    }

    @Step("I check if Same MO Alarms Table is visible")
    public boolean checkVisibilityOfSameMOAlarmsTable() {
        log.info("Checking visibility of Same MO Alarms Table");
        return driver.getPageSource().contains(SAME_MO_ALARMS_TABLE_ID);
    }

    public boolean checkIfPageTitleIsCorrect(String pageTitleLabel) {
        for (int i = 0; i < 100; i++) {
            if (driver.getPageSource().contains(pageTitleLabel)) {
                return true;
            }
            DelayUtils.sleep(50);
        }
        return false;
    }
}
