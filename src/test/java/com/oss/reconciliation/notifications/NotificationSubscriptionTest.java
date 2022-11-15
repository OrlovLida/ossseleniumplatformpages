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

import io.qameta.allure.Description;

public class NotificationSubscriptionTest extends BaseTestCase {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationSubscriptionTest.class);
    private static final String CM_DOMAIN_NAME = "AT-SYS-NOTIFICATION-SUBSCRIPTIONS-TEST";
    private static final String ON_HOLD_STATE = "On hold";
    private static final String DISABLED_STATE = "Disabled";
    private static final String ENABLED_STATE = "Enabled";
    private static final String S_0 = "0";
    private static final String S_4 = "4";
    private static final String S_0_PERCENT = "0%";
    
    private String newestNotification = null;
    private String oldestNotification = null;
    
    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private SubscriptionConfigurationPage subscriptionConfigurationPage;

    @BeforeClass
    public void openConsole() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        driver.manage().deleteAllCookies();
    }

    @Test(priority = 1, description = "Move to Subscription Configuration View")
    @Description("Open NDCV and move to Subscription Configuration View")
    public void openViewAndSelect() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        LOG.info("Checking amount of reconciliation states");
        Assert.assertEquals(networkDiscoveryControlViewPage.countReconciliationStates(), 2);
        LOG.info("Tab Labels on NetworkElementDiscoveryViewPage are: " + networkDiscoveryControlViewPage.getTabsLabels());
        Assert.assertFalse(networkDiscoveryControlViewPage.getTabsLabels().contains("Notifications"));
        networkDiscoveryControlViewPage.moveToSubscriptionConfiguration();
    }

    @Test(priority = 2, description = "Check if going back to NDCV works correctly")
    @Description("Check if going back to NDCV works correctly")
    public void goBackToNDCV() {
        subscriptionConfigurationPage = new SubscriptionConfigurationPage(driver);
        subscriptionConfigurationPage.goBackToNDCVWithoutSelectedSubscription();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage = new NetworkDiscoveryControlViewPage(driver);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.moveToSubscriptionConfiguration();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.selectFirstNodeOnTree();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.goBackToNDCVWithSelectedSubscription();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.moveToSubscriptionConfiguration();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 3, description = "Get Notification Attributes")
    @Description("Get Notification Attributes")
    public void getNotificationAttributes() {
        subscriptionConfigurationPage = new SubscriptionConfigurationPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.selectFirstNodeOnTree();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        LOG.info("Checking how many Notification Subscriptions there are");
        LOG.info("Notification Subscriptions amount is: " + subscriptionConfigurationPage.countNotificationSubscriptions());
        Assert.assertEquals(subscriptionConfigurationPage.countNotificationSubscriptions(), 3);

        subscriptionConfigurationPage.addRows("DM_RecoControl_NotificationSubscription.currentOccupancy");
        subscriptionConfigurationPage.addRows("Newest notification");
        subscriptionConfigurationPage.addRows("DM_RecoControl_NotificationSubscription.occupancyPercent");
        subscriptionConfigurationPage.addRows("Oldest notification");
        subscriptionConfigurationPage.addRows("DM_RecoControl_NotificationSubscription.totalCapacity");
        LOG.info("Column headers: " + subscriptionConfigurationPage.getColumnHeaders());

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertFalse(subscriptionConfigurationPage.getTabLabels().contains("Properties"));
    }

    @Test(priority = 4, description = "Check if everything on Enabled Notification works correctly")
    @Description("Check if everything on Enabled Notification works correctly")
    public void checkEnabledNotification() {
        LOG.info("Searching for subscription");
        subscriptionConfigurationPage.searchForSubscription("Update");
        subscriptionConfigurationPage.selectFirstSubscription();
        
        Assert.assertEquals(subscriptionConfigurationPage.getState(), ENABLED_STATE);
        Assert.assertEquals(subscriptionConfigurationPage.getCurrentOccupancy(), S_0);
        Assert.assertEquals(subscriptionConfigurationPage.getOccupancyPercent(), S_0_PERCENT);
        Assert.assertEquals(subscriptionConfigurationPage.getTotalCapacity(), S_4);
        
        newestNotification = subscriptionConfigurationPage.getNewestNotification();
        oldestNotification = subscriptionConfigurationPage.getOldestNotification();
        Assert.assertFalse(subscriptionConfigurationPage.isDateFormatCorrect(newestNotification));
        Assert.assertFalse(subscriptionConfigurationPage.isDateFormatCorrect(oldestNotification));
        
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.hold();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.refreshPage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        
        Assert.assertEquals(subscriptionConfigurationPage.getState(), ON_HOLD_STATE);
        Assert.assertEquals(subscriptionConfigurationPage.getCurrentOccupancy(), S_0);
        Assert.assertEquals(subscriptionConfigurationPage.getOccupancyPercent(), S_0_PERCENT);
        Assert.assertEquals(subscriptionConfigurationPage.getTotalCapacity(), S_4);

        newestNotification = subscriptionConfigurationPage.getNewestNotification();
        oldestNotification = subscriptionConfigurationPage.getOldestNotification();
        Assert.assertFalse(subscriptionConfigurationPage.isDateFormatCorrect(newestNotification));
        Assert.assertFalse(subscriptionConfigurationPage.isDateFormatCorrect(oldestNotification));
        
        subscriptionConfigurationPage.disable();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.refreshPage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        
        Assert.assertEquals(subscriptionConfigurationPage.getState(), DISABLED_STATE);
        Assert.assertEquals(subscriptionConfigurationPage.getCurrentOccupancy(), S_0);
        Assert.assertEquals(subscriptionConfigurationPage.getOccupancyPercent(), S_0_PERCENT);
        Assert.assertEquals(subscriptionConfigurationPage.getTotalCapacity(), S_4);

        newestNotification = subscriptionConfigurationPage.getNewestNotification();
        oldestNotification = subscriptionConfigurationPage.getOldestNotification();
        Assert.assertFalse(subscriptionConfigurationPage.isDateFormatCorrect(newestNotification));
        Assert.assertFalse(subscriptionConfigurationPage.isDateFormatCorrect(oldestNotification));
        
        subscriptionConfigurationPage.enable();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.refreshPage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(subscriptionConfigurationPage.getState(), ENABLED_STATE);
        Assert.assertEquals(subscriptionConfigurationPage.getCurrentOccupancy(), S_0);
        Assert.assertEquals(subscriptionConfigurationPage.getOccupancyPercent(), S_0_PERCENT);
        Assert.assertEquals(subscriptionConfigurationPage.getTotalCapacity(), S_4);

        newestNotification = subscriptionConfigurationPage.getNewestNotification();
        oldestNotification = subscriptionConfigurationPage.getOldestNotification();
        Assert.assertFalse(subscriptionConfigurationPage.isDateFormatCorrect(newestNotification));
        Assert.assertFalse(subscriptionConfigurationPage.isDateFormatCorrect(oldestNotification));
        
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 5, description = "Check if everything on Hold Notification works correctly")
    @Description("Check if everything on Hold Notification works correctly")
    public void checkHoldNotification() {
        LOG.info("Searching for subscription");
        subscriptionConfigurationPage.searchForSubscription("Create");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.selectFirstSubscription();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(subscriptionConfigurationPage.getState(), ON_HOLD_STATE);
        Assert.assertEquals(subscriptionConfigurationPage.getCurrentOccupancy(), "2");
        Assert.assertEquals(subscriptionConfigurationPage.getOccupancyPercent(), "50%");
        Assert.assertEquals(subscriptionConfigurationPage.getTotalCapacity(), S_4);
       
        newestNotification = subscriptionConfigurationPage.getNewestNotification();
        oldestNotification = subscriptionConfigurationPage.getOldestNotification();
        Assert.assertTrue(subscriptionConfigurationPage.isDateFormatCorrect(newestNotification));
        Assert.assertTrue(subscriptionConfigurationPage.isDateFormatCorrect(oldestNotification));

        Assert.assertFalse(subscriptionConfigurationPage.isBufferZeroPercent());
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.clearBuffer();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.refreshPage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        
        Assert.assertTrue(subscriptionConfigurationPage.isBufferZeroPercent());
        Assert.assertEquals(subscriptionConfigurationPage.getCurrentOccupancy(), S_0);
    }

    @Test(priority = 6, description = "Check if everything on Disabled Notification works correctly")
    @Description("Check if everything on Disabled Notification works correctly")
    public void checkDisabledNotification() {
        LOG.info("Searching for subscription");
        subscriptionConfigurationPage.searchForSubscription("Remove");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.selectFirstSubscription();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        
        Assert.assertEquals(subscriptionConfigurationPage.getState(), DISABLED_STATE);
        Assert.assertEquals(subscriptionConfigurationPage.getCurrentOccupancy(), S_0);
        Assert.assertEquals(subscriptionConfigurationPage.getOccupancyPercent(), S_0_PERCENT);
        Assert.assertEquals(subscriptionConfigurationPage.getTotalCapacity(), S_4);

        newestNotification = subscriptionConfigurationPage.getNewestNotification();
        oldestNotification = subscriptionConfigurationPage.getOldestNotification();
        Assert.assertFalse(subscriptionConfigurationPage.isDateFormatCorrect(newestNotification));
        Assert.assertFalse(subscriptionConfigurationPage.isDateFormatCorrect(oldestNotification));
    }
}
