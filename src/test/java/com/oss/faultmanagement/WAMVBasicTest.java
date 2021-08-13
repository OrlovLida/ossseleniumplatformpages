package com.oss.faultmanagement;

import com.oss.BaseTestCase;
import com.oss.pages.faultmanagement.FMDashboardPage;
import com.oss.pages.faultmanagement.WAMVPage;
import com.oss.utils.TestListener;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestListener.class})
public class WAMVBasicTest extends BaseTestCase {
    private static String alarmListName = "Test Simple Alarm Collection";
    private FMDashboardPage fmDashboardPage;

    @BeforeMethod
    public void goToFMDashboardPage() {
        fmDashboardPage = FMDashboardPage.goToPage(driver, BASIC_URL);
    }

    @Test
    public void openSelectedWAMV() {
        fmDashboardPage.searchForAlarmList(alarmListName);
        fmDashboardPage.openAlarmListFromList(0);

        WAMVPage wamvPage = WAMVPage.initializeWAMV(driver);
        String actualItem = wamvPage.getViewHeader();

//        WAMVPage wamvPage = new WAMVPage(driver);
//        String actualItem = wamvPage.getViewHeader();

        Assert.assertEquals(actualItem, "*" + alarmListName);


    }

    private String removeFromString(String item) {
        return "aa";
    }
}
