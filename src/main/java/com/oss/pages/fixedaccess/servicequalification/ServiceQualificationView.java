package com.oss.pages.fixedaccess.servicequalification;

import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;


public class ServiceQualificationView extends BasePage {

    private static final String CHANGE_PARAMETERS_BUTTON_ID = "openWizard";
    private static final String SERVICE_QUALIFICATION_VIEW_TABLE_ID = "sqTableAppId";
    private static final String SERVICE_CONNECTION_OPTION_COLUMN_LABEL = "Service Connection Options";
    private static final String DOWNLOAD_SPEED_COLUMN_LABEL = "Download Speed";
    private static final String UPLOAD_SPEED_COLUMN_LABEL = "Upload Speed";
    private static final String AVAILABILITY_COLUMN_LABEL = "Availability";
    private static final String DISTRIBUTION_AREA_COLUMN_LABEL = "Distribution Area";

    private Map<String, Map<String, String>> allValuesInTable = new HashMap<>();

    public ServiceQualificationView(WebDriver driver) {
        super(driver);
    }

    @Step("Click Button \"Change Parameters\"")
    public ServiceQualificationWizard clickButtonChangeParameters() {
        getOldActionsContainer().callActionById(CHANGE_PARAMETERS_BUTTON_ID);
        return new ServiceQualificationWizard(driver);
    }

    @Step("Select particular row in table for column Service Connection Options with value {value}")
    public ServiceQualificationView selectParticularRowInTableForColumnServiceConnectionOptionsWithValue(String value) {
        int rowNumber = getOldTable().getRowNumber(value, SERVICE_CONNECTION_OPTION_COLUMN_LABEL);
        getOldTable().selectRow(rowNumber);
        return this;
    }

    @Step("Get Download Speed for {technologyName} from table")
    public String getDownloadSpeedFromTable(String technologyName) {
        collectValuesForTwoSpecificColumns(SERVICE_CONNECTION_OPTION_COLUMN_LABEL, DOWNLOAD_SPEED_COLUMN_LABEL);
        return allValuesInTable.get(DOWNLOAD_SPEED_COLUMN_LABEL).get(technologyName);
    }

    @Step("Get Upload Speed for {technologyName} from table")
    public String getUploadSpeedFromTable(String technologyName) {
        collectValuesForTwoSpecificColumns(SERVICE_CONNECTION_OPTION_COLUMN_LABEL, UPLOAD_SPEED_COLUMN_LABEL);
        return allValuesInTable.get(UPLOAD_SPEED_COLUMN_LABEL).get(technologyName);
    }

    @Step("Get Availability for {technologyName} from table")
    public String getAvailabilityForTechnologyFromTable(String technologyName) {
        collectValuesForTwoSpecificColumns(SERVICE_CONNECTION_OPTION_COLUMN_LABEL, AVAILABILITY_COLUMN_LABEL);
        return allValuesInTable.get(AVAILABILITY_COLUMN_LABEL).get(technologyName);
    }

    @Step("Get DA id for {technologyName} from table")
    public String getDistributionAreaIdFromTable(String technologyName) {
        collectValuesForTwoSpecificColumns(SERVICE_CONNECTION_OPTION_COLUMN_LABEL, DISTRIBUTION_AREA_COLUMN_LABEL);
        return allValuesInTable.get(DISTRIBUTION_AREA_COLUMN_LABEL).get(technologyName);
    }

    @Step("Get value of {property Name}")
    public String getValueFromPropertyPanelInSQView(String propertyName) {
        return getOldPropertyPanel().getPropertyValue(propertyName);
    }

    @Step("Check problem description message")
    public String getProblemDescriptionTextFromPropertyPanelInSQView() {
        String descriptionText;
        DelayUtils.sleep();
        try {
            descriptionText = driver.findElement(By.xpath("//div[@class='textFieldCont medium text-default OSSRichText']/p")).getText();
        } catch (NoSuchElementException ex) {
            descriptionText = "";
        }
        return descriptionText;
    }

    public void deleteParticularDataSetFromColectionWithAllDataSetsFromTable(String columnName) {
        allValuesInTable.remove(columnName);
    }

    private OldTable getOldTable() {
        waitForPageToLoad();
        return OldTable.createByComponentDataAttributeName(driver, wait, SERVICE_QUALIFICATION_VIEW_TABLE_ID);
    }

    private OldPropertyPanel getOldPropertyPanel() {
        waitForPageToLoad();
        return OldPropertyPanel.create(driver, wait);
    }

    private void collectValuesForTwoSpecificColumns(String firstColumnName, String secondColumnName) {
        if (!allValuesInTable.containsKey(secondColumnName)) {
            Map<String, String> tableValues = new HashMap<>();
            int rowNumbers = getOldTable().getNumberOfRowsInTable(SERVICE_CONNECTION_OPTION_COLUMN_LABEL);
            for (int i = 0; i < rowNumbers; i++) {
                tableValues.put(getValueFromCell(i, firstColumnName), getValueFromCell(i, secondColumnName));
            }
            allValuesInTable.put(secondColumnName, tableValues);
        }
    }

    private String getValueFromCell(int rowNumber, String columnHeaderName) {
        return getOldTable().getCellValue(rowNumber, columnHeaderName);
    }

    private OldActionsContainer getOldActionsContainer() {
        return OldActionsContainer.createForMainWindow(driver, wait);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

}
