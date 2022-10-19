package com.oss.pages.transport.ipam;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;


public class ChangeIPNetworkWizardPage extends BasePage {
    private static final String WIZARD_ID = "ipnetworkReassignmentWizardWigetId";
    private static final String SELECT_DESTINATION_NETWORK_COMPONENT_ID = "mainStepSelectNetworkComponentId";
    private static final String TABLE_CONFLICTS_STEP_IP_OBJECTS_TABLE_COMPONENT_ID = "ipnetworkReassignmentWizardWidgetId";
    private static final String CONFLICTS_STEP_RESOLVE_BUTTON_COMPONENT_ID = "conflictsStepResolveButtonComponentId";
    private static final String ID = "NEEDS_TO_UPDATE_ID";

    public ChangeIPNetworkWizardPage(WebDriver driver) {
        super(driver);
    }

    public TableComponent getTableComponent() {
        Widget.waitForWidget(wait, TableWidget.TABLE_WIDGET_CLASS);
        return TableComponent.create(driver, wait, TABLE_CONFLICTS_STEP_IP_OBJECTS_TABLE_COMPONENT_ID);
    }

    public void changeIPNetworkWithOutConflicts(String destinationNetworkName) {
        selectNetworkStep(destinationNetworkName);
        integrityStep();
        DelayUtils.sleep(500);
        conflictsStep();
        summaryStep();
    }

    private void selectNetworkStep(String destinationNetworkName) {
        Wizard selectNetworkStep = Wizard.createByComponentId(driver, wait, ID);
        selectNetworkStep.getComponent(SELECT_DESTINATION_NETWORK_COMPONENT_ID, Input.ComponentType.SEARCH_FIELD).setValueContains(Data.createFindFirst(destinationNetworkName));
        selectNetworkStep.clickNext();
    }

    private void integrityStep() {
        Wizard integrityStep = Wizard.createByComponentId(driver, wait, ID);
        integrityStep.clickNext();
    }

    private void conflictsStep() {
        Wizard conflictsStep = Wizard.createByComponentId(driver, wait, ID);
        getTableComponent().getVisibleRows().forEach(tableRow -> {
            getTableComponent().selectRow(0);
            conflictsStep.clickButtonById(CONFLICTS_STEP_RESOLVE_BUTTON_COMPONENT_ID);
        });
        conflictsStep.clickNext();
    }

    private void summaryStep() {
        Wizard summaryStep = Wizard.createByComponentId(driver, wait, ID);
        summaryStep.clickAccept();
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

}
