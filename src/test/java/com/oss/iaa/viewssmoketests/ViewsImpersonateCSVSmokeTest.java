package com.oss.iaa.viewssmoketests;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.GlobalNotificationContainer;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.layout.ErrorCard;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.iaa.keycloak.KeycloakPage;

import io.qameta.allure.Description;
import io.qameta.allure.Step;

public class ViewsImpersonateCSVSmokeTest extends BaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewsImpersonateCSVSmokeTest.class);

    private static final String BOOKMARKS = "Bookmarks";
    private static final String CUSTOM = "Custom";
    private static final String HISTORICAL_ALARM_LIST = "HistoricalAlarmList";
    private static final String OPERATORS_VIEW = "OperatorsView";
    private static final String WEB_ALARM_COUNTERS_VIEW = "WebAlarmCountersView";
    private static final String WEB_ALARM_HISTORICAL_VIEW = "WebAlarmHistoricalView";
    private static final String WEB_ALARM_MANAGEMENT_VIEW = "WebAlarmManagementView";
    private static final String WEB_MAP_MONITORING_VIEW = "WebMapMonitoringView";
    private static final String WEB_OBJECT_MONITORING_VIEW = "WebObjectMonitoringView";
    private static final String UNKNOWN_VIEW_TYPE = "Unknown View Type for your input";

    private static final String HISTORICAL_ALARM_VIEWS_URL_PATTERN = "%s/#/views/fault-management/historical-alarm-management/%s";
    private static final String CUSTOM_DASHBOARD_URL_PATTERN = "%s/#/dashboard/dashboardmanagercore/dashboards/custom/%s";
    private static final String OBJECT_MONITORING_VIEW_URL_PATTERN = "%s/#/view/service-monitoring/object-monitoring/%s";
    private static final String ALARM_COUNTERS_VIEW_URL_PATTERN = "%s/#/views/fault-management/alarm-counters/%s";
    private static final String ALARM_MANAGEMENT_VIEW_URL_PATTERN = "%s/#/views/fault-management/alarm-management/%s";
    private static final String MAP_MONITORING_VIEW_URL_PATTERN = "%s/#/view/service-monitoring/alarm-map/%s";
    private static final String BOOKMARKS_VIEW_URL_PATTERN = "%s/#/%s";
    public static int expectedTime;

    @BeforeClass
    @Parameters("expectedLoadTimeInMilisec")
    public void openWebConsole(
            @Optional("6000") int expectedLoadTimeInMilisec
    ) {
        expectedTime = expectedLoadTimeInMilisec;
        KeycloakPage keycloakPage = new KeycloakPage(driver);
        keycloakPage.loginToKeycloak(BASIC_URL);
    }

    @Test(priority = 1, dataProvider = "views", testName = "Check View", description = "Check View")
    @Description("Check views")
    public void checkView(String viewType, String chosenView, String userName) {
        KeycloakPage keycloakPage = new KeycloakPage(driver);
        keycloakPage.impersonateLogin(BASIC_URL, userName);

        String url = String.format(getURLPattern(viewType), BASIC_URL, chosenView);
        driver.get(url);
        Assert.assertTrue(DelayUtils.isPageLoaded(driver, webDriverWait, expectedTime));
        checkGlobalNotificationContainer(chosenView);
        checkErrorPage(chosenView);
        LOGGER.info("Opened page: {}", url);
    }

    private String getURLPattern(String viewType) {
        switch (viewType) {
            case BOOKMARKS: {
                return BOOKMARKS_VIEW_URL_PATTERN;
            }
            case CUSTOM: {
                return CUSTOM_DASHBOARD_URL_PATTERN;
            }
            case HISTORICAL_ALARM_LIST:
            case WEB_ALARM_HISTORICAL_VIEW: {
                return HISTORICAL_ALARM_VIEWS_URL_PATTERN;
            }
            case OPERATORS_VIEW:
            case WEB_OBJECT_MONITORING_VIEW: {
                return OBJECT_MONITORING_VIEW_URL_PATTERN;
            }
            case WEB_ALARM_COUNTERS_VIEW: {
                return ALARM_COUNTERS_VIEW_URL_PATTERN;
            }
            case WEB_ALARM_MANAGEMENT_VIEW: {
                return ALARM_MANAGEMENT_VIEW_URL_PATTERN;
            }
            case WEB_MAP_MONITORING_VIEW: {
                return MAP_MONITORING_VIEW_URL_PATTERN;
            }
            default:
                throw new IllegalArgumentException(UNKNOWN_VIEW_TYPE);
        }
    }

    @Step("Check Global Notification container")
    private void checkGlobalNotificationContainer(String viewName) {
        GlobalNotificationContainer globalNotificationContainer = GlobalNotificationContainer.create(driver, webDriverWait);
        if (globalNotificationContainer.isErrorNotificationPresent()) {
            GlobalNotificationContainer.NotificationInformation information = globalNotificationContainer.getNotificationInformation();
            LOGGER.error(information.getMessage());
            Assert.fail(String.format("Global Notification shows error on %s page.", viewName));
        }
    }

    @Step("Check System Message")
    private void checkSystemMessage() {
        SystemMessageContainer systemMessage = SystemMessageContainer.create(this.driver, new WebDriverWait(this.driver, Duration.ofSeconds(5)));
        List<String> errors = systemMessage.getErrors();
        errors.forEach(LOGGER::error);
        Assert.assertTrue(errors.isEmpty(), "Some errors occurred during the test. Please check logs for details.\n");
    }

    @Step("Check Error Page")
    private void checkErrorPage(String viewName) {
        ErrorCard errorCard = ErrorCard.create(driver, webDriverWait);
        if (errorCard.isErrorPagePresent()) {
            ErrorCard.ErrorInformation errorInformation = errorCard.getErrorInformation();
            LOGGER.error(errorInformation.getErrorText());
            LOGGER.error(errorInformation.getErrorDescription());
            LOGGER.error(errorInformation.getErrorMessage());
            Assert.fail(String.format("Error Page is shown on %s page.", viewName));
        }
        checkSystemMessage();
    }

    @DataProvider(name = "views")
    public Object[][] views() {
        Object[][] csvArrayObject = readFromCSV();
        return csvArrayObject;
    }

    public String[][] readFromCSV() {

        String returnObj[][] = null;
        String csvFile = "./testdata.csv";
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ";";
        ArrayList<String> content = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(csvFile));
            int dataLength;
            int listSize;

            while ((line = br.readLine()) != null) {
                content.add(line);
            }
            listSize = content.size();
            dataLength = content.get(0).split(cvsSplitBy).length;
            returnObj = new String[listSize][dataLength];
            String[] data;
            for (int i = 0; i<listSize; i++) {
                data = content.get(i).split(cvsSplitBy);
                for (int j=0; j< dataLength ; j++) {
                    returnObj[i][j] = data[j];
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return returnObj;
    }
}