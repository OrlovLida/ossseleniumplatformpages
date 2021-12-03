package com.oss.pages.transport.loopbackInterface;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.CommonHierarchyApp;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.platform.OldInventoryView.OldInventoryViewPage;
import org.openqa.selenium.WebDriver;

/**
 * @author Kamil Jacko
 */
public class LoopbackInterfaceWizardPage extends BasePage {

    private final Wizard wizard;

    private static final String NUMBER_FIELD_ID = "uid-number";
    private static final String DESCRIPTION_FIELD_ID = "uid-description";

    public LoopbackInterfaceWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }

    public void setNumber(String number) {
        wizard.setComponentValue(NUMBER_FIELD_ID, number, Input.ComponentType.TEXT_FIELD);
    }

    public void setDescription(String description) {
        wizard.setComponentValue(DESCRIPTION_FIELD_ID, description, Input.ComponentType.TEXT_FIELD);
    }

    public void clearNumber() {
        Input numberComponent = wizard.getComponent(NUMBER_FIELD_ID, Input.ComponentType.TEXT_FIELD);
        numberComponent.clearByAction();
    }

    public void clearDescription() {
        Input descriptionComponent = wizard.getComponent(DESCRIPTION_FIELD_ID, Input.ComponentType.TEXT_FIELD);
        descriptionComponent.clearByAction();
    }

    public void clickNextStep() {
        wizard.clickNextStep();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void searchLocationAndDevice(String location, String device) {
        CommonHierarchyApp hierarchyApp = CommonHierarchyApp.createByClass(driver, wait);
        hierarchyApp.navigateToPath(location, device);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public OldInventoryViewPage clickAccept() {
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickAcceptOldWizard();
        return new OldInventoryViewPage(driver);
    }
}
