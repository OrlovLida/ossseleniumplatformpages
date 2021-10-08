package com.oss.pages.transport.ipam;

import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.*;

/**
 * @author Ewa Frączek
 */

class ReserveIPAddressWizardPage extends BasePage {
    private static final String HOST_RESERVE_IP_ADDRESS_FIELD_UID = "host-reserve-ip-address-field-uid";
    private static final String HOST_RESERVE_DESCRIPTION_FIELD_UID = "host-reserve-description-field-uid";
    private static final String HOST_RESERVE_MODE_UID_INPUT = "host-reserve-mode-uid-input";
    private static final String HOST_RESERVE_NUMBER_ADDRESSES_FIELD_UID = "host-reserve-number-addresses-field-uid";
    private static final String RESERVE_MULTIPLE_ADDRESSES = "Reserve multiple addresses";
    private static final String HOST_RESERVE_CONSECUTIVE_FIELD_UID = "host-reserve-consecutive-field-uid";

    ReserveIPAddressWizardPage(WebDriver driver) {
        super(driver);
    }

    void reserveIPAddressAndAccept(String description) {
        Wizard reserveIPAddressWizard = reserveIPAddress(description);
        reserveIPAddressWizard.clickAccept();
    }

    void reserveIPAddressAndClickOk(String description) {
        Wizard reserveIPAddressWizard = reserveIPAddress(description);
        reserveIPAddressWizard.clickOK();
    }

    private Wizard reserveIPAddress(String description) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard reserveIPAddressWizard = Wizard.createWizard(driver, wait);
        DelayUtils.waitForPageToLoad(driver, wait);
        reserveIPAddressWizard.getComponent(HOST_RESERVE_DESCRIPTION_FIELD_UID, TEXT_FIELD).setValueContains(Data.createFindFirst(description));
        DelayUtils.waitForPageToLoad(driver, wait);
        return reserveIPAddressWizard;
    }

    void bulkIPAddressReservation(String numberOfHostAddressesToReserve, Boolean reserveConsecutive) {
        Wizard reserveIPAddressWizard = Wizard.createWizard(driver, wait);
        reserveIPAddressWizard.getComponent(HOST_RESERVE_MODE_UID_INPUT, SEARCH_FIELD).clear();
        reserveIPAddressWizard.getComponent(HOST_RESERVE_MODE_UID_INPUT, SEARCH_FIELD).setValueContains(Data.createFindFirst(RESERVE_MULTIPLE_ADDRESSES));
        DelayUtils.waitForPageToLoad(driver, wait);
        reserveIPAddressWizard.getComponent(HOST_RESERVE_NUMBER_ADDRESSES_FIELD_UID, TEXT_FIELD).setSingleStringValueContains(numberOfHostAddressesToReserve);
        DelayUtils.waitForPageToLoad(driver, wait);
        reserveIPAddressWizard.setComponentValue(HOST_RESERVE_CONSECUTIVE_FIELD_UID, reserveConsecutive.toString(), CHECKBOX);
        DelayUtils.sleep(1000);
        reserveIPAddressWizard.clickAccept();
    }

    void reserveGivenIPAddress(String ipAddress) {
        Wizard reserveIPAddressWizard = Wizard.createWizard(driver, wait);
        DelayUtils.waitForPageToLoad(driver, wait);
        reserveIPAddressWizard.getComponent(HOST_RESERVE_IP_ADDRESS_FIELD_UID, TEXT_FIELD).clear();
        DelayUtils.waitForPageToLoad(driver, wait);
        reserveIPAddressWizard.getComponent(HOST_RESERVE_IP_ADDRESS_FIELD_UID, SEARCH_FIELD).setValueContains(Data.createFindFirst(ipAddress));
        DelayUtils.sleep(1000);
        reserveIPAddressWizard.clickAccept();
    }

}
