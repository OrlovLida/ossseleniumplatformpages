package com.oss.bigdata.dfe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.problems.ProblemsPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

@Listeners({TestListener.class})
public class ProblemsViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(ProblemsViewTest.class);
    private static final String PROBLEM_DESCRIPTION = "Problem Selenium Test";

    private ProblemsPage problemsPage;
    private String problemName;
    private String updatedProblemName;

    @BeforeClass
    public void goToProblemsView() {
        problemsPage = ProblemsPage.goToPage(driver, BASIC_URL);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String date = simpleDateFormat.format(new Date());
        problemName = "Selenium_" + date + "_ProbTest";
        updatedProblemName = problemName + "_updated";
    }

    @Test(priority = 1, testName = "Add new Problem", description = "Add new Problem")
    @Description("Add new Problem")
    public void addProblem() {
        problemsPage.clickAddNewProblem();
        problemsPage.getProblemsPopup().fillProblemsPopup(problemName, PROBLEM_DESCRIPTION);
        problemsPage.getProblemsPopup().clickSave();
        Boolean problemIsCreated = problemsPage.problemExistsIntoTable(problemName);

        if (!problemIsCreated) {
            log.info("Cannot find created problem");
        }
        Assert.assertTrue(problemIsCreated);
    }

    @Test(priority = 2, testName = "Edit Problem", description = "Edit Problem")
    @Description("Edit Problem")
    public void editProblem() {
        Boolean problemExists = problemsPage.problemExistsIntoTable(problemName);
        if (problemExists) {
            problemsPage.selectFoundProblem();
            problemsPage.clickEditProblem();
            problemsPage.getProblemsPopup().fillName(updatedProblemName);
            problemsPage.getProblemsPopup().clickSave();
            Boolean dictionaryIsCreated = problemsPage.problemExistsIntoTable(updatedProblemName);

            Assert.assertTrue(dictionaryIsCreated);

        } else {
            log.error("Problem with name: {} doesn't exist", updatedProblemName);
            Assert.fail();
        }
    }

    @Test(priority = 3, testName = "Delete Problem", description = "Delete Problem")
    @Description("Delete Problem")
    public void deleteProblem() {
        Boolean problemExists = problemsPage.problemExistsIntoTable(updatedProblemName);
        if (problemExists) {
            problemsPage.selectFoundProblem();
            problemsPage.clickDeleteProblem();
            problemsPage.confirmDelete();
            Boolean problemDeleted = !problemsPage.problemExistsIntoTable(updatedProblemName);

            Assert.assertTrue(problemDeleted);
        } else {
            log.error("Problem with name: {} was not deleted", updatedProblemName);
            Assert.fail();
        }
    }

}

