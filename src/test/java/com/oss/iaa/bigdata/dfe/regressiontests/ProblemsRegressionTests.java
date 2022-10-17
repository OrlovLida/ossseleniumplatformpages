package com.oss.iaa.bigdata.dfe.regressiontests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.dfe.problems.ProblemsPage;

import io.qameta.allure.Description;

public class ProblemsRegressionTests extends BaseTestCase {

    private static final String PROBLEMS_VIEW_TITLE = "Problems Configuration";
    private ProblemsPage problemsPage;

    @BeforeMethod
    public void openPage() {
        problemsPage = ProblemsPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Opening Problems View", description = "Opening Problems View")
    @Description("Opening problems View")
    public void openProblemsViewTest() {
        Assert.assertEquals(problemsPage.getViewTitle(), PROBLEMS_VIEW_TITLE);
        Assert.assertFalse(problemsPage.isTableEmpty());
    }

    @Test(priority = 2, testName = "Display Problems", description = "Displaying List of Problems")
    @Description("Displaying List of Problems")
    public void displayProblemsTest() {
        Assert.assertFalse(problemsPage.isTableEmpty());
    }

    @Parameters({"problemName"})
    @Test(priority = 3, testName = "Display Thresholds", description = "Display List of Thresholds for selected Problem")
    @Description("Display List of Thresholds for selected Problem")
    public void displayThresholdsTest(
            @Optional("t:SMOKE#Problem") String problemName
    ) {
        boolean isProblemInTable = problemsPage.problemExistsIntoTable(problemName);
        if (isProblemInTable) {
            problemsPage.selectFoundProblem();
            Assert.assertFalse(problemsPage.isThresholdsTableEmpty());
        } else {
            Assert.fail("Problem with name: " + problemName + " is not in the Problems Table, select existing problem");
        }
    }

    @Parameters({"problemName"})
    @Test(priority = 4, testName = "Check Problem Identifier", description = "Check Problem Identifier")
    @Description("Check Problem Identifier")
    public void checkProblemId(
            @Optional("t:SMOKE#Problem") String problemName
    ) {
        boolean isProblemInTable = problemsPage.problemExistsIntoTable(problemName);
        if (isProblemInTable) {
            problemsPage.selectFoundProblem();
            String problemId = problemsPage.getProblemIdentifier();
            problemsPage.clearSearchField();
            problemsPage.searchForProblem(problemId);
            Assert.assertEquals(problemsPage.getProblemIdentifier(), problemId);
        } else {
            Assert.fail("Problem with name: " + problemName + " is not in the Problems Table, select existing problem");
        }
    }
}
