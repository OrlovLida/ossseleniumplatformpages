package com.oss.faultmanagement;

import com.oss.BaseTestCase;
import com.oss.bigdata.kpiview.KpiViewTest;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.faultmanagement.FMDashboardPage;
import com.oss.pages.faultmanagement.WAMVPage;
import com.oss.pages.platform.HomePage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Bartosz Nowak
 */
@Listeners({TestListener.class})
public class WAMVBasicTest extends BaseTestCase {
    private static final Logger log = LoggerFactory.getLogger(KpiViewTest.class);
    private final List<String> ackValues = Arrays.asList("True", "False");
    private final List<String> noteValues = Arrays.asList("Selenium_automated_test", "");

    private FMDashboardPage fmDashboardPage;
    private HomePage homePage;
    private WAMVPage wamvPage;

    @BeforeMethod
    public void goToFMDashboardPage() {
        fmDashboardPage = FMDashboardPage.goToPage(driver, BASIC_URL);
        homePage = new HomePage(driver);
        wamvPage = new WAMVPage(driver);
    }

    @Parameters({"alarmListName", "alarmListRow", "row"})
    @Test(priority = 1, testName = "Check Ack., Deack. and Note options", description = "Acknowledge, Deacknowledge, Note")
    @Description("I verify if Web Alarm Management View opens and basic options works")
    public void openSelectedWAMVAndCheckAckDeackNoteFunctionality(
            @Optional("Test Simple Alarm Collection") String alarmListName,
            @Optional("2") int alarmListRow,
            @Optional("0") int row
    ) {
        try {
            fmDashboardPage.searchInAlarmManagementView(alarmListName);
            fmDashboardPage.openAlarmManagementViewByRow(row);
            assertThat(homePage.getPageTitle()).isIn("*" + alarmListName, alarmListName);
            wamvPage.selectSpecificRow(alarmListRow);

            for (int i = 0; i <= 1; i++) {
                if (i == 0) {
                    wamvPage.clickOnAckButton();
                } else {
                    wamvPage.clickOnDeackButton();
                }
                waitForAckColumnChange(alarmListRow, ackValues.get(i));
                Assert.assertEquals(wamvPage.getTitleFromAckStatusCell(alarmListRow), ackValues.get(i));

                wamvPage.addNote(noteValues.get(i));
                waitForNoteColumnChange(alarmListRow, noteValues.get(i));
                Assert.assertEquals(wamvPage.getTextFromNoteStatusCell(alarmListRow), noteValues.get(i));

            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Parameters({"alarmListName", "alarmListRow", "row", "adapterName"})
    @Test(priority = 2, testName = "Check tabs from Area 3", description = "Check tabs")
    @Description("I verify if Web Alarm Management View opens and if it is possible to click on tabs")
    public void openSelectedWAMVAndCheckArea3Tabs(
            @Optional("Test Simple Alarm Collection") String alarmListName,
            @Optional("2") int alarmListRow,
            @Optional("0") int row,
            @Optional("MonitoringBrokerSystem") String adapterName
    ) {
        try {
            fmDashboardPage.searchInAlarmManagementView(alarmListName);
            fmDashboardPage.openAlarmManagementViewByRow(row);
            assertThat(homePage.getPageTitle()).isIn("*" + alarmListName, alarmListName);
            wamvPage.selectSpecificRow(alarmListRow);
            wamvPage.clickOnSameMODetailsTab();
            wamvPage.clickOnAdditionalTextTab();
            wamvPage.clickOnAlarmDetailsTab();
            Assert.assertEquals(wamvPage.getAdapterNameValueFromAlarmDetailsTab(), adapterName);
            Assert.assertEquals(wamvPage.getNotificationIdentifierValueFromAlarmDetailsTab(), wamvPage.getTextFromNotificationIdentifierCell(alarmListRow));
            wamvPage.clickOnSameMOAlarmsTab();
            Assert.assertEquals(wamvPage.checkVisibilityOfSameMOAlarmsTable(), true);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void waitForAckColumnChange(int alarmListRow, String type) {
        for (int i = 0; i < 25; i++) {
            if (wamvPage.getTitleFromAckStatusCell(alarmListRow).equals(type)) {
                break;
            } else {
                DelayUtils.sleep(100);
            }
        }
    }

    public void waitForNoteColumnChange(int alarmListRow, String type) {
        for (int i = 0; i < 25; i++) {
            if (wamvPage.getTextFromNoteStatusCell(alarmListRow).equals(type)) {
                break;
            } else {
                DelayUtils.sleep(100);
            }
        }
    }
}
