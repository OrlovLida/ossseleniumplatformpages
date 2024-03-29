package com.oss.pages.iaa.servicedesk.issue.wizard;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.qameta.allure.Step;

public class ParticipantsPromptPage extends SDWizardPage {

    private static final String PARTICIPANT_FIRST_NAME_ID = "firstName";
    private static final String PARTICIPANT_SURNAME_ID = "surname";
    private static final String PARTICIPANT_ROLE_ID = "role";
    private static final String PARTICIPANT_EMAIL_ID = "_email";
    private static final String PARTICIPANT_NAME_ID = "name";
    private static final String ADD_PARTICIPANT_ID = "AddParticipantButtonId";
    private static final String SAVE_BUTTON_ID = "EditParticipantButtonId";
    private static final String SEARCH_PARTICIPANT_BOX_ID = "search_participant";

    public ParticipantsPromptPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }

    @Step("Set Participant Name")
    public ParticipantsPromptPage setParticipantName(String participantName) {
        insertValueToComponent(participantName, PARTICIPANT_FIRST_NAME_ID);
        return this;
    }

    @Step("Set Participant Name")
    public ParticipantsPromptPage setParticipantNameDTAG(String participantName) {
        insertValueToComponent(participantName, PARTICIPANT_NAME_ID);
        return this;
    }

    @Step("Set Participant Surname")
    public ParticipantsPromptPage setParticipantSurname(String participantSurname) {
        insertValueToComponent(participantSurname, PARTICIPANT_SURNAME_ID);
        return this;
    }

    @Step("Set Participant Role")
    public ParticipantsPromptPage setParticipantRole(String participantRole) {
        insertValueToComponent(participantRole, PARTICIPANT_ROLE_ID);
        return this;
    }

    @Step("Search for existing participant")
    public ParticipantsPromptPage searchParticipant(String participantName) {
        insertValueContainsToComponent(participantName, SEARCH_PARTICIPANT_BOX_ID);
        return this;
    }

    @Step("Set Participant Email")
    public ParticipantsPromptPage setParticipantEmail(String participantEmail) {
        insertValueContainsToComponent(participantEmail, PARTICIPANT_EMAIL_ID);
        return this;
    }

    @Step("Fill Participant prompt")
    public ParticipantsPromptPage fillParticipantPrompt(String name, String surname, String role) {
        setParticipantName(name);
        setParticipantSurname(surname);
        setParticipantRole(role);
        return this;
    }

    @Step("Click Add Participant button in prompt")
    public void clickAddParticipantInPrompt() {
        clickButton(ADD_PARTICIPANT_ID);
    }

    @Step("Click Save button in prompt")
    public void clickSaveEditedParticipant() {
        clickButton(SAVE_BUTTON_ID);
    }
}