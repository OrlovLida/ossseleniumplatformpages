package com.oss.fixedaccess.servicequalification;

import com.oss.BaseTestCase;
import com.oss.fixedaccess.datafortests.AccessTechnologiesEnum;
import com.oss.fixedaccess.datafortests.PhysicalDataCreatorForSimpleCasesWithSQ;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.pages.fixedaccess.servicequalification.ServiceQualificationView;
import com.oss.pages.fixedaccess.servicequalification.ServiceQualificationWizard;
import com.oss.untils.Environment;
import io.qameta.allure.Description;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


public class ServiceQualificationViewSimpleCasesTests extends BaseTestCase {


    private static final String REQUIRED_DOWNLOAD_SPEED_ERROR_MESSAGE = "Required download speed is greater than max download speed";
    private static final String DA_QUERY_OPTION = "Distribution Area";
    private static final String INSTALLATION_ADDRESS_QUERY_OPTION = "Installation Address";
    private static final String TECHNICAL_STANDARD = "FTTH";
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

    private ServiceQualificationWizard serviceQualificationWizard;
    private ServiceQualificationView serviceQualificationView;
    private PhysicalDataCreatorForSimpleCasesWithSQ physicalDataCreatorForSimpleCasesWithSQ;
    private SoftAssert softAssert;
    private Environment env = Environment.getInstance();

    @BeforeClass
    @Description("Init classes")
    public void init() {
        serviceQualificationWizard = new ServiceQualificationWizard(driver);
        serviceQualificationView = new ServiceQualificationView(driver);
        physicalDataCreatorForSimpleCasesWithSQ = new PhysicalDataCreatorForSimpleCasesWithSQ(env);
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
    }

    @Test()
    @Description("Test for check for all Access technologies, that data in table and in property panel are correct. /n This test doesn't check resources for each access technology. Test run for DA name and installation address name.")
    public void checkDataInTableAndPropertyPanelForAllAccessTechnologiesInSQViewHappyPathTest() {

        getPerspectiveChooser().setLivePerspective();
        serviceQualificationWizard.openServiceQualificationWizard()
                .setQueryOption(INSTALLATION_ADDRESS_QUERY_OPTION);

        useProperMethodForSearchDAfrAddress(installationAddressName, installationAddressId);

        serviceQualificationWizard
                .setProvideAlternative("true")
                .setProvideResource("false")
                .setProvideServiceNode("false")
                .clickAccept();

        String expectedAvailability = "True";
        String expectedCoverage = "True";
        String expectedDistributionAreaId = String.valueOf(distributionAreaId);

        checkDataInTableAndPropertyPanelForAllAccessTechnologies(AccessTechnologiesEnum.values(), expectedDistributionAreaId, expectedAvailability, expectedCoverage, distributionAreaName, TECHNICAL_STANDARD);

        serviceQualificationView.clickButtonChangeParameters()
                .setQueryOption(DA_QUERY_OPTION);

        useProperMethodForSearchDAfrAddress(distributionAreaName, Long.valueOf(distributionAreaId));

        serviceQualificationWizard.clickAccept();

        checkDataInTableAndPropertyPanelForAllAccessTechnologies(AccessTechnologiesEnum.values(), expectedDistributionAreaId, expectedAvailability, expectedCoverage, distributionAreaName, TECHNICAL_STANDARD);

        softAssert.assertAll();

    }

    @Test()
    @Description("Test for check for all Access technologies, that error code {REQUIRED_DOWNLOAD_SPEED_ERROR_MESSAGE} appear when download speed is grater than max. Test run for DA name and installation address name.")
    public void checkErrorForRequiredDownloadSpeedGraterThanMaxDownloadSpeedForAllAccessTechnologiesTest() {

        getPerspectiveChooser().setLivePerspective();
        serviceQualificationWizard.openServiceQualificationWizard()
                .setQueryOption(INSTALLATION_ADDRESS_QUERY_OPTION);

        useProperMethodForSearchDAfrAddress(installationAddressName, installationAddressId);

        serviceQualificationWizard
                .setProvideAlternative("true")
                .setProvideResource("false")
                .setProvideServiceNode("false")
                .setRequiredDownloadSpeed("100000")
                .setRequiredUploadSpeed("100000")
                .clickAccept();

        String expectedAvailability = "False";
        String availabilityColumnName = "Availability";

        serviceQualificationView.deleteParticularDataSetFromColectionWithAllDataSetsFromTable(availabilityColumnName);

        checkProblemDescriptionAndAvailabilityForAllAccessTechnologies(AccessTechnologiesEnum.values(), REQUIRED_DOWNLOAD_SPEED_ERROR_MESSAGE, expectedAvailability);

        serviceQualificationView.clickButtonChangeParameters()
                .setQueryOption(DA_QUERY_OPTION);

        useProperMethodForSearchDAfrAddress(distributionAreaName, Long.valueOf(distributionAreaId));

        serviceQualificationWizard.clickAccept();

        checkProblemDescriptionAndAvailabilityForAllAccessTechnologies(AccessTechnologiesEnum.values(), REQUIRED_DOWNLOAD_SPEED_ERROR_MESSAGE, expectedAvailability);

        softAssert.assertAll();

    }

    @Test()
    @Description("Delete whole data structure for tests")
    public void deleteDataForTests() {
        physicalDataCreatorForSimpleCasesWithSQ.deleteAllDataForSimplePhysicalDataForSQTests(Long.valueOf(distributionAreaId), cableOutletCPODF, cableCPODFSplitter, cableSplitterCOODF, cableCOODFAN, accessNodeId, centralOfficeODFId, splitterId, installationODFId, opticalOutletId, installationBuildingId, centralOfficeBuildingId, centralOfficeAddressId, installationAddressId);
    }

    private void useProperMethodForSearchDAfrAddress(String addressOrDaName, Long xId) {
        if (addressOrDaName.equals("")) {
            serviceQualificationWizard.openAdvancedSearchWindow()
                    .setXidInAdvancedSearchFilter(xId)
                    .selectFirstResultInAdvancedSearchTable()
                    .clickButtonAddInAdvancedSearchWindow();
        } else {
            serviceQualificationWizard.setAddressOrDA(addressOrDaName);
        }
    }

    private void checkDataInPropertyPanelForParticularAccessTechnology(String expectedCovergae, String expectedDistributionAreaName, String expectedDistributionAreaId, String expectedPrimaryTechnology, String expectedTechnicalStandard, String expectedMediumType) {
        String coverageFromPropertyPanel = serviceQualificationView.getValueFromPropertyPanelInSQView("Coverage");
        String distributionAreaNameFromPropertyPanel = serviceQualificationView.getValueFromPropertyPanelInSQView("Distribution Area");
        String distributionAreaIdFromPropertyPanel = serviceQualificationView.getValueFromPropertyPanelInSQView("Distribution Area Id:");
        String primaryTechnologyFromPropertyPanel = serviceQualificationView.getValueFromPropertyPanelInSQView("Primary Technology");
        String technicalStandardFromPropertyPanel = serviceQualificationView.getValueFromPropertyPanelInSQView("Technical standard");
        String mediumTypeFromPropertyPanel = serviceQualificationView.getValueFromPropertyPanelInSQView("Medium Type");
        softAssert.assertEquals(coverageFromPropertyPanel, expectedCovergae);
        softAssert.assertEquals(distributionAreaNameFromPropertyPanel, expectedDistributionAreaName);
        softAssert.assertEquals(distributionAreaIdFromPropertyPanel, expectedDistributionAreaId);
        softAssert.assertEquals(primaryTechnologyFromPropertyPanel, expectedPrimaryTechnology);
        softAssert.assertEquals(technicalStandardFromPropertyPanel, expectedTechnicalStandard);
        softAssert.assertEquals(mediumTypeFromPropertyPanel, expectedMediumType);
    }

    private void checkDataInTableForParticularAccessTechnology(AccessTechnologiesEnum accessTechnologiesEnum, String expectedDownloadSpeed, String expectedUploadSpeed, String expectedDistributionAreaId, String expectedAvailability) {
        String downloadSpeedFromTable = serviceQualificationView.getDownloadSpeedFromTable(accessTechnologiesEnum.getName());
        String uploadSpeedFromTable = serviceQualificationView.getUploadSpeedFromTable(accessTechnologiesEnum.getName());
        String distributionAreaIdFromTable = serviceQualificationView.getDistributionAreaIdFromTable(accessTechnologiesEnum.getName());
        softAssert.assertEquals(downloadSpeedFromTable, expectedDownloadSpeed);
        softAssert.assertEquals(uploadSpeedFromTable, expectedUploadSpeed);
        softAssert.assertEquals(distributionAreaIdFromTable, expectedDistributionAreaId);
        checkAvailabilityFromTableForParticularAccessTechnology(accessTechnologiesEnum, expectedAvailability);
    }

    private void checkAvailabilityFromTableForParticularAccessTechnology(AccessTechnologiesEnum accessTechnologiesEnum, String expectedAvailability) {
        String availabilityFromTable = serviceQualificationView.getAvailabilityForTechnologyFromTable(accessTechnologiesEnum.getName());
        softAssert.assertEquals(availabilityFromTable, expectedAvailability);
    }

    private void checkProblemDescriptionForParticularAccessTechnology(String problemDescriptionError) {
        String problemDescription = serviceQualificationView.getProblemDescriptionTextFromPropertyPanelInSQView();
        softAssert.assertEquals(problemDescription, problemDescriptionError);
    }

    private void checkProblemDescriptionAndAvailabilityForAllAccessTechnologies(AccessTechnologiesEnum[] accessTechnologiesEnum, String problemDescriptionError, String expectedAvailability) {
        for (AccessTechnologiesEnum i : accessTechnologiesEnum) {
            selectParticularRowInTableForColumnServiceConnectionOptionsWithValue(i.getName());
            checkProblemDescriptionForParticularAccessTechnology(problemDescriptionError);
            checkAvailabilityFromTableForParticularAccessTechnology(i, expectedAvailability);
        }
    }

    private void checkDataInTableAndPropertyPanelForAllAccessTechnologies(AccessTechnologiesEnum[] accessTechnologiesEnum, String expectedDistributionAreaId, String expectedAvailability, String expectedCoverage, String distributionAreaName, String technicalStandard) {
        for (AccessTechnologiesEnum i : accessTechnologiesEnum) {
            selectParticularRowInTableForColumnServiceConnectionOptionsWithValue(i.getName());
            checkDataInTableForParticularAccessTechnology(i, i.getDownloadSpeed(), i.getUploadSpeed(), expectedDistributionAreaId, expectedAvailability);
            checkDataInPropertyPanelForParticularAccessTechnology(expectedCoverage, distributionAreaName, expectedDistributionAreaId, i.getName(), technicalStandard, i.getSupportedMediumType());
        }
    }

    private void selectParticularRowInTableForColumnServiceConnectionOptionsWithValue(String cellValue) {
        serviceQualificationView.selectParticularRowInTableForColumnServiceConnectionOptionsWithValue(cellValue);
    }

    private PerspectiveChooser getPerspectiveChooser() {
        return PerspectiveChooser.create(driver, webDriverWait);
    }

}
