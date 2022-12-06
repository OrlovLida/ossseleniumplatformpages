package com.oss.pages.bpm.processinstances.creation;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;

import io.qameta.allure.Step;

public class ProcessRolesStepWizardPage extends ProcessWizardPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessRolesStepWizardPage.class);
    private static final String NO_ROLE_EXCEPTION = "No Role on list.";
    public static final String NO_ELEMENT_WITH_ID = "No element with id = {}";
    public static final String CHANGING_PLANNER_AND_PROCESS_ROLE_ID_FROM_STRINGS_TO_HASH_MULTIMAP = "Changing planner and processRoleId from Strings to HashMultimap";
    public static final String ADDING_PLANNERS = "Adding planners";
    private final Wizard wizard = Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2);

    public ProcessRolesStepWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Adding planner {planner} to {processRoleId}")
    public ProcessRolesStepWizardPage addPlanner(String processRoleId, String planner) {
        LOGGER.info(CHANGING_PLANNER_AND_PROCESS_ROLE_ID_FROM_STRINGS_TO_HASH_MULTIMAP);
        Multimap<String, String> processRoles = HashMultimap.create(1, 1);
        processRoles.put(processRoleId, planner);
        return addPlanners(processRoles);
    }

    @Step("Adding planners {processRoles}")
    public ProcessRolesStepWizardPage addPlanners(Multimap<String, String> processRoles) {
        Set<String> processRoleIds = processRoles.keySet();
        Multimap<Input, String> inputsAndAssociatedRoles = HashMultimap.create();
        for (String processRoleId : processRoleIds) {
            Input component = getProcessRoleInput(processRoleId);
            HashSet<String> tempPlanners = new HashSet<>(processRoles.get(processRoleId));
            HashSet<String> stringValues = new HashSet<>(component.getStringValues());
            tempPlanners.removeAll(stringValues);
            inputsAndAssociatedRoles.putAll(component, tempPlanners);
        }
        LOGGER.info(ADDING_PLANNERS);
        return changePlannersState(inputsAndAssociatedRoles);
    }

    @Step("Deleting planner {planner} from {processRoleId}")
    public ProcessRolesStepWizardPage deletePlanner(String processRoleId, String planner) {
        LOGGER.info(CHANGING_PLANNER_AND_PROCESS_ROLE_ID_FROM_STRINGS_TO_HASH_MULTIMAP);
        Multimap<String, String> processRoles = HashMultimap.create(1, 1);
        processRoles.put(processRoleId, planner);
        return deletePlanners(processRoles);
    }

    @Step("Deleting planners {processRoles}")
    public ProcessRolesStepWizardPage deletePlanners(Multimap<String, String> processRoles) {
        Set<String> processRoleIds = processRoles.keySet();
        Multimap<Input, String> inputsAndAssociatedRoles = HashMultimap.create();
        for (String processRoleId : processRoleIds) {
            Input component = getProcessRoleInput(processRoleId);
            HashSet<String> stringValues = new HashSet<>(wizard.getComponent(processRoleId).getStringValues());
            if (stringValues.containsAll(processRoles.get(processRoleId))) {
                inputsAndAssociatedRoles.putAll(component, processRoles.get(processRoleId));
            } else {
                throw new NoSuchElementException(NO_ROLE_EXCEPTION);
            }
        }
        return changePlannersState(inputsAndAssociatedRoles);
    }

    private Input getProcessRoleInput(String processRoleId) {
        Input component;
        try {
            component = wizard.getComponent(processRoleId);
        } catch (NoSuchElementException e) {
            LOGGER.error(NO_ELEMENT_WITH_ID, processRoleId);
            throw e;
        }
        return component;
    }

    @Step("Changing planners {inputsRoles}")
    private ProcessRolesStepWizardPage changePlannersState(Multimap<Input, String> inputsRoles) {
        inputsRoles.asMap().forEach((input, valueCollection) -> valueCollection.forEach(input::setSingleStringValue));
        return new ProcessRolesStepWizardPage(driver);
    }
}