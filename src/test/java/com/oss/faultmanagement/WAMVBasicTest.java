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

import java.util.Arrays;
import java.util.List;

/**
 * @author Bartosz Nowak
 */
@Listeners({TestListener.class})
public class WAMVBasicTest extends BaseTestCase {
    private static final Logger log = LoggerFactory.getLogger(WAMVBasicTest.class);
    private final List<String> ackValues = Arrays.asList("True", "False");
    private final List<String> noteValues = Arrays.asList("Selenium_automated_test", "");
    private final String ACKNOWLEDGE_VALUE = "True";
    private final String DEACKNOWLEDGE_VALUE = "False";
    private final String TEST_NOTE_VALUE = "Selenium_automated_test";
    private final String EMPTY_NOTE_VALUE = "";
    private static final String ALARM_MANAGEMENT_VIEW_ID = "_UserViewsListALARM_MANAGEMENT";


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
    @Test(priority = 2, testName = "Check Ack., Deack. and Note options", description = "Acknowledge, Deacknowledge, Note")
    @Description("I verify if acknowledge, deacknowledge and note options work in WAMV")
    public void openSelectedWAMVAndCheckAckDeackNoteFunctionality(
            @Optional("Selenium_test_alarm_list") String alarmListName,
            @Optional("2") int alarmListRow,
            @Optional("0") int alarmManagementViewRow
    ) {
        try {
            fmDashboardPage.searchInSpecificView(ALARM_MANAGEMENT_VIEW_ID, alarmListName);
            wamvPage = fmDashboardPage.openSelectedView(ALARM_MANAGEMENT_VIEW_ID, alarmManagementViewRow);
            wamvPage.selectSpecificRow(alarmListRow);
            wamvPage.clickOnAckButton();
            String ackValue = wamvPage.getTitleFromAckStatusCell(alarmListRow, ACKNOWLEDGE_VALUE);
            Assert.assertEquals(ackValue, ACKNOWLEDGE_VALUE);
//            wamvPage.addNote(TEST_NOTE_VALUE);
//            Assert.assertEquals(wamvPage.getTextFromNoteStatusCell(alarmListRow, TEST_NOTE_VALUE), TEST_NOTE_VALUE);
            wamvPage.clickOnDeackButton();
            String deackValue = wamvPage.getTitleFromAckStatusCell(alarmListRow, DEACKNOWLEDGE_VALUE);
            Assert.assertEquals(deackValue, DEACKNOWLEDGE_VALUE);
//            wamvPage.addNote(EMPTY_NOTE_VALUE);
//            Assert.assertEquals(wamvPage.getTextFromNoteStatusCell(alarmListRow, EMPTY_NOTE_VALUE), EMPTY_NOTE_VALUE);
//            for (int i = 0; i <= 1; i++) {
//                if (i == 0) {
//                    wamvPage.clickOnAckButton();
//                } else {
//                    wamvPage.clickOnDeackButton();
//                }
//                waitForAckColumnChange(alarmListRow, ackValues.get(i));
//                Assert.assertEquals(wamvPage.getTitleFromAckStatusCell(alarmListRow), ackValues.get(i));
//                wamvPage.addNote(noteValues.get(i));
//                waitForNoteColumnChange(alarmListRow, noteValues.get(i));
//                Assert.assertEquals(wamvPage.getTextFromNoteStatusCell(alarmListRow), noteValues.get(i));
//
//            }
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"alarmListName", "alarmListRow", "alarmManagementViewRow", "adapterName"})
    @Test(priority = 3, testName = "Check tabs from Area 3", description = "Check tabs from Area 3")
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
            Assert.assertTrue(wamvPage.checkIfPageTitleIsCorrect(alarmListName));
            wamvPage.selectSpecificRow(alarmListRow);
            wamvPage.clickOnSameMOAlarmsTab();
            Assert.assertTrue(wamvPage.checkVisibilityOfSameMOAlarmsTable());
            wamvPage.clickOnSameMODetailsTab();
            wamvPage.clickOnAdditionalTextTab();
            wamvPage.clickOnAlarmDetailsTab();
            Assert.assertEquals(wamvPage.getAdapterNameValueFromAlarmDetailsTab(), adapterName);
            Assert.assertEquals(wamvPage.getNotificationIdentifierValueFromAlarmDetailsTab(), wamvPage.getTextFromNotificationIdentifierCell(alarmListRow));

        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

//    public void waitForAckColumnChange(int alarmListRow, String type) {
//        for (int i = 0; i < 25; i++) {
//            if (wamvPage.getTitleFromAckStatusCell(alarmListRow).equals(type)) {
//                break;
//            } else {
//                DelayUtils.sleep(100);
//            }
//        }
//    }
//
//    public void waitForNoteColumnChange(int alarmListRow, String type) {
//        for (int i = 0; i < 25; i++) {
//            if (wamvPage.getTextFromNoteStatusCell(alarmListRow).equals(type)) {
//                break;
//            } else {
//                DelayUtils.sleep(100);
//            }
//        }
//    }
}
