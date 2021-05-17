package com.oss.pages.transport.ipam;

import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

/**
 * @author Ewa Frączek
 */

public class ReserveIPAddressWizardPage extends BasePage {
    private static final String HOST_RESERVE_IP_ADDRESS_FIELD_UID = "host-reserve-ip-address-field-uid";
    private static final String HOST_RESERVE_DESCRIPTION_FIELD_UID = "host-reserve-description-field-uid";

    public ReserveIPAddressWizardPage(WebDriver driver) {
        super(driver);
    }

    public void reserveIPAddress(String description){
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard reserveIPAddressWizard = Wizard.createWizard(driver, wait);
        DelayUtils.waitForPageToLoad(driver, wait);
        reserveIPAddressWizard.getComponent(HOST_RESERVE_DESCRIPTION_FIELD_UID, TEXT_FIELD).setValueContains(Data.createFindFirst(description));
        DelayUtils.waitForPageToLoad(driver, wait);
        reserveIPAddressWizard.clickOK();
    }
}
