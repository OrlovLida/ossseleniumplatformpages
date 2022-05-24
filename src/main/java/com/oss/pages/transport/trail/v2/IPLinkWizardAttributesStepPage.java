/* @(#) $Id$
 *
 * Copyright (c) 2000-2021 ComArch S.A. All Rights Reserved. Any usage, duplication or redistribution of this
 * software is allowed only according to separate agreement prepared in written between ComArch and authorized party.
 */
package com.oss.pages.transport.trail.v2;

import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;

import io.qameta.allure.Step;

public class IPLinkWizardAttributesStepPage extends TrailWizardAttributesPage {

    public static final String TRAIL_TYPE = "IP Link";

    private static final String EFFECTIVE_BANDWIDTH_COMPONENT_ID = "oss.transport.trail.type.IP Link.Effective bandwidth [Mbps]";
    private static final String IP_ADDRESS_VERSION_COMPONENT_ID = "ipAddressVersionComponent";
    private static final String START_IP_ADDRESS_COMPONENT_ID = "oss.transport.trail.type.IP Link.Start IP Address";
    private static final String START_IP_ADDRESS_MASK_COMPONENT_ID = "oss.transport.trail.type.IP Link.Start IP Mask";
    private static final String END_IP_ADDRESS_COMPONENT_ID = "oss.transport.trail.type.IP Link.End IP Address";
    private static final String END_IP_ADDRESS_MASK_COMPONENT_ID = "oss.transport.trail.type.IP Link.End IP Mask";
    private static final String MPLS_ACTIVE_COMPONENT_ID = "oss.transport.trail.type.IP Link.MPLS active";
    private static final String MPLS_TE_ACTIVE_COMPONENT_ID = "oss.transport.trail.type.IP Link.MPLS-TE active";
    private static final String MPLS_TP_ACTIVE_COMPONENT_ID = "oss.transport.trail.type.IP Link.MPLS-TP active";

    protected IPLinkWizardAttributesStepPage(WebDriver driver) {
        super(driver);
    }

    public static IPLinkWizardAttributesStepPage getAttributesStepPage(WebDriver driver) {
        return new IPLinkWizardAttributesStepPage(driver);
    }

    @Step("Set Effective Bandwidth - {effectiveBandwidth}")
    public void setEffectiveBandwidth(String effectiveBandwidth) {
        setTextField(EFFECTIVE_BANDWIDTH_COMPONENT_ID, effectiveBandwidth);
    }

    @Step("Clear Effective Bandwidth")
    public void clearEffectiveBandwidth() {
        clearTextField(EFFECTIVE_BANDWIDTH_COMPONENT_ID);
    }

    @Step("Set IP Address Version - IPv4")
    public void setDefaultIPAddressVersion() {
        setIPAddressVersion(IPAddressVersion.IPv4);
    }

    @Step("Set IP Address Version- {ipAddressVersion}")
    public void setIPAddressVersion(IPAddressVersion ipAddressVersion) {
        setComboboxField(IP_ADDRESS_VERSION_COMPONENT_ID, ipAddressVersion.toString());
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Set Start IP Address - {startIPAddress}")
    public void setStartIPAddress(String startIPAddress) {
        setTextField(START_IP_ADDRESS_COMPONENT_ID, startIPAddress);
    }

    @Step("Clear Start IP Address")
    public void clearStartIPAddress() {
        clearTextField(START_IP_ADDRESS_COMPONENT_ID);
    }

    @Step("Set Start IP Address Mask - {startIPAddressMask}")
    public void setStartIPAddressMask(String startIPAddressMask) {
        setTextField(START_IP_ADDRESS_MASK_COMPONENT_ID, startIPAddressMask);
    }

    @Step("Clear Start IP Address Mask")
    public void clearStartIPAddressMask() {
        clearTextField(START_IP_ADDRESS_MASK_COMPONENT_ID);
    }

    @Step("Set End IP Address - {endIPAddress}")
    public void setEndIPAddress(String endIPAddress) {
        setTextField(END_IP_ADDRESS_COMPONENT_ID, endIPAddress);
    }

    @Step("Clear End IP Address")
    public void clearEndIPAddress() {
        clearTextField(END_IP_ADDRESS_COMPONENT_ID);
    }

    @Step("Set End IP Address Mask - {endIPAddressMask}")
    public void setEndIPAddressMask(String endIPAddressMask) {
        setTextField(END_IP_ADDRESS_MASK_COMPONENT_ID, endIPAddressMask);
    }

    @Step("Clear End IP Address Mask")
    public void clearEndIPAddressMask() {
        clearTextField(END_IP_ADDRESS_MASK_COMPONENT_ID);
    }

    @Step("Set MPLS Active - {mplsActive}")
    public void setMplsActive(boolean mplsActive) {
        setCheckbox(MPLS_ACTIVE_COMPONENT_ID, mplsActive);
    }

    @Step("Uncheck MPLS Active")
    public void clearMplsActive() {
        clearCheckbox(MPLS_ACTIVE_COMPONENT_ID);
    }

    @Step("Set MPLS TE Active - {mplsTeActive}")
    public void setMplsTeActive(boolean mplsTeActive) {
        setCheckbox(MPLS_TE_ACTIVE_COMPONENT_ID, mplsTeActive);
    }

    @Step("Uncheck MPLS TE Active")
    public void clearMplsTeActive() {
        clearCheckbox(MPLS_TE_ACTIVE_COMPONENT_ID);
    }

    @Step("Set MPLS TP Active - {mplsTpActive}")
    public void setMplsTpActive(boolean mplsTpActive) {
        setCheckbox(MPLS_TP_ACTIVE_COMPONENT_ID, mplsTpActive);
    }

    @Step("Uncheck MPLS TP Active")
    public void clearMplsTpActive() {
        clearCheckbox(MPLS_TP_ACTIVE_COMPONENT_ID);
    }

    @Override
    @Step("Click Next button")
    public IPLinkTerminationStepPage next() {
        clickNext();
        return IPLinkTerminationStepPage.getIPLinkTerminationStepPage(driver);
    }

    public enum IPAddressVersion {
        IPv4, IPv6
    }
}
