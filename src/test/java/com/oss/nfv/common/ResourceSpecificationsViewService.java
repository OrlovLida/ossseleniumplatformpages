package com.oss.nfv.common;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.resourcecatalog.ResourceSpecificationsViewPage;

/**
 * @author Marzena Tolpa
 */
public class ResourceSpecificationsViewService {

    private static final String SPECIFICATION_NAME_ATTRIBUTE_NAME_LABEL = "Specification Name";
    private static final String CREATE_LOGICAL_FUNCTION = "Create Logical Function";

    public static void openCreateLogicalFunctionWizard(String resourceSpecificationIdentifier,
                                                       String resourceSpecificationName,
                                                       WebDriversData webDriversData) {
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(
                webDriversData.getWebDriver(), webDriversData.getWebDriverWait());
        searchResourceSpecification(resourceSpecificationIdentifier, resourceSpecificationsViewPage);
        selectResourceSpecificationOnTree(resourceSpecificationName, resourceSpecificationsViewPage);
        DelayUtils.waitForPageToLoad(webDriversData.getWebDriver(), webDriversData.getWebDriverWait());
        openCreateLogicalFunctionWizard(resourceSpecificationsViewPage);
    }

    public static void selectResourceSpecificationOnTree(String resourceSpecificationIdentifier,
                                                         String resourceSpecificationName,
                                                         ResourceSpecificationsViewPage resourceSpecificationsViewPage) {
        searchResourceSpecification(resourceSpecificationIdentifier, resourceSpecificationsViewPage);
        selectResourceSpecificationOnTree(resourceSpecificationName, resourceSpecificationsViewPage);
    }

    private static void searchResourceSpecification(String resourceSpecificationIdentifier,
                                                    ResourceSpecificationsViewPage resourceSpecificationsViewPage) {
        resourceSpecificationsViewPage.setSearchText(resourceSpecificationIdentifier);
    }

    private static void selectResourceSpecificationOnTree(String resourceSpecificationName,
                                                          ResourceSpecificationsViewPage resourceSpecificationsViewPage) {
        resourceSpecificationsViewPage.selectTreeNode(resourceSpecificationName, SPECIFICATION_NAME_ATTRIBUTE_NAME_LABEL);
    }

    private static void openCreateLogicalFunctionWizard(ResourceSpecificationsViewPage resourceSpecificationsViewPage) {
        resourceSpecificationsViewPage.callActionByLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_LOGICAL_FUNCTION);
    }
}
