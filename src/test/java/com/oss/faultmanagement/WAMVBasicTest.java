package com.oss.faultmanagement;

import com.oss.BaseTestCase;
import com.oss.pages.faultmanagement.FMDashboardPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class WAMVBasicTest extends BaseTestCase {
    private FMDashboardPage fmDashboardPage;

    @BeforeMethod
    public void goToFMDashboardPage() {
        fmDashboardPage = FMDashboardPage.goToPage(driver, BASIC_URL);
    }

    @Test
    public void openSelectedWAMV() {
        fmDashboardPage.searchForAlarmList();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
