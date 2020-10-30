package com.oss.pages.radio;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class Cell4GWizardPage extends BasePage {

    private static final String CELL4G_WIZARD_DATA_ATTRIBUTE_NAME = "cell-4g-wizard";
    private static final String CELL4G_NAME_DATA_ATTRIBUTE_NAME = "name";
    private static final String CELL4G_E_NODE_B_ID_DATA_ATTRIBUTE_NAME = "eNodeBId";
    private static final String CELL4G_CELL_ID_DATA_ATTRIBUTE_NAME = "cellId";
    private static final String CELL4G_LOCAL_CELL_ID_DATA_ATTRIBUTE_NAME = "localCellId";
    private static final String CELL4G_USE_FIRST_AVAILABLE_ID_DATA_ATTRIBUTE_NAME = "useFirstAvailableId";
    private static final String CELL4G_ADMINISTRATIVE_STATE_DATA_ATTRIBUTE_NAME = "administrativeState";
    private static final String CELL4G_OPERATIONAL_STATE_DATA_ATTRIBUTE_NAME = "operationalState";
    private static final String CELL4G_LOCATION_DATA_ATTRIBUTE_NAME = "";
    private static final String CELL4G_CARRIER_DATA_ATTRIBUTE_NAME = "carrier";
    private static final String CELL4G_COVERAGE_DATA_ATTRIBUTE_NAME = "coverage";
    private static final String CELL4G_MIMO_DATA_ATTRIBUTE_NAME = "mimoMode";
    private static final String CELL4G_MODE_DATA_ATTRIBUTE_NAME = "cellDuplexModeField";
    private static final String CELL4G_BANDWIDTH_DL_DATA_ATTRIBUTE_NAME = "bandwidthDl";
    private static final String CELL4G_BANDWIDTH_UL_DATA_ATTRIBUTE_NAME = "bandwidthUl";
    private static final String CELL4G_TAC_DATA_ATTRIBUTE_NAME = "tac";
    private static final String CELL4G_PCI_DATA_ATTRIBUTE_NAME = "pci";
    private static final String CELL4G_ROOT_SEQUENCE_INDEX_DATA_ATTRIBUTE_NAME = "rootSequenceIndex";
    private static final String CELL4G_REFERENCE_SIGNAL_POWER_DATA_ATTRIBUTE_NAME = "rsPower";
    private static final String CELL4G_PA_OUTPUT_DATA_ATTRIBUTE_NAME = "paOutput";
    private static final String CELL4G_PA_DATA_ATTRIBUTE_NAME = "pa";
    private static final String CELL4G_PB_DATA_ATTRIBUTE_NAME = "pb";
    private static final String CELL4G_TAL_DATA_ATTRIBUTE_NAME = "tals";
    private static final String CELL4G_RADIUS_DATA_ATTRIBUTE_NAME = "radius";
    private static final String CELL4G_USER_LABEL_DATA_ATTRIBUTE_NAME = "userLabel";
    private static final String CELL4G_DESCRIPTION_DATA_ATTRIBUTE_NAME = "description";

    public Cell4GWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Click Accept button")
    public void accept() {
        getCell4GWizard().clickAccept();
    }

    @Step("Create Cell4G with mandatory fields (Name, eNodeB, Cell Id, Carrier) filled in")
    public void createCell4G(String cell4GName, String eNodeBName, String cellId, String carrier4G) {
        setName(cell4GName);
        setENodeBName(eNodeBName);
        setCell4GId(cellId);
        setCarrier4G(carrier4G);
        accept();
    }

    @Step("Set name")
    public Cell4GWizardPage setName(String cell4GName) {
        getCell4GWizard().setComponentValue(CELL4G_NAME_DATA_ATTRIBUTE_NAME, cell4GName, Input.ComponentType.TEXT_FIELD);
        return this;
    }

    @Step("Set eNodeB name")
    public Cell4GWizardPage setENodeBName(String eNodeBName) {
        getCell4GWizard().setComponentValue(CELL4G_E_NODE_B_ID_DATA_ATTRIBUTE_NAME, eNodeBName, Input.ComponentType.SEARCH_FIELD);
        return this;
    }

    @Step("Set Cell 4G Id")
    public Cell4GWizardPage setCell4GId(String cellId) {
        getCell4GWizard().setComponentValue(CELL4G_CELL_ID_DATA_ATTRIBUTE_NAME, cellId, Input.ComponentType.TEXT_FIELD);
        return this;
    }

    @Step("Set Carrier 4G")
    public Cell4GWizardPage setCarrier4G(String carrier4G) {
        getCell4GWizard().getComponent(CELL4G_CARRIER_DATA_ATTRIBUTE_NAME, Input.ComponentType.COMBOBOX).setSingleStringValueContains(carrier4G);
        return this;
    }

    @Step("Set description")
    public Cell4GWizardPage setDescription(String description) {
        getCell4GWizard().setComponentValue(CELL4G_DESCRIPTION_DATA_ATTRIBUTE_NAME, description, Input.ComponentType.TEXT_FIELD);
        return this;
    }

    private Wizard getCell4GWizard() {
        return Wizard.createByComponentId(driver, wait, CELL4G_WIZARD_DATA_ATTRIBUTE_NAME);
    }

}