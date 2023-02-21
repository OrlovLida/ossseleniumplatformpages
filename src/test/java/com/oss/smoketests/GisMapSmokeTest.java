package com.oss.smoketests;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.GlobalNotificationContainer;
import com.oss.framework.components.layout.ErrorCard;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.gisview.GisViewPage;
import com.oss.pages.platform.HomePage;

import io.qameta.allure.Description;

public class GisMapSmokeTest extends BaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(GisMapSmokeTest.class);
    private static final String PATH_NAME = "gismap";
    private static final String FILE_NAME = "smoketest.png";
    private static final String MAP_NAME = "Here Terrain Map";
    private static final String RED_COLOR_LOG_PATTERN = "Red color value is %s.";
    private static final String GREEN_COLOR_LOG_PATTERN = "Green color value is %s.";
    private static final String BLUE_COLOR_LOG_PATTERN = "Blue color value is %s.";
    private static final String CANVAS_LENGTH_EXCEPTION_PATTERN = "Canvas object length is %s. Expected to be at least 2000000.";
    private static final String CANVAS_PRESENT_EXCEPTION = "Canvas object does not exist.";
    private static final String GENERATE_IMAGE_EXCEPTION = "Problem generationg the map image.";
    private static final String MAP_DIMENSIONS_PATTERN = "Map dimensions - Height: %s, Width: %s.";
    private static final int BLACK_COLOR = 16777216;
    private static String FILE_PATH;

    @Test(priority = 1, description = "Open GIS View")
    @Description("Open GIS View")
    public void openGisView() {
        waitForPageToLoad();
        checkErrorPage();
        checkGlobalNotificationContainer();
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu("GIS View", "Resource Inventory");
        waitForPageToLoad();
    }

    @Test(priority = 2, description = "Check if Canvas object is present", dependsOnMethods = {"openGisView"})
    @Description("Check if Canvas object is present")
    public void isCanvasObjectPresent() {
        GisViewPage gisViewPage = GisViewPage.getGisViewPage(driver, webDriverWait);
        checkErrorPage();
        checkGlobalNotificationContainer();
        gisViewPage.setMap(MAP_NAME);
        waitForPageToLoad();
        Assert.assertTrue(gisViewPage.isCanvasPresent(), CANVAS_PRESENT_EXCEPTION);
    }

    @Test(priority = 3, description = "Check Canvas object bytes size", dependsOnMethods = {"isCanvasObjectPresent"})
    @Description("Check Canvas object bytes size")
    public void checkCanvasObjectSize() {
        String canvasObject = GisViewPage.getGisViewPage(driver, webDriverWait).getCanvasObject();
        Assert.assertTrue(canvasObject.length() > 2000000, CANVAS_LENGTH_EXCEPTION_PATTERN);
        Assert.assertTrue(generateImage(canvasObject), GENERATE_IMAGE_EXCEPTION);
    }

    @Test(priority = 4, description = "Check the colors of Canvas object", dependsOnMethods = {"checkCanvasObjectSize"})
    @Description("Check the colors of Canvas object")
    public void isCanvasObjectColors() throws IOException {
        BufferedImage img = ImageIO.read(new File(FILE_PATH));
        SoftAssert softAssert = new SoftAssert();
        int height = img.getHeight();
        int width = img.getWidth();
        LOGGER.debug(String.format(MAP_DIMENSIONS_PATTERN, height, width));
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color c = new Color(img.getRGB(i, j));
                if (c.getRGB() != BLACK_COLOR) {
                    int red = c.getRed();
                    int green = c.getGreen();
                    int blue = c.getBlue();
                    softAssert.assertNotEquals(red, 0, String.format(RED_COLOR_LOG_PATTERN, red));
                    softAssert.assertNotEquals(green, 0, String.format(GREEN_COLOR_LOG_PATTERN, green));
                    softAssert.assertNotEquals(blue, 0, String.format(BLUE_COLOR_LOG_PATTERN, blue));
                }
            }
        }
        softAssert.assertAll();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
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
            OutputStream out = Files.newOutputStream(Paths.get(FILE_PATH));
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
}

