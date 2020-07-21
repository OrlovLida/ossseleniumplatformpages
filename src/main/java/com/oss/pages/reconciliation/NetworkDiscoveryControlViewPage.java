package com.oss.pages.reconciliation;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;

import com.oss.framework.components.notifications.Notifications;
import com.oss.framework.components.notifications.NotificationsInterface;
import com.oss.framework.components.systemMessage.SystemMessage;
import com.oss.framework.components.systemMessage.SystemMessageInterface;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;

public class NetworkDiscoveryControlViewPage extends BasePage {

    private TreeWidget mainTree;
    private String reconciliation = "narComponent_CmDomainActionFullReconciliationId";

    public static NetworkDiscoveryControlViewPage goToNetworkInconsistenciesViewPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/reco/network-repository-view/network-discovery" +
                "?perspective=NETWORK", basicURL));
        return new NetworkDiscoveryControlViewPage(driver);
    }

    protected NetworkDiscoveryControlViewPage(WebDriver driver) {
        super(driver);
    }

    public void createCmDomain(String cmDomainName, String cmInterfaceName, String domainName) {
        TabsInterface tabs = TabWindowWidget.create(driver, wait);
        tabs.selectTabByLabel("Tree");
        tabs.callActionByLabel("CREATE", "Create CM Domain");
        new CmDomainWizardPage(driver).fillCmDomainWizardAndClose(cmDomainName, cmInterfaceName, domainName);
    }

    public TreeWidget getTreeView() {
        if (mainTree == null) {
            Widget.waitForWidget(wait, "TreeView");
            mainTree = TreeWidget.createByClass(driver, "TreeView", wait);
        }
        return mainTree;
    }

    public void queryAndSelectCmDomain(String cmDomainName) {
        getTreeView()
                .performSearchWithEnter(cmDomainName).waitForTreeExpansion();
        getTreeView()
                .selectTreeRowByText(cmDomainName);
    }

    public boolean runReconciliation() {
        TabsInterface tabs = TabWindowWidget.create(driver, wait);
        tabs.selectTabByLabel("Tree");
        tabs.callActionById(reconciliation);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel("Reconcile");
        SystemMessageInterface info = SystemMessage.create(driver, wait);
        return info.getMessage().equals("Reconciliation started.");
    }

    public void deleteCmDomain(String cmDomainName) {
        NotificationsInterface notifications = Notifications.create(driver, wait);
        notifications.clearAllNotification();
        TabsInterface tabs = TabWindowWidget.create(driver, wait);
        tabs.selectTabByLabel("Tree");
        tabs.callActionByLabel("EDIT", "Delete CM Domain");
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel("Delete");
        SystemMessageInterface info = SystemMessage.create(driver, wait);
        Assertions.assertThat(info.getMessage().equals("Deleting CM Domain started. Please check notifications for updates.")).isTrue();
        Assertions.assertThat(notifications.waitAndGetFinishedNotificationText().equals("Deleting CM Domain: " + cmDomainName + "")).isTrue();
    }

}