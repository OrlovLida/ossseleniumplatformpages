package com.oss.pages.iaa.faultmanagement;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.prompts.Modal;
import com.oss.framework.components.search.AdvancedSearch;
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
    private static final String BUTTON_ALARM_DETAILS_ID = "Details";
    private static final String ACKNOWLEDGE_COLUMN_ID = "cell-row-col-acknowledge";
    private static final String NOTIFICATION_IDENTIFIER_COLUMN_ID = "cell-row-col-notificationIdentifier";
    private static final String NOTE_COLUMN_ID = "cell-row-col-note";
    private static final String MO_IDENTIFIER_COLUMN_ID = "cell-row-col-moIdentifier";
    private static final String EVENT_TIME_COLUMN_ID = "cell-row-col-eventTime";
    private static final String EVENT_TIME_PATTERN = "HH:mm:ss yyyy-MM-dd";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(EVENT_TIME_PATTERN);

    private static final String PROPERTY_PANEL_IN_MODAL_ID = "card-content__modalWindow";
    private static final String SAME_MO_ALARMS_TABLE_ID = "area3-mo-alarms";
    private static final String NOTIFICATION_IDENTIFIER_VALUE = "Notification Identifier";

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
        WebDriverWait wait = new WebDriverWait(driver,  Duration.ofSeconds(90));
        log.info("Initialization of WAMV page");
        return new WAMVPage(driver, wait);
    }

    @Step("I create a SameMOAlarmsTable")
    private FMSMTable createSameMOAlarmsTable() {
        log.info("Creating Same MO Alarms Table");
        return FMSMTable.createById(driver, wait, SAME_MO_ALARMS_TABLE_ID);
    }

    @Step("I create a button")
    protected Button createButton(String buttonId) {
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
        DelayUtils.waitForPageToLoad(driver, wait);
        createButton(BUTTON_NOTE_TEST_ID).click();
        DelayUtils.sleep(1000);
        EditNoteWizardPage editNote = new EditNoteWizardPage(driver);
        editNote.typeNote(note);
        log.info("Adding note: {}", note);
    }

    @Step("Open alarm details from AREA2")
    public void openAlarmDetails() {
        createButton(BUTTON_ALARM_DETAILS_ID).click();
        log.info("Open alarm details from AREA2");
    }

    @Step("I get notification identifier from Alarms Details Modal View")
    public String getNotificationIdentifierFromAlarmDetailsModal() {
        log.info("Checking notification identifier value from Alarm Details Modal View");
        DelayUtils.waitForPageToLoad(driver, wait);
        return getPropertyPanel().getPropertyValue(NOTIFICATION_IDENTIFIER_VALUE);
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
        DelayUtils.waitForPageToLoad(driver, wait);
        fmsmTable.getCell(row, NOTE_COLUMN_ID).waitForExpectedValue(wait, value);
        log.info("Returning cell text from note column in row: {}", row);
        return fmsmTable.getCellValue(row, NOTE_COLUMN_ID);
    }

    @Step("I return a cell text from MO Identifier column")
    public String getTextFromMOIdentifierCell(int row) {
        log.info("Returning cell text from MO Identifier  in row: {}", row);
        return fmsmTable.getCellValue(row, MO_IDENTIFIER_COLUMN_ID);
    }

    @Step("I return a cell text from Event Time column")
    public String getTextFromEventTimeCell(int row) {
        log.info("Returning cell text from Event Time in row: {}", row);
        return fmsmTable.getCellValue(row, EVENT_TIME_COLUMN_ID);
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

    @Step("Search in view for specific attribute")
    public void searchInView(String searchedAttribute, String widgetId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        AdvancedSearch.createByWidgetId(driver, wait, widgetId).fullTextSearch(searchedAttribute);
        log.info("Searching in {} for text {}", widgetId, searchedAttribute);
    }

    @Step("Sorting column {columnHeader} Ascending")
    public void sortColumnByASC(String columnHeader) {
        fmsmTable.sortColumnByASC(columnHeader);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking on Column Options button - sort by ASC");
    }

    @Step("Sorting column {columnHeader} Descending")
    public void sortColumnByDESC(String columnHeader) {
        fmsmTable.sortColumnByDESC(columnHeader);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking on Column Options button - sort by DESC");
    }

    @Step("Get Title of Modal View")
    public String getModalViewTitle() {
        return getModal().getModalTitle();
    }

    @Step("Close Modal View")
    public void closeModalView() {
        getModal().clickClose();
    }

    @Step("Check if dates are sorted")
    public boolean areDatesSorted(String date1, String date2) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return LocalDateTime.parse(date2, DATE_TIME_FORMATTER)
                .isAfter(LocalDateTime.parse(date1, DATE_TIME_FORMATTER));
    }

    private OldPropertyPanel getPropertyPanel() {
        return OldPropertyPanel.createById(driver, wait, PROPERTY_PANEL_IN_MODAL_ID);
    }

    private Modal getModal() {
        return Modal.create(driver, wait);
    }
}
