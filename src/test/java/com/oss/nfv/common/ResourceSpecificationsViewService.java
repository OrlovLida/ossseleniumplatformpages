package com.oss.nfv.common;

import com.oss.pages.resourcecatalog.ResourceSpecificationsViewPage;

/**
 * @author Marzena Tolpa
 */
public class ResourceSpecificationsViewService {

    private static final String SPECIFICATION_NAME_ATTRIBUTE_NAME_LABEL = "Specification Name";

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

}
