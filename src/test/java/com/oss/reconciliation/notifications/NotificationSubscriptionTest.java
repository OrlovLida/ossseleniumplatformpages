package com.oss.reconciliation.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.notifications.SubscriptionConfigurationPage;

public class NotificationSubscriptionTest extends BaseTestCase {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationSubscriptionTest.class);
    private static final String CM_DOMAIN_NAME = "AT-SYS-NOTIFICATION-SUBSCRIPTIONS-TEST";
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
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        LOG.info("Checking amount of reconciliation states");
//        Assert.assertEquals(networkDiscoveryControlViewPage.countReconciliationStates(), 2);
//        LOG.info("Tab Labels on NetworkElementDiscoveryViewPage are: " + networkDiscoveryControlViewPage.getTabsLabels());
//        Assert.assertFalse(networkDiscoveryControlViewPage.getTabsLabels().contains("Notifications"));
        networkDiscoveryControlViewPage.moveToSubscriptionConfiguration();
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
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.selectFirstNodeOnTree();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Thread.sleep(25000);
        LOG.info("Checking how many Notification Subscriptions there are");
        LOG.info("Notification Subscriptions amount is: " + subscriptionConfigurationPage.countNotificationSubscriptions());
        Assert.assertEquals(subscriptionConfigurationPage.countNotificationSubscriptions(), 3);
        LOG.info("Searching for subscription");
        subscriptionConfigurationPage.searchForSubscription("Update");
        System.out.println("Column headers: " + subscriptionConfigurationPage.getColumnHeaders());
        subscriptionConfigurationPage.selectFirstSubscription();
//        subscriptionConfigurationPage.addRows("DM_RecoControl_NotificationSubscription.currentOccupancy\n");
//        subscriptionConfigurationPage.addRows("Newest notification");
//        subscriptionConfigurationPage.addRows("DM_RecoControl_NotificationSubscription.occupancyPercent\n");
//        subscriptionConfigurationPage.addRows("Oldest notification");
//        subscriptionConfigurationPage.addRows("DM_RecoControl_NotificationSubscription.totalCapacity");
//        Assert.assertEquals(subscriptionConfigurationPage.getTabLabels(), tabLabelsList);


    }

    @Test(priority = 4)
    public void checkEnabledNotification() {

    }

    @Test(priority = 5)
    public void checkHoldNotification() {
        System.out.println(subscriptionConfigurationPage.getState());
        System.out.println(subscriptionConfigurationPage.getBufferStatePercent());
//        System.out.println(subscriptionConfigurationPage.getNewestNotification());
//        System.out.println(subscriptionConfigurationPage.getOldestNotification());
        //Check if buffer is 0 and clear it
//        Assert.assertFalse(subscriptionConfigurationPage.isBufferZeroPercent());
        subscriptionConfigurationPage.clearBuffer();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.refreshPage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(subscriptionConfigurationPage.isBufferZeroPercent());

        //Check if there is no Properties in Tabs
        Assert.assertFalse(subscriptionConfigurationPage.getTabLabels().contains("Properties"));
    }

    @Test(priority = 6)
    public void checkDisabledNotification() {

    }
}
