package com.oss.pages.fixedaccess.servicequalification;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.advancedsearch.AdvancedSearchWidget;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class ServiceQualificationWizard extends BasePage {

    private static final String SQ_WIZARD_ID = "sqWebViewID";
    private static final String RADIO_BUTTONS_FOR_QUERY_OPTION = "sqRadioButtonsID";
    private static final String SEARCH_FIELD_FOR_QUERY = "sqSearchFieldUID_OSF";
    private static final String REQUIRED_DOWNLOAD_SPEED_INPUT = "sqRequiredDownloadSpeedFieldUID";
    private static final String REQUIRED_UPLOAD_SPEED_INPUT = "sqRequiredUploadSpeedFieldUID";
    private static final String PROVIDE_ALTERNATIVE_CHECKBOX = "sqProvideAlternativesCheckboxUID";
    private static final String PROVIDE_RESOURCE_CHECKBOX = "sqProvideResourcesCheckboxUID";
    private static final String PROVIDE_SERVICE_NODE_CHECKBOX = "sqProvideServiceNodesCheckboxUID";
    private static final String ADVANCED_SEARCH_BUTTON = "sqSearchFieldUID_OSF_as-modal";
    private static final String ADVANCED_SEARCH_ID = "advancedSearch";
    private static final String TABLE_DISTRIBUTION_AREA_SQ_SEARCH_FIELD_UID = "table-DistributionArea_sqSearchFieldUID_object_factory_modal_result_DistributionArea";
    private static final String TABLE_ADDRESS_SQ_SEARCH_FIELD_UID = "table-Address_sqSearchFieldUID_object_factory_modal_result_Address";

    public ServiceQualificationWizard(WebDriver driver) {
        super(driver);
    }

    @Step("Open wizard for Service Qualification")
    public ServiceQualificationWizard openServiceQualificationWizard(String url) {
        waitForPageToLoad();
        driver.get(String.format("%s/#/view/fixed-access/service-qualification/wizard?perspective=LIVE", url));
        return this;
    }

    @Step("Set query option for SQ (Address or DA as input)")
    public ServiceQualificationWizard setQueryOption(String option) {
        getServiceQualificationWizard().setComponentValue(RADIO_BUTTONS_FOR_QUERY_OPTION, option);
        waitForPageToLoad();
        return this;
    }

    @Step("Set address or DA for query")
    public ServiceQualificationWizard setAddressOrDA(String queryParameter) {
        Input input = getServiceQualificationWizard().getComponent(SEARCH_FIELD_FOR_QUERY);
        input.clear();
        input.setSingleStringValueContains(queryParameter);
        return this;
    }

    @Step("Set Required Download Speed")
    public ServiceQualificationWizard setRequiredDownloadSpeed(String requiredDownloadSpeed) {
        waitForPageToLoad();
        getServiceQualificationWizard().setComponentValue(REQUIRED_DOWNLOAD_SPEED_INPUT, requiredDownloadSpeed);
        return this;
    }

    @Step("Set Required Upload Speed")
    public ServiceQualificationWizard setRequiredUploadSpeed(String requiredUploadSpeed) {
        getServiceQualificationWizard().setComponentValue(REQUIRED_UPLOAD_SPEED_INPUT, requiredUploadSpeed);
        return this;
    }

    @Step("Set provide alternative checkbox")
    public ServiceQualificationWizard setProvideAlternative(String provideAlternativeValue) {
        waitForPageToLoad();
        getServiceQualificationWizard().setComponentValue(PROVIDE_ALTERNATIVE_CHECKBOX, provideAlternativeValue);
        return this;
    }

    @Step("Set provide resource checkbox")
    public ServiceQualificationWizard setProvideResource(String provideResourceValue) {
        waitForPageToLoad();
        getServiceQualificationWizard().setComponentValue(PROVIDE_RESOURCE_CHECKBOX, provideResourceValue);
        return this;
    }

    @Step("Set provide service node checkbox")
    public ServiceQualificationWizard setProvideServiceNode(String provideServiceNodeValue) {
        waitForPageToLoad();
        getServiceQualificationWizard().setComponentValue(PROVIDE_SERVICE_NODE_CHECKBOX, provideServiceNodeValue);
        return this;
    }

    @Step("Open advanced search window")
    public ServiceQualificationWizard openAdvancedSearchWindow() {
        waitForPageToLoad();
        getServiceQualificationWizard().clickButtonById(ADVANCED_SEARCH_BUTTON);
        return this;
    }

    @Step("Set xid in advanced search filter")
    public ServiceQualificationWizard setXidInAdvancedSearchFilter(Long id) {
        waitForPageToLoad();
        getAdvancedSearch().getComponent("id").setSingleStringValueContains(id.toString());
        return this;
    }

    @Step("Select first result in advanced search table")
    public ServiceQualificationWizard selectFirstResultInAdvancedSearchTable(String tableType) {
        waitForPageToLoad();
        if (tableType.equals("DA")) {
            driver.findElement(By.xpath(".//div[@data-testid='" + TABLE_DISTRIBUTION_AREA_SQ_SEARCH_FIELD_UID + "' ]//div[@data-row='0' and @data-col='name']")).click();
        } else {
            driver.findElement(By.xpath(".//div[@data-testid='" + TABLE_ADDRESS_SQ_SEARCH_FIELD_UID + "' ]//div[@data-row='0' and @data-col='name']")).click();
        }
        return this;
    }

    @Step("Click button Add in advanced search window")
    public ServiceQualificationWizard clickButtonAddInAdvancedSearchWindow() {
        getAdvancedSearch().clickAdd();
        return this;
    }

    @Step("Click Accept button")
    public ServiceQualificationView clickAccept() {
        waitForPageToLoad();
        getServiceQualificationWizard().clickAccept();
        return new ServiceQualificationView(driver);
    }

    private Wizard getServiceQualificationWizard() {
        return Wizard.createByComponentId(driver, wait, SQ_WIZARD_ID);
    }

    private AdvancedSearchWidget getAdvancedSearch() {
        return AdvancedSearchWidget.createById(driver, wait, ADVANCED_SEARCH_ID);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}
