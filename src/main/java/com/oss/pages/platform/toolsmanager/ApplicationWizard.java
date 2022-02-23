package com.oss.pages.platform.toolsmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.list.EditableList;
import com.oss.framework.wizard.Wizard;

public class ApplicationWizard {
    
    private static final String APPLICATION_INPUT_ID = "application-input";
    private static final String NAME_TEXT_FIELD_ID = "name";
    private static final String DESCRIPTION_TEXT_AREA_ID = "description";
    private static final String SAVE_BUTTON_FULL_XPATH = "//div[@class='popupBackground']//a[@class='CommonButton btn btn-primary btn-md']";
    private static final String QUERY_PARAM_KEY_COLUMN_ID = "1_key";
    private static final String QUERY_PARAM_VALUE_COLUMN_ID = "1_value";
    private static final String EDITABLE_LIST_KEY_COMPONENT_ID = "key-TEXT_FIELD";
    private static final String EDITABLE_LIST_VALUE_COMPONENT_ID = "value-TEXT_FIELD";
    private static final String PATH_TYPE = "Path";
    private static final String TYPE_COLUMN_ID = "type";
    private static final String COMBOBOX_ID = "type-COMBOBOX";
    private static final String VALUE_COLUMN_ID = "value";
    private static final String EDITABLE_LIST_ID = "ExtendedList-additionalQueryParamsList";
    
    private WebDriver driver;
    private WebDriverWait wait;
    private Wizard wizard;
    
    private ApplicationWizard(WebDriver driver, WebDriverWait wait, Wizard wizard) {
        this.driver = driver;
        this.wait = wait;
        this.wizard = wizard;
    }
    
    public static ApplicationWizard create(WebDriver driver, WebDriverWait wait) {
        Wizard applicationWizard = Wizard.createByComponentId(driver, wait, "popup_container");
        return new ApplicationWizard(driver, wait, applicationWizard);
    }
    
    public void setApplication(String application) {
        Input applicationInput = ComponentFactory.create(APPLICATION_INPUT_ID, Input.ComponentType.COMBOBOX, driver, wait);
        applicationInput.setSingleStringValue(application);
    }
    
    public void setApplicationName(String applicationName) {
        wizard.setComponentValue(NAME_TEXT_FIELD_ID, applicationName, Input.ComponentType.TEXT_FIELD);
    }
    
    public void setDescription(String applicationName) {
        wizard.setComponentValue(DESCRIPTION_TEXT_AREA_ID, applicationName, Input.ComponentType.TEXT_AREA);
    }
    
    public void setPolicies(String policiesType, String politicsName) {
        if (!isPoliciesSet()) {
            togglePolicies("true");
        }
        
        wizard.setComponentValue("politics_type", policiesType, Input.ComponentType.COMBOBOX);
        wizard.setComponentValue("politics_name", politicsName, Input.ComponentType.COMBOBOX);
    }
    
    public void togglePolicies(String value) {
        wizard.setComponentValue("politics", value, Input.ComponentType.CHECKBOX);
    }
    
    public boolean isPoliciesSet() {
        return wizard.getComponent("politics", Input.ComponentType.CHECKBOX).getStringValue().equals("true");
    }
    
    public void clickSave() {
        driver.findElement(By.xpath(SAVE_BUTTON_FULL_XPATH)).click();
    }
    
    public void addQueryParam(String key, String testValue) {
        EditableList.Row firstRow = getRow();
        firstRow.setValue(key, QUERY_PARAM_KEY_COLUMN_ID, EDITABLE_LIST_KEY_COMPONENT_ID, Input.ComponentType.TEXT_FIELD);
        firstRow.setValue(testValue, QUERY_PARAM_VALUE_COLUMN_ID, EDITABLE_LIST_VALUE_COMPONENT_ID, Input.ComponentType.TEXT_FIELD);
    }
    
    public void addPathParam(String value) {
        EditableList.Row firstRow = getRow();
        firstRow.setValue(PATH_TYPE, TYPE_COLUMN_ID, COMBOBOX_ID, Input.ComponentType.COMBOBOX);
        firstRow.setValue(value, VALUE_COLUMN_ID, EDITABLE_LIST_VALUE_COMPONENT_ID, Input.ComponentType.TEXT_FIELD);
    }
    
    public void addApplication(String application, String name) {
        setApplication(application);
        setApplicationName(name);
        clickSave();
    }
    
    private EditableList.Row getRow() {
        EditableList editableList = EditableList.createById(driver, wait, EDITABLE_LIST_ID);
        editableList.expandCategory("Params");
        return editableList.addRow();
    }
}