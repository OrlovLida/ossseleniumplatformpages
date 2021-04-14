package com.oss.pages.bigdata.kqiview;

import com.oss.framework.components.dpe.kpitoolbarpanel.ExportPanel;
import com.oss.framework.components.dpe.kpitoolbarpanel.ExportPanel.ExportType;
import com.oss.framework.components.dpe.kpitoolbarpanel.FiltersPanel;
import com.oss.framework.components.dpe.kpitoolbarpanel.LayoutPanel.LayoutType;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.dpe.kpichartwidget.KpiChartWidget;
import com.oss.framework.widgets.dpe.toolbarpanel.KpiToolbarPanel;
import com.oss.framework.widgets.dpe.treewidget.KpiTreeWidget;
import com.oss.pages.BasePage;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.oss.configuration.Configuration.CONFIGURATION;

public class KpiViewPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(KpiViewPage.class);

    private static final String INDICATORS_TREE_ID = "_Indicators";
    private static final String DIMENSIONS_TREE_ID = "_Dimensions";

    public KpiViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open KPI View")
    public static KpiViewPage goToPage(WebDriver driver, String basicURL){
        WebDriverWait wait = new WebDriverWait(driver, 90);

        String pageUrl = String.format("%s/#/view/Assurance/KPIView", basicURL);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);

        return new KpiViewPage(driver, wait);
    }

    private void selectTreeNodes(List<String> nodesToExpand, List<String> nodesToSelect, String componentId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        KpiTreeWidget indicatorsTree = KpiTreeWidget.create(driver, wait, componentId);
        indicatorsTree.selectNodes(nodesToExpand, nodesToSelect);
        log.debug("Expanded nodes: {}", nodesToExpand);
        log.debug("Selecting: {}", nodesToSelect);
    }

    @Step("I set filters: {enabledFilters}")
    public void setFilters(List<String> enabledFilters){
        DelayUtils.waitForPageToLoad(driver, wait);
        KpiToolbarPanel toolbar = KpiToolbarPanel.create(driver, wait);
        FiltersPanel filtersPanel = toolbar.getFiltersPanel();
        filtersPanel.openFilters();
        filtersPanel.clearFilters();
        filtersPanel.turnOnFilters(enabledFilters);
        filtersPanel.clickConfirm();

        log.info("Selected filters: {}", enabledFilters);
    }

    @Step("I select indicator")
    public void selectIndicator(List<String> nodesToExpand, List<String> nodesToSelect){
        log.info("Select indicator nodes: " + nodesToSelect);
        selectTreeNodes(nodesToExpand, nodesToSelect, INDICATORS_TREE_ID);
    }

    @Step("I select dimension")
    public void selectDimension(List<String> nodesToExpand, List<String> nodesToSelect){
        log.info("Select dimension nodes: " + nodesToSelect);
        selectTreeNodes(nodesToExpand, nodesToSelect, DIMENSIONS_TREE_ID);
    }

    @Step("I apply changes")
    public void applyChanges(){
        log.info("Apply changes");
        KpiToolbarPanel toolbar = KpiToolbarPanel.create(driver, wait);
        toolbar.clickApply();
    }

    @Step("I see chart is displayed")
    public void seeChartIsDisplayed(){
        log.info("Waiting for chart presence");
        KpiChartWidget.create(driver, wait).waitForPresenceAndVisibility();
    }

    @Step("I hover over some point")
    public void hoverOverPoint() {
        log.info("Hovering over some point");
        KpiChartWidget.create(driver, wait).hoverMouseOverPoint();
    }

    @Step("I export chart")
    public void exportChart() {
        KpiToolbarPanel toolbar = KpiToolbarPanel.create(driver, wait);
        ExportPanel exportPanel = toolbar.getExportPanel();
        log.info("Exporting chart to JPG");
        exportPanel.exportKpiToFile(ExportType.JPG);
        log.info("Exporting chart to PNG");
        exportPanel.exportKpiToFile(ExportType.PNG);
        log.info("Exporting chart to PDF");
        exportPanel.exportKpiToFile(ExportType.PDF);
        log.info("Exporting chart to XLSX");
        exportPanel.exportKpiToFile(ExportType.XLSX);
    }

    @Step("Attach exported chart to report")
    public void attachExportedChartToReport(){
        if(ifDownloadDirExists()){
            File directory = new File(CONFIGURATION.getDownloadDir());
            List<File> listFiles = (List<File>) FileUtils.listFiles(directory, new WildcardFileFilter("kpi_view_export_*.*"), null);
            try {
                for(File file: listFiles){
                    log.info("Attaching file: {}", file.getCanonicalPath());
                    attachFile(file);
                }
            } catch(IOException e){
                log.error("Failed attaching files: {}", e.getMessage());
            }

        }
    }

    @Attachment(value="Exported chart")
    public byte[] attachFile(File file) throws IOException {
        return FileUtils.readFileToByteArray(file);
    }

    @Step("I change layout")
    public void changeLayout(){
        KpiToolbarPanel toolbar = KpiToolbarPanel.create(driver, wait);
        toolbar.getLayoutPanel().changeLayout(LayoutType.LAYOUT_2x2);

    }

    @Step("I maximize chart")
    public void maximizeChart(){
        KpiChartWidget.create(driver, wait).maximizeChart();
    }

    @Step("I minimize chart")
    public void minimizeChart(){
        KpiChartWidget.create(driver, wait).minimizeChart();
    }

    private boolean ifDownloadDirExists(){
        File tmpDir = new File(CONFIGURATION.getDownloadDir());
        if(tmpDir.exists()){
            log.info("Download directory was successfully created");
            return true;
        }
        return false;
    }

}
