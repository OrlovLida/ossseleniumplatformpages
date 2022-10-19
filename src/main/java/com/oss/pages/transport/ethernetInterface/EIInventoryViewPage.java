package com.oss.pages.transport.ethernetInterface;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.BasePage;
import com.oss.pages.platform.OldInventoryView.OldInventoryViewPage;

/**
 * @author Kamil Jacko
 */
public class EIInventoryViewPage extends BasePage {

    private final TabsInterface tabsInterface;
    private Map<String, String> propertiesToValuesMap = new HashMap<>();

    private static final String ADMINISTRATIVE_STATE = "Administrative State";
    private static final String AUTO_NEGOTIATION = "Auto-Negotation";
    private static final String ADMINISTRATIVE_SPEED = "Administrative Speed";
    private static final String ADMINISTRATIVE_DUPLEX_MODE = "Administrative Duplex Mode";
    private static final String MAXIMUM_FRAME_SIZE = "Maximum Frame Size";
    private static final String FLOW_CONTROL = "Flow Control";
    private static final String MTU = "MTU";
    private static final String ENCAPSULATION = "Encapsulation";
    private static final String BANDWIDTH = "Bandwidth";
    private static final String SWITCH_PORT = "Switchport";
    private static final String SWITCH_MODE = "Switchport mode";
    private static final String ACCESS_FUNCTION = "Access Function";
    private static final String DESCRIPTION = "Description";
    private static final String PROPERTIES_TAB = "Properties";
    private static final String PROPERTY_PANEL_ID = "properties(EthernetInterface)";
    private static final String TAB_ID = "2";

    public EIInventoryViewPage(WebDriver driver) {
        super(driver);
        DelayUtils.waitForPageToLoad(driver, wait);
        tabsInterface = TabsWidget.createById(driver, wait, TAB_ID);
    }

    public void selectEI() {
        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        oldInventoryViewPage.getTableWidget().selectRow(0);
    }

    public EIWizardPage editEI() {
        tabsInterface.callActionByLabel("EDIT", "Edit Ethernet Interface");
        return new EIWizardPage(driver);
    }

    public void openPropertiesTab() {
        tabsInterface.selectTabByLabel(PROPERTIES_TAB);
    }

    public void obtainPropertyValues() {
        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        propertiesToValuesMap = oldInventoryViewPage.getProperties(PROPERTY_PANEL_ID);
    }

    public String getAdministrativeStateValue() {
        return propertiesToValuesMap.get(ADMINISTRATIVE_STATE);
    }

    public String getAutoNegotiationValue() {
        return propertiesToValuesMap.get(AUTO_NEGOTIATION);
    }

    public String getAdministrativeSpeed() {
        return propertiesToValuesMap.get(ADMINISTRATIVE_SPEED);
    }

    public String getAdministrativeDuplexMode() {
        return propertiesToValuesMap.get(ADMINISTRATIVE_DUPLEX_MODE);
    }

    public String getEncapsulationValue() {
        return propertiesToValuesMap.get(ENCAPSULATION);
    }

    public String getMTUValue() {
        return propertiesToValuesMap.get(MTU);
    }

    public String getMaximumFrameSizeValue() {
        return propertiesToValuesMap.get(MAXIMUM_FRAME_SIZE);
    }

    public String getFlowControlValue() {
        return propertiesToValuesMap.get(FLOW_CONTROL);
    }

    public String getBandwidthValue() {
        return propertiesToValuesMap.get(BANDWIDTH);
    }

    public String getSwitchPortValue() {
        return propertiesToValuesMap.get(SWITCH_PORT);
    }

    public String getSwitchModeValue() {
        return propertiesToValuesMap.get(SWITCH_MODE);
    }

    public String getAccessFunctionValue() {
        return propertiesToValuesMap.get(ACCESS_FUNCTION);
    }

    public String getDescriptionValue() {
        return propertiesToValuesMap.get(DESCRIPTION);
    }
}
