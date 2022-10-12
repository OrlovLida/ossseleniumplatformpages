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

public class Network_View_Smoke_Test extends BaseTestCase {

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
    private final SoftAssert softAssert = new SoftAssert();
    private NetworkViewPage networkViewPage;
    private AdvancedSearchWidget advancedSearchWidget;
    private String Xid;
    private String Name;

    @BeforeClass
    public void openWebConsole() {
        waitForPageToLoad();
    }

    @Test(description = "Get to network view using tools on main page")
    @Description("Get to network view using tools on main page")
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

    @Test(description = "Get XId and Identifier from last device on first page", dependsOnMethods = {"openNetworkViewUsingToolsManager", "openAddToView"})
    @Description("Get XId and Identifier from last device on first page")
    public void getLastDeviceOfFirstPageXIdAndIdentifier() {
        TableComponent tableComponent = advancedSearchWidget.getTableComponent();
        waitForPageToLoad();
        tableComponent.getPaginationComponent().changeRowsCount(PAGE_SIZE_OPTION);
        int lastElement = tableComponent.getPaginationComponent().getRowsCount() - 1;
        waitForPageToLoad();
        Xid = tableComponent.getCellValue(lastElement, XID_IN_ADVANCED_SEARCH);
        Name = tableComponent.getCellValue(lastElement, NAME_IN_ADVANCED_SEARCH);
    }

    @Test(description = "Insert data to filters", dependsOnMethods = {"openNetworkViewUsingToolsManager", "openAddToView", "getLastDeviceOfFirstPageXIdAndIdentifier"})
    @Description("Insert data to filters")
    public void insertDataToFilters() {
        advancedSearchWidget.setFilter(XID_IN_ADVANCED_SEARCH, Xid);
        advancedSearchWidget.setFilter(NAME_IN_ADVANCED_SEARCH, Name);
        waitForPageToLoad();
        int numberOfObjectMatching = advancedSearchWidget.getTableComponent().getVisibleRows().size();
        softAssert.assertEquals(EXPECTED_NUMBER_OF_OBJECTS_MATCHING, numberOfObjectMatching);
        Multimap<String, String> expectedFilters = HashMultimap.create();
        expectedFilters.put(XID_IN_FILTERS, Xid);
        expectedFilters.put(NAME_IN_OBJECTS_ON_VIEW_CONTENT_COLUMN_ID, Name);
        softAssert.assertEquals(expectedFilters, advancedSearchWidget.getAppliedFilters());
    }

    @Test(description = "Add object to network view", dependsOnMethods = {"openNetworkViewUsingToolsManager", "openAddToView", "getLastDeviceOfFirstPageXIdAndIdentifier", "insertDataToFilters"})
    @Description("Add object to network view")
    public void addObjectsToView() {
        advancedSearchWidget.getTableComponent().clickRow(INDEX_OF_FIRST_ELEMENT_IN_TABLE);
        advancedSearchWidget.clickAdd();
        networkViewPage.expandViewContentPanel();
        softAssert.assertTrue(networkViewPage.isObjectInViewContent(XID_IN_OBJECTS_ON_VIEW_CONTENT_COLUMN_ID, Xid));
        softAssert.assertTrue(networkViewPage.isObjectInViewContent(NAME_IN_OBJECTS_ON_VIEW_CONTENT_COLUMN_ID, Name));
    }

    @Test(description = "Delete object from network view", dependsOnMethods = {"openNetworkViewUsingToolsManager", "openAddToView", "getLastDeviceOfFirstPageXIdAndIdentifier", "addObjectsToView"})
    @Description("Delete object from network view")
    public void deleteObjectFromView() {
        networkViewPage.selectObjectInViewContent(XID_IN_OBJECTS_ON_VIEW_CONTENT_COLUMN_ID, Xid);
        networkViewPage.useContextAction(NetworkViewPage.DISPLAY_ACTION, NetworkViewPage.REMOVE_FROM_VIEW_ACTION);
        softAssert.assertFalse(networkViewPage.isObjectInViewContent(XID_IN_OBJECTS_ON_VIEW_CONTENT_COLUMN_ID, Xid));
        softAssert.assertFalse(networkViewPage.isObjectInViewContent(NAME_IN_OBJECTS_ON_VIEW_CONTENT_COLUMN_ID, Name));
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}
