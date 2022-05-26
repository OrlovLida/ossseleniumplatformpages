package com.oss.pages.servicedesk.issue.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProblemSolutionTab extends NotesTab {

    private static final String PROBLEM_SOLUTION_COMPONENT_ID = "_problemSolutionWidget";

    public ProblemSolutionTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    public String getTextFieldId() {
        return PROBLEM_SOLUTION_COMPONENT_ID;
    }
}
