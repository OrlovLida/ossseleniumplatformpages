package com.oss.pages.dpe.qvp;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.layout.Card;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.utils.DelayUtils.waitForPageToLoad;

public class GlobalQuickViewPolicyPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(GlobalQuickViewPolicyPage.class);
    private static final String CARD_WINDOW_ID_AREA1 = "card-content_1";
    private static final String CREATE_GLOBAL_QVP_BUTTON_ID = "CreateGlobalProblemQVPTypeAction";
    private static final String EDIT_GLOBAL_QVP_BUTTON_ID = "EditGlobalProblemQVPTypeAction";
    private static final String MOVE_UP_BUTTON_ID = "MoveUPGQVPProblemAction";
    private static final String MOVE_TOP_BUTTON_ID = "MoveTOPGQVPProblemAction";
    private static final String MOVE_DOWN_BUTTON_ID = "MoveDOWNGQVPProblemAction";
    private static final String MOVE_BOTTOM_BUTTON_ID = "MoveBOTTOMGQVPProblemAction";
    private static final String DELETE_BUTTON_ID = "DeleteGlobalProblemQVPTypeAction";
    private static final String QVP_TABLE_ID = "table(ITGlobalQuickViewPolicyProblem)";
    private static final String NAME_ON_VIEW_COLUMN_LABEL = "Name on View";
    private static final String POSITION_PROPERTY_NAME = "Position";
    private static final String PROPERTY_NAME_COLUMN_LABEL = "Property Name";
    private static final String PROPERTY_VALUE_COLUMN_LABEL = "Property Value";
    private static final String PROPERTIES_TABLE_ID = "properties(ITGlobalQuickViewPolicyProblem)";
    private static final String CONFIRM_DELETE_BUTTON_ID = "ConfirmationBox__remove_popup_action_button";

    public GlobalQuickViewPolicyPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Opening Global QVP Page")
    public static GlobalQuickViewPolicyPage openGlobalQVPPage(WebDriver driver, String basicURL, WebDriverWait wait) {
        String pageUrl = String.format("%s/#/view/inventory/view/type/ITGlobalQuickViewPolicyProblem", basicURL);
        driver.get(pageUrl);
        waitForPageToLoad(driver, wait);
        log.info("Opening page: {}", pageUrl);
        return new GlobalQuickViewPolicyPage(driver, wait);
    }

    @Step("Click Create global QVP")
    public QVPPrompt clickCreateGlobalQVP() {
        callActionsContainerActionById(CREATE_GLOBAL_QVP_BUTTON_ID);
        log.info("Click Create global QVP");
        return new QVPPrompt(driver, wait);
    }

    @Step("Click Edit global QVP")
    public QVPPrompt clickEditGlobalQVP() {
        callActionsContainerActionById(EDIT_GLOBAL_QVP_BUTTON_ID);
        log.info("Click Edit global QVP");
        return new QVPPrompt(driver, wait);
    }

    @Step("Click Move Up button")
    public void clickMoveUp(String qvpName) {
        selectQVP(qvpName);
        callActionsContainerActionById(MOVE_UP_BUTTON_ID);
        log.info("Clicking Move Up button");
    }

    @Step("Click Move Top button")
    public void clickMoveTop(String qvpName) {
        selectQVP(qvpName);
        callActionsContainerActionById(MOVE_TOP_BUTTON_ID);
        log.info("Clicking Move Top button");
    }

    @Step("Click Move Down button")
    public void clickMoveDown(String qvpName) {
        selectQVP(qvpName);
        callActionsContainerActionById(MOVE_DOWN_BUTTON_ID);
        log.info("Clicking Move Down button");
    }

    @Step("Click Move Bottom button")
    public void clickMoveBottom(String qvpName) {
        selectQVP(qvpName);
        callActionsContainerActionById(MOVE_BOTTOM_BUTTON_ID);
        log.info("Clicking Move Bottom button");
    }

    @Step("Click Delete button")
    public void clickDeleteQVP() {
        callActionsContainerActionById(DELETE_BUTTON_ID);
        log.info("Clicking Delete QVP button");
    }

    @Step("Confirm deleting global QVP")
    public void confirmDelete() {
        ConfirmationBox.create(driver, wait).clickButtonById(CONFIRM_DELETE_BUTTON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Click confirm deleting global QVP");
    }

    private void callActionsContainerActionById(String actionId) {
        Card.createCard(driver, wait, CARD_WINDOW_ID_AREA1).callActionById(actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Count QVP in table")
    public int countQVP() {
        return getQVPTable().countRows(POSITION_PROPERTY_NAME);
    }

    @Step("Get Created QVP Name")
    public String getCreatedQVPName() {
        return getQVPTable().getCellValue(countQVP() - 1, NAME_ON_VIEW_COLUMN_LABEL);
    }

    @Step("Get name of QVP in row: {row}")
    public String getQVPNameInRow(int row) {
        return getQVPTable().getCellValue(row, NAME_ON_VIEW_COLUMN_LABEL);
    }

    @Step("Select QVP with name: {qvpName}")
    public void selectQVP(String qvpName) {
        getQVPTable().selectRowByAttributeValueWithLabel(NAME_ON_VIEW_COLUMN_LABEL, qvpName);
        log.info("Selecting QVP with name: {}", qvpName);
    }

    @Step("Get {propertyName} value from properties table")
    public String getPropertyFromPropertyTable(String propertyName) {
        return getPropertiesTable().getCellValue(getPropertyRow(propertyName), PROPERTY_VALUE_COLUMN_LABEL);
    }

    private int getPropertyRow(String propertyName) {
        return getPropertiesTable().getRowNumber(propertyName, PROPERTY_NAME_COLUMN_LABEL);
    }

    private OldTable getQVPTable() {
        return OldTable.createById(driver, wait, QVP_TABLE_ID);
    }

    private OldTable getPropertiesTable() {
        return OldTable.createById(driver, wait, PROPERTIES_TABLE_ID);
    }
}
