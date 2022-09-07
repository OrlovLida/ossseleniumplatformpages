package com.oss.reconciliation.notifications;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.notifications.SubscriptionConfigurationPage;

public class NotificationSubscriptionTest extends BaseTestCase {

    private static final String CM_DOMAIN_NAME = "Notification_test_AG";
    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private SubscriptionConfigurationPage subscriptionConfigurationPage;
//    private List<String> tabLabelsList = Collections.singletonList("Notification subscriptions");

    @BeforeClass
    public void openConsole() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        driver.manage().deleteAllCookies();
    }

    @Test(priority = 1)
    public void openViewAndSelect() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        System.out.println(networkDiscoveryControlViewPage.getTabsLabels());
        Assert.assertFalse(networkDiscoveryControlViewPage.getTabsLabels().contains("Notifications"));
        networkDiscoveryControlViewPage.moveToSubscriptionConfiguration();

        subscriptionConfigurationPage = new SubscriptionConfigurationPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

//    @Test(priority = 2)
//    public void goBackToNDCV() {
//        subscriptionConfigurationPage = new SubscriptionConfigurationPage(driver);
//        subscriptionConfigurationPage.goBackToNDCVWithoutSelectedSubscription();
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        networkDiscoveryControlViewPage = new NetworkDiscoveryControlViewPage(driver);
//        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
//        networkDiscoveryControlViewPage.moveToSubscriptionConfiguration();
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        subscriptionConfigurationPage.selectFirstItem();
//        subscriptionConfigurationPage.goBackToNDCVWithSelectedSubscription();
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
//        networkDiscoveryControlViewPage.moveToSubscriptionConfiguration();
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//    }

    @Test(priority = 3)
    public void getNotificationAttributes() throws InterruptedException {
        subscriptionConfigurationPage = new SubscriptionConfigurationPage(driver);
        subscriptionConfigurationPage.selectFirstNodeOnTree();
        Thread.sleep(10000);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.searchForSubscription("Update");
        System.out.println("Column headers: " + subscriptionConfigurationPage.getColumnHeaders());
        subscriptionConfigurationPage.selectFirstSubscription();
//        subscriptionConfigurationPage.addRows("DM_RecoControl_NotificationSubscription.currentOccupancy\n");
//        subscriptionConfigurationPage.addRows("Newest notification");
//        subscriptionConfigurationPage.addRows("DM_RecoControl_NotificationSubscription.occupancyPercent\n");
//        subscriptionConfigurationPage.addRows("Oldest notification");
//        subscriptionConfigurationPage.addRows("DM_RecoControl_NotificationSubscription.totalCapacity");
//        Assert.assertEquals(subscriptionConfigurationPage.getTabLabels(), tabLabelsList);

        //Check if buffer is 0 and clear it
        Assert.assertTrue(subscriptionConfigurationPage.isBufferZeroPercent());
        subscriptionConfigurationPage.clearBuffer();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.refreshPage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(subscriptionConfigurationPage.isBufferZeroPercent());

        //Check if there is no Properties in Tabs
        Assert.assertFalse(subscriptionConfigurationPage.getTabLabels().contains("Properties"));

        //Print attributes
        System.out.println(subscriptionConfigurationPage.getState());
        System.out.println(subscriptionConfigurationPage.getOccupancyPercent());
        System.out.println(subscriptionConfigurationPage.getNewestNotification());
        System.out.println(subscriptionConfigurationPage.getOldestNotification());
    }
}
