package com.oss.pages.platform;

import com.oss.framework.mainheader.ButtonPanel;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class HierarchyViewPage extends BasePage {

    private TreeWidget mainTree;

    public HierarchyViewPage(WebDriver driver) {
        super(driver);
    }

    public TreeWidget getTreeWidget() {
        if(mainTree == null){
            Widget.waitForWidget(wait, "TreeWidget");
            mainTree = TreeWidget.createByClass(driver, "TreeWidget", wait);
        }
        return mainTree;
    }

    @Step("Open Hierarchy View")
    public static HierarchyViewPage goToHierarchyViewPage(WebDriver driver, String basicURL, String type, String xid) {
        driver.get(String.format("%s/#/views/management/views/hierarchy-view/" + type + "?" + xid +
                "&perspective=LIVE" , basicURL));
        return new HierarchyViewPage(driver);
    }

    @Step("Open save configuration wizard for page")
    public SaveConfigurationWizard openSavePageConfigurationWizard(){
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).getButtonIcon("fa fa-fw fa-floppy-o").click();
        return new SaveConfigurationWizard(driver);
    }

    @Step("Open choose configuration wizard for page")
    public ChooseConfigurationWizard openChooseConfigurationForPageWizard(){
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).getButtonIcon("fa fa-fw fa-cog").click();
        return new ChooseConfigurationWizard(driver);
    }

    @Step("Open download configuration wizard for page")
    public ChooseConfigurationWizard openDownloadConfigurationForPageWizard(){
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).getButtonIcon("fa fa-fw fa-download").click();
        return new ChooseConfigurationWizard(driver);
    }
}
