package com.oss.reconciliation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.google.common.collect.ImmutableList;
import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.reconciliation.ManualMatchingPage;

public class ManualMatchingTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(ManualMatchingTest.class);

    private ManualMatchingPage manualMatchingPage;
    private SoftAssert softAssert;

    private static final List<String> columnsHeadersAssertionList = new ImmutableList.Builder<String>()
            .add("Network Type")
            .add("Network Name")
            .add("Inventory Type")
            .add("Inventory Name")
            .add("Inventory ID")
            .add("User")
            .build();

    @BeforeClass
    public void openConsole() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        softAssert = new SoftAssert();
    }

    @Test(priority = 1)
    public void assertColumnsHeaders() {
        ManualMatchingPage manualMatchingPage = ManualMatchingPage.goToManualMatchingPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        List<String> attributes = manualMatchingPage.getColumnHeaders();
        Assert.assertEquals(attributes.size(), columnsHeadersAssertionList.size());
        for (int i = 0; i< attributes.size(); i++) {
            log.info("Checking attribute with index: " + i + ", which equals: '" + columnsHeadersAssertionList.get(i) + "' on declared assertionList, and equals '" + attributes.get(i) + "' on properties list taken from GUI");
            softAssert.assertEquals(attributes.get(i), columnsHeadersAssertionList.get(i));
        }
        softAssert.assertAll();
    }

//    @Test(priority = 2)
//    public void selectFirstRow() {
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        manualMatchingPage.searchByNetworkName("poznan_b");
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        manualMatchingPage.selectFirstRow();
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        manualMatchingPage.editMatching();
//    }
//
//    @Test(priority = 3)
//    public void createMatching() {
//        manualMatchingPage.clickCreate();
//    }
//


}
