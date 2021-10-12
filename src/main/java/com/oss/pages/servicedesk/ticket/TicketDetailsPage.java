package com.oss.pages.servicedesk.ticket;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Combobox;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.data.Data;
import com.oss.framework.listwidget.CommonList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.view.Card;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.pages.BasePage;
import com.oss.pages.servicedesk.ticket.wizard.WizardPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketDetailsPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(TicketDetailsPage.class);

    public static final String DETAILS_PAGE_URL_PATTERN = "%s/#/view/service-desk/trouble-ticket/details/%s";

    private static final String EDIT_DETAILS_LABEL = "Edit details";
    private static final String RELEASE_LABEL = "Release";
    private static final String CREATE_SUB_TICKET = "TT_DETAILS_SUBTICKET_CREATE_PROMPT_TITLE";
    private static final String CHECKLIST_APP_ID = "_checklistApp";
    private static final String SKIP_BUTTON_LABEL = "SKIP";
    private static final String EXTERNAL_LIST_ID = "_detailsExternalsListApp";
    private static final String EXTERNAL_INFO_LABEL = "External Info";

    public TicketDetailsPage(WebDriver driver) {
        super(driver);
    }

    @Step("I open edit ticket wizard")
    public WizardPage openEditTicketWizard(WebDriver driver) {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickContextAction(EDIT_DETAILS_LABEL);
        log.info("Ticket Wizard edit is opened");
        return new WizardPage(driver);
    }

    public void releaseTicket() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button releaseButton = Button.create(driver, RELEASE_LABEL);
        releaseButton.click();
        log.info("Clicking release button");
    }

    public void clickContextAction(String contextActionLabel) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabWindowWidget.create(driver, wait).callActionByLabel(contextActionLabel);
        log.info("Clicking Context action {}", contextActionLabel);
    }

    public void selectTab(WebDriver driver, String tabLabel) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabWindowWidget.create(driver, wait).selectTabByLabel(tabLabel);
        log.info("Selecting tab {}", tabLabel);
    }

    @Step("I open create subticket wizard for flow {flowType}")
    public WizardPage openCreateSubTicketWizard(WebDriver driver, String flowType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createBySelectorAndId(driver, "button", CREATE_SUB_TICKET).click();
        DropdownList.create(driver, wait).selectOptionWithId(flowType);
        log.info("Create subticket wizard for {} is opened", flowType);
        return new WizardPage(driver);
    }

    public void skipAllActionsOnCheckList() {
        CommonList commonList = CommonList.create(driver, wait, CHECKLIST_APP_ID);
        commonList.getAllRows()
                .forEach(row -> row.callActionIcon(SKIP_BUTTON_LABEL));
        log.info("Skipping all actions on checklist");
    }

    public void changeStatus(String statusName) {
        Combobox statusComboBox = Combobox.createServiceDeskStatusComboBox(driver, wait);
        statusComboBox.setValue(Data.createSingleData(statusName));
        log.info("Changing status to {}", statusName);
    }

    @Step("Maximize window")
    public void maximizeWindow(String windowId) {
        Card card = Card.createCard(driver, wait, windowId);
        card.maximizeCard();
        log.info("Maximizing window");
    }

    public boolean checkExistingExternal(String expectedExistingExternal) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Checking if expected external '{}' exists on the list", expectedExistingExternal);
        if (CommonList.create(driver, wait, EXTERNAL_LIST_ID).isRowVisible(EXTERNAL_INFO_LABEL, expectedExistingExternal)) {
            log.info("Expected external '{}' exists on the list", expectedExistingExternal);
            return true;
        } else {
            log.info("Expected external '{}' does not exists on the list", expectedExistingExternal);
            return false;
        }
    }
}
