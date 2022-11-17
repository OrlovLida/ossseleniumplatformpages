package com.oss.pages.bpm;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.BasePage;
import com.oss.pages.platform.HomePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.oss.pages.bpm.ProcessOverviewPage.BPM_AND_PLANNING;
import static com.oss.pages.bpm.ProcessOverviewPage.NETWORK_PLANNING;

public class ProcessDetailsPage extends BasePage {
    protected static final String PROCESS_DETAILS = "Process Details";
    private static final String VALIDATION_TABLE_DATA_ATTRIBUTE_NAME = "plaPlanView_validationTable";
    private static final String OBJECT_TABLE_DATA_ATTRIBUTE_NAME = "plaPlanView_objectsApp";
    private static final String PROCEED_WITH_CANCELLATION_ID = "ConfirmationBox_plaCancelObjectsWizard_confirmBox_action_button";
    private static final String CANCEL_ID = "objectsRemoveAction";
    private static final String TAB_ID = "plaPlanView_leftSideWindow";
    private static final String CLOSE_BUTTON_TITLE = "Close";

    public ProcessDetailsPage(WebDriver driver) {
        super(driver);
    }

    public static ProcessDetailsPage goToProcessDetailsView(WebDriver driver, String basicURL, int projectId) {
        driver.get(String.format("%s/#/view/planning/projects" +
                "?project_id=%d" + "&perspective=PLAN", basicURL, projectId));
        return new ProcessDetailsPage(driver);
    }

    public static ProcessDetailsPage goToProcessDetailsView(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPageToLoad(driver, wait);
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu(PROCESS_DETAILS, BPM_AND_PLANNING, NETWORK_PLANNING);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new ProcessDetailsPage(driver);
    }

    public void selectTab(String label) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsInterface tab = TabsWidget.createById(driver, wait, TAB_ID);
        tab.selectTabByLabel(label);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public boolean isValidationResultPresent() {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldTable oldTable = OldTable.createById(driver, wait, VALIDATION_TABLE_DATA_ATTRIBUTE_NAME);
        return !oldTable.hasNoData();
    }

    public void closeProcessDetailsPromt() {
        Button.createByLabel(driver, CLOSE_BUTTON_TITLE).click();
    }

    @Step("Removing all planned object")
    public void removeAllObjects() {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldTable oldTable = OldTable.createById(driver, wait, OBJECT_TABLE_DATA_ATTRIBUTE_NAME);
        final int totalCount = oldTable.getTotalCount();
        for (int i = 0; i < totalCount; i++) {
            oldTable.selectRow(0);
            oldTable.callAction(CANCEL_ID);
            ConfirmationBox.create(driver, wait).clickButtonById(PROCEED_WITH_CANCELLATION_ID);
        }
    }
}