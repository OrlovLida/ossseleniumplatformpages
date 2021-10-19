package com.oss.faultmanagement;

import com.oss.BaseTestCase;
import com.oss.bigdata.kpiview.KpiViewTest;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.faultmanagement.FMCreateWAMVPage;
import com.oss.pages.faultmanagement.FMDashboardPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Bartosz Nowak
 */
@Listeners({TestListener.class})
public class CreateAlarmListTest extends BaseTestCase {
    private static final Logger log = LoggerFactory.getLogger(KpiViewTest.class);
    private final String date = new SimpleDateFormat("dd-MM-yyyy_HH:mm").format(new Date());

    private FMDashboardPage fmDashboardPage;
    private FMCreateWAMVPage fmWAMVPage;

    @BeforeMethod
    public void goToFMDashboardPage() {
        fmDashboardPage = FMDashboardPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"name", "description", "folderName"})
    @Test(priority = 1, testName = "Create new alarm management view", description = "Set name, description, folder and filter")
    @Description("I verify if it is possible to create Web Alarm Management View")
    public void createNewWAMV(
            @Optional("Selenium_test_alarm_list") String name,
            @Optional("Selenium test description") String description,
            @Optional("SeleniumTest") String folderName
    ) {
        try {
            fmWAMVPage = fmDashboardPage.clickCreateNewAlarmList();
            fmWAMVPage.setName(name + '_' + date);
            fmWAMVPage.setDescription(description);
            fmWAMVPage.dragAndDropFilterByName(folderName);
            fmWAMVPage.selectFilterFromList(1);
            fmWAMVPage.clickAcceptButton();

        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }
}