package com.oss.web;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.pages.platform.NewInventoryViewPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Faustyna Szczepanik
 */
public class SetDataFormatAndTimeZoneTest extends BaseTestCase {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT1 = "MM/dd/yyyy hh:mm:ss a";
    private static final String TIME_ZONE_1 = "(UTC+10:00) Australia/Brisbane";
    private static final String LOCATION_TYPE = "Location";
    private final static String PROPERTY_PANEL_ID = "PropertyPanelWidget";
    private final static int ROW_ID = 0;
    private final static String DATE_CREATED_ID = "auditInformation.created.time";
    private final static String DATE_CREATED_LABEL = "Date";
    private final static String AUDIT_INFORMATION_LABEL = "Audit Information";
    private final static String CREATED_LABEL = "Created";
    private final static String USER2 = "webseleniumtests2";
    private static final String PASSWORD_2 = "oss";
    private final static String USER1 = "webseleniumtests";
    private static final String PASSWORD_1 = "Webtests123!";
    private NewInventoryViewPage inventoryViewPage;

    private static final DateTimeFormatter DFT1 = DateTimeFormatter.ofPattern(DATE_FORMAT1);
    private static final DateTimeFormatter PRESENT_FORMAT = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);

    @BeforeClass
    public void goToInventoryView() {
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, LOCATION_TYPE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    public void changeDataFormat() {

        homePage.chooseDataFormat(PRESENT_FORMAT, DFT1);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(ROW_ID, PROPERTY_PANEL_ID);
        propertyPanel.enableAttributeByLabel(DATE_CREATED_LABEL, AUDIT_INFORMATION_LABEL, CREATED_LABEL);
        String dateCreatedAfterChangeFormatValue = propertyPanel.getPropertyValue(DATE_CREATED_ID);
        Assert.assertTrue(checkDateFormat(DFT1, dateCreatedAfterChangeFormatValue));
    }

    @Test(priority = 2)
    public void checkIfDataFormatIsSavedForUser() {
        inventoryViewPage.changeUser(USER2, PASSWORD_2);
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, LOCATION_TYPE);
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(ROW_ID, PROPERTY_PANEL_ID);
        String dateCreatedValue = propertyPanel.getPropertyValue(DATE_CREATED_ID);
        Assert.assertTrue(checkDateFormat(PRESENT_FORMAT, dateCreatedValue));

        inventoryViewPage.changeUser(USER1, PASSWORD_1);
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, LOCATION_TYPE);
        PropertyPanel propertyPanel1 = inventoryViewPage.getPropertyPanel(ROW_ID, PROPERTY_PANEL_ID);
        String dateCreatedValueU1 = propertyPanel1.getPropertyValue(DATE_CREATED_ID);
        Assert.assertTrue(checkDateFormat(DFT1, dateCreatedValueU1));
    }

    @Test(priority = 3)
    public void changeTimeZone() {
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(ROW_ID, PROPERTY_PANEL_ID);
        String dateCreatedValueInDefaultTimeZone = propertyPanel.getPropertyValue(DATE_CREATED_ID);
        LocalDateTime dateCreatedValue = LocalDateTime.parse(dateCreatedValueInDefaultTimeZone, DFT1);
        LocalDateTime shiftedDateTimeAsiaTokio = dateCreatedValue.plusHours(8);
        homePage.disableAutoTimeZone();
        homePage.chooseTimeZone(TIME_ZONE_1);
        PropertyPanel propertyPanel1 = inventoryViewPage.getPropertyPanel(ROW_ID, PROPERTY_PANEL_ID);
        String dateCreatedAfterChangeZone = propertyPanel1.getPropertyValue(DATE_CREATED_ID);
        LocalDateTime dateCreatedValueAfterChangeZone = LocalDateTime.parse(dateCreatedAfterChangeZone, DFT1);
        Assert.assertEquals(shiftedDateTimeAsiaTokio, dateCreatedValueAfterChangeZone);

        homePage.enableAutoTimeZone();
        PropertyPanel propertyPanel2 = inventoryViewPage.getPropertyPanel(ROW_ID, PROPERTY_PANEL_ID);
        String dateCreatedAfterEnableAutoTimeZone = propertyPanel2.getPropertyValue(DATE_CREATED_ID);
        Assert.assertEquals(dateCreatedValueInDefaultTimeZone, dateCreatedAfterEnableAutoTimeZone);
    }

    private boolean checkDateFormat(DateTimeFormatter dft, String data) {
        try {
            dft.parse(data);
            return true;
        } catch (Exception ex) {
        }
        return false;
    }
}




