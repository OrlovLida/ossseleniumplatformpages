/* @(#) $Id$
 *
 * Copyright (c) 2000-2021 ComArch S.A. All Rights Reserved. Any usage, duplication or redistribution of this
 * software is allowed only according to separate agreement prepared in written between ComArch and authorized party.
 */
package com.oss.pages.transport.trail.v2;

import org.openqa.selenium.WebDriver;
import io.qameta.allure.Step;

public class IPLinkTerminationStepPage extends TerminationStepPage {
    
    private static final String CREATE_ETHERNET_LINK_COMPONENT_ID = "IPLink.CreateAssociatedEthernetLinkComboboxComponent";
    
    public static IPLinkTerminationStepPage getIPLinkTerminationStepPage(WebDriver driver) {
        return new IPLinkTerminationStepPage(driver);
    }
    
    protected IPLinkTerminationStepPage(WebDriver driver) {
        super(driver);
    }
    
    @Step("Set Create Associated Ethernet Link - {createAssociatedEthernetLink}")
    public void setCreateAssociatedEthernetLink(boolean createAssociatedEthernetLink) {
        setCheckbox(CREATE_ETHERNET_LINK_COMPONENT_ID, createAssociatedEthernetLink);
    }
    
}
