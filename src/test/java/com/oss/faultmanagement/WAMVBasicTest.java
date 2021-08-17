package com.oss.faultmanagement;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.oss.BaseTestCase;
import com.oss.pages.faultmanagement.FMDashboardPage;
import com.oss.pages.faultmanagement.WAMVPage;
import com.oss.pages.platform.HomePage;
import com.oss.utils.TestListener;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestListener.class})
public class WAMVBasicTest extends BaseTestCase {
    private static String alarmListName = "Test Simple Alarm Collection";
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
        fmDashboardPage.searchForAlarmList(alarmListName);
        fmDashboardPage.openAlarmListFromList(0);
        Assert.assertEquals(homePage.getPageTitle(), "*" + alarmListName, "it is not working fine");

        wamvPage.selectSpecificRow(1);
        wamvPage.clickOnAckButton();
        Assert.assertEquals(wamvPage.getTextFromAckStatusCell(1, "acknowledge"), "True");

        wamvPage.clickOnDeackButton();
        Assert.assertEquals(wamvPage.getTextFromAckStatusCell(1, "acknowledge"), "False");



    }
}
