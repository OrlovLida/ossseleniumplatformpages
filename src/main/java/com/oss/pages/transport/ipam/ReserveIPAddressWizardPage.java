package com.oss.pages.transport.ipam;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

/**
 * @author Ewa Frączek
 */

public class ReserveIPAddressWizardPage extends BasePage {
    private static final String HOST_RESERVE_IP_ADDRESS_FIELD_UID = "host-reserve-ip-address-field-uid";
    private static final String HOST_RESERVE_DESCRIPTION_FIELD_UID = "host-reserve-description-field-uid";

    public ReserveIPAddressWizardPage(WebDriver driver) {
        super(driver);
    }

    public void reserveIPAddress(String ipAddress, String description){
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard reserveIPAddressWizard = Wizard.createWizard(driver, wait);
        Input componentIPAddress = reserveIPAddressWizard.getComponent(HOST_RESERVE_IP_ADDRESS_FIELD_UID, Input.ComponentType.SEARCH_FIELD);
        componentIPAddress.setSingleStringValue(ipAddress);
        Input componentDescription = reserveIPAddressWizard.getComponent(HOST_RESERVE_DESCRIPTION_FIELD_UID, Input.ComponentType.TEXT_FIELD);
        componentDescription.setSingleStringValue(description);
        reserveIPAddressWizard.clickOK();
    }

    public void reserveIPAddress(String ipAddress){
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard reserveIPAddressWizard = Wizard.createWizard(driver, wait);
        Input componentIPAddress = reserveIPAddressWizard.getComponent(HOST_RESERVE_IP_ADDRESS_FIELD_UID, Input.ComponentType.SEARCH_FIELD);
        componentIPAddress.setSingleStringValue(ipAddress);
        reserveIPAddressWizard.clickOK();
    }

    public void reserveIPAddress(){
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard reserveIPAddressWizard = Wizard.createWizard(driver, wait);
        reserveIPAddressWizard.clickOK();
    }
}
