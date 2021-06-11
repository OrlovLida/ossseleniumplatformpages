package com.oss;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.treewidget.InlineMenu;
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

    private static final String TREE_WIDGET_URL = String.format("%s/#/views/management/views/hierarchy-view/PhysicalDevice?perspective=LIVE",BASIC_URL);
    private HierarchyViewPage hierarchyViewPage;
    private String firstNodeLabel;

    SoftAssert soft = new SoftAssert();

    @BeforeClass
    public void goToHierarchyViewPage() {hierarchyViewPage = homePage.goToHierarchyViewPage(TREE_WIDGET_URL);
        firstNodeLabel = hierarchyViewPage.getTreeWidget().getFirstNodeLabel();
    }

    @Test (priority = 0)
    public void selectFirstNode() {
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        hierarchyViewPage
                .getTreeWidget()
                .selectNode();
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        hierarchyViewPage.getTreeWidget().getNode("39938734").click();
        Assertions.assertThat(hierarchyViewPage
                .getTreeWidget().isNodeSelected()).isTrue();
    }

    @Test (priority = 1)
    public void expandNode() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
       TreeWidget treeWidget = hierarchyViewPage.getTreeWidget();
        treeWidget.getFirstNode().expandNode();
        Assertions.assertThat(treeWidget.getFirstNode().isExpanded());
    }

    @Test (priority = 5)
    public void displayInlineActions() {
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        boolean inlineMenu = hierarchyViewPage
                .getTreeWidget()
                .selectNode().getSelectedNodes().get(0).isInlineActionPresent();
        Assert.assertTrue(inlineMenu, "Inline actions are not displayed");
    }

    @Test (priority = 6)
    public void checkAvailableActionsForSelectedNode() {
        TreeWidget treeWidget = hierarchyViewPage
                .getTreeWidget()
                .selectNode();
        soft.assertTrue(treeWidget.isActionDisplayed("CREATE"), "Create button is not displayed");
        soft.assertTrue(treeWidget.isActionDisplayed("EDIT"), "Edit button is not displayed");
        soft.assertTrue(treeWidget.isActionDisplayed("NAVIGATION"), "Navigation button is not displayed");
        soft.assertTrue(treeWidget.isActionDisplayed("OTHER"), "Other button is not displayed");
    }

    @Test (priority = 7)
    public void checkAvailableActionsInDefaultView() {
        TreeWidget treeWidget = hierarchyViewPage
                .getTreeWidget();
        Assert.assertTrue(treeWidget.isActionDisplayed("CREATE"), "Create button is not displayed");
        Assert.assertTrue(treeWidget.isActionDisplayed("frameworkCustomButtonsGroup"), "Hamburger menu is not displayed");

    }

    @Test (priority = 8)
    public void searchWithNotExistingData() {
        hierarchyViewPage
                .getTreeWidget()
                .performSearch("hjfkahfdadf");
        Assert.assertTrue(hierarchyViewPage.getTreeWidget().isTreeWidgetEmpty(), "Tree widget is not empty");
    }

    @Test (priority = 9)
    public void searchWithExistingData() {
        TreeWidget treeWidget = hierarchyViewPage
                .getTreeWidget();
                treeWidget.performSearch(firstNodeLabel);
        Assert.assertTrue(treeWidget.isSearchingCorrect(firstNodeLabel), "Searching is not correct");

    }
}
