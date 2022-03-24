package com.oss.pages.nfv.networkslice;

import com.oss.framework.components.tree.TreeComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class NetworkSliceWizardStep extends BasePage {

    protected final Wizard networkSliceWizard;

    protected NetworkSliceWizardStep(WebDriver driver, WebDriverWait wait, Wizard networkSliceWizard) {
        super(driver, wait);
        this.networkSliceWizard = networkSliceWizard;
    }

    public TreeComponent getStructureTree() {
        return this.networkSliceWizard.getTreeComponent();
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