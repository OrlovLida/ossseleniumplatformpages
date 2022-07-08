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
 * @author Agnieszka Podolska
 */
@Listeners({TestListener.class})
public class WAMVArea3Test extends BaseTestCase {
    private static final Logger log = LoggerFactory.getLogger(WAMVArea3Test.class);

    private final String EXPECTED_TEXT = "SeleniumTest";
    private final String PERCEIVED_SEVERITY = "CRITICAL";
    private static final String ALARM_MANAGEMENT_VIEW_ID = "_UserViewsListALARM_MANAGEMENT";
    private static final String AREA_3_WINDOW_ID = "AREA3";
    private static final String AREA_2_WINDOW_ID = "AREA2";
    private static final String EVENT_TIME_COLUMN_HEADER = "col-eventTime";

    private static final String SAME_MO_ALARMS_TABLE_ID = "area3-mo-alarms";
    private static final String CORRELATED_ALARMS_TABLE_ID = "area3-correlated-alarms";
    private static final String ALARM_CHANGES_TABLE_ID = "area3-alarm-changes";
    private static final String TROUBLE_TICKETS_TABLE_ID = "area3-trouble-tickets";
    private static final String OUTAGES_TABLE_ID = "area3-outages";
    private static final String ROOT_CAUSE_ALARMS_TABLE_ID = "area3-root-cause-alarms";
    private static final String LOCATION_ALARMS_TABLE_ID = "area3-location-alarms";
    private static final String HISTORICAL_ALARMS_TABLE_ID = "area3-historical-alarms";
    private static final String SHOW_EMPTY_MOST_WANTED_TAB = "most-wanted-alarm-details";
    private static final String KNOW_HOW_TEXT_WIDGET_ID = "text-widget";
    private static final String KNOW_HOW_INPUT_ID = "KnowHowHTMLInput";
    private static final String KNOW_HOW_TEXT = "Selenium Test Know How";

    private FMSMDashboardPage fmsmDashboardPage;
    private WAMVPage wamvPage;
    private Area3Page area3Page;

    @Parameters({"chosenDashboard", "alarmListName", "alarmManagementViewRow"})
    @BeforeClass
    public void goToFMDashboardPage(
            @Optional("FaultManagement") String chosenDashboard,
            @Optional("Selenium_test_alarm_list") String alarmListName,
            @Optional("0") int alarmManagementViewRow
    ) {
        fmsmDashboardPage = fmsmDashboardPage.goToPage(driver, BASIC_URL, chosenDashboard);
        wamvPage = searchAndOpenWamv(alarmListName, alarmManagementViewRow);
        area3Page = new Area3Page(driver);
    }

    @Parameters({"alarmListRow"})
    @Test(priority = 1, testName = "Check  Area 3 tabs with tables", description = "Check  Area 3 tabs with tables")
    @Description("I verify if tables in Tabs from Area 3 are visible")
    public void openSelectedWAMVAndCheckArea3Tabs(
            @Optional("0") int alarmListRow
    ) {
        try {
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

    @Parameters({"alarmListRow"})
    @Test(priority = 2, testName = "Check MO Properties", description = "Check MO Properties")
    @Description("I verify if MO Properties works in WAMV")
    public void openWAMVAndCheckMoProperties(
            @Optional("1") int alarmListRow
    ) {
        try {
            wamvPage.selectSpecificRow(alarmListRow);
            area3Page.clickOnMOPropertiesTab();
            Assert.assertEquals(wamvPage.getTextFromMOIdentifierCell(alarmListRow), area3Page.getIdentifierFromMOPropertiesTab());
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"alarmListRow", "adapterName"})
    @Test(priority = 3, testName = "Check Alarm Details in AREA3", description = "Check Alarm Details in AREA3")
    @Description("I verify if Alarm Details works in WAMV in AREA3")
    public void openWAMVAndCheckAlarmDetails(
            @Optional("3") int alarmListRow,
            @Optional("AdapterAlarmGeneratorFromFile") String adapterName
    ) {
        try {
            wamvPage.selectSpecificRow(alarmListRow);
            area3Page.clickOnAlarmDetailsTab();
            Assert.assertEquals(area3Page.getAdapterNameFromAlarmDetailsTab(), adapterName);
            Assert.assertEquals(area3Page.getNotificationIdentifierFromAlarmDetailsTab(), wamvPage.getTextFromNotificationIdentifierCell(alarmListRow));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"alarmListRow"})
    @Test(priority = 4, testName = "Check Show Empty Option in Most Wanted Tab", description = "Check Show Empty Option in Most Wanted Tab")
    @Description("I verify if Show Empty Option works in Most Wanted Tab")
    public void checkShowEmptyOption(
            @Optional("1") int alarmListRow
    ) {
        try {
            wamvPage.selectSpecificRow(alarmListRow);
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

    @Parameters({"alarmListRow"})
    @Test(priority = 5, testName = "Check Alarm Changes", description = "Check Alarm Changes")
    @Description("I verify if Alarm Changes works in WAMV")
    public void openWAMVAndCheckAlarmChanges(
            @Optional("1") int alarmListRow
    ) {
        try {
            wamvPage.selectSpecificRow(alarmListRow);
//            TODO Test will be working after fix OSSNGSA-10793, add assert - ack deack
            area3Page.clickOnAlarmChangesTab();
            Assert.assertTrue(area3Page.checkIfTableExists(ALARM_CHANGES_TABLE_ID));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"alarmListRow"})
    @Test(priority = 6, testName = "Check Know How", description = "Check Know How")
    @Description("I verify if Know How works in WAMV")
    public void openWAMVAndCheckKnowHow(
            @Optional("0") int alarmListRow
    ) {
        try {
            wamvPage.selectSpecificRow(alarmListRow);
            area3Page.clickOnKnowHowTab();
            area3Page.clickEditButton();
            area3Page.setValueInHtmlEditor(KNOW_HOW_TEXT, KNOW_HOW_INPUT_ID);
            area3Page.clickSaveButton();
            Assert.assertEquals(area3Page.checkValueInHtmlEditor(KNOW_HOW_TEXT_WIDGET_ID), KNOW_HOW_TEXT);
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"alarmListRow"})
    @Test(priority = 7, testName = "Check Additional Text Tab", description = "Check Additional Text Tab")
    @Description("I verify if Additional Text works in WAMV")
    public void openWAMVAndCheckAdditionalText(
            @Optional("0") int alarmListRow
    ) {
        try {
            wamvPage.selectSpecificRow(alarmListRow);
            area3Page.clickOnAdditionalTextTab();
            Assert.assertTrue(area3Page.isAdditionalTextDisplayed(EXPECTED_TEXT, AREA_3_WINDOW_ID));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"alarmListRow"})
    @Test(priority = 8, testName = "Sorting in WAMV", description = "Sorting in WAMV")
    @Description("I verify if sorting in WAMV works properly")
    public void openWAMVAndCheckSorting(
            @Optional("0") int alarmListRow
    ) {
        try {
            wamvPage.selectSpecificRow(alarmListRow);
            wamvPage.sortColumnByDESC(EVENT_TIME_COLUMN_HEADER);
            wamvPage.sortColumnByASC(EVENT_TIME_COLUMN_HEADER);
            Assert.assertTrue(wamvPage.areDatesSorted(wamvPage.getTextFromEventTimeCell(0), wamvPage.getTextFromEventTimeCell(1)));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"alarmListRow"})
    @Test(priority = 9, testName = "Searching in WAMV", description = "Searching in WAMV")
    @Description("I verify if searching in WAMV works properly")
    public void openWAMVAndCheckSearch(
            @Optional("0") int alarmListRow
    ) {
        try {
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

    private WAMVPage searchAndOpenWamv(String alarmListName, int alarmManagementViewRow) {
        fmsmDashboardPage.searchInView(ALARM_MANAGEMENT_VIEW_ID, alarmListName);
        DelayUtils.sleep(8000); // TODO delete it after fix OSSNGSA-11102
        return fmsmDashboardPage.openSelectedView(ALARM_MANAGEMENT_VIEW_ID, alarmManagementViewRow);
    }
}