package com.oss.pages.transport.traffic.classs;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

/**
 * @author Kamil Sikora
 */
public abstract class TrafficClassWizardPage extends BasePage {

    private static final String NAME_FIELD_ID = "uid-name";
    private static final String DESCRIPTION_FIELD_ID = "uid-description";
    private static final String MATCH_FIELD_ID = "uid-match-input";
    private static final String IP_PRECEDENCE_FIELD_ID = "uid-ipPrecedence";
    private static final String MPLS_FIELD_ID = "uid-mplsExperimental";
    private static final String MPLS_TOP_FIELD_ID = "uid-mplsExperimentalTopmost";
    private static final String IP_DSCP_FIELD_ID = "uid-ipDSCP-input";
    private static final String ACCESS_LIST_FIELD_ID = "uid-accessList";
    private static final String INPUT_INTERFACE_FIELD_ID = "uid-inputInterface-input";
    private static final String PROTOCOL_FIELD_ID = "uid-protocol";
    private static final String CIR_INGRESS_FIELD_ID = "uid-cirIngress";
    private static final String CIR_EGRESS_FIELD_ID = "uid-cirEgress";
    private static final String PIR_INGRESS_FIELD_ID = "uid-pirIngress";
    private static final String PIR_EGRESS_FIELD_ID = "uid-pirEgress";

    protected TrafficClassWizardPage(WebDriver webDriver) {
        super(webDriver);
    }

    public abstract Wizard getWizard();

    @Step("Click next step button")
    public void clickNextStep() {
        getWizard().clickNextStep();
    }

    @Step("Set name to {name}")
    public void setName(String name) {
        getWizard().setComponentValue(NAME_FIELD_ID, name, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set description to {description}")
    public void setDescription(String description) {
        getWizard().setComponentValue(DESCRIPTION_FIELD_ID, description, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set Ip precedence to {ipPrecedence}")
    public void setIpPrecedence(String ipPrecedence) {
        getWizard().setComponentValue(IP_PRECEDENCE_FIELD_ID, ipPrecedence, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set Mpls to {mpls}")
    public void setMpls(String mpls) {
        getWizard().setComponentValue(MPLS_FIELD_ID, mpls, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set Mpsl Top to {mplsTop}")
    public void setMplsTop(String mplsTop) {
        getWizard().setComponentValue(MPLS_TOP_FIELD_ID, mplsTop, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set Access list to {accessList}")
    public void setAccessList(String accessList) {
        getWizard().setComponentValue(ACCESS_LIST_FIELD_ID, accessList, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set Protocol to {protocol}")
    public void setProtocol(String protocol) {
        getWizard().setComponentValue(PROTOCOL_FIELD_ID, protocol, Input.ComponentType.TEXT_FIELD);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Set Match type to {type}")
    public void setMatchType(String type) {
        getWizard().setComponentValue(MATCH_FIELD_ID, type, Input.ComponentType.COMBOBOX);
    }

    @Step("Set Ip dscp to {ipDscp}")
    public void setIpDscp(String ipDscp) {
        getWizard().setComponentValue(IP_DSCP_FIELD_ID, ipDscp, Input.ComponentType.COMBOBOX);
    }

    @Step("Set Input interface to {inputInterface}")
    public void setInputInterface(String inputInterface) {
        Input component = getWizard().getComponent(INPUT_INTERFACE_FIELD_ID, Input.ComponentType.COMBOBOX);
        component.setValueContains(Data.createSingleData(inputInterface));
    }

    @Step("Set CIR Ingress to {cirIngress}")
    public void setCirIngress(String cirIngress) {
        getWizard().setComponentValue(CIR_INGRESS_FIELD_ID, cirIngress, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set CIR Egress to {cirEgress}")
    public void setCirEgress(String cirEgress) {
        getWizard().setComponentValue(CIR_EGRESS_FIELD_ID, cirEgress, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set PIR Ingress to {pirIngress}")
    public void setPirIngress(String pirIngress) {
        getWizard().setComponentValue(PIR_INGRESS_FIELD_ID, pirIngress, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set PIR Egress to {pirEgress}")
    public void setPirEgress(String pirEgress) {
        getWizard().setComponentValue(PIR_EGRESS_FIELD_ID, pirEgress, Input.ComponentType.TEXT_FIELD);
    }

}