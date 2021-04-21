package com.oss.pages.transport.ipam;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.*;

public class AssignIPSubnetWizardPage extends BasePage {
    private static final String ASSIGNMENT_TYPE_ATTRIBUTE_ID = "createAssignmentTypeComponentId";
    private static final String ASSIGNMENT_ATTRIBUTE_ID = "createSubnetAssignmentAdvancedSearchComponentId";
    private static final String ROLE_ATTRIBUTE_ID = "createRoleComponentId";

    public AssignIPSubnetWizardPage(WebDriver driver) {
        super(driver);
    }

    public void assignIPSubnet(String assignmentType, String assignmentName, String role){
        Wizard assignIPSubnet = createWizard();
        assignIPSubnet.setComponentValue(ASSIGNMENT_TYPE_ATTRIBUTE_ID, assignmentType, COMBOBOX);
        Input searchField = assignIPSubnet.getComponent(ASSIGNMENT_ATTRIBUTE_ID, SEARCH_FIELD);
        searchField.setValueContains(Data.createFindFirst(assignmentName));
        assignIPSubnet.setComponentValue(ROLE_ATTRIBUTE_ID, role, COMBOBOX);
        assignIPSubnet.clickAccept();
    }

    private Wizard createWizard(){
        return Wizard.createPopupWizard(driver, wait);
    }
}
