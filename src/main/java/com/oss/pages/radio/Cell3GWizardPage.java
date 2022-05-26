package com.oss.pages.radio;

import org.openqa.selenium.WebDriver;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class Cell3GWizardPage extends BasePage {

    private static final String CELL3G_WIZARD_DATA_ATTRIBUTE_NAME = "cell-3g-wizard";
    private static final String CELL3G_NAME_DATA_ATTRIBUTE_NAME = "name";
    private static final String CELL3G_NODE_B_ID_DATA_ATTRIBUTE_NAME = "nodeBId";
    private static final String CELL3G_CELL_ID_DATA_ATTRIBUTE_NAME = "cellId";
    private static final String CELL3G_LOCAL_CELL_ID_DATA_ATTRIBUTE_NAME = "localCellId";
    private static final String CELL3G_USE_FIRST_AVAILABLE_ID_DATA_ATTRIBUTE_NAME = "useFirstAvailableId";
    private static final String CELL3G_ADMINISTRATIVE_STATE_DATA_ATTRIBUTE_NAME = "administrativeState";
    private static final String CELL3G_OPERATIONAL_STATE_DATA_ATTRIBUTE_NAME = "operationalState";
    private static final String CELL3G_LOCATION_DATA_ATTRIBUTE_NAME = "location";
    private static final String CELL3G_CARRIER_DATA_ATTRIBUTE_NAME = "carrier";
    private static final String CELL3G_COVERAGE_DATA_ATTRIBUTE_NAME = "coverage";
    private static final String CELL3G_RADIUS_DATA_ATTRIBUTE_NAME = "radius";
    private static final String CELL3G_MODE_DATA_ATTRIBUTE_NAME = "cellDuplexModeField";
    private static final String CELL3G_BANDWIDTH_DL_DATA_ATTRIBUTE_NAME = "bandwidthDl";
    private static final String CELL3G_BANDWIDTH_UL_DATA_ATTRIBUTE_NAME = "bandwidthUl";
    private static final String CELL3G_LAC_DATA_ATTRIBUTE_NAME = "lac";
    private static final String CELL3G_SAC_DATA_ATTRIBUTE_NAME = "sac";
    private static final String CELL3G_RAC_DATA_ATTRIBUTE_NAME = "rac";
    private static final String CELL3G_URA_DATA_ATTRIBUTE_NAME = "ura";
    private static final String CELL3G_TX_POWER_ATTRIBUTE_NAME = "txPower";
    private static final String CELL3G_C_PICH_POWER_DATA_ATTRIBUTE_NAME = "cPICHPower";
    private static final String CELL3G_SCRAMBLING_CODE_DATA_ATTRIBUTE_NAME = "scramblingCode";
    private static final String CELL3G_USER_LABEL_DATA_ATTRIBUTE_NAME = "userLabel";
    private static final String CELL3G_DESCRIPTION_DATA_ATTRIBUTE_NAME = "description";

    public Cell3GWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Click Accept button")
    public void accept() {
        getCell3GWizard().clickAccept();
    }

    @Step("Create Cell3G with mandatory fields (Name, NodeB, Cell Id, Carrier) filled in")
    public void createCell3G(String cell3GName, String nodeBName, String cellId, String carrier3G) {
        setName(cell3GName);
        setNodeBName(nodeBName);
        setCell3GId(cellId);
        setCarrier3G(carrier3G);
        accept();
    }

    @Step("Set name")
    public Cell3GWizardPage setName(String cell4GName) {
        getCell3GWizard().setComponentValue(CELL3G_NAME_DATA_ATTRIBUTE_NAME, cell4GName);
        return this;
    }

    @Step("Set NodeB name")
    public void setNodeBName(String nodeBName) {
        getCell3GWizard().setComponentValue(CELL3G_NODE_B_ID_DATA_ATTRIBUTE_NAME, nodeBName);
    }

    @Step("Set Cell 3G Id")
    public void setCell3GId(String cellId) {
        getCell3GWizard().setComponentValue(CELL3G_CELL_ID_DATA_ATTRIBUTE_NAME, cellId);
    }

    @Step("Set Carrier 3G using contains")
    public void setCarrier3G(String carrier3G) {
        getCell3GWizard().getComponent(CELL3G_CARRIER_DATA_ATTRIBUTE_NAME).setSingleStringValueContains(carrier3G);
    }

    @Step("Set description")
    public Cell3GWizardPage setDescription(String description) {
        getCell3GWizard().setComponentValue(CELL3G_DESCRIPTION_DATA_ATTRIBUTE_NAME, description);
        return this;
    }

    private Wizard getCell3GWizard() {
        return Wizard.createByComponentId(driver, wait, CELL3G_WIZARD_DATA_ATTRIBUTE_NAME);
    }
}
