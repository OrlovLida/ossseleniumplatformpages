/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.transport;

import com.oss.framework.components.ComponentFactory;
import com.oss.framework.components.Input;
import com.oss.framework.data.Data;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

/**
 * @author Kamil Szota
 */
public class VRFImpExpRouteTargetWizardPage extends BasePage {

    private static final String IMP_EXP_WIZARD_VIEW_ID = "Add_Imp/Exp_Route_Target_id";
    private static final String ADDRESS_FAMILY_FIELD_ID = "Address_Family_udi";

    private final Wizard wizard;

    public VRFImpExpRouteTargetWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }

    public void selectRouteTarget(String routeTarget) {
        Input routeTargetComponent = wizard.getComponent(IMP_EXP_WIZARD_VIEW_ID, Input.ComponentType.SEARCH_FIELD);
        routeTargetComponent.clear();
        routeTargetComponent.setSingleStringValue(routeTarget);
    }

    public void setAddressFamily(String addressFamily) {
        Input addressFamilyComponent = ComponentFactory.create(ADDRESS_FAMILY_FIELD_ID, Input.ComponentType.COMBOBOX, driver, wait);
        addressFamilyComponent.setValue(Data.createSingleData(addressFamily));
    }

    public VRFOverviewPage clickAccept() {
        wizard.clickAcceptOldWizard();
        return new VRFOverviewPage(driver);
    }
}
