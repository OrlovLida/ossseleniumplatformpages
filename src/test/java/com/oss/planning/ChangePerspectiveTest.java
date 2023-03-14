package com.oss.planning;

import com.oss.BaseTestCase;
import com.oss.bpm.BpmPhysicalDataCreator;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.planning.ProcessDetailsPage;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDate;

public class ChangePerspectiveTest extends BaseTestCase {
    private static final String PERSPECTIVE_PATTERN = "perspective=%s";
    private static final String PROJECT_PATTERN = "project_id=%s";
    private static final String DATE_PATTERN = "date=%s";
    private static final String WITH_REMOVED_PATTERN = "withRemoved=%s";
    public static final String LIVE = "Live";
    public static final String NETWORK = "Network";
    public static final String PLAN = "Plan";
    private static final String READ_ONLY = " - Read only";
    public static final String NAME_ATTRIBUTE_ID = "Name";
    private static final LocalDate TODAY = LocalDate.now();
    private static final String PROJECT_NAME = "Change Perspective Selenium Test " + BpmPhysicalDataCreator.nextMaxInt();
    private static final String INVALID_PERSPECTIVE_MESSAGE = "Invalid perspective";
    private static final String INVALID_PROJECT_NAME_MESSAGE = "Invalid Project Name";
    private static final LocalDate projectFDD = TODAY.plusDays(10);
    private static final String PROJECT_CONTEXT = String.format(PLAN + " - Process: %1$s (%2$s)", PROJECT_NAME, projectFDD);
    private static final String PROJECT_RO_CONTEXT = String.format(PLAN + READ_ONLY + " Process: %1$s (%2$s)", PROJECT_NAME, projectFDD);
    private static final String DATE_CONTEXT_PATTERN = PLAN + " - Read only (%s)";
    private static final String INVALID_WITH_REMOVED_FLAG = "Invalid With Removed flag value.";
    private String projectId;
    private PerspectiveChooser perspectiveChooser;

    @BeforeClass
    public void createProject() {
        waitForPageToLoad();
        projectId = BpmPhysicalDataCreator.createProject(PROJECT_NAME, projectFDD);
        perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1, description = "Switch perspective to NETWORK")
    @Description("Switch perspective to NETWORK.")
    public void setNetworkPerspective() {
        waitForPageToLoad();
        perspectiveChooser.setNetworkPerspective();
        waitForPageToLoad();
        Assert.assertEquals(perspectiveChooser.getCurrentPerspective(), NETWORK, INVALID_PERSPECTIVE_MESSAGE);
        Assert.assertTrue(driver.getCurrentUrl().contains(
                String.format(PERSPECTIVE_PATTERN, NETWORK.toUpperCase())), INVALID_PERSPECTIVE_MESSAGE);
    }

    @Test(priority = 2, description = "Switch perspective to Plan (project)")
    @Description("Switch perspective to Plan (project).")
    public void setProjectPerspective() {
        waitForPageToLoad();
        perspectiveChooser.setPlanPerspective(PROJECT_NAME);
        waitForPageToLoad();
        Assert.assertEquals(perspectiveChooser.getCurrentPerspective(), PROJECT_CONTEXT, INVALID_PERSPECTIVE_MESSAGE);
        Assert.assertTrue(driver.getCurrentUrl().contains(String.format(PROJECT_PATTERN, projectId)), INVALID_PERSPECTIVE_MESSAGE);
        Assert.assertEquals(ProcessDetailsPage.goToProcessDetailsView(driver, webDriverWait)
                .getProjectAttribute(NAME_ATTRIBUTE_ID), PROJECT_NAME, INVALID_PROJECT_NAME_MESSAGE);
        BpmPhysicalDataCreator.cancelProject(projectId);
        driver.navigate().refresh();
        waitForPageToLoad();
        Assert.assertEquals(perspectiveChooser.getCurrentPerspective(), PROJECT_RO_CONTEXT, INVALID_PERSPECTIVE_MESSAGE);
    }

    @Test(priority = 3, description = "Switch perspective to Plan (date)")
    @Description("Switch perspective to Plan (date).")
    public void setDatePerspective() {
        waitForPageToLoad();
        perspectiveChooser.setPlanDatePerspective(String.valueOf(TODAY.plusDays(5)));
        waitForPageToLoad();
        Assert.assertEquals(perspectiveChooser.getCurrentPerspective(),
                String.format(DATE_CONTEXT_PATTERN, TODAY.plusDays(5)), INVALID_PERSPECTIVE_MESSAGE);
        Assert.assertTrue(driver.getCurrentUrl().contains(
                String.format(DATE_PATTERN, TODAY.plusDays(5))), INVALID_PERSPECTIVE_MESSAGE);
    }

    @Test(priority = 4, description = "Switch perspective to LIVE")
    @Description("Switch perspective to LIVE.")
    public void setLivePerspective() {
        waitForPageToLoad();
        perspectiveChooser.setLivePerspective();
        waitForPageToLoad();
        Assert.assertEquals(perspectiveChooser.getCurrentPerspective(), LIVE, INVALID_PERSPECTIVE_MESSAGE);
        Assert.assertTrue(driver.getCurrentUrl().contains(
                String.format(PERSPECTIVE_PATTERN, LIVE.toUpperCase())), INVALID_PERSPECTIVE_MESSAGE);
    }

    @Test(priority = 5, description = "Switch withRemoved flag to true")
    @Description("Switch withRemoved flag to true")
    public void setWithRemovedPerspective() {
        waitForPageToLoad();
        perspectiveChooser.setWithRemove();
        waitForPageToLoad();
        Assert.assertTrue(driver.getCurrentUrl().contains(String.format(WITH_REMOVED_PATTERN, "true")), INVALID_WITH_REMOVED_FLAG);
    }

    @Test(priority = 6, description = "Switch withRemoved flag to false")
    @Description("Switch withRemoved flag to false")
    public void setWithoutRemovedPerspective() {
        waitForPageToLoad();
        perspectiveChooser.setWithoutRemoved();
        waitForPageToLoad();
        Assert.assertFalse(driver.getCurrentUrl().contains(String.format(WITH_REMOVED_PATTERN, "true")), INVALID_WITH_REMOVED_FLAG);
    }

    @Test(priority = 7, description = "Switch perspective to NETWORK without NETWORK_EDITOR")
    @Description("Switch perspective to NETWORK without NETWORK_EDITOR keycloak role.")
    public void setNetworkReadOnlyPerspective() {
        waitForPageToLoad();
        homePage.changeUser(BpmPhysicalDataCreator.BPM_BASIC_USER_LOGIN, BpmPhysicalDataCreator.BPM_BASIC_USER_PASSWORD);
        waitForPageToLoad();
        perspectiveChooser.setNetworkPerspective();
        waitForPageToLoad();
        Assert.assertEquals(perspectiveChooser.getCurrentPerspective(), NETWORK + READ_ONLY, INVALID_PERSPECTIVE_MESSAGE);
        Assert.assertTrue(driver.getCurrentUrl().contains(
                String.format(PERSPECTIVE_PATTERN, NETWORK.toUpperCase())), INVALID_PERSPECTIVE_MESSAGE);
    }

    @Test(priority = 8, description = "Switch perspective to LIVE without LIVE_EDITOR")
    @Description("Switch perspective to LIVE without LIVE_EDITOR keycloak role.")
    public void setLiveReadOnlyPerspective() {
        waitForPageToLoad();
        perspectiveChooser.setLivePerspective();
        waitForPageToLoad();
        Assert.assertEquals(perspectiveChooser.getCurrentPerspective(), LIVE + READ_ONLY, INVALID_PERSPECTIVE_MESSAGE);
        Assert.assertTrue(driver.getCurrentUrl().contains(
                String.format(PERSPECTIVE_PATTERN, LIVE.toUpperCase())), INVALID_PERSPECTIVE_MESSAGE);
    }

}
