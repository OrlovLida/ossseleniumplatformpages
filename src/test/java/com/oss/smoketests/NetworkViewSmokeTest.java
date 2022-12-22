package com.oss.smoketests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.oss.BaseTestCase;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.navigation.toolsmanager.ToolsManagerWindow;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.advancedsearch.AdvancedSearchWidget;
import com.oss.pages.transport.NetworkViewPage;

import io.qameta.allure.Description;

public class NetworkViewSmokeTest extends BaseTestCase {

    private static final String CATEGORY_NAME = "Network Domains";
    private static final String VIEW_NAME = "Network View";
    private static final int EXPECTED_NUMBER_OF_OBJECTS_MATCHING = 1;
    private static final String ADVANCED_SEARCH_WIDGET_ID = "advancedSearch";
    private static final String XID_IN_ADVANCED_SEARCH = "id";
    private static final String NAME_IN_ADVANCED_SEARCH = "name";
    private static final String XID_IN_OBJECTS_ON_VIEW_CONTENT_COLUMN_ID = "Identifier";
    private static final String NAME_IN_OBJECTS_ON_VIEW_CONTENT_COLUMN_ID = "Name";
    private static final String XID_IN_FILTERS = "XId";
    private static final int PAGE_SIZE_OPTION = 10;
    private static final int INDEX_OF_FIRST_ELEMENT_IN_TABLE = 0;
    private NetworkViewPage networkViewPage;
    private AdvancedSearchWidget advancedSearchWidget;
    private String xid;
    private String name;

    @BeforeClass
    public void openWebConsole() {
        waitForPageToLoad();
    }

    @Test(description = "Go to network view using tools on main page")
    @Description("Go to network view using tools on main page")
    public void openNetworkViewUsingToolsManager() {
        ToolsManagerWindow toolsManagerWindow = ToolsManagerWindow.create(driver, webDriverWait);
        toolsManagerWindow.openApplication(CATEGORY_NAME, VIEW_NAME);
    }

    @Test(description = "Open Add To View Wizard", dependsOnMethods = {"openNetworkViewUsingToolsManager"})
    @Description("Open Add To View Wizard")
    public void openAddToView() {
        networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction(NetworkViewPage.ADD_TO_VIEW_ACTION, NetworkViewPage.DEVICE_ACTION);
        advancedSearchWidget = AdvancedSearchWidget.createById(driver, webDriverWait, ADVANCED_SEARCH_WIDGET_ID);
    }

    @Test(priority = 1, description = "Get XId and Identifier from last device on first page", dependsOnMethods = {"openNetworkViewUsingToolsManager", "openAddToView"})
    @Description("Get XId and Identifier from last device on first page")
    public void getLastDeviceOfFirstPageXIdAndName() {
        TableComponent tableComponent = advancedSearchWidget.getTableComponent();
        waitForPageToLoad();
        tableComponent.getPaginationComponent().changeRowsCount(PAGE_SIZE_OPTION);
        int lastElement = tableComponent.getPaginationComponent().getRowsCount() - 1;
        waitForPageToLoad();
        xid = tableComponent.getCellValue(lastElement, XID_IN_ADVANCED_SEARCH);
        name = tableComponent.getCellValue(lastElement, NAME_IN_ADVANCED_SEARCH);
    }

    @Test(priority = 2, description = "Insert data to filters", dependsOnMethods = {"openNetworkViewUsingToolsManager", "openAddToView", "getLastDeviceOfFirstPageXIdAndName"})
    @Description("Insert data to filters")
    public void insertDataToFilters() {
        SoftAssert softAssert = new SoftAssert();
        advancedSearchWidget.setFilter(XID_IN_ADVANCED_SEARCH, xid);
        advancedSearchWidget.setFilter(NAME_IN_ADVANCED_SEARCH, name);
        waitForPageToLoad();
        int numberOfObjectMatching = advancedSearchWidget.getTableComponent().getVisibleRows().size();
        softAssert.assertEquals(EXPECTED_NUMBER_OF_OBJECTS_MATCHING, numberOfObjectMatching);
        Multimap<String, String> expectedFilters = HashMultimap.create();
        expectedFilters.put(XID_IN_FILTERS, xid);
        expectedFilters.put(NAME_IN_OBJECTS_ON_VIEW_CONTENT_COLUMN_ID, name);
        softAssert.assertEquals(expectedFilters, advancedSearchWidget.getAppliedFilters());
        softAssert.assertAll();
    }

    @Test(priority = 3, description = "Add object to network view", dependsOnMethods = {"openNetworkViewUsingToolsManager", "openAddToView", "getLastDeviceOfFirstPageXIdAndName", "insertDataToFilters"})
    @Description("Add object to network view")
    public void addObjectsToView() {
        SoftAssert softAssert = new SoftAssert();
        advancedSearchWidget.getTableComponent().clickRow(INDEX_OF_FIRST_ELEMENT_IN_TABLE);
        advancedSearchWidget.clickAdd();
        networkViewPage.expandViewContentPanel();
        softAssert.assertTrue(networkViewPage.isObjectInViewContent(XID_IN_OBJECTS_ON_VIEW_CONTENT_COLUMN_ID, xid));
        softAssert.assertTrue(networkViewPage.isObjectInViewContent(NAME_IN_OBJECTS_ON_VIEW_CONTENT_COLUMN_ID, name));
        softAssert.assertAll();
    }

    @Test(priority = 4, description = "Delete object from network view", dependsOnMethods = {"openNetworkViewUsingToolsManager", "openAddToView", "getLastDeviceOfFirstPageXIdAndName", "addObjectsToView"})
    @Description("Delete object from network view")
    public void removeObjectFromView() {
        SoftAssert softAssert = new SoftAssert();
        networkViewPage.selectObjectInViewContent(XID_IN_OBJECTS_ON_VIEW_CONTENT_COLUMN_ID, xid);
        networkViewPage.useContextAction(NetworkViewPage.DISPLAY_ACTION, NetworkViewPage.REMOVE_FROM_VIEW_ACTION);
        softAssert.assertFalse(networkViewPage.isObjectInViewContent(XID_IN_OBJECTS_ON_VIEW_CONTENT_COLUMN_ID, xid));
        softAssert.assertFalse(networkViewPage.isObjectInViewContent(NAME_IN_OBJECTS_ON_VIEW_CONTENT_COLUMN_ID, name));
        softAssert.assertAll();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}
