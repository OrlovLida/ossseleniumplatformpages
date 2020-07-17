package com.oss.pages.reconciliation;

import org.openqa.selenium.WebDriver;

import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;

public class NetworkDiscoveryControlViewPage extends BasePage {

    private TreeWidget mainTree;

    public static NetworkDiscoveryControlViewPage goToNetworkInconsistenciesViewPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/reco/network-repository-view/network-discovery" +
                "?perspective=NETWORK", basicURL));
        return new NetworkDiscoveryControlViewPage(driver);
    }

    protected NetworkDiscoveryControlViewPage(WebDriver driver) {
        super(driver);
    }

    public void useContextAction(String groupLabel, String name) {
        TabsInterface tabs = TabWindowWidget.create(driver, wait);
        tabs.selectTabByLabel("Tree");
        tabs.callActionByLabel(groupLabel, name);
    }

    public TreeWidget getTreeView() {
        if (mainTree == null) {
            Widget.waitForWidget(wait, "TreeView");
            mainTree = TreeWidget.createByClass(driver, "TreeView", wait);
        }
        return mainTree;
    }

}