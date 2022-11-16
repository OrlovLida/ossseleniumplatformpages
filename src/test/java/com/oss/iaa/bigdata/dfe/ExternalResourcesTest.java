package com.oss.iaa.bigdata.dfe;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.dfe.externalresource.ExternalResourcesPage;
import com.oss.pages.iaa.bigdata.dfe.externalresource.ExternalResourcesPopupPage;
import com.oss.pages.iaa.bigdata.utils.ConstantsDfe;

import io.qameta.allure.Description;

public class ExternalResourcesTest extends BaseTestCase {

    private static final String EXTERNAL_RESOURCE_TYPE = "Database";
    private static final String CONNECTION_URL = "jdbc:";
    private static final String ADD_WIZARD_ID = "add-prompt-id_prompt-card";
    private static final String EDIT_WIZARD_ID = "edit-prompt-id_prompt-card";

    private ExternalResourcesPage externalResource;
    private String externalResourceName;
    private String updatedExternalResourceName;

    @BeforeClass
    public void goToExternalResourceView() {
        externalResource = ExternalResourcesPage.goToPage(driver, BASIC_URL);

        externalResourceName = ConstantsDfe.createName() + "_ExtResourceTest";
        updatedExternalResourceName = externalResourceName + "_updated";
    }

    @Test(priority = 1, testName = "Add new External Resource", description = "Add new External Resource")
    @Description("Add new External Resource")
    public void addExternalResource() {
        externalResource.clickAddNewExternalResource();
        ExternalResourcesPopupPage externalResourceWizard = new ExternalResourcesPopupPage(driver, webDriverWait, ADD_WIZARD_ID);
        externalResourceWizard.fillExternalResourcesPopup(externalResourceName, EXTERNAL_RESOURCE_TYPE, CONNECTION_URL);
        externalResourceWizard.clickSave();
        boolean externalResourceIsCreated = externalResource.externalResourceExistsIntoTable(externalResourceName);

        Assert.assertTrue(externalResourceIsCreated);
    }

    @Test(priority = 2, testName = "Edit External Resource", description = "Edit External Resource")
    @Description("Edit External Resource")
    public void editExternalResource() {
        boolean externalResourceExists = externalResource.externalResourceExistsIntoTable(externalResourceName);
        if (externalResourceExists) {
            externalResource.selectFirstExternalResourceInTable();
            externalResource.clickEditExternalResource();
            ExternalResourcesPopupPage externalResourceWizard = new ExternalResourcesPopupPage(driver, webDriverWait, EDIT_WIZARD_ID);
            externalResourceWizard.fillName(updatedExternalResourceName);
            externalResourceWizard.clickSave();
            boolean externalResourceIsCreated = externalResource.externalResourceExistsIntoTable(updatedExternalResourceName);

            Assert.assertTrue(externalResourceIsCreated);
        } else {
            Assert.fail(String.format("External Resource with name: %s doesn't exist", externalResourceName));
        }
    }

    @Test(priority = 3, testName = "Delete External Resource", description = "Delete External Resource")
    @Description("Delete External Resource")
    public void deleteExternalResource() {
        boolean externalResourceExists = externalResource.externalResourceExistsIntoTable(externalResourceName);
        if (externalResourceExists) {
            externalResource.selectFirstExternalResourceInTable();
            externalResource.clickDeleteExternalResource();
            externalResource.confirmDelete();
            boolean externalResourceDeleted = !externalResource.externalResourceExistsIntoTable(externalResourceName);

            Assert.assertTrue(externalResourceDeleted);
        } else {
            Assert.fail(String.format("External Resource with name: %s was not deleted", externalResourceName));
        }
    }
}
