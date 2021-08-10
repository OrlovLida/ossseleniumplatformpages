package com.oss.pages.fixedaccess.servicequalification;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.CHECKBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.RADIO_BUTTON;
import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;


public class ServiceQualificationWizard extends BasePage {

    private static final String RADIO_BUTTONS_FOR_QUERY_OPTION = "sqRadioButtonsID";
    private static final String SEARCH_FIELD_FOR_QUERY = "sqSearchFieldUID_OSF";
    private static final String REQUIRED_DOWNLOAD_SPEED_INPUT = "sqRequiredDownloadSpeedFieldUID";
    private static final String REQUIRED_UPLOAD_SPEED_INPUT = "sqRequiredUploadSpeedFieldUID";
    private static final String PROVIDE_ALTERNATIVE_CHECKBOX = "sqProvideAlternativesCheckboxUID";
    private static final String PROVIDE_RESOURCE_CHECKBOX = "sqProvideResourcesCheckboxUID";
    private static final String PROVIDE_SERVICE_NODE_CHECKBOX = "sqProvideServiceNodesCheckboxUID";
    private static final String ADVANCED_SEARCH_BUTTON = "btn-as-modal";
    private static final String ADVANCED_SEARCH_WIDGET_ID = "advancedSearch";
    private static final String ADVANCED_SEARCH_ID = "advancedSearch";
    private static final String SERVICE_QUALIFICATION = "Service Qualification";
    private static final String NETWORK_DOMAINS = "Network domains";
    private static final String FIXED_ACCESS = "Fixed Access";

    public ServiceQualificationWizard(WebDriver driver) {
        super(driver);
    }

    @Step("Open wizard for Service Qualification")
    public ServiceQualificationWizard openServiceQualificationWizard() {
        waitForPageToLoad();
        SideMenu sideMenu = SideMenu.create(driver, wait);
        sideMenu.callActionByLabel(SERVICE_QUALIFICATION, NETWORK_DOMAINS, FIXED_ACCESS);
        waitForPageToLoad();
        return this;
    }

    @Step("Set query option for SQ (Address or DA as input)")
    public ServiceQualificationWizard setQueryOption(String option) {
        getServiceQualificationWizard().setComponentValue(RADIO_BUTTONS_FOR_QUERY_OPTION, option, RADIO_BUTTON);
        DelayUtils.sleep(2000);
        return this;
    }

    @Step("Set address or DA for query")
    public ServiceQualificationWizard setAddressOrDA(String queryParameter) {
        Input input = getServiceQualificationWizard().getComponent(SEARCH_FIELD_FOR_QUERY, SEARCH_FIELD);
        input.clear();
        input.setSingleStringValueContains(queryParameter);
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

    @Step("Open advanced search window")
    public ServiceQualificationWizard openAdvancedSearchWindow() {
        getServiceQualificationWizard().callButtonById(ADVANCED_SEARCH_BUTTON);
        return this;
    }

    @Step("Set xid in advanced search filter")
    public ServiceQualificationWizard setXidInAdvancedSearchFilter(Long id) {
        getAdvancedSearchWindow().getComponent("id", TEXT_FIELD).setSingleStringValueContains(id.toString());
        return this;
    }

    @Step("Select first result in advanced search table")
    public ServiceQualificationWizard selectFirstResultInAdvancedSearchTable() {
        getAdvancedSearchTableWidget().selectRow(0);
        return this;
    }

    @Step("Click button Add in advanced search window")
    public ServiceQualificationWizard clickButtonAddInAdvancedSearchWindow() {
        getAdvancedSearchWindow().clickAdd();
        return this;
    }

    @Step("Click Accept button")
    public ServiceQualificationView clickAccept() {
        getServiceQualificationWizard().clickAccept();
        return new ServiceQualificationView(driver);
    }

    private Wizard getServiceQualificationWizard() {
        return Wizard.createWizard(driver, wait);
    }

    private TableWidget getAdvancedSearchTableWidget() {
        return TableWidget.createById(driver, ADVANCED_SEARCH_WIDGET_ID, wait);
    }

    private AdvancedSearch getAdvancedSearchWindow() {
        return AdvancedSearch.createById(driver, wait, ADVANCED_SEARCH_ID);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}
