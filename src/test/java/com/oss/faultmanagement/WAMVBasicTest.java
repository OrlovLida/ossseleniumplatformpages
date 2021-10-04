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

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Listeners({TestListener.class})
public class WAMVBasicTest extends BaseTestCase {
    private static final String alarmListName = "Test Simple Alarm Collection";
    private final int alarmListRow = 2;
    private final List<String> ackValues = Arrays.asList("True", "False");
    private final List<String> noteValues = Arrays.asList("Selenium_automaited_test", "");

    private FMDashboardPage fmDashboardPage;
    private HomePage homePage;
    private WAMVPage wamvPage;

    @BeforeMethod
    public void goToFMDashboardPage() {
        fmDashboardPage = FMDashboardPage.goToPage(driver, BASIC_URL);
        homePage = new HomePage(driver);
        wamvPage = new WAMVPage(driver);
    }

    @Test
    public void openSelectedWAMV() {
        int row = 0;

        fmDashboardPage.searchForAlarmList(alarmListName);
        fmDashboardPage.openAlarmListFromList(row);
        assertThat(homePage.getPageTitle()).isIn("*" + alarmListName, alarmListName);

        wamvPage.selectSpecificRow(alarmListRow);

        for (int i = 0; i <= 1; i++) {
            if (i == 0) {
                wamvPage.clickOnAckButton();
            } else {
                wamvPage.clickOnDeackButton();
            }
            waitForAckColumnChange(ackValues.get(i));
            Assert.assertEquals(wamvPage.getTitleFromAckStatusCell(alarmListRow), ackValues.get(i));

            wamvPage.addNote(noteValues.get(i));
            waitForNoteColumnChange(noteValues.get(i));
            Assert.assertEquals(wamvPage.getTextFromNoteStatusCell(alarmListRow), noteValues.get(i));
        }
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
            if (wamvPage.getTextFromNoteStatusCell(alarmListRow).equals(type)) {
                break;
            } else {
                DelayUtils.sleep(100);
            }
        }
    }

}
