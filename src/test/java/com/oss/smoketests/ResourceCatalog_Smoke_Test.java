package com.oss.smoketests;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.layout.ErrorCard;
import com.oss.framework.navigation.toolsmanager.ToolsManagerWindow;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.resourcecatalog.ResourceSpecificationsViewPage;

import io.qameta.allure.Description;
import io.qameta.allure.Step;

public class ResourceCatalog_Smoke_Test extends BaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceCatalog_Smoke_Test.class);
    private static final String RESOURCE_CATALOG = "Resource Catalog";
    private static final String RESOURCE_SPECIFICATIONS = "Resource Specifications";
    private static final String CHASSIS = "Chassis";
    private static final String PORT = "Port";
    private static final String TRAIL = "Trail";
    private static final String LOCATION = "Location";
    private static final String PHYSICAL_DEVICE = "Physical Device";
    private static final String LOGICAL_FUNCTION = "Logical Function";
    private static final String TERMINATION_POINT = "Termination Point";
    private static final String LOG_PATTENR = "Checking if specification with name %s exists.";

    @Test(priority = 1, description = "Open Resource Catalog view")
    @Description("Open Resource Catalog view")
    public void openResourceCatalog() {
        waitForPageToLoad();
        checkErrorPage();
        ToolsManagerWindow toolsManagerWindow = ToolsManagerWindow.create(driver, webDriverWait);
        toolsManagerWindow.openApplication(RESOURCE_CATALOG, RESOURCE_SPECIFICATIONS);
        waitForPageToLoad();
        checkErrorPage();
    }

    @Test(priority = 2, description = "Change pagination to 100 objects per page")
    @Description("Change pagination to 100 objects per page")
    public void changePageOption() {
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        resourceSpecificationsViewPage.setPageSize(100);
        waitForPageToLoad();
        checkErrorPage();
    }

    @Test(dataProvider = "types", priority = 3, description = "Search for specifications and check if it exists")
    @Description("Search for specifications and check if it exists")
    public void searchSpecification(String type) {
        LOGGER.info(String.format(LOG_PATTENR, type));
        Assert.assertTrue(checkSpecification(type));
    }

    @Step("Checking {specificationName} specification.")
    public boolean checkSpecification(String specificationName) {
        waitForPageToLoad();
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        resourceSpecificationsViewPage.clickClearAll();
        resourceSpecificationsViewPage.searchByAttribute("name", specificationName);
        waitForPageToLoad();
        resourceSpecificationsViewPage.collapseFirstNode();
        List<String> visibleRS = resourceSpecificationsViewPage.getAllVisibleSpecificationNames();
        return visibleRS.stream().anyMatch(rs -> rs.equals(specificationName));
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

    @DataProvider(name = "types")
    public Object[] types() {
        return new Object[]{
                CHASSIS,
                PORT,
                TRAIL,
                LOCATION,
                PHYSICAL_DEVICE,
                LOGICAL_FUNCTION,
                TERMINATION_POINT
        };
    }
}
