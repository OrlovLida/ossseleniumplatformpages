package com.oss.pages.bpm.tasks.taskforms;

import com.google.common.collect.Maps;
import com.oss.framework.bpm.bpmpropertypanel.BpmPropertyPanel;
import com.oss.framework.bpm.bpmrolloutpanel.BpmRolloutPanel;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.widgets.treetable.OldTreeTableWidget;
import com.oss.pages.BasePage;
import com.oss.pages.bpm.planning.ProcessDetailsPage;
import com.oss.pages.bpm.processinstances.creation.ProcessWizardPage;
import com.oss.pages.bpm.tasks.SetupIntegrationWizardPage;
import com.oss.pages.dms.AttachFileWizardPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Paweł Rother
 */
public class IPDTaskFormPage extends BasePage {
    public static final String PROCESS_NAME_ATTRIBUTE = "Process Name";
    public static final String PROCESS_DESCRIPTION_ATTRIBUTE = "Process Description";
    public static final String FINISHED_DUE_DATE_ATTRIBUTE = "Finished Due Date";
    private static final String IP_TABLE_ID = "form.specific.ip_involved_nrp_group.ip_involved_nrp_table";
    private static final String PROCESS_INFO_LIST = "form.group1.processInfoWidget";
    private static final String ATTACHMENTS_LIST_ID = "attachmentManagerBusinessView_commonTreeTable_BPMTask";
    private static final String ATTACHMENTS_AND_DIRECTORIES = "Attachments and directories";
    private static final String HOME_LABEL = "HOME";
    private static final String PERFORM_CONFIGURATION_BUTTON_LABEL = "Perform Configuration";
    private static final String PLAN_VIEW_BUTTON_LABEL = "Plan View";
    private static final String PROCEED_BUTTON_LABEL = "Proceed";
    private static final String FORM_TAB_LABEL = "Form";
    private static final String TASK_ATTACHMENTS_TAB_LABEL = "Tasks Attachments";
    private static final String ATTACH_FILE_BUTTON_ID = "addAttachmentAction";
    private static final String UPLOAD_ANYWAY_LABEL = "Upload anyway";
    private static final String TRANSITION_COMBOBOX_ID = "transitionComboBox";
    private static final String ASSIGN_TASK_ICON_ID = "form.toolbar.assignTask";
    private static final String COMPLETE_TASK_ICON_ID = "form.toolbar.closeTask";
    private static final String SETUP_INTEGRATION_ICON_ID = "form.toolbar.setupIntegrationButton";
    private static final String PROPERTY_PANEL_ID = "bpm_task_view_attributes-app";
    private static final String DRP_CREATION_ICON_ID = "form.toolbar.createChildProcess";

    private final String tabsId;
    private final TabsInterface tabWidget;

    protected IPDTaskFormPage(WebDriver driver, WebDriverWait wait, String tabsTasksViewId) {
        super(driver, wait);
        this.tabsId = tabsTasksViewId;
        this.tabWidget = getTabWidget(tabsTasksViewId);
    }

    public static IPDTaskFormPage create(WebDriver driver, WebDriverWait wait, String tabsTasksViewId) {
        return new IPDTaskFormPage(driver, wait, tabsTasksViewId);
    }

    private TabsInterface getTabWidget(String tabWidgetId) {
        return TabsWidget.createById(driver, wait, tabWidgetId);
    }

    public void selectTabById(String tabId) {
        tabWidget.selectTabById(tabId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void selectTabByLabel(String tabLabel) {
        tabWidget.selectTabByLabel(tabLabel);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public TableInterface getIPTable() {
        return OldTable.createById(driver, wait, IP_TABLE_ID);
    }

    @Step("Searching for panel by ID = {id} and expanding it")
    public void expandPanel(String id) {
        BpmRolloutPanel.createById(driver, wait, id).expandRolloutPanel();
    }

    @Step("Searching for panel by ID = {id} and collapsing it")
    public void collapsePanel(String id) {
        BpmRolloutPanel.createById(driver, wait, id).collapseRolloutPanel();
    }

    private void actionTask(String actionId) {
        selectTabByLabel(FORM_TAB_LABEL);
        callAction(actionId);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel(PROCEED_BUTTON_LABEL);
    }

    private void callAction(String actionId) {
        tabWidget.callActionById(actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public SetupIntegrationWizardPage openSetupIntegrationWizard() {
        selectTabByLabel(FORM_TAB_LABEL);
        callAction(SETUP_INTEGRATION_ICON_ID);
        return new SetupIntegrationWizardPage(driver);
    }

    public ProcessWizardPage openDesignRequestProcessCreationWizard() {
        selectTabByLabel(FORM_TAB_LABEL);
        callAction(DRP_CREATION_ICON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new ProcessWizardPage(driver);
    }

    public void startTask() {
        actionTask(ASSIGN_TASK_ICON_ID);
    }

    public void completeTask() {
        actionTask(COMPLETE_TASK_ICON_ID);
    }

    public void attachFile(String filePath) {
        selectTabByLabel(TASK_ATTACHMENTS_TAB_LABEL);
        callAction(ATTACH_FILE_BUTTON_ID);
        AttachFileWizardPage attachFileWizardPage = new AttachFileWizardPage(driver);
        attachFileWizardPage.selectRadioButton(UPLOAD_ANYWAY_LABEL);
        attachFileWizardPage.attachFile(filePath);
        attachFileWizardPage.skipAndAccept();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public List<String> getListOfAttachments() {
        OldTreeTableWidget treeTable =
                OldTreeTableWidget.create(driver, wait, ATTACHMENTS_LIST_ID);
        List<String> allNodes = treeTable.getAllVisibleNodes(ATTACHMENTS_AND_DIRECTORIES);
        return allNodes.stream().filter(node -> !node.equals(HOME_LABEL)).collect(Collectors.toList());
    }

    public void clickPerformConfigurationButton() {
        Button button = Button.createByLabel(driver, tabsId, PERFORM_CONFIGURATION_BUTTON_LABEL);
        button.click();
    }

    public ProcessDetailsPage clickPlanViewButton() {
        Button button = Button.createByLabel(driver, tabsId, PLAN_VIEW_BUTTON_LABEL);
        button.clickWithRetry(ProcessDetailsPage.OBJECTS_TABLE_ID);
        return new ProcessDetailsPage(driver);
    }

    public void setTransition(String transitionName) {
        Input input = ComponentFactory.create(TRANSITION_COMBOBOX_ID, Input.ComponentType.BPM_COMBOBOX, driver, wait);
        input.setSingleStringValue(transitionName);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public String getAttributeValue(String propertyName) {
        return BpmPropertyPanel.createById(driver, wait, PROPERTY_PANEL_ID).getPropertyValue(propertyName);
    }

    public Map<String, String> getProcessInfo() {
        CommonList processInfoList = CommonList.create(driver, wait, PROCESS_INFO_LIST);
        Map<String, String> attributes = Maps.newHashMap();
        processInfoList.getRows().forEach(row -> {
            List<String> rowValues = row.getValues();
            attributes.put(rowValues.get(0), rowValues.get(1));
        });
        return attributes;
    }

    public String getProcessInfo(String attributeName) {
        return getProcessInfo().get(attributeName);
    }

}
