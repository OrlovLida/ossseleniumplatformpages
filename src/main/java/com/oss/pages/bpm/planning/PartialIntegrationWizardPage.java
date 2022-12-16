package com.oss.pages.bpm.planning;

import com.google.common.collect.ImmutableMap;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PartialIntegrationWizardPage extends BasePage {

    public static final String OBJECT_TYPE = "Object Type";
    public static final String OBJECT_NAME = "Object Name";
    public static final String OPERATION_TYPE = "Operation Type";
    private static final String PLANNED_OBJECTS_TABLE_ID = "table-plaPartialActivationWizard_appId";
    private static final String OBJECTS_TO_INTEGRATION_TABLE_ID = "table-plaPartialActivationWizard_appId";
    private static final String PLANNED_OBJECTS_SEARCH_INPUT_ID = "RootsInProjectSearchCompId";
    private static final String OBJECTS_TO_INTEGRATION_SEARCH_INPUT_ID = "RootsToActivateSearchCompId";
    private static final String CANCEL_BUTTON_ID = "wizard-cancel-button-plaPartialActivationWizard_appId";
    private static final String APPLY_BUTTON_ID = "wizard-submit-button-plaPartialActivationWizard_appId";
    private static final String CHOOSE_TO_INTEGRATION_BUTTON_ID = "ChooseToActivationBtnCompId";
    private static final String BACK_TO_PLANNED_OBJECTS_BUTTON_ID = "BackToPlannedBtnCompId";
    private static final String WIZARD_ID = "plaPartialActivationWizard_prompt-card";
    private static final String OBJECT_TYPE_COLUMN_ID = "objectTypeColId";
    private static final String OBJECT_NAME_COLUMN_ID = "objectNameColId";
    private static final String OPERATION_TYPE_COLUMN_ID = "operationTypeColId";

    private final Wizard partialIntegrationWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    public PartialIntegrationWizardPage(WebDriver driver) {
        super(driver);
    }

    private TableComponent getPlannedObjectsTable() {
        return TableComponent.createById(driver, wait, PLANNED_OBJECTS_TABLE_ID);
    }

    private TableComponent getObjectsToIntegrationTable() {
        return TableComponent.createById(driver, wait, OBJECTS_TO_INTEGRATION_TABLE_ID);
    }

    private TableComponent getTable(boolean isForPlannedObjectsTable) {
        if (isForPlannedObjectsTable) {
            return getPlannedObjectsTable();
        } else {
            return getObjectsToIntegrationTable();
        }
    }

    public void clickApplyButton() {
        partialIntegrationWizard.clickButtonById(APPLY_BUTTON_ID);
    }

    public void clickCancelButton() {
        partialIntegrationWizard.clickButtonById(CANCEL_BUTTON_ID);
    }

    private List<Map<String, String>> getObjectsAttributes(boolean isForPlannedObjectsTable) {
        return getTable(isForPlannedObjectsTable).getVisibleRows().stream().map(tableRow -> {
            TableComponent.Row row = (TableComponent.Row) tableRow;
            return buildObjectAttributesMap(row);
        }).collect(Collectors.toList());
    }

    public List<Map<String, String>> getPlannedObjectsAttributes() {
        return getObjectsAttributes(true);
    }

    public List<Map<String, String>> getObjectsForIntegrationAttributes() {
        return getObjectsAttributes(false);
    }

    private Map<String, String> getObjectAttributes(String objectName, boolean isForPlannedObjectsTable) {
        Input search;
        if (isForPlannedObjectsTable) {
            search = ComponentFactory.create(PLANNED_OBJECTS_SEARCH_INPUT_ID, driver, wait);
        } else {
            search = ComponentFactory.create(OBJECTS_TO_INTEGRATION_SEARCH_INPUT_ID, driver, wait);
        }
        search.clear();
        search.setSingleStringValue(objectName);
        DelayUtils.waitForPageToLoad(driver, wait);
        TableComponent.Row row = getTable(isForPlannedObjectsTable).getRow(0);
        return buildObjectAttributesMap(row);
    }

    private Map<String, String> buildObjectAttributesMap(TableComponent.Row row) {
        return ImmutableMap.<String, String>builder().
                put(OBJECT_TYPE, row.getColumnValue(OBJECT_TYPE_COLUMN_ID)).
                put(OBJECT_NAME, row.getColumnValue(OBJECT_NAME_COLUMN_ID)).
                put(OPERATION_TYPE, row.getColumnValue(OPERATION_TYPE_COLUMN_ID)).
                build();
    }

    public Map<String, String> getPlannedObjectAttributes(String objectName) {
        return getObjectAttributes(objectName, true);
    }

    public Map<String, String> getObjectForIntegrationAttributes(String objectName) {
        return getObjectAttributes(objectName, false);
    }

    private List<String> getObjectsAttribute(String attributeId, boolean isForPlannedObjectsTable) {
        return getTable(isForPlannedObjectsTable).getVisibleRows().stream().
                map(tableRow -> {
                    TableComponent.Row row = (TableComponent.Row) tableRow;
                    return row.getColumnValue(attributeId);
                }).collect(Collectors.toList());
    }

    public List<String> getPlannedObjectsNames() {
        return getObjectsAttribute(OBJECT_NAME_COLUMN_ID, true);
    }

    public List<String> getObjectsToIntegrationNames() {
        return getObjectsAttribute(OBJECT_NAME_COLUMN_ID, false);
    }

    public List<String> getPlannedObjectsIdentifiers() {
        return getObjectsAttribute(OBJECT_TYPE_COLUMN_ID, true);
    }

    public List<String> getObjectsToIntegrationIdentifiers() {
        return getObjectsAttribute(OBJECT_TYPE_COLUMN_ID, false);
    }

    public PartialIntegrationWizardPage moveObjectsToIntegration(List<String> objectsNames) {
        Input search = ComponentFactory.create(PLANNED_OBJECTS_SEARCH_INPUT_ID, driver, wait);
        for (String objectName : objectsNames) {
            search.clear();
            search.setSingleStringValue(objectName);
            DelayUtils.waitForPageToLoad(driver, wait);
            getPlannedObjectsTable().selectRow(0);
            DelayUtils.waitForPageToLoad(driver, wait);
            partialIntegrationWizard.clickButtonById(CHOOSE_TO_INTEGRATION_BUTTON_ID);
            DelayUtils.waitForPageToLoad(driver, wait);
        }
        search.clear();
        return this;
    }

    public PartialIntegrationWizardPage moveObjectToIntegration(String objectName) {
        return moveObjectsToIntegration(Collections.singletonList(objectName));
    }

    public PartialIntegrationWizardPage moveBackObjectsToPlanned(List<String> objectsNames) {
        Input search = ComponentFactory.create(OBJECTS_TO_INTEGRATION_SEARCH_INPUT_ID, driver, wait);
        for (String objectName : objectsNames) {
            search.clear();
            search.setSingleStringValue(objectName);
            DelayUtils.waitForPageToLoad(driver, wait);
            getObjectsToIntegrationTable().selectRow(0);
            DelayUtils.waitForPageToLoad(driver, wait);
            partialIntegrationWizard.clickButtonById(BACK_TO_PLANNED_OBJECTS_BUTTON_ID);
            DelayUtils.waitForPageToLoad(driver, wait);
        }
        search.clear();
        return this;
    }

    public PartialIntegrationWizardPage moveBackObjectToPlanned(String objectName) {
        return moveBackObjectsToPlanned(Collections.singletonList(objectName));
    }


}
