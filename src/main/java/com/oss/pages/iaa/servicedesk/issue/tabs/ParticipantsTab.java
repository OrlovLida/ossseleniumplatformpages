package com.oss.pages.iaa.servicedesk.issue.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.iaa.servicedesk.BaseSDPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.ParticipantsPromptPage;

import io.qameta.allure.Step;

public class ParticipantsTab extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(ParticipantsTab.class);

    private static final String ADD_PARTICIPANT_ID = "_createParticipant";
    private static final String EDIT_PARTICIPANT_ID = "_editParticipant";
    private static final String UNLINK_PARTICIPANT_ID = "_unlinkParticipant";
    private static final String DELETE_PARTICIPANT_ID = "_deleteParticipantButton";
    private static final String PARTICIPANTS_TABLE_ID = "_participantsTableApp";
    private static final String FIRST_NAME_ATTRIBUTE_ID = "First Name";
    private static final String SURNAME_ATTRIBUTE_ID = "Surname";
    private static final String ROLE_ATTRIBUTE_ID = "Role";
    private static final String ADD_PARTICIPANT_PROMPT_ID = "_createParticipantModal_prompt-card";
    private static final String EDIT_PARTICIPANT_PROMPT_ID = "_editParticipantFormApp";
    private static final String CONFIRM_REMOVE_ID = "ConfirmationBox__confirmDeleteParticipantApp_action_button";

    public ParticipantsTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Click Add Participant")
    public ParticipantsPromptPage clickAddParticipant() {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickContextActionFromButtonContainer(ADD_PARTICIPANT_ID);
        log.info("Add Participant prompt is opened");
        return new ParticipantsPromptPage(driver, wait, ADD_PARTICIPANT_PROMPT_ID);
    }

    @Step("Click Edit Participant")
    public ParticipantsPromptPage clickEditParticipant() {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickContextActionFromButtonContainer(EDIT_PARTICIPANT_ID);
        log.info("Edit Participant prompt is opened");
        return new ParticipantsPromptPage(driver, wait, EDIT_PARTICIPANT_PROMPT_ID);
    }

    @Step("Click Unlink Participant")
    public void clickUnlinkParticipant() {
        clickContextActionFromButtonContainer(UNLINK_PARTICIPANT_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking Unlink Participant");
    }

    @Step("Click Remove Participant")
    public void clickRemoveParticipant() {
        clickContextActionFromButtonContainer(DELETE_PARTICIPANT_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking Remove Participant");
    }

    @Step("Click Remove button in prompt")
    public void clickConfirmRemove() {
        Button.createById(driver, CONFIRM_REMOVE_ID).click();
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking Remove button in prompt");
    }

    public boolean isAddParticipantButtonActive() {
        return isActionPresent(ADD_PARTICIPANT_ID);
    }

    @Step("Check participant first name")
    public String checkParticipantFirstName(int participantIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check participant first name");
        return getParticipantTable().getCellValue(participantIndex, FIRST_NAME_ATTRIBUTE_ID);
    }

    @Step("Check participant surname")
    public String checkParticipantSurname(int participantIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check participant surname");
        return getParticipantTable().getCellValue(participantIndex, SURNAME_ATTRIBUTE_ID);
    }

    @Step("Check participant role")
    public String checkParticipantRole(int participantIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check participant role");
        return getParticipantTable().getCellValue(participantIndex, ROLE_ATTRIBUTE_ID);
    }

    @Step("Check in which row is new participant")
    public int participantRow(String firstName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getParticipantTable().getRowNumber(firstName, FIRST_NAME_ATTRIBUTE_ID);
    }

    @Step("Check in which row is new participant using '{attributeID}' attribute")
    public int participantRowByAttribute(String attribute, String attributeID) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getParticipantTable().getRowNumber(attribute, attributeID);
    }

    @Step("Count Participants in Table")
    public int countParticipantsInTable() {
        return getParticipantTable().countRows(FIRST_NAME_ATTRIBUTE_ID);
    }

    @Step("Select participant in table")
    public void selectParticipant(int row) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getParticipantTable().selectRow(row);
        log.info("Selecting participant in row: {}", row);
    }

    public ParticipantsTab createParticipantAndLinkToIssue(String name, String surname, String role) {
        clickAddParticipant()
                .fillParticipantPrompt(name, surname, role)
                .clickAddParticipantInPrompt();
        return this;
    }

    public ParticipantsTab addExistingParticipant(String name, String role) {
        clickAddParticipant()
                .searchParticipant(name)
                .setParticipantRole(role)
                .clickAddParticipantInPrompt();
        return this;
    }

    public ParticipantsTab editParticipant(String editedName, String editedSurname, String role) {
        clickEditParticipant()
                .fillParticipantPrompt(editedName, editedSurname, role)
                .clickSaveEditedParticipant();
        return this;
    }

    private OldTable getParticipantTable() {
        return OldTable.createById(driver, wait, PARTICIPANTS_TABLE_ID);
    }
}
