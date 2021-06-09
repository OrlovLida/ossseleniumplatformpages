package com.oss.bigdata.dfe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.externalresource.ExternalResourcesPage;
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
public class ExternalResourcesTest extends BaseTestCase {

    private final static String EXTERNAL_RESOURCE_TYPE = "Database";
    private final static String CONNECTION_URL = "jdbc:";
    private final static Logger log = LoggerFactory.getLogger(ExternalResourcesTest.class);

    private ExternalResourcesPage externalResource;
    private String externalResourceName;
    private String updatedExternalResourceName;

    @BeforeClass
    public void goToExternalResourceView() {
        externalResource = ExternalResourcesPage.goToPage(driver, BASIC_URL);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String date = simpleDateFormat.format(new Date());
        externalResourceName = "Selenium_" + date + "_ExtResourceTest";
        updatedExternalResourceName = externalResourceName + "_updated";
    }

    @Test(priority = 1, testName = "Add new External Resource", description = "Add new External Resource")
    @Description("Add new External Resource")
    public void addExternalResource() {
        externalResource.clickAddNewExternalResource();
        externalResource.getExternalResourcePopup().fillExternalResourcesPopup(externalResourceName, EXTERNAL_RESOURCE_TYPE, CONNECTION_URL);
        externalResource.getExternalResourcePopup().clickSave();
        Boolean externalResourceIsCreated = externalResource.externalResourceExistsIntoTable(externalResourceName);

        Assert.assertTrue(externalResourceIsCreated);
    }

    @Test(priority = 2, testName = "Edit External Resource", description = "Edit External Resource", enabled = false)
    @Description("Edit External Resource")
    public void editExternalResource() {
        Boolean externalResourceExists = externalResource.externalResourceExistsIntoTable(externalResourceName);
        if (externalResourceExists) {
            externalResource.selectFoundExternalResource();
            externalResource.clickEditExternalResource();
            externalResource.getExternalResourcePopup().fillName(updatedExternalResourceName);
            externalResource.getExternalResourcePopup().clickSave();
            Boolean externalResourceIsCreated = externalResource.externalResourceExistsIntoTable(updatedExternalResourceName);

            Assert.assertTrue(externalResourceIsCreated);

        } else {
            log.error("External Resource with name: {} doesn't exist", externalResourceName);
            Assert.fail();
        }
    }

    @Test(priority = 3, testName = "Delete External Resource", description = "Delete External Resource")
    @Description("Delete External Resource")
    public void deleteExternalResource() {
        Boolean externalResourceExists = externalResource.externalResourceExistsIntoTable(externalResourceName);
        if (externalResourceExists) {
            externalResource.selectFoundExternalResource();
            externalResource.clickDeleteExternalResource();
            externalResource.confirmDelete();
            Boolean externalResourceDeleted = !externalResource.externalResourceExistsIntoTable(externalResourceName);

            Assert.assertTrue(externalResourceDeleted);
        } else {
            log.error("External Resource with name: {} was not deleted", externalResourceName);
            Assert.fail();
        }
    }
}
