package com.oss.pages.transport.ipam;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

public class AssignIPSubnetWizardPage extends BasePage {
    private static final String WIZARD_ID = "createSubnetAssignmentWebViewId_prompt-card";
    private static final String ASSIGNMENT_TYPE_ATTRIBUTE_ID = "createAssignmentTypeComponentId";
    private static final String ASSIGNMENT_ATTRIBUTE_ID = "createSubnetAssignmentAdvancedSearchComponentId";
    private static final String ROLE_ATTRIBUTE_ID = "createRoleComponentId";
    private static final String ASSIGN_SUBNET_SUBMIT_BUTTON = "wizard-submit-button-createSubnetAssignmentSimpleWizardId";

    public AssignIPSubnetWizardPage(WebDriver driver) {
        super(driver);
    }

    public void assignIPSubnet(String assignmentType, String assignmentName, String role) {
        Wizard assignIPSubnet = createWizard();
        assignIPSubnet.setComponentValue(ASSIGNMENT_TYPE_ATTRIBUTE_ID, assignmentType);
        Input searchField = assignIPSubnet.getComponent(ASSIGNMENT_ATTRIBUTE_ID);
        searchField.setValueContains(Data.createFindFirst(assignmentName));
        assignIPSubnet.setComponentValue(ROLE_ATTRIBUTE_ID, role);
        assignIPSubnet.clickButtonById(ASSIGN_SUBNET_SUBMIT_BUTTON);
    }

    private Wizard createWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }
}
