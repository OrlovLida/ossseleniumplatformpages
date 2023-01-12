package com.oss.smoketests;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.GlobalNotificationContainer;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.layout.ErrorCard;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.untils.Environment;

import io.qameta.allure.Description;

public class CscvSmokeTest extends BaseTestCase {

    private static final String PATH_NAME = "gismap";
    private static final String FILE_NAME = "smoketestCSCV.png";
    private static final String MAP_NAME = "Here Terrain Map";
    private static final String RED_COLOR_LOG_PATTERN = "Red color value is %s.";
    private static final String GREEN_COLOR_LOG_PATTERN = "Green color value is %s.";
    private static final String BLUE_COLOR_LOG_PATTERN = "Blue color value is %s.";
    private static final String NO_SITE_EXCEPTION = "There is no physical location with type = Site available on the environment.";
    private static final String SITE = "Site";
    private static final String NAME = "Name";
    private static final String TYPE = "Type";
    private static final String IDENTIFIER = "Identifier";
    private static final String LIFECYCLE_STATE = "Lifecycle State";
    private static final String DESCRIPTION = "Description";
    private static final String REMARKS = "Remarks";
    private static final String LONGITUDE = "Longitude";
    private static final String LATITUDE = "Latitude";
    private static final String COORDINATE_X = "Coordinate X";
    private static final String COORDINATE_Y = "Coordinate Y";
    private static final String CITY = "City";
    private static final String COUNTRY = "Country";
    private static final String REGION = "Region";
    private static final String STREET = "Street";
    private static final String STREET_NAME = "Street Number";
    private static final String POSTAL_CODE = "Postal Code";
    private static final String PROPERTY_PANEL_ID = "propertiesTabbApp";
    private static String FILE_PATH;
    private static final Logger LOGGER = LoggerFactory.getLogger(CscvSmokeTest.class);
    private final Environment env = Environment.getInstance();

    @Test(priority = 1, description = "Open Cell Site Configuration View")
    @Description("Open Cell Site Configuration View")
    public void openCSCV() {
        waitForPageToLoad();
        driver.get(String.format("%s/#/view/radio/cellsite/xid?perspective=LIVE&ids=%s", BASIC_URL, getLocationsIds()));
        waitForPageToLoad();
        checkErrorPage();
        checkGlobalNotificationContainer();
    }

    @Test(priority = 2, description = "Check context actions", dependsOnMethods = {"openCSCV"})
    @Description("Check context actions")
    public void checkContextActions() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.expandTreeToSite();
        waitForPageToLoad();
        Assert.assertEquals(cellSiteConfigurationPage.getTree().getGroupActionLabel(ActionsContainer.SHOW_ON_GROUP_ID), "Show on");
        waitForPageToLoad();
        Assert.assertTrue(cellSiteConfigurationPage.getTabTable().getGroupActionLabel("add").isEmpty());
    }

    @Test(priority = 3, description = "Check property panel", dependsOnMethods = {"checkContextActions"})
    @Description("Check property panel")
    public void checkPropertyPanel() {
        OldPropertyPanel oldPropertyPanel = OldPropertyPanel.createById(driver, webDriverWait, PROPERTY_PANEL_ID);
        Assert.assertEquals(oldPropertyPanel.getPropertyValue(TYPE), SITE);
        Assert.assertEquals(oldPropertyPanel.getVisibleAttributes(), defaultPropertiesList);
    }

    @Test(priority = 4, description = "Check if Canvas object is present", dependsOnMethods = {"openCSCV"})
    @Description("Check if Canvas object is present")
    public void isCanvasObjectPresent() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        checkErrorPage();
        checkGlobalNotificationContainer();
        cellSiteConfigurationPage.setMap(MAP_NAME);
        waitForPageToLoad();
        Assert.assertTrue(cellSiteConfigurationPage.isCanvasPresent());
    }

    @Test(priority = 5, description = "Check Canvas object bytes size", dependsOnMethods = {"isCanvasObjectPresent"})
    @Description("Check Canvas object bytes size")
    public void checkCanvasObjectSize() {
        String canvasObject = new CellSiteConfigurationPage(driver).getCanvasObject();
        Assert.assertTrue(canvasObject.length() > 150000);
        Assert.assertTrue(generateImage(canvasObject));
    }

    @Test(priority = 6, description = "Check the colors of Canvas object", dependsOnMethods = {"checkCanvasObjectSize"})
    @Description("Check the colors of Canvas object")
    public void isCanvasObjectColors() throws IOException {
        BufferedImage img = ImageIO.read(new File(FILE_PATH));
        int height = img.getHeight();
        int width = img.getWidth();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color c = new Color(img.getRGB(i, j));
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();
                Assert.assertNotEquals(red, 0, String.format(RED_COLOR_LOG_PATTERN, red));
                Assert.assertNotEquals(green, 0, String.format(GREEN_COLOR_LOG_PATTERN, green));
                Assert.assertNotEquals(blue, 0, String.format(BLUE_COLOR_LOG_PATTERN, blue));
            }
        }
    }

    private boolean generateImage(String imgStr) {
        if (imgStr == null)
            return false;
        try {
            byte[] b = imgStr.getBytes(StandardCharsets.UTF_8);
            b = Base64.getMimeDecoder().decode(b);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            FILE_PATH = getFilePath() + "/" + FILE_NAME;
            OutputStream out = new FileOutputStream(FILE_PATH);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (IOException | URISyntaxException e) {
            return false;
        }
    }

    private String getFilePath() throws URISyntaxException {
        String filePath;
        URL res = getClass().getClassLoader().getResource(PATH_NAME);
        try {
            assert res != null;
            File file = Paths.get(res.toURI()).toFile();
            filePath = file.getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new URISyntaxException("Cant load file", e.getReason());
        }
        return filePath;
    }

    private void checkErrorPage() {
        ErrorCard errorCard = ErrorCard.create(driver, webDriverWait);
        if (errorCard.isErrorPagePresent()) {
            ErrorCard.ErrorInformation errorInformation = errorCard.getErrorInformation();
            LOGGER.error(errorInformation.getErrorText());
            LOGGER.error(errorInformation.getErrorDescription());
            LOGGER.error(errorInformation.getErrorMessage());
            Assert.fail("Error Page is shown.");
        }
    }

    private void checkGlobalNotificationContainer() {
        GlobalNotificationContainer globalNotificationContainer = GlobalNotificationContainer.create(driver, webDriverWait);
        if (globalNotificationContainer.isErrorNotificationPresent()) {
            GlobalNotificationContainer.NotificationInformation information = globalNotificationContainer.getNotificationInformation();
            LOGGER.error(information.getMessage());
            Assert.fail("Global Notification shows error.");
        }
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private int getLocationsIds() {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        return locationInventoryRepository.getFirstSite().stream().findFirst().orElseThrow(() -> new NoSuchElementException(NO_SITE_EXCEPTION));
    }

    private static final List<String> defaultPropertiesList = new ImmutableList.Builder<String>()
            .add(NAME)
            .add(TYPE)
            .add(IDENTIFIER)
            .add(LIFECYCLE_STATE)
            .add(DESCRIPTION)
            .add(REMARKS)
            .add(LONGITUDE)
            .add(LATITUDE)
            .add(COORDINATE_X)
            .add(COORDINATE_Y)
            .add(CITY)
            .add(COUNTRY)
            .add(REGION)
            .add(STREET)
            .add(STREET_NAME)
            .add(POSTAL_CODE)
            .build();
}
