package com.oss.pages.nfv.networkslicesubnet;

import com.oss.framework.components.tree.TreeComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class NetworkSliceSubnetWizardStep extends BasePage {

    protected final Wizard networkSliceSubnetWizard;

    protected NetworkSliceSubnetWizardStep(WebDriver driver, WebDriverWait wait, Wizard networkSliceSubnetWizard) {
        super(driver, wait);
        this.networkSliceSubnetWizard = networkSliceSubnetWizard;
    }

    public TreeComponent getStructureTree() {
        return this.networkSliceSubnetWizard.getTreeComponent();
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