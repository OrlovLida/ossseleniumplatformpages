package com.oss.faultmanagement;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.faultmanagement.FMDashboardPage;
import com.oss.pages.faultmanagement.WAMVPage;
import com.oss.pages.platform.HomePage;
import com.oss.utils.TestListener;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Listeners({TestListener.class})
public class WAMVBasicTest extends BaseTestCase {
    private static String alarmListName = "Test Simple Alarm Collection";
    private FMDashboardPage fmDashboardPage;
    private HomePage homePage;
    private WAMVPage wamvPage;
    private int row = 0;
    private int alarmListRow = 1;

    @BeforeMethod
    public void goToFMDashboardPage() {
        fmDashboardPage = FMDashboardPage.goToPage(driver, BASIC_URL);
        homePage = new HomePage(driver);
        wamvPage = new WAMVPage(driver);
    }

    @Test
    public void openSelectedWAMV() {
        fmDashboardPage.searchForAlarmList(alarmListName);
        fmDashboardPage.openAlarmListFromList(row);
        assertThat(homePage.getPageTitle()).isIn("*" + alarmListName, alarmListName);


        wamvPage.selectSpecificRow(alarmListRow);

        wamvPage.clickOnAckButton();
        waitForAckColumnChange("True");
        Assert.assertEquals(wamvPage.getTitleFromAckStatusCell(alarmListRow), "True");


        wamvPage.clickOnDeackButton();
        waitForAckColumnChange("False");
        Assert.assertEquals(wamvPage.getTitleFromAckStatusCell(alarmListRow), "False");


        wamvPage.selectSpecificRow(alarmListRow + 1);
        wamvPage.addNote("Selenium_automaited_test");
        waitForNoteColumnChange("Selenium_automaited_test");
        Assert.assertEquals(wamvPage.getTextFromNoteStatusCell(alarmListRow + 1), "Selenium_automaited_test");
        wamvPage.addNote("");
        waitForNoteColumnChange("");
        Assert.assertEquals(wamvPage.getTextFromNoteStatusCell(alarmListRow + 1), "");

    }

    public void waitForAckColumnChange(String type) {
        for (int i = 0; i < 25; i++) {
            if (wamvPage.getTitleFromAckStatusCell(alarmListRow).equals(type)) {
                break;
            } else {
                DelayUtils.sleep(100);
            }
        }
    }

    public void waitForNoteColumnChange(String type) {
        for (int i = 0; i < 25; i++) {
            if (wamvPage.getTextFromNoteStatusCell(alarmListRow + 1).equals(type)) {
                break;
            } else {
                DelayUtils.sleep(100);
            }
        }
    }

}
