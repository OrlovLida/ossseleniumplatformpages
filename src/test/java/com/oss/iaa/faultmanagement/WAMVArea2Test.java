package com.oss.iaa.faultmanagement;

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
import com.oss.pages.iaa.faultmanagement.FMSMDashboardPage;
import com.oss.pages.iaa.faultmanagement.WAMVPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

/**
 * @author Bartosz Nowak
 * @author Agnieszka Podolska
 */
@Listeners({TestListener.class})
public class WAMVArea2Test extends BaseTestCase {
    private static final Logger log = LoggerFactory.getLogger(WAMVArea2Test.class);

    private final String ACKNOWLEDGE_VALUE = "True";
    private final String DEACKNOWLEDGE_VALUE = "False";
    private final String TEST_NOTE_VALUE = "Selenium_automated_test";
    private final String EMPTY_NOTE_VALUE = "";
    private static final String ALARM_MANAGEMENT_VIEW_ID = "_UserViewsListALARM_MANAGEMENT";
    private static final String ALARM_DETAILS_MODAL_VIEW_TITLE = "Alarm Details";

    private FMSMDashboardPage fmsmDashboardPage;
    private WAMVPage wamvPage;

    @Parameters({"chosenDashboard", "alarmListName", "alarmManagementViewRow"})
    @BeforeClass
    public void goToFMDashboardPage(
            @Optional("FaultManagement") String chosenDashboard,
            @Optional("Selenium_test_alarm_list") String alarmListName,
            @Optional("0") int alarmManagementViewRow
    ) {
        fmsmDashboardPage = FMSMDashboardPage.goToPage(driver, BASIC_URL, chosenDashboard);
    }

    @Parameters({"alarmListName", "alarmManagementViewRow"})
    @Test(priority = 1, testName = "Check WAMV Title", description = "WAMV title check")
    @Description("I verify if Web Alarm Management View opens and its title is correct")
    public void openSelectedWAMVAndCheckPageTitle(
            @Optional("Selenium_test_alarm_list") String alarmListName,
            @Optional("0") int alarmManagementViewRow
    ) {
        try {
            wamvPage = searchAndOpenWamv(alarmListName, alarmManagementViewRow);
            Assert.assertTrue(wamvPage.checkIfPageTitleIsCorrect(alarmListName));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"alarmListRow"})
    @Test(priority = 2, testName = "Check Acknowledge and Deacknowledge options", description = "Check Acknowledge and Deacknowledge options")
    @Description("I verify if acknowledge and deacknowledge work in WAMV")
    public void openSelectedWAMVAndCheckAckDeackFunctionality(
            @Optional("0") int alarmListRow
    ) {
        try {
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

    @Parameters({"alarmListRowNote"})
    @Test(priority = 3, testName = "Check Note option", description = "Check Note option")
    @Description("I verify if note option works in WAMV")
    public void openSelectedWAMVAndCheckNoteFunctionality(
            @Optional("1") int alarmListRowNote
    ) {
        try {
            wamvPage.selectSpecificRow(alarmListRowNote);
            wamvPage.addNote(TEST_NOTE_VALUE);
            Assert.assertEquals(wamvPage.getTextFromNoteStatusCell(alarmListRowNote, TEST_NOTE_VALUE), TEST_NOTE_VALUE);
            wamvPage.addNote(EMPTY_NOTE_VALUE);
            Assert.assertEquals(wamvPage.getTextFromNoteStatusCell(alarmListRowNote, EMPTY_NOTE_VALUE), EMPTY_NOTE_VALUE);
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"alarmListRowAlarmDetails"})
    @Test(priority = 4, testName = "Check Alarm details from context actions in AREA2", description = "Check Alarm details from context actions  in AREA2")
    @Description("I verify if alarm details from context actions works in AREA2")
    public void CheckAlarmDetailsInArea2(
            @Optional("0") int alarmListRowAlarmDetails
    ) {
        try {
            String notificationId = wamvPage.getTextFromNotificationIdentifierCell(alarmListRowAlarmDetails);
            wamvPage.selectSpecificRow(alarmListRowAlarmDetails);
            wamvPage.openAlarmDetails();
            Assert.assertEquals(wamvPage.getModalViewTitle(), ALARM_DETAILS_MODAL_VIEW_TITLE);
            Assert.assertEquals(notificationId, wamvPage.getNotificationIdentifierFromAlarmDetailsModal());
            wamvPage.closeModalView();
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