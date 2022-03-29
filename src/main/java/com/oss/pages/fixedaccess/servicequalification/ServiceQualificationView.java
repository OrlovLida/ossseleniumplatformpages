package com.oss.pages.fixedaccess.servicequalification;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class ServiceQualificationView extends BasePage {

    private static final String SERVICE_QUALIFICATION_VIEW_TABLE_ID = "sqViewLeftWindowId";
    private static final String SERVICE_QUALIFICATION_PROP_PANEL_ID = "sqViewRightWindowId";
    private static final String SERVICE_CONNECTION_OPTION_COLUMN_LABEL = "Service Connection Options";
    private static final String DOWNLOAD_SPEED_COLUMN_LABEL = "Download Speed";
    private static final String UPLOAD_SPEED_COLUMN_LABEL = "Upload Speed";
    private static final String AVAILABILITY_COLUMN_LABEL = "Availability";
    private static final String DISTRIBUTION_AREA_COLUMN_LABEL = "Distribution Area";
    private static final String PRIMARY_TECH_PROPERTY_NAME = "Primary Technology";

    private final Map<String, Map<String, String>> allValuesInTable = new HashMap<>();

    public ServiceQualificationView(WebDriver driver) {
        super(driver);
    }

    @Step("Select particular row in table for column Service Connection Options with value {value}")
    public ServiceQualificationView selectParticularRowInTableForColumnServiceConnectionOptionsWithValue(String value) {
        int rowNumber = getOldTable().getRowNumber(value, SERVICE_CONNECTION_OPTION_COLUMN_LABEL);
        getOldTable().selectRow(rowNumber);
        return this;
    }

    @Step("Get Download Speed for {technologyName} from table")
    public String getDownloadSpeedFromTable(String technologyName) {
        collectValuesForFirstColumnAndAnotherOne(DOWNLOAD_SPEED_COLUMN_LABEL);
        return allValuesInTable.get(DOWNLOAD_SPEED_COLUMN_LABEL).get(technologyName);
    }

    @Step("Get Upload Speed for {technologyName} from table")
    public String getUploadSpeedFromTable(String technologyName) {
        collectValuesForFirstColumnAndAnotherOne(UPLOAD_SPEED_COLUMN_LABEL);
        return allValuesInTable.get(UPLOAD_SPEED_COLUMN_LABEL).get(technologyName);
    }

    @Step("Get Availability for {technologyName} from table")
    public String getAvailabilityForTechnologyFromTable(String technologyName) {
        collectValuesForFirstColumnAndAnotherOne(AVAILABILITY_COLUMN_LABEL);
        return allValuesInTable.get(AVAILABILITY_COLUMN_LABEL).get(technologyName);
    }

    @Step("Get DA id for {technologyName} from table")
    public String getDistributionAreaIdFromTable(String technologyName) {
        collectValuesForFirstColumnAndAnotherOne(DISTRIBUTION_AREA_COLUMN_LABEL);
        return allValuesInTable.get(DISTRIBUTION_AREA_COLUMN_LABEL).get(technologyName);
    }

    @Step("Get value of {property Name}")
    public String getValueFromPropertyPanelInSQView(String propertyName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getOldPropertyPanel().getPropertyValue(propertyName);
    }

    @Step("Check problem description message")
    public String getProblemDescriptionTextFromPropertyPanelInSQView() {
        String descriptionText;
        waitForPageToLoad();
        try {
            descriptionText = driver.findElement(By.xpath("//div[@class='textFieldCont medium text-default OSSRichText']/p")).getText();
        } catch (NoSuchElementException ex) {
            descriptionText = StringUtils.EMPTY;
        }
        return descriptionText;
    }

    public void deleteParticularDataSetFromCollectionWithAllDataSetsFromTable(String columnName) {
        allValuesInTable.remove(columnName);
    }

    private OldTable getOldTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createById(driver, wait, SERVICE_QUALIFICATION_VIEW_TABLE_ID);
    }

    private OldPropertyPanel getOldPropertyPanel() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldPropertyPanel.createById(driver, wait, SERVICE_QUALIFICATION_PROP_PANEL_ID);
    }

    private void collectValuesForFirstColumnAndAnotherOne(String columnName) {
        if (!allValuesInTable.containsKey(columnName)) {
            Map<String, String> tableValues = new HashMap<>();
            int rowNumbers = getOldTable().countRows(SERVICE_CONNECTION_OPTION_COLUMN_LABEL);
            for (int i = 0; i < rowNumbers; i++) {
                getOldTable().selectRow(i);
                tableValues.put(getValueFromPropertyPanelInSQView(PRIMARY_TECH_PROPERTY_NAME), getValueFromCell(i, columnName));
            }
            allValuesInTable.put(columnName, tableValues);
        }
    }

    private String getValueFromCell(int rowNumber, String columnHeaderName) {
        return getOldTable().getCellValue(rowNumber, columnHeaderName);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

}
