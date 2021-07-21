package com.oss;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.TreeWidgetV2.TreeWidgetV2;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.platform.HierarchyViewPage;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class HierarchyViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(HierarchyViewTest.class);

    private HierarchyViewPage hierarchyViewPage;

    SoftAssert soft = new SoftAssert();

    @BeforeClass
    public void goToHierarchyViewPage() {
        hierarchyViewPage = HierarchyViewPage.openHierarchyViewPage(driver, BASIC_URL, "PhysicalDevice");
    }

    @Test (priority = 0)
    public void selectFirstNode() {
        hierarchyViewPage.getTreeWidget().selectFirstNode();
        Assertions.assertThat(hierarchyViewPage
                .getTreeWidget().getFirstNode().isToggled()).isTrue();
    }

    @Test (priority = 1)
    public void expandNode() {
        TreeWidgetV2 treeWidget = hierarchyViewPage.getTreeWidget();
        treeWidget.getFirstNode().expandNode();
        Assertions.assertThat(treeWidget.getFirstNode().isExpanded());
    }

    @Test (priority = 5)
    public void displayInlineActions() {
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
//        boolean inlineMenu = hierarchyViewPage
//                .getTreeWidget()
//                .selectFirstNode().getSelectedNodes().get(0);
//        Assert.assertTrue(inlineMenu, "Inline actions are not displayed");
    }

    @Test (priority = 6)
    public void checkAvailableActionsForSelectedNode() {
        TreeWidgetV2 treeWidget = hierarchyViewPage.getTreeWidget();
        treeWidget.selectFirstNode();
        soft.assertTrue(treeWidget.isActionDisplayed("CREATE"), "Create button is not displayed");
        soft.assertTrue(treeWidget.isActionDisplayed("EDIT"), "Edit button is not displayed");
        soft.assertTrue(treeWidget.isActionDisplayed("NAVIGATION"), "Navigation button is not displayed");
        soft.assertTrue(treeWidget.isActionDisplayed("OTHER"), "Other button is not displayed");
    }

    @Test (priority = 7)
    public void checkAvailableActionsInDefaultView() {
        TreeWidgetV2 treeWidget = hierarchyViewPage
                .getTreeWidget();
        Assert.assertTrue(treeWidget.isActionDisplayed("CREATE"), "Create button is not displayed");
        Assert.assertTrue(treeWidget.isActionDisplayed("frameworkCustomButtonsGroup"), "Hamburger menu is not displayed");

    }

    @Test (priority = 8)
    public void searchWithNotExistingData() {
        TreeWidgetV2 treeWidgetV2 = hierarchyViewPage
                .getTreeWidget();
        treeWidgetV2.performSearch("hjfkahfdadf");
        Assert.assertTrue(hierarchyViewPage.getTreeWidget().isTreeWidgetEmpty(), "Tree widget is not empty");
    }

    @Test (priority = 9)
    public void searchWithExistingData() {
//        TreeWidget treeWidget = hierarchyViewPage
//                .getTreeWidget();
//                treeWidget.performSearch(firstNodeLabel);
//        Assert.assertTrue(treeWidget.isSearchingCorrect(firstNodeLabel), "Searching is not correct");

    }
}
