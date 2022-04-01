package com.oss.bigdata.dfe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.problems.ProblemsPage;
import com.oss.pages.bigdata.dfe.problems.ProblemsPopupPage;
import com.oss.pages.bigdata.utils.ConstantsDfe;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class ProblemsViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(ProblemsViewTest.class);
    private static final String PROBLEM_DESCRIPTION = "Problem Selenium Test";
    private static final String ADD_WIZARD = "add-prompt-id_prompt-card";
    private static final String EDIT_WIZARD = "edit-prompt-id_prompt-card";

    private ProblemsPage problemsPage;
    private String problemName;
    private String updatedProblemName;

    @BeforeClass
    public void goToProblemsView() {
        problemsPage = ProblemsPage.goToPage(driver, BASIC_URL);

        problemName = ConstantsDfe.createName() + "_ProbTest";
        updatedProblemName = problemName + "_updated";
    }

    @Test(priority = 1, testName = "Add new Problem", description = "Add new Problem")
    @Description("Add new Problem")
    public void addProblem() {
        problemsPage.clickAddNewProblem();
        ProblemsPopupPage problemsWizard = new ProblemsPopupPage(driver, webDriverWait, ADD_WIZARD);
        problemsWizard.fillProblemsPopup(problemName, PROBLEM_DESCRIPTION);
        problemsWizard.clickSave();
        boolean problemIsCreated = problemsPage.problemExistsIntoTable(problemName);

        if (!problemIsCreated) {
            log.info("Cannot find created problem");
        }
        Assert.assertTrue(problemIsCreated);
    }

    @Test(priority = 2, testName = "Edit Problem", description = "Edit Problem")
    @Description("Edit Problem")
    public void editProblem() {
        boolean problemExists = problemsPage.problemExistsIntoTable(problemName);
        if (problemExists) {
            problemsPage.selectFoundProblem();
            problemsPage.clickEditProblem();
            ProblemsPopupPage problemsWizard = new ProblemsPopupPage(driver, webDriverWait, EDIT_WIZARD);
            problemsWizard.fillName(updatedProblemName);
            problemsWizard.clickSave();
            boolean dictionaryIsCreated = problemsPage.problemExistsIntoTable(updatedProblemName);

            Assert.assertTrue(dictionaryIsCreated);
        } else {
            log.error("Problem with name: {} doesn't exist", updatedProblemName);
            Assert.fail();
        }
    }

    @Test(priority = 3, testName = "Delete Problem", description = "Delete Problem")
    @Description("Delete Problem")
    public void deleteProblem() {
        boolean problemExists = problemsPage.problemExistsIntoTable(updatedProblemName);
        if (problemExists) {
            problemsPage.selectFoundProblem();
            problemsPage.clickDeleteProblem();
            problemsPage.confirmDelete();
            boolean problemDeleted = !problemsPage.problemExistsIntoTable(updatedProblemName);

            Assert.assertTrue(problemDeleted);
        } else {
            log.error("Problem with name: {} was not deleted", updatedProblemName);
            Assert.fail();
        }
    }
}