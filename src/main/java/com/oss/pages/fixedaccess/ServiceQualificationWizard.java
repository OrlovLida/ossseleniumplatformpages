package com.oss.pages.fixedaccess;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.CHECKBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.RADIO_BUTTON;
import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.SWITCHER;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;


public class ServiceQualificationWizard extends BasePage {

    private static final String RADIO_BUTTONS_FOR_QUERY_OPTION = "sqRadioButtonsID";
    private static final String SEARCH_FIELD_FOR_QUERY = "sqSearchFieldUID_OSF";
    private static final String ADDRESS_DETAILS_SWITCHER_FOR_QUERY = "sqSwitchButtonUID";
    private static final String REQUIRED_DOWNLOAD_SPEED_INPUT = "sqRequiredDownloadSpeedFieldUID";
    private static final String REQUIRED_UPLOAD_SPEED_INPUT = "sqRequiredUploadSpeedFieldUID";
    private static final String PROVIDE_ALTERNATIVE_CHECKBOX = "sqProvideAlternativesCheckboxUID";
    private static final String PROVIDE_RESOURCE_CHECKBOX = "sqProvideResourcesCheckboxUID";
    private static final String PROVIDE_SERVICE_NODE_CHECKBOX = "sqProvideServiceNodesCheckboxUID";

    public ServiceQualificationWizard(WebDriver driver) {
        super(driver);
    }

    @Step("Open wizard for Service Qualification")
    public ServiceQualificationWizard openServiceQualificationWizard() {
        waitForPageToLoad();
        SideMenu sideMenu = SideMenu.create(driver, wait);
        sideMenu.callActionByLabel("Service Qualification", "Wizards", "Fixed Access");
        waitForPageToLoad();
        return this;
    }

    @Step("Set query option for SQ (Address or DA as input)")
    public ServiceQualificationWizard setQueryOption(String option) {
        getServiceQualificationWizard().setComponentValue(RADIO_BUTTONS_FOR_QUERY_OPTION, option, RADIO_BUTTON);
        return this;
    }

    @Step("Set address or DA for query")
    public ServiceQualificationWizard setAddressOrDA(String queryParameter) {
        Input queryInput = getServiceQualificationWizard().getComponent(SEARCH_FIELD_FOR_QUERY, SEARCH_FIELD);
        queryInput.clearByAction();
        queryInput.setValueContains(Data.createFindFirst(queryParameter));
        return this;
    }

    @Step("Set switcher for show or hide address details")
    public ServiceQualificationWizard setSwitcherShowAddressDetails(String option) {
        getServiceQualificationWizard().setComponentValue(ADDRESS_DETAILS_SWITCHER_FOR_QUERY, option, SWITCHER);
        return this;
    }

    @Step("Set Required Download Speed")
    public ServiceQualificationWizard setRequiredDownloadSpeed(String requiredDownloadSpeed) {
        getServiceQualificationWizard().setComponentValue(REQUIRED_DOWNLOAD_SPEED_INPUT, requiredDownloadSpeed, TEXT_FIELD);
        return this;
    }

    @Step("Set Required Upload Speed")
    public ServiceQualificationWizard setRequiredUploadSpeed(String requiredUploadSpeed) {
        getServiceQualificationWizard().setComponentValue(REQUIRED_UPLOAD_SPEED_INPUT, requiredUploadSpeed, TEXT_FIELD);
        return this;
    }

    @Step("Set provide alternative checkbox")
    public ServiceQualificationWizard setProvideAlternative(String provideAlternativeValue) {
        getServiceQualificationWizard().setComponentValue(PROVIDE_ALTERNATIVE_CHECKBOX, provideAlternativeValue, CHECKBOX);
        return this;
    }

    @Step("Set provide resource checkbox")
    public ServiceQualificationWizard setProvideResource(String provideResourceValue) {
        getServiceQualificationWizard().setComponentValue(PROVIDE_RESOURCE_CHECKBOX, provideResourceValue, CHECKBOX);
        return this;
    }

    @Step("Set provide service node checkbox")
    public ServiceQualificationWizard setProvideServiceNode(String provideServiceNodeValue) {
        getServiceQualificationWizard().setComponentValue(PROVIDE_SERVICE_NODE_CHECKBOX, provideServiceNodeValue, CHECKBOX);
        return this;
    }

    private Wizard getServiceQualificationWizard() {
        return Wizard.createWizard(driver, wait);
    }

    @Step("Click Accept button")
    public ServiceQualificationView clickAccept() {
        getServiceQualificationWizard().clickAccept();
        return new ServiceQualificationView(driver);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

}
