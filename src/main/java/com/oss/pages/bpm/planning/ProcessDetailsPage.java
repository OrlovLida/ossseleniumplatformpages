package com.oss.pages.bpm.planning;

import com.comarch.oss.web.pages.HomePage;
import com.google.common.collect.Maps;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Map;

import static com.oss.pages.bpm.processinstances.ProcessOverviewPage.BPM_AND_PLANNING;
import static com.oss.pages.bpm.processinstances.ProcessOverviewPage.NETWORK_PLANNING;

public class ProcessDetailsPage extends BasePage {
    public static final String PROCESS_DETAILS = "Process Details";
    public static final String OBJECT_NAME_ATTRIBUTE_NAME = "Object name";
    public static final String OBJECT_TYPE_ATTRIBUTE_NAME = "Object type";
    public static final String ACTIVATED_STATUS = "Activated";
    private static final String VALIDATION_TABLE_DATA_ATTRIBUTE_NAME = "plaPlanView_validationTable";
    private static final String PROJECT_DESCRIPTION_TABLE_ID = "plaPlanView_descriptionApp";
    private static final String PROJECT_INFORMATION_TABLE_ID = "plaPlanView_informationApp";
    private static final String OBJECT_TABLE_DATA_ATTRIBUTE_NAME = "plaPlanView_objectsApp";
    private static final String PROCEED_WITH_CANCELLATION_ID = "ConfirmationBox_plaCancelObjectsWizard_confirmBox_action_button";
    private static final String CANCEL_ID = "objectsRemoveAction";
    private static final String TAB_ID = "plaPlanView_leftSideWindow";
    private static final String CLOSE_BUTTON_TITLE = "Close";
    private static final String REFRESH_TABLE_ID = "tableRefreshButton";
    private static final String OBJECT_STATUS_ATTRIBUTE_NAME = "Object status";
    private static final String OBJECT_OPERATION_TYPE_ATTRIBUTE_NAME = "Operation type";
    public static final String DESCRIPTION_ATTRIBUTE_NAME = "Description";
    public static final String PROCESS_STATUS_ATTRIBUTE_NAME = "Process status";
    public static final String LOCK_STATUS_ATTRIBUTE_NAME = "Lock status";
    public static final String CODE_ATTRIBUTE_NAME = "Code";
    public static final String NAME_ATTRIBUTE_NAME = "Name";
    public static final String INTEGRATION_DATE_ATTRIBUTE_NAME = "Integration date";
    public static final String VALIDATION_STATUS_ATTRIBUTE_NAME = "Validation status";

    public ProcessDetailsPage(WebDriver driver) {
        super(driver);
    }

    public static ProcessDetailsPage goToProcessDetailsView(WebDriver driver, String basicURL, Long projectId) {
        driver.get(String.format("%s/#/view/planning/projects" +
                "?project_id=%d" + "&perspective=PLAN", basicURL, projectId));
        DelayUtils.waitForPageToLoad(driver, new WebDriverWait(driver, Duration.ofSeconds(45)));
        return new ProcessDetailsPage(driver);
    }

    public static ProcessDetailsPage goToProcessDetailsView(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPageToLoad(driver, wait);
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu(PROCESS_DETAILS, BPM_AND_PLANNING, NETWORK_PLANNING);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new ProcessDetailsPage(driver);
    }

    public ProcessDetailsPage selectTab(String label) {
        waitForPageToLoad();
        TabsInterface tab = TabsWidget.createById(driver, wait, TAB_ID);
        tab.selectTabByLabel(label);
        waitForPageToLoad();
        return this;
    }

    public boolean isValidationResultPresent() {
        waitForPageToLoad();
        return !getValidationResultsTable().hasNoData();
    }

    public void closeProcessDetailsPromt() {
        Button.createByLabel(driver, CLOSE_BUTTON_TITLE).click();
    }

    @Step("Removing all planned object")
    public void removeAllObjects() {
        waitForPageToLoad();
        OldTable oldTable = getObjectsTable();
        final int totalCount = oldTable.getTotalCount();
        for (int i = 0; i < totalCount; i++) {
            oldTable.selectRow(0);
            oldTable.callAction(CANCEL_ID);
            ConfirmationBox.create(driver, wait).clickButtonById(PROCEED_WITH_CANCELLATION_ID);
        }
    }

    private OldTable getObjectsTable() {
        return OldTable.createById(driver, wait, OBJECT_TABLE_DATA_ATTRIBUTE_NAME);
    }

    private OldTable getValidationResultsTable() {
        return OldTable.createById(driver, wait, VALIDATION_TABLE_DATA_ATTRIBUTE_NAME);
    }

    private CommonList getProjectInformationTable() {
        return CommonList.create(driver, wait, PROJECT_INFORMATION_TABLE_ID);
    }

    private CommonList getProjectDescriptionTable() {
        return CommonList.create(driver, wait, PROJECT_DESCRIPTION_TABLE_ID);
    }

    public ProcessDetailsPage selectObject(String columnLabel, String value) {
        waitForPageToLoad();
        OldTable table = getObjectsTable();
        table.searchByColumn(columnLabel, value);
        waitForPageToLoad();
        table.doRefreshWhileNoData(10000, REFRESH_TABLE_ID);
        table.selectRowByAttributeValueWithLabel(columnLabel, value);
        waitForPageToLoad();
        return this;
    }

    public int getObjectsAmount() {
        waitForPageToLoad();
        return getObjectsTable().getTotalCount();
    }

    public ProcessDetailsPage clearAllColumnFilters() {
        waitForPageToLoad();
        getObjectsTable().clearAllColumnValues();
        return this;
    }

    private String getObjectAttribute(String attributeName) {
        waitForPageToLoad();
        return getObjectsTable().getCellValue(0, attributeName);
    }


    public String getObjectStatus() {
        return getObjectAttribute(OBJECT_STATUS_ATTRIBUTE_NAME);
    }

    public String getObjectOperationType() {
        return getObjectAttribute(OBJECT_OPERATION_TYPE_ATTRIBUTE_NAME);
    }

    public String getProjectAttribute(String attributeName) {
        if (attributeName.equals(DESCRIPTION_ATTRIBUTE_NAME)) {
            return getProjectDescriptionTable().getRows().get(0).getValue(DESCRIPTION_ATTRIBUTE_NAME);
        } else {
            return getProjectInformationTable().getRows().get(0).getValue(attributeName);
        }
    }

    public Map<String, String> getProjectAttributes() {
        Map<String, String> attributesMap = Maps.newHashMap();
        CommonList infoTable = getProjectInformationTable();
        infoTable.getRowHeaders().forEach(attributeName ->
                attributesMap.put(attributeName, infoTable.getRows().get(0).getValue(attributeName)));
        attributesMap.put(DESCRIPTION_ATTRIBUTE_NAME, getProjectAttribute(DESCRIPTION_ATTRIBUTE_NAME));
        return attributesMap;
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

}