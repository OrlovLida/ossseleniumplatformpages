package com.oss.pages.nfv;

import com.oss.framework.components.tree.TreeComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class VNFWizardStep extends BasePage {

    protected final Wizard vnfWizard;

    protected VNFWizardStep(WebDriver driver, WebDriverWait wait, Wizard vnfWizard) {
        super(driver, wait);
        this.vnfWizard = vnfWizard;
    }

    public TreeComponent getStructureTree() {
        return this.vnfWizard.getTreeComponent();
    }

    public void selectNode(String path) {
        TreeComponent.Node node = getStructureTree().getNodeByPath(path);
        if (!node.isToggled()) {
            node.toggleNode();
        }
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public boolean nodeExists(String path) {
        return getStructureTree().findNodeByLabelsPath(path).isPresent();
    }
}
