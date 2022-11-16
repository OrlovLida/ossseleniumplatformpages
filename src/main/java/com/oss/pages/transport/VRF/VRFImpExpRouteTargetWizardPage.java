/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.transport.VRF;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

/**
 * @author Kamil Szota
 */
public class VRFImpExpRouteTargetWizardPage extends BasePage {

    private static final String IMP_EXP_WIZARD_VIEW_ID = "Add_Imp/Exp_Route_Target_id";
    private static final String ADDRESS_FAMILY_FIELD_ID = "Address_Family_udi";
    private static final String ID = "NEEDS_TO_UPDATE_ID";

    private final Wizard wizard;

    public VRFImpExpRouteTargetWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, ID);
    }

    @Step("Set Route Target value to {routeTarget}")
    public void selectRouteTarget(String routeTarget) {
        Input routeTargetComponent = wizard.getComponent(IMP_EXP_WIZARD_VIEW_ID, Input.ComponentType.SEARCH_FIELD);
        routeTargetComponent.clear();
        routeTargetComponent.setSingleStringValue(routeTarget);
    }

    @Step("Set Address family value to {addressFamily}")
    public void setAddressFamily(String addressFamily) {
        Input addressFamilyComponent = ComponentFactory.create(ADDRESS_FAMILY_FIELD_ID, Input.ComponentType.COMBOBOX, driver, wait);
        addressFamilyComponent.setValue(Data.createSingleData(addressFamily));
    }

    @Step("Click accept button")
    public VRFOverviewPage clickAccept() {
        wizard.clickAccept();
        return new VRFOverviewPage(driver);
    }
}
