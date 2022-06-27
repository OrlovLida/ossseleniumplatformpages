package com.oss.reconciliation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.layout.ErrorCard;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.HomePage;

import io.qameta.allure.Description;

public class RecoViews_Test extends BaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecoViews_Test.class);

    @BeforeClass
    public void openNetworkDiscoveryControlView() {
        waitForPageToLoad();
    }

    @Test(dataProvider = "data", priority = 1, description = "Checking {viewName} from Network Discovery and Reconciliation")
    @Description("Checking {viewName} from Network Discovery and Reconciliation")
    public void checkRecoViews(String viewName, String path) {
        String groupName = "Network Discovery and Reconciliation";
        HomePage homePage = new HomePage(driver);
        if (path == null || path.isEmpty()) {
            LOGGER.debug("Checking " + viewName);
            homePage.chooseFromLeftSideMenu(viewName, groupName);
        } else {
            LOGGER.debug("Checking " + viewName + " in " + path);
            homePage.chooseFromLeftSideMenu(viewName, groupName, path);
        }
        waitForPageToLoad();
        checkErrorPage();
    }

    @DataProvider(name = "data")
    private Object[][] getData() {
        return new Object[][]{
                {"Network Discovery Control", ""},
                {"Network Inconsistencies View", ""},
                {"VS Viewer", ""},
                {"TO Viewer", ""},
                {"VS Viewer 2.0", ""},
                {"Type Specific VS Viewer", ""},
                {"Metamodel Editor", ""},
                {"Parameter Audit Control", "Parameters Audit"},
                {"Validation Rules Manager", "Parameters Audit"}
        };
    }

    private void checkErrorPage() {
        ErrorCard errorCard = ErrorCard.create(driver, webDriverWait);
        if (errorCard.isErrorPagePresent()) {
            ErrorCard.ErrorInformation errorInformation = errorCard.getErrorInformation();
            LOGGER.error(errorInformation.getErrorText());
            LOGGER.error(errorInformation.getErrorDescription());
            LOGGER.error(errorInformation.getErrorMessage());
            Assert.fail("Error Page is shown.");
        }
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}