package com.oss.E2E;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.gisView.CreateDuctWizardPage;
import com.oss.pages.gisView.GisViewPage;
import com.oss.pages.physical.LocationWizardPage;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class OSPConfiguration extends BaseTestCase {
    private GisViewPage gisViewPage;
    private String BUILDING_A_NAME = "Building A";
    private String GEOGRAPHICAL_ADDRESS_BUILDING = " Jasna 1, Gliwice";
    private String GEOGRAPHICAL_ADDRESS_MANHOLE = " Jasna 34848, Gliwice";
    private String MANHOLE_MODEL = "SK-1";
    private String MANHOLE_A_NAME = "Manhole A";
    private String DUCT_MODEL = "DVK 50";
    private String DUCT_TYPE_PRIMARY = "Primary Duct";


    @BeforeClass
    public void openGisViewByLink() {
        gisViewPage = GisViewPage.goToGisViewPage(driver, BASIC_URL);
    }

    @Test(priority = 1)
    public void createBuilding() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.searchFirstResult("gliwice");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(2, 4);
        gisViewPage.useContextAction("Draw Single Location", "Building");
        gisViewPage.clickOnMapByCoordinates(4, 2);

        LocationWizardPage locationWizardPage = new LocationWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.setLocationName(BUILDING_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.clickNext();

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.setGeographicalAddress(GEOGRAPHICAL_ADDRESS_BUILDING);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        locationWizardPage.setNumberOfLocations("2");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.accept();
        checkPopup();
    }

    @Test(priority = 2)
    public void modifyBuilding() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseObjectFromList("[Buildings] Building A_002", 4, 2);

        gisViewPage.useContextAction("Edit", "Edit Location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        LocationWizardPage locationWizardPage = new LocationWizardPage(driver);
        locationWizardPage.setLocationName("Building C");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.setStreetNumber("10");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.accept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionById("Move Location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.dragAndDropObject(4, 2, 4, 8);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionById("Save Edited Shape");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
    }


    @Test(priority = 3)
    public void createManhole() {
        gisViewPage.clickOnMapByCoordinates(2, 4);
        gisViewPage.useContextAction("Draw Single Location", "Manhole");
        gisViewPage.clickOnMapByCoordinates(5, 3);
        LocationWizardPage locationWizardPage = new LocationWizardPage(driver);
        locationWizardPage.setModel(MANHOLE_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.setLocationName(MANHOLE_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        locationWizardPage.setGeographicalAddress(GEOGRAPHICAL_ADDRESS_MANHOLE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.accept();
        checkPopup();
    }

    @Test(priority = 4)
    public void createDuctFromBuildingAToManhole() {
        gisViewPage.clickOnMapByCoordinates(2, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionById("Draw New Duct");
        gisViewPage.clickOnMapByCoordinates(4, 2);
        gisViewPage.clickOnMapByCoordinates(5, 3);
        gisViewPage.doubleClickOnMapByCoordinates(5, 3);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CreateDuctWizardPage createDuctWizardPage = new CreateDuctWizardPage(driver);
        createDuctWizardPage.setModel(DUCT_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createDuctWizardPage.setType(DUCT_TYPE_PRIMARY);
        createDuctWizardPage.create();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
    }

    @Test(priority = 5)
    public void createDuctFromBuildingCToManhole() {
        gisViewPage.clickOnMapByCoordinates(2, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionById("Draw New Duct");
        gisViewPage.clickOnMapByCoordinates(4, 8);
        gisViewPage.clickOnMapByCoordinates(5, 3);
        gisViewPage.doubleClickOnMapByCoordinates(5, 3);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CreateDuctWizardPage createDuctWizardPage = new CreateDuctWizardPage(driver);
        createDuctWizardPage.setModel(DUCT_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createDuctWizardPage.setType(DUCT_TYPE_PRIMARY);
        createDuctWizardPage.create();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.enableLayerInTree("Duct");
    }

    public void checkPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }
}
