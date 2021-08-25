package com.oss.pages.transport.ipam;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.widgets.Wizard.createWizard;

public class ChangeIPNetworkWizardPage extends BasePage {
    private static final String WIZARD_ID = "ipnetworkReassignmentWizardWigetId";
    private static final String SELECT_DESTINATION_NETWORK_COMPONENT_ID = "mainStepSelectNetworkComponentId";

    public ChangeIPNetworkWizardPage(WebDriver driver) {
        super(driver);
    }

    public void changeIPNetworkWithOutConflicts(String destinationNetworkName) {
        selectNetworkStep(destinationNetworkName);
        integrityStep();
        conflictsStep();
        summaryStep();
    }

    private void selectNetworkStep(String destinationNetworkName) {
        Wizard selectNetworkStep = createWizard(driver, wait);
        selectNetworkStep.getComponent(SELECT_DESTINATION_NETWORK_COMPONENT_ID, Input.ComponentType.SEARCH_FIELD).setValueContains(Data.createFindFirst(destinationNetworkName));
        selectNetworkStep.clickNext();
    }

    private void integrityStep() {
        Wizard integrityStep = createWizard(driver, wait);
        integrityStep.clickNext();
    }

    private void conflictsStep() {
        Wizard conflictsStep = createWizard(driver, wait);
        conflictsStep.clickNext();
    }

    private void summaryStep() {
        Wizard summaryStep = createWizard(driver, wait);
        summaryStep.clickAccept();
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

}
