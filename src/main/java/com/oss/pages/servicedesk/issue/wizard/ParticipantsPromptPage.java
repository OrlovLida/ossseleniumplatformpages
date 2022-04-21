package com.oss.pages.servicedesk.issue.wizard;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.qameta.allure.Step;

public class ParticipantsPromptPage extends SDWizardPage {

    private static final String PARTICIPANT_FIRST_NAME_ID = "firstName";
    private static final String PARTICIPANT_SURNAME_ID = "surname";
    private static final String PARTICIPANT_ROLE_ID = "role";
    private static final String ADD_PARTICIPANT_ID = "_searchParticipant_buttons-2";
    private static final String ADD_PARTICIPANT_PROMPT_ID = "_createParticipantModal_prompt-card";

    public ParticipantsPromptPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait, ADD_PARTICIPANT_PROMPT_ID);
    }

    @Step("Set Participant Name")
    public void setParticipantName(String participantName) {
        insertValueToTextComponent(participantName, PARTICIPANT_FIRST_NAME_ID);
    }

    @Step("Set Participant Surname")
    public void setParticipantSurname(String participantSurname) {
        insertValueToTextComponent(participantSurname, PARTICIPANT_SURNAME_ID);
    }

    @Step("Set Participant Role")
    public void setParticipantRole(String participantRole) {
        insertValueToComboBoxComponent(participantRole, PARTICIPANT_ROLE_ID);
    }

    @Step("Click Add Participant button in prompt")
    public void clickAddParticipant() {
        clickButton(ADD_PARTICIPANT_ID);
    }
}
