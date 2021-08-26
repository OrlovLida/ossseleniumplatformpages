package com.oss.faultmanagement;

import com.oss.BaseTestCase;
import com.oss.pages.faultmanagement.FMFilterManagerPage;
import com.oss.utils.TestListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

@Listeners({TestListener.class})
public class FilterManagerTest extends BaseTestCase {
    private FMFilterManagerPage fmFilterManagerPage;

    @BeforeMethod
    public void goToFMDashboardPage() {
        fmFilterManagerPage = FMFilterManagerPage.goToFilterManagerPage(driver, BASIC_URL);

    }

    @Test
    public void openSelectedWAMV() {
        String date = new SimpleDateFormat("dd-MM-yyyy_HH:mm").format(new Date());
        fmFilterManagerPage.createFolder("selenium_test_folder" + "_" + date);
        fmFilterManagerPage.createFilter("selenium_test_filter" + "_" + date, "Alarm");
    }
}
