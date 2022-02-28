package com.oss.faultmanagement;

import com.oss.BaseTestCase;
import com.oss.pages.faultmanagement.FMDashboardPage;
import com.oss.pages.faultmanagement.WAMVPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

/**
 * @author Bartosz Nowak
 */
@Listeners({TestListener.class})
public class WAMVBasicTest extends BaseTestCase {
    private static final Logger log = LoggerFactory.getLogger(WAMVBasicTest.class);
    private final String ACKNOWLEDGE_VALUE = "True";
    private final String DEACKNOWLEDGE_VALUE = "False";
    private final String TEST_NOTE_VALUE = "Selenium_automated_test";
    private final String EMPTY_NOTE_VALUE = "";
    private final String EXPECTED_TEXT = "SeleniumTest";
    private final String PERCEIVED_SEVERITY = "Critical";
    private static final String ALARM_MANAGEMENT_VIEW_ID = "_UserViewsListALARM_MANAGEMENT";
    private static final String AREA_3_WINDOW_ID = "AREA3";
    private static final String AREA_2_WINDOW_ID = "AREA2";

    private FMDashboardPage fmDashboardPage;
    private WAMVPage wamvPage;

    @BeforeMethod
    public void goToFMDashboardPage() {
        fmDashboardPage = FMDashboardPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"alarmListName", "alarmManagementViewRow"})
    @Test(priority = 1, testName = "Check WAMV Title", description = "WAMV title check")
    @Description("I verify if Web Alarm Management View opens and its title is correct")
    public void openSelectedWAMVAndCheckPageTitle(
            @Optional("Selenium_test_alarm_list") String alarmListName,
            @Optional("0") int alarmManagementViewRow
    ) {
        try {
            fmDashboardPage.searchInSpecificView(ALARM_MANAGEMENT_VIEW_ID, alarmListName);
            wamvPage = fmDashboardPage.openSelectedView(ALARM_MANAGEMENT_VIEW_ID, alarmManagementViewRow);
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
            fmDashboardPage.searchInSpecificView(ALARM_MANAGEMENT_VIEW_ID, alarmListName);
            wamvPage = fmDashboardPage.openSelectedView(ALARM_MANAGEMENT_VIEW_ID, alarmManagementViewRow);
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
            fmDashboardPage.searchInSpecificView(ALARM_MANAGEMENT_VIEW_ID, alarmListName);
            wamvPage = fmDashboardPage.openSelectedView(ALARM_MANAGEMENT_VIEW_ID, alarmManagementViewRow);
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
    @Test(priority = 4, testName = "Check tabs from Area 3", description = "Check tabs from Area 3")
    @Description("I verify if Tabs from Area 3 work properly")
    public void openSelectedWAMVAndCheckArea3Tabs(
            @Optional("Selenium_test_alarm_list") String alarmListName,
            @Optional("0") int alarmListRow,
            @Optional("0") int alarmManagementViewRow,
            @Optional("AdapterAlarmGeneratorFromFile") String adapterName
    ) {
        try {
            fmDashboardPage.searchInSpecificView(ALARM_MANAGEMENT_VIEW_ID, alarmListName);
            wamvPage = fmDashboardPage.openSelectedView(ALARM_MANAGEMENT_VIEW_ID, alarmManagementViewRow);
            wamvPage.selectSpecificRow(alarmListRow);
            wamvPage.clickOnSameMOAlarmsTab();
            Assert.assertTrue(wamvPage.checkVisibilityOfSameMOAlarmsTable());
            wamvPage.clickOnSameMODetailsTab();
            wamvPage.clickOnAdditionalTextTab();
            Assert.assertTrue(wamvPage.checkDisplayedText(EXPECTED_TEXT, AREA_3_WINDOW_ID));
            wamvPage.clickOnAlarmDetailsTab();
            Assert.assertEquals(wamvPage.getAdapterNameValueFromAlarmDetailsTab(), adapterName);
            Assert.assertEquals(wamvPage.getNotificationIdentifierValueFromAlarmDetailsTab(), wamvPage.getTextFromNotificationIdentifierCell(alarmListRow));

        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"alarmListName", "alarmListRow", "alarmManagementViewRow"})
    @Test(priority = 5, testName = "Searching in WAMV", description = "Searching in WAMV")
    @Description("I verify if searching in WAMV works properly")
    public void openSelectedWAMVAndCheckSearch(
            @Optional("Selenium_test_alarm_list") String alarmListName,
            @Optional("0") int alarmListRow,
            @Optional("0") int alarmManagementViewRow
    ) {
        try {
            fmDashboardPage.searchInSpecificView(ALARM_MANAGEMENT_VIEW_ID, alarmListName);
            wamvPage = fmDashboardPage.openSelectedView(ALARM_MANAGEMENT_VIEW_ID, alarmManagementViewRow);
            wamvPage.searchInView(PERCEIVED_SEVERITY, AREA_2_WINDOW_ID);
            wamvPage.selectSpecificRow(alarmListRow);
            wamvPage.clickOnAdditionalTextTab();
            wamvPage.clickOnAlarmDetailsTab();
            Assert.assertEquals(wamvPage.getPerceivedSeverityValueFromAlarmDetailsTab(), PERCEIVED_SEVERITY);

        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }
}