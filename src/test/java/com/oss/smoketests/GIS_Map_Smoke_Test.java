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

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.layout.ErrorCard;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.gisview.GisViewPage;
import com.oss.pages.platform.HomePage;

import io.qameta.allure.Description;

public class GIS_Map_Smoke_Test extends BaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(GIS_Map_Smoke_Test.class);
    private static final String PATH_NAME = "gismap";
    private static final String FILE_NAME = "smoketest.png";
    private static final String OPEN_STREET_MAP = "Open Street Map";
    private static final String RED_COLOR_LOG_PATTERN = "Red color value is %s.";
    private static final String GREEN_COLOR_LOG_PATTERN = "Green color value is %s.";
    private static final String BLUE_COLOR_LOG_PATTERN = "Blue color value is %s.";
    private static String FILE_PATH;

    @Test(priority = 1, description = "Open GIS View")
    @Description("Open GIS View")
    public void openGisView() {
        waitForPageToLoad();
        checkErrorPage();
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu("GIS View", "Resource Inventory");
        waitForPageToLoad();
    }

    @Test(priority = 2, description = "Check if Canvas object is present", dependsOnMethods = {"openGisView"})
    @Description("Check if Canvas object is present")
    public void isCanvasObjectPresent() {
        GisViewPage gisViewPage = GisViewPage.getGisViewPage(driver, webDriverWait);
        checkErrorPage();
        gisViewPage.setMap(OPEN_STREET_MAP);
        waitForPageToLoad();
        Assert.assertTrue(gisViewPage.isCanvasPresent());
    }

    @Test(priority = 3, description = "Check Canvas object bytes size", dependsOnMethods = {"isCanvasObjectPresent"})
    @Description("Check Canvas object bytes size")
    public void checkCanvasObjectSize() {
        String canvasObject = GisViewPage.getGisViewPage(driver, webDriverWait).getCanvasObject();
        Assert.assertTrue(canvasObject.length() > 80000);
        Assert.assertTrue(generateImage(canvasObject));
    }

    @Test(priority = 4, description = "Check the colors of Canvas object", dependsOnMethods = {"checkCanvasObjectSize"})
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
}

