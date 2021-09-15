package com.oss.faultmanagement;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.faultmanagement.FMDashboardPage;
import com.oss.pages.faultmanagement.WAMVPage;
import com.oss.pages.platform.HomePage;
import com.oss.utils.TestListener;
import org.testng.Assert;
import static org.assertj.core.api.Assertions.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestListener.class})
public class WAMVBasicTest extends BaseTestCase {
    private static String alarmListName = "Test Simple Alarm Collection";
    private FMDashboardPage fmDashboardPage;
    private HomePage homePage;
    private WAMVPage wamvPage;
    private int row = 0;

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

        wamvPage.selectSpecificRow(row);

        wamvPage.clickOnAckButton();
        waitForAckColumnChange("True");
        Assert.assertEquals(wamvPage.getTextFromAckStatusCell(row), "True");


        wamvPage.clickOnDeackButton();
        waitForAckColumnChange("False");
        Assert.assertEquals(wamvPage.getTextFromAckStatusCell(row), "False");
    }

    public void waitForAckColumnChange(String type) {
        for (int i = 0; i < 25; i++){
            if(wamvPage.getTextFromAckStatusCell(row).equals(type)){
                break;
            } else {
                DelayUtils.sleep(100);
            }
        }
    }

}
