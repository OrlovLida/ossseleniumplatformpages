/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.transport.trail;

import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.WebDriver;

/**
 * @author Robert Nawrat
 */
public class MPLSNetworkWizardPage extends TrailWizardPage {

    private static final String TRAIl_TYPE = "MPLS Network";

    private static final String AUTONOMOUS_SYSTEM_COMPONENT_ID = "oss.transport.trail.type.MPLS Network.Autonomous System";

    public MPLSNetworkWizardPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getTrailType() {
        return TRAIl_TYPE;
    }

    public void clearAutonomousSystem() {
        clearSearchField(AUTONOMOUS_SYSTEM_COMPONENT_ID);
    }

    public void setAutonomousSystem(String autonomousSystem) {
        setSearchField(AUTONOMOUS_SYSTEM_COMPONENT_ID, autonomousSystem);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

}
