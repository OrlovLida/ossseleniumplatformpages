package com.oss.pages.nfv.networkservice;

import com.oss.framework.components.tree.TreeComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class NetworkServiceWizardStep extends BasePage {

    protected final Wizard networkServiceWizard;

    protected NetworkServiceWizardStep(WebDriver driver, WebDriverWait wait, Wizard networkServiceWizard) {
        super(driver, wait);
        this.networkServiceWizard = networkServiceWizard;
    }

    public TreeComponent getStructureTree() {
        return this.networkServiceWizard.getTreeComponent();
    }

    public void selectNode(String path) {
        TreeComponent.Node node = getStructureTree().getNodeByLabelsPath(path);
        if (!node.isToggled()) {
            node.toggleNode();
        }
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public boolean nodeExists(String path) {
        return getStructureTree().findNodeByLabelsPath(path).isPresent();
    }

}
