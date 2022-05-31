/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.transport.trail;

import org.openqa.selenium.WebDriver;

/**
 * @author Robert Nawrat
 */
public class IPLinkWizardPage extends TrailWizardPage {

    private static final String TRAIl_TYPE = "IP Link";

    public IPLinkWizardPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getTrailType() {
        return TRAIl_TYPE;
    }
}
