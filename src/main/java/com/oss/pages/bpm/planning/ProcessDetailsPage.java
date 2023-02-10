package com.oss.pages.bpm.planning;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.BasePage;
import com.oss.pages.platform.HomePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.oss.pages.bpm.processinstances.ProcessOverviewPage.BPM_AND_PLANNING;
import static com.oss.pages.bpm.processinstances.ProcessOverviewPage.NETWORK_PLANNING;

public class ProcessDetailsPage extends BasePage {
    public static final String PROCESS_DETAILS = "Process Details";
    public static final String OBJECT_NAME_ATTRIBUTE_NAME = "Object name";
    public static final String OBJECT_TYPE_ATTRIBUTE_NAME = "Object type";
    public static final String ACTIVATED_STATUS = "Activated";
    private static final String VALIDATION_TABLE_DATA_ATTRIBUTE_NAME = "plaPlanView_validationTable";
    private static final String OBJECT_TABLE_DATA_ATTRIBUTE_NAME = "plaPlanView_objectsApp";
    private static final String PROCEED_WITH_CANCELLATION_ID = "ConfirmationBox_plaCancelObjectsWizard_confirmBox_action_button";
    private static final String CANCEL_ID = "objectsRemoveAction";
    private static final String TAB_ID = "plaPlanView_leftSideWindow";
    private static final String CLOSE_BUTTON_TITLE = "Close";
    private static final String REFRESH_TABLE_ID = "tableRefreshButton";
    private static final String OBJECT_STATUS_ATTRIBUTE_NAME = "Object status";
    private static final String OBJECT_OPERATION_TYPE_ATTRIBUTE_NAME = "Operation type";


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

    public ProcessDetailsPage selectObject(String attributeName, String value) {
        waitForPageToLoad();
        TableInterface table = getObjectsTable();
        table.searchByAttributeWithLabel(attributeName, Input.ComponentType.TEXT_FIELD, value);
        waitForPageToLoad();
        table.doRefreshWhileNoData(10000, REFRESH_TABLE_ID);
        table.selectRowByAttributeValueWithLabel(attributeName, value);
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

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

}