package com.oss.fixedaccess.servicequalification;

import java.util.Set;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.fixedaccess.datafortests.AccessTechnology.AccessTechnologyController;
import com.oss.fixedaccess.datafortests.PhysicalDataCreator;
import com.oss.pages.fixedaccess.servicequalification.ServiceQualificationView;
import com.oss.pages.fixedaccess.servicequalification.ServiceQualificationWizard;
import com.oss.untils.Environment;

import io.qameta.allure.Description;

public class ServiceQualificationViewSimpleCasesTests extends BaseTestCase {

    private static final String REQUIRED_DOWNLOAD_SPEED_ERROR_MESSAGE = "Required download speed is greater than max download speed";
    private static final String DA_QUERY_OPTION = "Distribution Area";
    private static final String INSTALLATION_ADDRESS_QUERY_OPTION = "Installation Address";
    private static final String TECHNICAL_STANDARD = "FTTH";
    private static final String DA_TYPE = "DA";
    private static final String ADDRESS_TYPE = "ADDRESS";
    private static String distributionAreaName;
    private static String distributionAreaId;
    private static String installationAddressName;
    private static Long installationAddressId;
    private static Long centralOfficeAddressId;
    private static Long installationBuildingId;
    private static Long centralOfficeBuildingId;
    private static Long cableOutletCPODF;
    private static Long cableCPODFSplitter;
    private static Long cableSplitterCOODF;
    private static Long cableCOODFAN;
    private static Long accessNodeId;
    private static Long centralOfficeODFId;
    private static Long splitterId;
    private static Long installationODFId;
    private static Long opticalOutletId;
    private final Environment env = Environment.getInstance();
    private ServiceQualificationWizard serviceQualificationWizard;
    private ServiceQualificationView serviceQualificationView;
    private PhysicalDataCreator physicalDataCreatorForSimpleCasesWithSQ;
    private AccessTechnologyController accessTechnologyController;
    private SoftAssert softAssert;

    @BeforeClass
    @Description("Init classes and data")
    public void init() {
        serviceQualificationWizard = new ServiceQualificationWizard(driver);
        serviceQualificationView = new ServiceQualificationView(driver);
        physicalDataCreatorForSimpleCasesWithSQ = new PhysicalDataCreator(env);
        accessTechnologyController = new AccessTechnologyController(env);
        softAssert = new SoftAssert();
    }

    @Test()
    @Description("Create whole data structure for tests /nDA {distributionAreaName} xid:{distributionAreaId}")
    public void createDataForTests() {
        physicalDataCreatorForSimpleCasesWithSQ.createSimplePhysicalDataForSQTests();
        distributionAreaId = String.valueOf(physicalDataCreatorForSimpleCasesWithSQ.getDistributionAreaId());
        distributionAreaName = physicalDataCreatorForSimpleCasesWithSQ.getDistributionAreaName(Long.valueOf(distributionAreaId));
        installationAddressId = physicalDataCreatorForSimpleCasesWithSQ.getInstallationAddressId();
        installationAddressName = physicalDataCreatorForSimpleCasesWithSQ.getAddressName(installationAddressId);
        centralOfficeAddressId = physicalDataCreatorForSimpleCasesWithSQ.getCentralOfficeAddressId();
        installationBuildingId = physicalDataCreatorForSimpleCasesWithSQ.getInstallationBuildingId();
        centralOfficeBuildingId = physicalDataCreatorForSimpleCasesWithSQ.getCentralOfficeBuildingId();
        cableOutletCPODF = physicalDataCreatorForSimpleCasesWithSQ.getCableOutletCPODF();
        cableCPODFSplitter = physicalDataCreatorForSimpleCasesWithSQ.getCableCPODFSplitter();
        cableSplitterCOODF = physicalDataCreatorForSimpleCasesWithSQ.getCableSplitterCOODF();
        cableCOODFAN = physicalDataCreatorForSimpleCasesWithSQ.getCableCOODFAN();
        accessNodeId = physicalDataCreatorForSimpleCasesWithSQ.getAccessNodeId();
        centralOfficeODFId = physicalDataCreatorForSimpleCasesWithSQ.getCentralOfficeODFId();
        splitterId = physicalDataCreatorForSimpleCasesWithSQ.getSplitterId();
        installationODFId = physicalDataCreatorForSimpleCasesWithSQ.getInstallationODFId();
        opticalOutletId = physicalDataCreatorForSimpleCasesWithSQ.getOpticalOutletId();
        accessTechnologyController.fillAccessTechnologyContainer();
    }

    @Test()
    @Description("Test for check for all Access technologies, that data in table and in property panel are correct. " +
            "/n This test doesn't check resources for each access technology. Test run for installation address name.")
    public void checkDataInSQViewTestForAddressName() {

        serviceQualificationWizard.openServiceQualificationWizard(BASIC_URL)
                .setQueryOption(INSTALLATION_ADDRESS_QUERY_OPTION);

        useProperMethodForSearchDAorAddress(installationAddressName, installationAddressId, ADDRESS_TYPE);

        serviceQualificationWizard
                .setProvideAlternative("true")
                .setProvideResource("false")
                .setProvideServiceNode("false")
                .clickAccept();

        String expectedAvailability = "True";
        String expectedCoverage = "True";
        String expectedDistributionAreaId = String.valueOf(distributionAreaId);

        checkDataInTableAndPropertyPanelForAllAccessTechnologies(accessTechnologyController.getAccessTechnologiesNamesSet(), expectedDistributionAreaId,
                expectedAvailability, expectedCoverage, distributionAreaName, TECHNICAL_STANDARD);
        softAssert.assertAll();
    }

    @Test()
    @Description("Test for check for all Access technologies, that data in table and in property panel are correct. " +
            "/n This test doesn't check resources for each access technology. Test run for DA name.")
    public void checkDataInSQViewForDAName() {

        serviceQualificationWizard.openServiceQualificationWizard(BASIC_URL)
                .setQueryOption(DA_QUERY_OPTION);

        searchDaOrAddressInAdvancedSearch(Long.valueOf(distributionAreaId), DA_TYPE);

        serviceQualificationWizard
                .setProvideAlternative("true")
                .setProvideResource("false")
                .setProvideServiceNode("false")
                .clickAccept();

        String expectedAvailability = "True";
        String expectedCoverage = "True";
        String expectedDistributionAreaId = String.valueOf(distributionAreaId);

        checkDataInTableAndPropertyPanelForAllAccessTechnologies(accessTechnologyController.getAccessTechnologiesNamesSet(), expectedDistributionAreaId,
                expectedAvailability, expectedCoverage, distributionAreaName, TECHNICAL_STANDARD);
        softAssert.assertAll();
    }

    @Test()
    @Description("Test for check for all Access technologies, that error code {REQUIRED_DOWNLOAD_SPEED_ERROR_MESSAGE} " +
            "appear when download speed is grater than max. Test run for installation address name.")
    public void checkErrorForRequiredDownloadSpeedForAddressName() {

        serviceQualificationWizard.openServiceQualificationWizard(BASIC_URL)
                .setQueryOption(INSTALLATION_ADDRESS_QUERY_OPTION);

        useProperMethodForSearchDAorAddress(installationAddressName, installationAddressId, ADDRESS_TYPE);

        serviceQualificationWizard
                .setProvideAlternative("true")
                .setProvideResource("false")
                .setProvideServiceNode("false")
                .setRequiredDownloadSpeed("100000")
                .setRequiredUploadSpeed("100000")
                .clickAccept();

        String expectedAvailability = "False";
        String availabilityColumnName = "Availability";

        serviceQualificationView.deleteParticularDataSetFromCollectionWithAllDataSetsFromTable(availabilityColumnName);
        checkProblemDescriptionAndAvailabilityForAllAccessTechnologies(accessTechnologyController.getAccessTechnologiesNamesSet(),
                REQUIRED_DOWNLOAD_SPEED_ERROR_MESSAGE, expectedAvailability);
        softAssert.assertAll();
    }

    @Test()
    @Description("Test for check for all Access technologies, that error code {REQUIRED_DOWNLOAD_SPEED_ERROR_MESSAGE} " +
            "appear when download speed is grater than max. Test run for DA name .")
    public void checkErrorForRequiredDownloadSpeedForDAName() {

        serviceQualificationWizard.openServiceQualificationWizard(BASIC_URL)
                .setQueryOption(DA_QUERY_OPTION);

        searchDaOrAddressInAdvancedSearch(Long.valueOf(distributionAreaId), DA_TYPE);

        serviceQualificationWizard
                .setProvideAlternative("true")
                .setProvideResource("false")
                .setProvideServiceNode("false")
                .setRequiredDownloadSpeed("100000")
                .setRequiredUploadSpeed("100000")
                .clickAccept();

        String expectedAvailability = "False";
        String availabilityColumnName = "Availability";

        serviceQualificationView.deleteParticularDataSetFromCollectionWithAllDataSetsFromTable(availabilityColumnName);
        checkProblemDescriptionAndAvailabilityForAllAccessTechnologies(accessTechnologyController.getAccessTechnologiesNamesSet(),
                REQUIRED_DOWNLOAD_SPEED_ERROR_MESSAGE, expectedAvailability);
        softAssert.assertAll();
    }

    @Test()
    @Description("Delete whole data structure for tests")
    public void deleteDataForTests() {
        physicalDataCreatorForSimpleCasesWithSQ.deleteAllDataForSimplePhysicalDataForSQTests(Long.valueOf(distributionAreaId),
                cableOutletCPODF, cableCPODFSplitter, cableSplitterCOODF, cableCOODFAN, accessNodeId, centralOfficeODFId,
                splitterId, installationODFId, opticalOutletId, installationBuildingId, centralOfficeBuildingId,
                centralOfficeAddressId, installationAddressId);
    }

    private void useProperMethodForSearchDAorAddress(String addressOrDaName, Long xId, String advancedSearchTableType) {
        if (addressOrDaName.equals("")) {
            searchDaOrAddressInAdvancedSearch(xId, advancedSearchTableType);
        } else {
            serviceQualificationWizard.setAddressOrDA(addressOrDaName);
        }
    }

    private void searchDaOrAddressInAdvancedSearch(Long xId, String advancedSearchTableType) {
        serviceQualificationWizard.openAdvancedSearchWindow()
                .setXidInAdvancedSearchFilter(xId)
                .selectFirstResultInAdvancedSearchTable(advancedSearchTableType)
                .clickButtonAddInAdvancedSearchWindow();
    }

    private void checkDataInPropertyPanelForParticularAccessTechnology(String expectedCovergae, String expectedDistributionAreaName,
                                                                       String expectedDistributionAreaId, String expectedPrimaryTechnology, String expectedTechnicalStandard, String expectedMediumType) {
        String coverageFromPropertyPanel = serviceQualificationView.getValueFromPropertyPanelInSQView("Coverage");
        String distributionAreaNameFromPropertyPanel = serviceQualificationView.getValueFromPropertyPanelInSQView("Distribution Area");
        String distributionAreaIdFromPropertyPanel = serviceQualificationView.getValueFromPropertyPanelInSQView("Distribution Area Id:");
        String primaryTechnologyFromPropertyPanel = serviceQualificationView.getValueFromPropertyPanelInSQView("Primary Technology");
        String technicalStandardFromPropertyPanel = serviceQualificationView.getValueFromPropertyPanelInSQView("Technical standard");
        String mediumTypeFromPropertyPanel = serviceQualificationView.getValueFromPropertyPanelInSQView("Medium Type");
        softAssert.assertEquals(templateForAssertInput(expectedPrimaryTechnology) + coverageFromPropertyPanel, templateForAssertInput(expectedPrimaryTechnology) + expectedCovergae);
        softAssert.assertEquals(templateForAssertInput(expectedPrimaryTechnology) + distributionAreaNameFromPropertyPanel, templateForAssertInput(expectedPrimaryTechnology) + expectedDistributionAreaName);
        softAssert.assertEquals(templateForAssertInput(expectedPrimaryTechnology) + distributionAreaIdFromPropertyPanel, templateForAssertInput(expectedPrimaryTechnology) + expectedDistributionAreaId);
        softAssert.assertEquals(templateForAssertInput(expectedPrimaryTechnology) + primaryTechnologyFromPropertyPanel, templateForAssertInput(expectedPrimaryTechnology) + expectedPrimaryTechnology);
        softAssert.assertEquals(templateForAssertInput(expectedPrimaryTechnology) + technicalStandardFromPropertyPanel, templateForAssertInput(expectedPrimaryTechnology) + expectedTechnicalStandard);
        softAssert.assertEquals(templateForAssertInput(expectedPrimaryTechnology) + mediumTypeFromPropertyPanel, templateForAssertInput(expectedPrimaryTechnology) + expectedMediumType);
    }

    private void checkDataInTableForParticularAccessTechnology(String accessTechnologyLayer, String expectedDownloadSpeed, String expectedUploadSpeed,
                                                               String expectedDistributionAreaId, String expectedAvailability) {
        String downloadSpeedFromTable = serviceQualificationView.getDownloadSpeedFromTable(accessTechnologyController.getAccessTechnology(accessTechnologyLayer).getName());
        String uploadSpeedFromTable = serviceQualificationView.getUploadSpeedFromTable(accessTechnologyController.getAccessTechnology(accessTechnologyLayer).getName());
        String distributionAreaIdFromTable = serviceQualificationView.getDistributionAreaIdFromTable(accessTechnologyController.getAccessTechnology(accessTechnologyLayer).getName());
        softAssert.assertEquals(templateForAssertInput(accessTechnologyController.getAccessTechnology(accessTechnologyLayer).getName())
                + downloadSpeedFromTable, templateForAssertInput(accessTechnologyController.getAccessTechnology(accessTechnologyLayer).getName()) + expectedDownloadSpeed);
        softAssert.assertEquals(templateForAssertInput(accessTechnologyController.getAccessTechnology(accessTechnologyLayer).getName())
                + uploadSpeedFromTable, templateForAssertInput(accessTechnologyController.getAccessTechnology(accessTechnologyLayer).getName()) + expectedUploadSpeed);
        softAssert.assertEquals(templateForAssertInput(accessTechnologyController.getAccessTechnology(accessTechnologyLayer).getName())
                + distributionAreaIdFromTable, templateForAssertInput(accessTechnologyController.getAccessTechnology(accessTechnologyLayer).getName()) + expectedDistributionAreaId);
        checkAvailabilityFromTableForParticularAccessTechnology(accessTechnologyLayer, expectedAvailability);
    }

    private String templateForAssertInput(String value) {
        return "For " + value + ": ";
    }

    private void checkAvailabilityFromTableForParticularAccessTechnology(String accessTechnologyLayer,
                                                                         String expectedAvailability) {
        String availabilityFromTable = serviceQualificationView.getAvailabilityForTechnologyFromTable(accessTechnologyController.getAccessTechnology(accessTechnologyLayer).getName());
        softAssert.assertEquals(templateForAssertInput(accessTechnologyController.getAccessTechnology(accessTechnologyLayer).getName())
                + availabilityFromTable, templateForAssertInput(accessTechnologyController.getAccessTechnology(accessTechnologyLayer).getName()) + expectedAvailability);
    }

    private void checkProblemDescriptionForParticularAccessTechnology(String accessTechnologyLayer, String problemDescriptionError) {
        String problemDescription = serviceQualificationView.getProblemDescriptionTextFromPropertyPanelInSQView();
        softAssert.assertEquals(templateForAssertInput(accessTechnologyController.getAccessTechnology(accessTechnologyLayer).getName())
                + problemDescription, templateForAssertInput(accessTechnologyController.getAccessTechnology(accessTechnologyLayer).getName()) + problemDescriptionError);
    }

    private void checkProblemDescriptionAndAvailabilityForAllAccessTechnologies(Set<String> accessTechnologyMapKeys,
                                                                                String problemDescriptionError, String expectedAvailability) {
        for (String accessTechnologyLayer : accessTechnologyMapKeys) {
            selectParticularRowInTableForColumnServiceConnectionOptionsWithValue(accessTechnologyController.getAccessTechnology(accessTechnologyLayer).getName());
            checkProblemDescriptionForParticularAccessTechnology(accessTechnologyLayer, problemDescriptionError);
            checkAvailabilityFromTableForParticularAccessTechnology(accessTechnologyLayer, expectedAvailability);
        }
    }

    private void checkDataInTableAndPropertyPanelForAllAccessTechnologies(Set<String> accessTechnologyMapKeys,
                                                                          String expectedDistributionAreaId, String expectedAvailability, String expectedCoverage,
                                                                          String distributionAreaName, String technicalStandard) {
        for (String accessTechnologyLayer : accessTechnologyMapKeys) {
            selectParticularRowInTableForColumnServiceConnectionOptionsWithValue(accessTechnologyController.getAccessTechnology(accessTechnologyLayer).getName());
            checkDataInTableForParticularAccessTechnology(accessTechnologyLayer, accessTechnologyController.getAccessTechnology(accessTechnologyLayer).getMaxDownloadSpeed(),
                    accessTechnologyController.getAccessTechnology(accessTechnologyLayer).getMaxUploadSpeed(), expectedDistributionAreaId, expectedAvailability);
            checkDataInPropertyPanelForParticularAccessTechnology(expectedCoverage, distributionAreaName,
                    expectedDistributionAreaId, accessTechnologyController.getAccessTechnology(accessTechnologyLayer).getName(),
                    technicalStandard, accessTechnologyController.getAccessTechnology(accessTechnologyLayer).getMediumType());
        }
    }

    private void selectParticularRowInTableForColumnServiceConnectionOptionsWithValue(String cellValue) {
        serviceQualificationView.selectParticularRowInTableForColumnServiceConnectionOptionsWithValue(cellValue);
    }

}
