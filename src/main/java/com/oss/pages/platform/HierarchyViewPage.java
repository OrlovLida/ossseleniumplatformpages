package com.oss.pages.platform;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.mainheader.ButtonPanel;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.prompts.Popup;
import com.oss.framework.components.tree.TreeComponent.Node;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.widgets.tree.TreeWidgetV2;
import com.oss.pages.BasePage;
import com.oss.pages.platform.configuration.SaveConfigurationWizard;
import com.oss.pages.platform.configuration.SaveConfigurationWizard.Field;

import io.qameta.allure.Step;

public class HierarchyViewPage extends BasePage {

    public static final String OPEN_HIERARCHY_VIEW_CONTEXT_ACTION_ID = "HierarchyView";
    private static final String BOTTOM_TABS_WIDGET_ID = "BottomDetailCard";
    private static final String HIERARCHY_VIEW_TREE_WIDGET_ID = "HierarchyTreeWidget";

    private HierarchyViewPage(WebDriver driver) {
        super(driver);
    }

    private HierarchyViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static HierarchyViewPage getHierarchyViewPage(WebDriver driver, WebDriverWait wait) {
        return new HierarchyViewPage(driver, wait);
    }

    public static HierarchyViewPage openHierarchyViewPage(WebDriver driver, String basicURL, String type) {
        driver.get(String.format("%s/#/views/management/views/hierarchy-view/%s?perspective=LIVE", basicURL, type));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        DelayUtils.waitForPageToLoad(driver, wait);
        return new HierarchyViewPage(driver, wait);
    }

    @Step("Open Hierarchy View")
    public static HierarchyViewPage goToHierarchyViewPage(WebDriver driver, String basicURL, String type, String xid) {
        driver.get(String.format("%s/#/views/management/views/hierarchy-view/%s?id=%s&perspective=LIVE", basicURL, type, xid));
        return new HierarchyViewPage(driver);
    }

    @Step("Open Hierarchy View In Plan Perspective")
    public static HierarchyViewPage goToHierarchyViewPage(WebDriver driver, String basicURL, String type, String xid, String projectId) {
        driver.get(String.format("%s/#/views/management/views/hierarchy-view/%s?project_id=%s&perspective=PLAN&id=%s", basicURL, type, projectId, xid));
        return new HierarchyViewPage(driver);
    }

    public TreeWidgetV2 getMainTree() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return TreeWidgetV2.create(driver, wait, HIERARCHY_VIEW_TREE_WIDGET_ID);
    }

    public Node getFirstNode() {
        return getMainTree().getNode(0);
    }

    public Node getNodeByLabelPath(String path) {
        return getMainTree().getNodeByLabelsPath(path);
    }

    public void searchObject(String text) {
        TreeWidgetV2 treeWidgetV2 = getMainTree();
        treeWidgetV2.fullTextSearch(text);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void clearFiltersOnMainTree(String filterName) {
        TreeWidgetV2 treeWidgetV2 = getMainTree();
        treeWidgetV2.clearFilter(filterName);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void clearFiltersOnMainTree() {
        TreeWidgetV2 treeWidgetV2 = getMainTree();
        treeWidgetV2.clearAllFilters();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public TabsWidget getBottomTabsWidget() {
        Widget.waitForWidget(wait, TabsWidget.TABS_WIDGET_CLASS);
        return TabsWidget.createById(driver, wait, BOTTOM_TABS_WIDGET_ID);
    }

    @Step("Check if tree has no data")
    public boolean hasNoData() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getMainTree().hasNoData();
    }

    @Step("Save new configuration for page")
    public HierarchyViewPage saveNewPageConfiguration(String configurationName, Field... fields) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).clickButton("ButtonSaveViewConfig");
        SaveConfigurationWizard.create(driver, wait).saveAsNew(configurationName, fields);
        return this;
    }

    @Step("Select First Object on Tree Widget")
    public void selectFirstObject() {
        getMainTree().selectNode(0);
    }

    @Step("Unselect First Object on Tree Widget")
    public void unselectFirstObject() {
        String nodeLabel = getMainTree().getNode(0).getLabel();
        getMainTree().unselectNodeByLabel(nodeLabel);
    }

    @Step("Use tree context action")
    public void callAction(String groupId, String actionId) {
        getMainTree().callActionById(groupId, actionId);
    }

    @Step("Use tree context action")
    public void callActionByLabel(String groupLabel, String actionLabel) {
        getMainTree().callActionByLabel(groupLabel, actionLabel);
    }

    @Step("Click {label} in Confirmation box")
    public void clickButtonInConfirmationBox(String label) {
        ConfirmationBox confirmationBox = ConfirmationBox.create(driver, wait);
        confirmationBox.clickButtonByLabel(label);
    }

    @Step("Expand tree node by label - {label}")
    public void expandTreeNode(String label) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getMainTree().expandNodeWithLabel(label);
    }

    public void collapseNodeByPath(String path) {
        getMainTree().getNodeByPath(path).collapseNode();
    }

    public Set<String> getNodeChildrenByPath(String path) {
        return getMainTree().getNodeChildrenByPath(path);
    }

    @Step("Select tree node by label - {label}")
    public void selectNodeByLabel(String label) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getMainTree()
                .selectNodeByLabel(label);
    }

    @Step("Select tree node by labels - {labels}")
    public void selectNodeByLabelsPath(String labels) {
        Node node = getMainTree().getNodeByLabelsPath(labels);
        if (!node.isToggled()) {
            node.toggleNode();
        }
    }

    public void unselectNodeByLabelsPath(String labels) {
        Node node = getMainTree().getNodeByLabelsPath(labels);
        if (node.isToggled()) {
            node.toggleNode();
        }
    }

    public String getGroupActionLabel(String groupId) {
        return getMainTree().getGroupActionLabel(groupId);
    }

    public List<String> getVisibleNodesLabel() {
        return getMainTree().getVisibleNodes().stream().map(Node::getLabel).collect(Collectors.toList());
    }

    public Optional<Popup> expandNextLevel(String pathLabel) {
        return getMainTree().getNodeByLabelsPath(pathLabel).expandNextLevel();
    }

    public boolean isNodePresent(String pathLabel) {
        return getMainTree().findNodeByLabelsPath(pathLabel).isPresent();
    }
}
