package com.oss.pages.bigdata.kqiview;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.iaa.widgets.dpe.toolbarpanel.FiltersPanel;
import com.oss.framework.iaa.widgets.dpe.toolbarpanel.KpiToolbarPanel;
import com.oss.framework.iaa.widgets.dpe.toolbarpanel.OptionsPanel;
import com.oss.framework.iaa.widgets.dpe.treewidget.KpiTreeWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.utils.DelayUtils.waitForPageToLoad;

public class KpiViewSetupPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(KpiViewSetupPage.class);

    private static final String INDICATORS_TREE_ID = "_Indicators";
    private static final String DIMENSIONS_TREE_ID = "_Dimensions";
    private static final String DIMENSION_OPTIONS_BUTTON_ID = "dimension-options-button";
    private final KpiViewPage kpiViewPage;
    private final KpiToolbarPanelPage kpiToolbarPanel;

    public KpiViewSetupPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        kpiViewPage = new KpiViewPage(driver, wait);
        kpiToolbarPanel = new KpiToolbarPanelPage(driver, wait);
    }

    @Step("I set filters: {enabledFilters}")
    public void setFilters(List<String> enabledFilters) {
        waitForPageToLoad(driver, wait);
        getFiltersPanel().clearFilters();
        getFiltersPanel().turnOnFilters(enabledFilters);
        getFiltersPanel().clickConfirm();

        log.info("Selected filters: {}", enabledFilters);
    }

    private FiltersPanel getFiltersPanel() {
        return KpiToolbarPanel.create(driver, wait).openFilterPanel();
    }

    @Step("I select indicator")
    public void selectIndicator(List<String> nodesToExpand, List<String> nodesToSelect) {
        log.info("Select indicator nodes: {}", nodesToSelect);
        selectTreeNodes(nodesToExpand, nodesToSelect, INDICATORS_TREE_ID);
    }

    @Step("I select dimension")
    public void selectDimension(List<String> nodesToExpand, List<String> nodesToSelect) {
        log.info("Select dimension nodes: {}", nodesToSelect);
        selectTreeNodes(nodesToExpand, nodesToSelect, DIMENSIONS_TREE_ID);
    }

    @Step("I select unfolded dimension")
    public void selectUnfoldedDimension(List<String> nodesToSelect) {
        log.info("Select dimension nodes: {}", nodesToSelect);
        selectUnfoldedTreeNodes(nodesToSelect, DIMENSIONS_TREE_ID);
    }

    @Step("I setup Indicators View")
    public void kpiViewSetup(String indicatorNodesToExpand, String indicatorNodesToSelect,
                             String dimensionNodesToExpand, String dimensionNodesToSelect, String filterName) {
        setFilters(Collections.singletonList(filterName));

        List<String> indicatorNodesToExpandList = Arrays.asList(indicatorNodesToExpand.split(","));
        List<String> indicatorNodesToSelectList = Arrays.asList(indicatorNodesToSelect.split(","));
        selectIndicator(indicatorNodesToExpandList, indicatorNodesToSelectList);
        List<String> dimensionNodesToSelectList = Arrays.asList(dimensionNodesToSelect.split(","));

        if (dimensionNodesToExpand != null) {
            List<String> dimensionNodesToExpandList = Arrays.asList(dimensionNodesToExpand.split(","));
            selectDimension(dimensionNodesToExpandList, dimensionNodesToSelectList);
        } else {
            selectUnfoldedDimension(dimensionNodesToSelectList);
        }

        kpiToolbarPanel.selectAggregationMethod(OptionsPanel.AggregationMethodOption.SUM);
        kpiToolbarPanel.unselectEveryAggMethodOtherThan(OptionsPanel.AggregationMethodOption.SUM);
        kpiToolbarPanel.closeOptionsPanel();

        kpiToolbarPanel.applyChanges();
        kpiViewPage.seeChartIsDisplayed();
    }

    @Step("Check if node is selected in the tree")
    public boolean isNodeInTreeSelected(String objectName, String treeId) {
        log.info("Checking if node: {} is selected in the tree", objectName);
        return getKpiTree(treeId).isNodeSelected(objectName);
    }

    @Step("I click on dimension node options button")
    public void clickDimensionOptions(String dimensionNodeName) {
        if (dimensionNodeName != null) {
            getKpiTree(DIMENSIONS_TREE_ID).clickNodeOptions(dimensionNodeName);
            log.info("I Click on options button in: {}", dimensionNodeName);
        } else {
            Button.createById(driver, DIMENSION_OPTIONS_BUTTON_ID).click();
            log.info("I click on option button in first dimension node");
        }
    }

    @Step("I search for Object in tree search toolbar")
    public void searchInToolbarPanel(String objectName, String treeId) {
        waitForPageToLoad(driver, wait);
        KpiTreeWidget kpiTreeWidget = getKpiTree(treeId);
        kpiTreeWidget.searchInToolbarPanel(objectName);
        waitForPageToLoad(driver, wait);
        kpiTreeWidget.selectResult(objectName);
        kpiTreeWidget.closeSearchToolbar();
    }

    private void selectTreeNodes(List<String> nodesToExpand, List<String> nodesToSelect, String componentId) {
        waitForPageToLoad(driver, wait);
        getKpiTree(componentId).selectNodes(nodesToExpand, nodesToSelect);
        log.debug("Expanded nodes: {}", nodesToExpand);
        log.debug("Selecting: {}", nodesToSelect);
    }

    private void selectUnfoldedTreeNodes(List<String> nodesToSelect, String componentId) {
        waitForPageToLoad(driver, wait);
        getKpiTree(componentId).selectExpandedObjects(nodesToSelect);
        log.debug("Selecting: {}", nodesToSelect);
    }

    private KpiTreeWidget getKpiTree(String componentId) {
        return KpiTreeWidget.create(driver, wait, componentId);
    }
}
