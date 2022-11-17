package com.oss.reconciliation.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

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
    private static final String ZERO_PERCENT = "0%";
    private static final String CURRENT_OCCUPANCY_ROW_ID = "DM_RecoControl_NotificationSubscription.currentOccupancy";
    private static final String NEWEST_NOTIFICATION_ROW_ID = "Newest notification";
    private static final String OLDEST_NOTIFICATION_ROW_ID = "Oldest notification";
    private static final String OCCUPANCY_PERCENT_ROW_ID = "DM_RecoControl_NotificationSubscription.occupancyPercent";
    private static final String TOTAL_CAPACITY_ROW_ID = "DM_RecoControl_NotificationSubscription.totalCapacity";
    private static final String UPDATE = "Update";
    private static final String PROPERTIES = "Properties";
    private static final String CREATE = "Create";
    private static final String TWO_PERCENT = "2";
    private static final String FIFTY_PERCENT = "50%";
    private static final String REMOVE = "Remove";
    private static final String NOTIFICATIONS = "Notifications";
    private static final String CHECK_IF_AMOUNT_OF_RECO_STATES_EQUALS_2_LOG = "Checking if amount of reconciliation states equals 2.";
    private static final String CHECKING_AMOUNT_OF_NOTIFICATION_SUBSCRIPTIONS_LOG = "Checking how many Notification Subscriptions there are.";
    private static final String SEARCHING_FOR_SUBSCRIPTIONS_LOG = "Searching for subscriptions.";
    private static final String COMPARE_TOTAL_CAPACITY_PARAMETER_PATTERN_LOG = "Comparing TotalCapacity parameter from web, which is: %s with value: %s.";
    private static final String COMPARE_OCCUPANCY_PERCENT_PARAMETER_PATTERN_LOG = "Comparing OccupancyPercent parameter from web, which is: %s with value: %s.";
    private static final String COMPARE_CURRENT_OCCUPANCY_PARAMETER_PATTERN_LOG = "Comparing CurrentOccupancy parameter from web, which is: %s with value: %s.";
    private static final String COMPARE_STATE_PARAMETER_PATTERN_LOG = "Comparing State parameter from web, which is: %s with value: %s.";
    private static final String TAB_LABELS_PATTERN_LOG = "Tab Labels on NetworkElementDiscoveryViewPage are: %s.";
    private static final String NOTIFICATION_SUBSCRIPTIONS_AMOUNT_PATTERN_LOG = "Notification Subscriptions amount is: %s.";
    private static final String COLUMN_HEADERS_PATTERN_LOG = "Column headers: %s.";
    private static final String CHECKING_DATE_FORMAT_LOG = "Checking date format.";
    private static final String CHECKING_PROPERTIES_TAB_LOG = "Checking if Properties is not one of the Tabs.";
    private static final String CHECKING_NOTIFICATIONS_TAB_LOG = "Checking if Notifications is not one of Tabs.";
    private static final String CHECKING_IF_BUFFER_IS_0_PERCENT_LOG = "Checking if buffer is 0%.";

    private String newestNotification = null;
    private String oldestNotification = null;
    
    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private SubscriptionConfigurationPage subscriptionConfigurationPage;

    private SoftAssert softAssert;

    @BeforeClass
    public void openConsole() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        driver.manage().deleteAllCookies();
    }

    @Test(priority = 1, description = "Move to Subscription Configuration View")
    @Description("Open NDCV and move to Subscription Configuration View")
    public void openViewAndSelect() {
        softAssert = new SoftAssert();
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        softAssert.assertEquals(networkDiscoveryControlViewPage.countReconciliationStates(), 2, CHECK_IF_AMOUNT_OF_RECO_STATES_EQUALS_2_LOG);
        LOG.info(String.format(TAB_LABELS_PATTERN_LOG, networkDiscoveryControlViewPage.getTabsLabels()));
        softAssert.assertFalse(networkDiscoveryControlViewPage.getTabsLabels().contains(NOTIFICATIONS), CHECKING_NOTIFICATIONS_TAB_LOG);
        networkDiscoveryControlViewPage.moveToSubscriptionConfiguration();
        softAssert.assertAll();
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
        softAssert = new SoftAssert();
        subscriptionConfigurationPage = new SubscriptionConfigurationPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.selectFirstNodeOnTree();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        LOG.info(CHECKING_AMOUNT_OF_NOTIFICATION_SUBSCRIPTIONS_LOG);
        softAssert.assertEquals(subscriptionConfigurationPage.countNotificationSubscriptions(), 3, String.format(NOTIFICATION_SUBSCRIPTIONS_AMOUNT_PATTERN_LOG, subscriptionConfigurationPage.countNotificationSubscriptions()));

        subscriptionConfigurationPage.addRows(CURRENT_OCCUPANCY_ROW_ID);
        subscriptionConfigurationPage.addRows(NEWEST_NOTIFICATION_ROW_ID);
        subscriptionConfigurationPage.addRows(OCCUPANCY_PERCENT_ROW_ID);
        subscriptionConfigurationPage.addRows(OLDEST_NOTIFICATION_ROW_ID);
        subscriptionConfigurationPage.addRows(TOTAL_CAPACITY_ROW_ID);
        LOG.info(String.format(COLUMN_HEADERS_PATTERN_LOG, subscriptionConfigurationPage.getColumnHeaders()));

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        softAssert.assertFalse(subscriptionConfigurationPage.getTabLabels().contains(PROPERTIES), CHECKING_PROPERTIES_TAB_LOG);
        softAssert.assertAll();
    }

    @Test(priority = 4, description = "Check if everything on Enabled Notification works correctly")
    @Description("Check if everything on Enabled Notification works correctly")
    public void checkEnabledNotification() {
        softAssert = new SoftAssert();
        LOG.info(SEARCHING_FOR_SUBSCRIPTIONS_LOG);
        subscriptionConfigurationPage.searchForSubscription(UPDATE);
        subscriptionConfigurationPage.selectFirstSubscription();
        
        softAssert.assertEquals(subscriptionConfigurationPage.getState(), ENABLED_STATE, String.format(COMPARE_STATE_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getState(), ENABLED_STATE));
        softAssert.assertEquals(subscriptionConfigurationPage.getCurrentOccupancy(), S_0, String.format(COMPARE_CURRENT_OCCUPANCY_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getCurrentOccupancy(), S_0));
        softAssert.assertEquals(subscriptionConfigurationPage.getOccupancyPercent(), ZERO_PERCENT, String.format(COMPARE_OCCUPANCY_PERCENT_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getOccupancyPercent(), ZERO_PERCENT));
        softAssert.assertEquals(subscriptionConfigurationPage.getTotalCapacity(), S_4, String.format(COMPARE_TOTAL_CAPACITY_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getTotalCapacity(), S_4));
        
        newestNotification = subscriptionConfigurationPage.getNewestNotification();
        oldestNotification = subscriptionConfigurationPage.getOldestNotification();
        softAssert.assertFalse(subscriptionConfigurationPage.isDateFormatCorrect(newestNotification), CHECKING_DATE_FORMAT_LOG);
        softAssert.assertFalse(subscriptionConfigurationPage.isDateFormatCorrect(oldestNotification), CHECKING_DATE_FORMAT_LOG);
        
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.hold();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.refreshPage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        
        softAssert.assertEquals(subscriptionConfigurationPage.getState(), ON_HOLD_STATE, String.format(COMPARE_STATE_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getState(), ON_HOLD_STATE));
        softAssert.assertEquals(subscriptionConfigurationPage.getCurrentOccupancy(), S_0, String.format(COMPARE_CURRENT_OCCUPANCY_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getCurrentOccupancy(), S_0));
        softAssert.assertEquals(subscriptionConfigurationPage.getOccupancyPercent(), ZERO_PERCENT, String.format(COMPARE_OCCUPANCY_PERCENT_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getOccupancyPercent(), ZERO_PERCENT));
        softAssert.assertEquals(subscriptionConfigurationPage.getTotalCapacity(), S_4, String.format(COMPARE_TOTAL_CAPACITY_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getTotalCapacity(), S_4));

        newestNotification = subscriptionConfigurationPage.getNewestNotification();
        oldestNotification = subscriptionConfigurationPage.getOldestNotification();
        softAssert.assertFalse(subscriptionConfigurationPage.isDateFormatCorrect(newestNotification), CHECKING_DATE_FORMAT_LOG);
        softAssert.assertFalse(subscriptionConfigurationPage.isDateFormatCorrect(oldestNotification), CHECKING_DATE_FORMAT_LOG);
        
        subscriptionConfigurationPage.disable();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.refreshPage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        
        softAssert.assertEquals(subscriptionConfigurationPage.getState(), DISABLED_STATE, String.format(COMPARE_STATE_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getState(), DISABLED_STATE));
        softAssert.assertEquals(subscriptionConfigurationPage.getCurrentOccupancy(), S_0, String.format(COMPARE_CURRENT_OCCUPANCY_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getCurrentOccupancy(), S_0));
        softAssert.assertEquals(subscriptionConfigurationPage.getOccupancyPercent(), ZERO_PERCENT, String.format(COMPARE_OCCUPANCY_PERCENT_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getOccupancyPercent(), ZERO_PERCENT));
        softAssert.assertEquals(subscriptionConfigurationPage.getTotalCapacity(), S_4, String.format(COMPARE_TOTAL_CAPACITY_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getTotalCapacity(), S_4));

        newestNotification = subscriptionConfigurationPage.getNewestNotification();
        oldestNotification = subscriptionConfigurationPage.getOldestNotification();
        softAssert.assertFalse(subscriptionConfigurationPage.isDateFormatCorrect(newestNotification), CHECKING_DATE_FORMAT_LOG);
        softAssert.assertFalse(subscriptionConfigurationPage.isDateFormatCorrect(oldestNotification), CHECKING_DATE_FORMAT_LOG);
        
        subscriptionConfigurationPage.enable();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.refreshPage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        softAssert.assertEquals(subscriptionConfigurationPage.getState(), ENABLED_STATE, String.format(COMPARE_STATE_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getState(), ENABLED_STATE));
        softAssert.assertEquals(subscriptionConfigurationPage.getCurrentOccupancy(), S_0, String.format(COMPARE_CURRENT_OCCUPANCY_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getCurrentOccupancy(), S_0));
        softAssert.assertEquals(subscriptionConfigurationPage.getOccupancyPercent(), ZERO_PERCENT, String.format(COMPARE_OCCUPANCY_PERCENT_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getOccupancyPercent(), ZERO_PERCENT));
        softAssert.assertEquals(subscriptionConfigurationPage.getTotalCapacity(), S_4, String.format(COMPARE_TOTAL_CAPACITY_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getTotalCapacity(), S_4));

        newestNotification = subscriptionConfigurationPage.getNewestNotification();
        oldestNotification = subscriptionConfigurationPage.getOldestNotification();
        softAssert.assertFalse(subscriptionConfigurationPage.isDateFormatCorrect(newestNotification), CHECKING_DATE_FORMAT_LOG);
        softAssert.assertFalse(subscriptionConfigurationPage.isDateFormatCorrect(oldestNotification), CHECKING_DATE_FORMAT_LOG);
        
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        softAssert.assertAll();
    }

    @Test(priority = 5, description = "Check if everything on Hold Notification works correctly")
    @Description("Check if everything on Hold Notification works correctly")
    public void checkHoldNotification() {
        softAssert = new SoftAssert();
        LOG.info(SEARCHING_FOR_SUBSCRIPTIONS_LOG);
        subscriptionConfigurationPage.searchForSubscription(CREATE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.selectFirstSubscription();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        softAssert.assertEquals(subscriptionConfigurationPage.getState(), ON_HOLD_STATE, String.format(COMPARE_STATE_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getState(), ON_HOLD_STATE));
        softAssert.assertEquals(subscriptionConfigurationPage.getCurrentOccupancy(), TWO_PERCENT, String.format(COMPARE_CURRENT_OCCUPANCY_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getCurrentOccupancy(), TWO_PERCENT));
        softAssert.assertEquals(subscriptionConfigurationPage.getOccupancyPercent(), FIFTY_PERCENT, String.format(COMPARE_OCCUPANCY_PERCENT_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getOccupancyPercent(), FIFTY_PERCENT));
        softAssert.assertEquals(subscriptionConfigurationPage.getTotalCapacity(), S_4, String.format(COMPARE_TOTAL_CAPACITY_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getTotalCapacity(), S_4));
       
        newestNotification = subscriptionConfigurationPage.getNewestNotification();
        oldestNotification = subscriptionConfigurationPage.getOldestNotification();
        softAssert.assertTrue(subscriptionConfigurationPage.isDateFormatCorrect(newestNotification), CHECKING_DATE_FORMAT_LOG);
        softAssert.assertTrue(subscriptionConfigurationPage.isDateFormatCorrect(oldestNotification), CHECKING_DATE_FORMAT_LOG);

        softAssert.assertFalse(subscriptionConfigurationPage.isBufferZeroPercent(), CHECKING_IF_BUFFER_IS_0_PERCENT_LOG);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.clearBuffer();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.refreshPage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        
        softAssert.assertTrue(subscriptionConfigurationPage.isBufferZeroPercent(), CHECKING_IF_BUFFER_IS_0_PERCENT_LOG);
        softAssert.assertEquals(subscriptionConfigurationPage.getCurrentOccupancy(), S_0, String.format(COMPARE_CURRENT_OCCUPANCY_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getCurrentOccupancy(), S_0));
        softAssert.assertAll();
    }

    @Test(priority = 6, description = "Check if everything on Disabled Notification works correctly")
    @Description("Check if everything on Disabled Notification works correctly")
    public void checkDisabledNotification() {
        softAssert = new SoftAssert();
        LOG.info(SEARCHING_FOR_SUBSCRIPTIONS_LOG);
        subscriptionConfigurationPage.searchForSubscription(REMOVE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        subscriptionConfigurationPage.selectFirstSubscription();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        softAssert.assertEquals(subscriptionConfigurationPage.getState(), DISABLED_STATE, String.format(COMPARE_STATE_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getState(), DISABLED_STATE));
        softAssert.assertEquals(subscriptionConfigurationPage.getCurrentOccupancy(), S_0, String.format(COMPARE_CURRENT_OCCUPANCY_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getCurrentOccupancy(), S_0));
        softAssert.assertEquals(subscriptionConfigurationPage.getOccupancyPercent(), ZERO_PERCENT, String.format(COMPARE_OCCUPANCY_PERCENT_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getOccupancyPercent(), ZERO_PERCENT));
        softAssert.assertEquals(subscriptionConfigurationPage.getTotalCapacity(), S_4, String.format(COMPARE_TOTAL_CAPACITY_PARAMETER_PATTERN_LOG, subscriptionConfigurationPage.getTotalCapacity(), S_4));

        newestNotification = subscriptionConfigurationPage.getNewestNotification();
        oldestNotification = subscriptionConfigurationPage.getOldestNotification();
        softAssert.assertFalse(subscriptionConfigurationPage.isDateFormatCorrect(newestNotification), CHECKING_DATE_FORMAT_LOG);
        softAssert.assertFalse(subscriptionConfigurationPage.isDateFormatCorrect(oldestNotification), CHECKING_DATE_FORMAT_LOG);
        softAssert.assertAll();
    }
}
