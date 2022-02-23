package com.oss.pages.nfv;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.oss.framework.components.inputs.Input.ComponentType.CHECKBOX;

public class VNFWizardSecondStep extends VNFWizardStep {

    private static final String TERMINATION_POINTS_RELATIONS_COMPONENT_ID = "step2-checkbox-tp-relations-component-id";

    private VNFWizardSecondStep(WebDriver driver, WebDriverWait wait, Wizard vnfWizard) {
        super(driver, wait, vnfWizard);

    }

    public static final VNFWizardSecondStep create(WebDriver driver, WebDriverWait wait, Wizard vnfWizard) {
        return new VNFWizardSecondStep(driver, wait, vnfWizard);
    }

    public void toggleTerminationPointRelations() {
        boolean selected = isTerminationPointRelationsSelected();
        vnfWizard.setComponentValue(TERMINATION_POINTS_RELATIONS_COMPONENT_ID, String.valueOf(!selected), CHECKBOX);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private boolean isTerminationPointRelationsSelected() {
        return vnfWizard.getComponent(TERMINATION_POINTS_RELATIONS_COMPONENT_ID, CHECKBOX)
                .getStringValue().equalsIgnoreCase(Boolean.TRUE.toString());
    }


}
