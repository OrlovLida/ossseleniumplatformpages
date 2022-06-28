package com.oss.faultmanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.faultmanagement.FMSMDashboardPage;
import com.oss.pages.faultmanagement.WAMVPage;
import com.oss.pages.faultmanagement.wamv.Area3Page;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

/**
 * @author Bartosz Nowak
 * @author Agnieszka Podolska
 */
@Listeners({TestListener.class})
public class WAMVBasicTest extends BaseTestCase {
    private static final Logger log = LoggerFactory.getLogger(WAMVBasicTest.class);
    private final String ACKNOWLEDGE_VALUE = "True";
    private final String DEACKNOWLEDGE_VALUE = "False";
    private final String TEST_NOTE_VALUE = "Selenium_automated_test";
    private final String EMPTY_NOTE_VALUE = "";
    private final String EXPECTED_TEXT = "SeleniumTest";
    private final String PERCEIVED_SEVERITY = "CRITICAL";
    private static final String ALARM_MANAGEMENT_VIEW_ID = "_UserViewsListALARM_MANAGEMENT";
    private static final String AREA_3_WINDOW_ID = "AREA3";
    private static final String AREA_2_WINDOW_ID = "AREA2";

    private static final String SAME_MO_ALARMS_TABLE_ID = "area3-mo-alarms";
    private static final String CORRELATED_ALARMS_TABLE_ID = "area3-correlated-alarms";
    private static final String ALARM_CHANGES_TABLE_ID = "area3-alarm-changes";
    private static final String TROUBLE_TICKETS_TABLE_ID = "area3-trouble-tickets";
    private static final String OUTAGES_TABLE_ID = "area3-outages";
    private static final String ROOT_CAUSE_ALARMS_TABLE_ID = "area3-root-cause-alarms";
    private static final String LOCATION_ALARMS_TABLE_ID = "area3-location-alarms";
    private static final String HISTORICAL_ALARMS_TABLE_ID = "area3-historical-alarms";
    private static final String SHOW_EMPTY_MOST_WANTED_TAB = "most-wanted-alarm-details";

    private FMSMDashboardPage fmsmDashboardPage;
    private WAMVPage wamvPage;
    private Area3Page area3Page;

    @Parameters("chosenDashboard")
    @BeforeClass
    public void goToFMDashboardPage(
            @Optional("FaultManagement") String chosenDashboard
    ) {
        fmsmDashboardPage = fmsmDashboardPage.goToPage(driver, BASIC_URL, chosenDashboard);
        area3Page = new Area3Page(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Parameters({"alarmListName", "alarmManagementViewRow"})
    @Test(priority = 1, testName = "Check WAMV Title", description = "WAMV title check")
    @Description("I verify if Web Alarm Management View opens and its title is correct")
    public void openSelectedWAMVAndCheckPageTitle(
            @Optional("Selenium_test_alarm_list") String alarmListName,
            @Optional("0") int alarmManagementViewRow
    ) {
        try {
            fmsmDashboardPage.searchInView(ALARM_MANAGEMENT_VIEW_ID, alarmListName);
            wamvPage = fmsmDashboardPage.openSelectedView(ALARM_MANAGEMENT_VIEW_ID, alarmManagementViewRow);
            Assert.assertTrue(wamvPage.checkIfPageTitleIsCorrect(alarmListName));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"alarmListName", "alarmListRow", "alarmManagementViewRow"})
    @Test(priority = 2, testName = "Check Acknowledge and Deacknowledge options", description = "Check Acknowledge and Deacknowledge options")
    @Description("I verify if acknowledge and deacknowledge work in WAMV")
    public void openSelectedWAMVAndCheckAckDeackFunctionality(
            @Optional("Selenium_test_alarm_list") String alarmListName,
            @Optional("0") int alarmListRow,
            @Optional("0") int alarmManagementViewRow
    ) {
        try {
            searchAndOpenWamv(alarmListName, alarmManagementViewRow);
            wamvPage.selectSpecificRow(alarmListRow);
            wamvPage.clickOnAckButton();
            Assert.assertEquals(wamvPage.getTitleFromAckStatusCell(alarmListRow, ACKNOWLEDGE_VALUE), ACKNOWLEDGE_VALUE);
            wamvPage.clickOnDeackButton();
            Assert.assertEquals(wamvPage.getTitleFromAckStatusCell(alarmListRow, DEACKNOWLEDGE_VALUE), DEACKNOWLEDGE_VALUE);
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"alarmListName", "alarmListRow", "alarmManagementViewRow"})
    @Test(priority = 3, testName = "Check Note option", description = "Check Note option")
    @Description("I verify if note option works in WAMV")
    public void openSelectedWAMVAndCheckNoteFunctionality(
            @Optional("Selenium_test_alarm_list") String alarmListName,
            @Optional("2") int alarmListRow,
            @Optional("0") int alarmManagementViewRow
    ) {
        try {
            searchAndOpenWamv(alarmListName, alarmManagementViewRow);
            wamvPage.selectSpecificRow(alarmListRow);
            wamvPage.addNote(TEST_NOTE_VALUE);
            Assert.assertEquals(wamvPage.getTextFromNoteStatusCell(alarmListRow, TEST_NOTE_VALUE), TEST_NOTE_VALUE);
            wamvPage.addNote(EMPTY_NOTE_VALUE);
            Assert.assertEquals(wamvPage.getTextFromNoteStatusCell(alarmListRow, EMPTY_NOTE_VALUE), EMPTY_NOTE_VALUE);
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"alarmListName", "alarmListRow", "alarmManagementViewRow", "adapterName"})
    @Test(priority = 4, testName = "Check  Area 3 tabs with tables from", description = "Check  Area 3 tabs with tables from")
    @Description("I verify if tables in Tabs from Area 3 are visible")
    public void openSelectedWAMVAndCheckArea3Tabs(
            @Optional("Selenium_test_alarm_list") String alarmListName,
            @Optional("0") int alarmListRow,
            @Optional("0") int alarmManagementViewRow,
            @Optional("AdapterAlarmGeneratorFromFile") String adapterName
    ) {
        try {
            searchAndOpenWamv(alarmListName, alarmManagementViewRow);
            wamvPage.selectSpecificRow(alarmListRow);
            area3Page.clickOnSameMOAlarmsTab();
            Assert.assertTrue(area3Page.checkIfTableExists(SAME_MO_ALARMS_TABLE_ID));
            area3Page.clickOnCorrelatedAlarmsTab();
            Assert.assertTrue(area3Page.checkIfTableExists(CORRELATED_ALARMS_TABLE_ID));
            area3Page.clickOnTroubleTicketsTab();
            Assert.assertTrue(area3Page.checkIfTableExists(TROUBLE_TICKETS_TABLE_ID));
            area3Page.clickRootCauseAlarmsTab();
            Assert.assertTrue(area3Page.checkIfTableExists(ROOT_CAUSE_ALARMS_TABLE_ID));
            area3Page.clickOnOutagesTab();
            Assert.assertTrue(area3Page.checkIfTableExists(OUTAGES_TABLE_ID));
            area3Page.clickOnLocationAlarmsTab();
            Assert.assertTrue(area3Page.checkIfTableExists(LOCATION_ALARMS_TABLE_ID));
            area3Page.clickOnHistoricalAlarmsTab();
            Assert.assertTrue(area3Page.checkIfTableExists(HISTORICAL_ALARMS_TABLE_ID));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"alarmListName", "alarmListRow", "alarmManagementViewRow"})
    @Test(priority = 5, testName = "Check MO Properties", description = "Check MO Properties")
    @Description("I verify if MO Properties works in WAMV")
    public void openWAMVAndCheckMoProperties(
            @Optional("Selenium_test_alarm_list") String alarmListName,
            @Optional("1") int alarmListRow,
            @Optional("0") int alarmManagementViewRow
    ) {
        try {
            searchAndOpenWamv(alarmListName, alarmManagementViewRow);
            wamvPage.selectSpecificRow(alarmListRow);
            area3Page.clickOnMOPropertiesTab();
            Assert.assertEquals(wamvPage.getTextFromMOIdentifierCell(alarmListRow), area3Page.getIdentifierFromMOPropertiesTab());
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"alarmListName", "alarmListRow", "alarmManagementViewRow", "adapterName"})
    @Test(priority = 6, testName = "Check Alarm Details", description = "Check Alarm Details")
    @Description("I verify if Alarm Details works in WAMV")
    public void openWAMVAndCheckAlarmDetails(
            @Optional("Selenium_test_alarm_list") String alarmListName,
            @Optional("1") int alarmListRow,
            @Optional("0") int alarmManagementViewRow,
            @Optional("AdapterAlarmGeneratorFromFile") String adapterName
    ) {
        try {
            searchAndOpenWamv(alarmListName, alarmManagementViewRow);
            wamvPage.selectSpecificRow(alarmListRow);
            area3Page.clickOnAlarmDetailsTab();
            Assert.assertEquals(area3Page.getAdapterNameFromAlarmDetailsTab(), adapterName);
            Assert.assertEquals(area3Page.getNotificationIdentifierFromAlarmDetailsTab(), wamvPage.getTextFromNotificationIdentifierCell(alarmListRow));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"alarmListName", "alarmListRow", "alarmManagementViewRow"})
    @Test(priority = 7, testName = "Check Show Empty Option", description = "Check Show Empty Option")
    @Description("I verify if Show Empty Option works")
    public void checkShowEmptyOption(
            @Optional("Selenium_test_alarm_list") String alarmListName,
            @Optional("1") int alarmListRow,
            @Optional("0") int alarmManagementViewRow
    ) {
        try {
            searchAndOpenWamv(alarmListName, alarmManagementViewRow);
            wamvPage.selectSpecificRow(alarmListRow);
            area3Page = new Area3Page(driver);
            area3Page.clickOnMostWantedTab();
            int nonEmptyRows = area3Page.countVisibleProperties();
            area3Page.checkShowEmptyCheckbox(SHOW_EMPTY_MOST_WANTED_TAB);
            int allRows = area3Page.countVisibleProperties();
            Assert.assertTrue(nonEmptyRows < allRows);
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"alarmListName", "alarmListRow", "alarmManagementViewRow"})
    @Test(priority = 8, testName = "Check Alarm Changes", description = "Check Alarm Changes")
    @Description("I verify if Alarm Changes works in WAMV")
    public void openWAMVAndCheckAlarmChanges(
            @Optional("Selenium_test_alarm_list") String alarmListName,
            @Optional("1") int alarmListRow,
            @Optional("0") int alarmManagementViewRow
    ) {
        try {
            searchAndOpenWamv(alarmListName, alarmManagementViewRow);
            wamvPage.selectSpecificRow(alarmListRow);
//            TODO Test will be working after fix OSSNGSA-10793, add assert - ack deack
            area3Page.clickOnAlarmChangesTab();
            Assert.assertTrue(area3Page.checkIfTableExists(ALARM_CHANGES_TABLE_ID));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"alarmListName", "alarmListRow", "alarmManagementViewRow"})
    @Test(priority = 9, testName = "Check Know How", description = "Check Know How")
    @Description("I verify if Know How works in WAMV")
    public void openWAMVAndCheckKnowHow(
            @Optional("Selenium_test_alarm_list") String alarmListName,
            @Optional("1") int alarmListRow,
            @Optional("0") int alarmManagementViewRow
    ) {
        try {
            searchAndOpenWamv(alarmListName, alarmManagementViewRow);
            wamvPage.selectSpecificRow(alarmListRow);
            area3Page.clickOnKnowHowTab();
            //TODO add assert
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"alarmListName", "alarmListRow", "alarmManagementViewRow"})
    @Test(priority = 10, testName = "Check Additional Text Tab", description = "Check Additional Text Tab")
    @Description("I verify if Additional Text works in WAMV")
    public void openWAMVAndCheckAdditionalText(
            @Optional("Selenium_test_alarm_list") String alarmListName,
            @Optional("0") int alarmListRow,
            @Optional("0") int alarmManagementViewRow
    ) {
        try {
            searchAndOpenWamv(alarmListName, alarmManagementViewRow);
            wamvPage.selectSpecificRow(alarmListRow);
            area3Page.clickOnAdditionalTextTab();
            Assert.assertTrue(area3Page.isAdditionalTextDisplayed(EXPECTED_TEXT, AREA_3_WINDOW_ID));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"alarmListName", "alarmListRow", "alarmManagementViewRow"})
    @Test(priority = 11, testName = "Searching in WAMV", description = "Searching in WAMV")
    @Description("I verify if searching in WAMV works properly")
    public void openWAMVAndCheckSearch(
            @Optional("Selenium_test_alarm_list") String alarmListName,
            @Optional("0") int alarmListRow,
            @Optional("0") int alarmManagementViewRow
    ) {
        try {
            searchAndOpenWamv(alarmListName, alarmManagementViewRow);
            wamvPage.searchInView(PERCEIVED_SEVERITY, AREA_2_WINDOW_ID);
            wamvPage.selectSpecificRow(alarmListRow);
            area3Page.clickOnAdditionalTextTab();
            area3Page.clickOnAlarmDetailsTab();
            Assert.assertEquals(area3Page.getPerceivedSeverityFromAlarmDetailsTab(), PERCEIVED_SEVERITY);
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    private void searchAndOpenWamv(String alarmListName, int alarmManagementViewRow) {
        fmsmDashboardPage.searchInView(ALARM_MANAGEMENT_VIEW_ID, alarmListName);
        wamvPage = fmsmDashboardPage.openSelectedView(ALARM_MANAGEMENT_VIEW_ID, alarmManagementViewRow);
    }
}