package com.oss.reconciliation.notifications;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.notifications.SubscriptionConfigurationPage;

public class NotificationSubscriptionTest extends BaseTestCase {

    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private SubscriptionConfigurationPage subscriptionConfigurationPage;
    private List<String> tabLabelsList = Collections.singletonList("Notification subscriptions");

    @BeforeClass
    public void openConsole() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        driver.manage().deleteAllCookies();
    }

    @Test(priority = 1)
    public void openViewAndSelect() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain("Notification_test_AG");
        networkDiscoveryControlViewPage.moveToSubscriptionConfiguration();

        subscriptionConfigurationPage = new SubscriptionConfigurationPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.selectFirstItem();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.searchForSubscription("Update");
    }

    @Test(priority = 2)
    public void getNotificationAttributes() {
        subscriptionConfigurationPage = new SubscriptionConfigurationPage(driver);
//        subscriptionConfigurationPage.addRows("DM_RecoControl_NotificationSubscription.currentOccupancy\n");
//        subscriptionConfigurationPage.addRows("Newest notification");
//        subscriptionConfigurationPage.addRows("DM_RecoControl_NotificationSubscription.occupancyPercent\n");
//        subscriptionConfigurationPage.addRows("Oldest notification");
//        subscriptionConfigurationPage.addRows("DM_RecoControl_NotificationSubscription.totalCapacity");
        Assert.assertEquals(subscriptionConfigurationPage.getTabLabels(), tabLabelsList);
        System.out.println(subscriptionConfigurationPage.getTabLabels());
        System.out.println(subscriptionConfigurationPage.getState());
        System.out.println(subscriptionConfigurationPage.getOccupancyPercent());
        System.out.println(subscriptionConfigurationPage.getNewestNotification());
        System.out.println(subscriptionConfigurationPage.getOldestNotification());
    }
}
