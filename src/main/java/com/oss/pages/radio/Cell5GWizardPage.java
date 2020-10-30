package com.oss.pages.radio;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class Cell5GWizardPage extends BasePage {

    private static final String CELL5G_WIZARD_DATA_ATTRIBUTE_NAME = "cell-5g-wizard-id";
    private static final String CELL5G_NAME_DATA_ATTRIBUTE_NAME = "name";
    private static final String CELL5G_G_NODE_B_ID_DATA_ATTRIBUTE_NAME = "gNodeBId";
    private static final String CELL5G_CELL_ID_DATA_ATTRIBUTE_NAME = "cellId";
    private static final String CELL5G_LOCAL_CELL_ID_DATA_ATTRIBUTE_NAME = "localCellId";
    private static final String CELL5G_USE_FIRST_AVAILABLE_ID_DATA_ATTRIBUTE_NAME = "useFirstAvailableId";
    private static final String CELL5G_ADMINISTRATIVE_STATE_DATA_ATTRIBUTE_NAME = "administrativeState";
    private static final String CELL5G_OPERATIONAL_STATE_DATA_ATTRIBUTE_NAME = "operationalState";
    private static final String CELL5G_LOCATION_DATA_ATTRIBUTE_NAME = "";
    private static final String CELL5G_CARRIER_DATA_ATTRIBUTE_NAME = "carrier";
    private static final String CELL5G_COVERAGE_DATA_ATTRIBUTE_NAME = "coverage";
    private static final String CELL5G_MIMO_DATA_ATTRIBUTE_NAME = "mimoMode";
    private static final String CELL5G_MODE_DATA_ATTRIBUTE_NAME = "cellDuplexModeField";
    private static final String CELL5G_BANDWIDTH_DL_DATA_ATTRIBUTE_NAME = "bandwidthDl";
    private static final String CELL5G_BANDWIDTH_UL_DATA_ATTRIBUTE_NAME = "bandwidthUl";
    private static final String CELL5G_TX_POWER_DATA_ATTRIBUTE_NAME = "txPower";
    private static final String CELL5G_TX_DIRECTION_DATA_ATTRIBUTE_NAME = "txDirection";
    private static final String CELL5G_SSB_FREQUENCY_DOMAIN_POSITION_DATA_ATTRIBUTE_NAME = "sSBFreqPos";
    private static final String CELL5G_TAC_DATA_ATTRIBUTE_NAME = "tac";
    private static final String CELL5G_PCI_DATA_ATTRIBUTE_NAME = "pci";
    private static final String CELL5G_ROOT_SEQUENCE_INDEX_DATA_ATTRIBUTE_NAME = "rootSequenceIndex";
    private static final String CELL5G_TAL_DATA_ATTRIBUTE_NAME = "tals";
    private static final String CELL5G_RADIUS_DATA_ATTRIBUTE_NAME = "radius";
    private static final String CELL5G_USER_LABEL_DATA_ATTRIBUTE_NAME = "userLabel";
    private static final String CELL5G_DESCRIPTION_DATA_ATTRIBUTE_NAME = "description";

    public Cell5GWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Click Accept button")
    public void accept() {
        getCell5GWizard().clickAccept();
    }

    @Step("Create Cell5G with mandatory fields (Name, gNodeB, Cell Id, Carrier) filled in")
    public void createCell5G(String cell5GName, String gNodeBName, String cellId, String carrier5G) {
        setName(cell5GName);
        setGNodeBName(gNodeBName);
        setCell5GId(cellId);
        setCarrier5G(carrier5G);
        accept();
    }

    @Step("Set name")
    public Cell5GWizardPage setName(String cell5GName) {
        getCell5GWizard().setComponentValue(CELL5G_NAME_DATA_ATTRIBUTE_NAME, cell5GName, Input.ComponentType.TEXT_FIELD);
        return this;
    }

    @Step("Set gNodeB name")
    public Cell5GWizardPage setGNodeBName(String gNodeBName) {
        getCell5GWizard().setComponentValue(CELL5G_G_NODE_B_ID_DATA_ATTRIBUTE_NAME, gNodeBName, Input.ComponentType.SEARCH_FIELD);
        return this;
    }

    @Step("Set Cell 5G Id")
    public Cell5GWizardPage setCell5GId(String cellId) {
        getCell5GWizard().setComponentValue(CELL5G_CELL_ID_DATA_ATTRIBUTE_NAME, cellId, Input.ComponentType.TEXT_FIELD);
        return this;
    }

    @Step("Set Carrier 5G")
    public Cell5GWizardPage setCarrier5G(String carrier5G) {
        getCell5GWizard().getComponent(CELL5G_CARRIER_DATA_ATTRIBUTE_NAME, Input.ComponentType.COMBOBOX).setSingleStringValueContains(carrier5G);
        return this;
    }

    @Step("Set description")
    public Cell5GWizardPage setDescription(String description) {
        getCell5GWizard().setComponentValue(CELL5G_DESCRIPTION_DATA_ATTRIBUTE_NAME, description, Input.ComponentType.TEXT_FIELD);
        return this;
    }

    private Wizard getCell5GWizard() {
        return Wizard.createByComponentId(driver, wait, CELL5G_WIZARD_DATA_ATTRIBUTE_NAME);
    }

}