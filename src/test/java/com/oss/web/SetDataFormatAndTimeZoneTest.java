package com.oss.web;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.comarch.oss.web.pages.NewInventoryViewPage;
import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;

/**
 * @author Faustyna Szczepanik
 */
public class SetDataFormatAndTimeZoneTest extends BaseTestCase {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT1 = "MM/dd/yyyy hh:mm:ss a";
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
    private static final String AUSTRALIA_SYDNEY_ZONE = "Australia/Sydney";
    private static final String EUROPE_PARIS_ZONE = "Europe/Paris";
    private static final String TIME_ZONE_EXCEPTION = "Value of Data (Created) attribute is incorrect. Proper value is ";
    private static final String DATA_TIME_FORMAT_EXCEPTION = "Data Time Format is incorrect. Proper format should be";
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
        if (!propertyPanel.getVisibleAttributes().contains(DATE_CREATED_ID)) {
            propertyPanel.enableAttributeByLabel(DATE_CREATED_LABEL, AUDIT_INFORMATION_LABEL, CREATED_LABEL);
        }
        String dateCreatedAfterChangeFormatValue = propertyPanel.getPropertyValue(DATE_CREATED_ID);
        Assert.assertTrue(checkDateFormat(DFT1, dateCreatedAfterChangeFormatValue), DATA_TIME_FORMAT_EXCEPTION + DFT1);
    }

    @Test(priority = 2)
    public void checkIfDataFormatIsSavedForUser() {
        inventoryViewPage.changeUser(USER2, PASSWORD_2);
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, LOCATION_TYPE);
        String dateCreatedValue = getCreatedObjectDataTime();
        Assert.assertTrue(checkDateFormat(PRESENT_FORMAT, dateCreatedValue), DATA_TIME_FORMAT_EXCEPTION + DEFAULT_DATE_FORMAT);

        inventoryViewPage.changeUser(USER1, PASSWORD_1);
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, LOCATION_TYPE);
        String dateCreatedValueU1 = getCreatedObjectDataTime();
        Assert.assertTrue(checkDateFormat(DFT1, dateCreatedValueU1), DATA_TIME_FORMAT_EXCEPTION + DATE_FORMAT1);
    }

    @Test(priority = 3)
    public void changeTimeZone() {
        String dateCreatedValueInDefaultTimeZone = getCreatedObjectDataTime();
        LocalDateTime dateCreatedValue = LocalDateTime.parse(dateCreatedValueInDefaultTimeZone, DFT1);
        LocalDateTime shiftedDateTimeAsiaTokio = getAustraliaDataTime(dateCreatedValue);
        homePage.disableAutoTimeZone();
        homePage.chooseTimeZone(AUSTRALIA_SYDNEY_ZONE);
        String dateCreatedAfterChangeZone = getCreatedObjectDataTime();
        LocalDateTime dateCreatedValueAfterChangeZone = LocalDateTime.parse(dateCreatedAfterChangeZone, DFT1);
        Assert.assertEquals(dateCreatedValueAfterChangeZone, shiftedDateTimeAsiaTokio, TIME_ZONE_EXCEPTION + shiftedDateTimeAsiaTokio);

        homePage.enableAutoTimeZone();
        String dateCreatedAfterEnableAutoTimeZone = getCreatedObjectDataTime();
        Assert.assertEquals(dateCreatedValueInDefaultTimeZone, dateCreatedAfterEnableAutoTimeZone);
    }

    private static LocalDateTime getAustraliaDataTime(LocalDateTime dateCreatedValue) {
        ZoneId zoneId = ZoneId.of(AUSTRALIA_SYDNEY_ZONE);
        ZonedDateTime europe = ZonedDateTime.of(dateCreatedValue, ZoneId.of(EUROPE_PARIS_ZONE));
        ZonedDateTime australia = europe.withZoneSameInstant(zoneId);
        return australia.toLocalDateTime();

    }

    private String getCreatedObjectDataTime() {
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(ROW_ID, PROPERTY_PANEL_ID);
        return propertyPanel.getPropertyValue(DATE_CREATED_ID);
    }

    private boolean checkDateFormat(DateTimeFormatter dft, String data) {
        try {
            dft.parse(data);
            return true;
        } catch (Exception ex) {
            return false;
        }

    }
}




