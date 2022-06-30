package com.oss.pages.bpm;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.widgets.treetable.OldTreeTableWidget;
import com.oss.pages.BasePage;
import com.oss.pages.dms.AttachFileWizardPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Paweł Rother
 */
public class IPDTaskFormPage extends BasePage {
    private static final String IP_TABLE_ID = "form.specific.ip_involved_nrp_group.ip_involved_nrp_table";
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


    private final String tabsId;
    private final TabsInterface tabWidget = getTabWidget();

    public IPDTaskFormPage(WebDriver driver, WebDriverWait wait, String tabsTasksViewId) {
        super(driver, wait);
        this.tabsId = tabsTasksViewId;
    }

    private TabsInterface getTabWidget() {
        return TabsWidget.createById(driver, wait, tabsId);
    }

    public static IPDTaskFormPage create(WebDriver driver, WebDriverWait wait, String tabsTasksViewId) {
        return new IPDTaskFormPage(driver, wait, tabsTasksViewId);
    }

    public void selectTabById(String tabId) {
        tabWidget.selectTabById(tabId);
    }

    public void selectTabByLabel(String tabLabel) {
        tabWidget.selectTabByLabel(tabLabel);
    }

    public TableInterface getIPTable() {
        return OldTable.createById(driver, wait, IP_TABLE_ID);
    }

    private void actionTask(String actionId) {
        selectTabByLabel(FORM_TAB_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
        callAction(actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel(PROCEED_BUTTON_LABEL);
    }

    private void callAction(String actionId) {
        selectTabByLabel(FORM_TAB_LABEL);
        tabWidget.callActionById(actionId);
    }

    public void setupIntegration() {
        callAction(SETUP_INTEGRATION_ICON_ID);
    }

    public void startTask() {
        actionTask(ASSIGN_TASK_ICON_ID);
    }

    public void completeTask() {
        actionTask(COMPLETE_TASK_ICON_ID);
    }

    public void attachFile(String filePath) {
        selectTabByLabel(TASK_ATTACHMENTS_TAB_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
        callAction(ATTACH_FILE_BUTTON_ID);
        AttachFileWizardPage attachFileWizardPage = new AttachFileWizardPage(driver);
        attachFileWizardPage.selectRadioButton(UPLOAD_ANYWAY_LABEL);
        attachFileWizardPage.attachFile(filePath);
        attachFileWizardPage.skipAndAccept();
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

    public void clickPlanViewButton() {
        Button button = Button.createByLabel(driver, PLAN_VIEW_BUTTON_LABEL);
        button.click();
    }

    public void setTransition(String transitionName) {
        Input input = ComponentFactory.create(TRANSITION_COMBOBOX_ID, Input.ComponentType.BPM_COMBOBOX, driver, wait);
        input.setSingleStringValue(transitionName);
    }


}