package com.oss.E2E;

import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.reconciliation.MetamodelPage;
import com.oss.pages.reconciliation.audit.CardinalityAndValueCheckDefinition;
import com.oss.pages.reconciliation.audit.ComplexValueCheckDefinition;
import com.oss.pages.reconciliation.audit.ObjectCardinalityCheckDefinition;
import com.oss.pages.reconciliation.audit.RuleDefinitionWizardPage;
import com.oss.pages.reconciliation.audit.SimpleValueCheckDefinition;
import com.oss.pages.reconciliation.audit.ValidationRulesGroupWizardPage;
import com.oss.pages.reconciliation.audit.ValidationRulesManagerPage;

import io.qameta.allure.Description;

public class BucOssNar002Test extends BaseTestCase {

    private static final String NETWORK_DISCOVERY_AND_RECONCILIATION = "Network Discovery and Reconciliation";
    private static final String PARAMETERS_AUDIT = "Parameters Audit";
    private static final String VALIDATION_RULES_MANAGER = "Validation Rules Manager";
    private static final String METAMODEL_EDITOR = "Metamodel Editor";
    private static final String HUAWEI_VALIDATION_RULES_GROUP_NAME = "Huawei 4G defaults";
    private static final String NOKIA_VALIDATION_RULES_GROUP_NAME = "Nokia 4G defaults";
    private static final String HUAWEI_INTERFACE_NAME = "Huawei U2000 RAN";
    private static final String NOKIA_INTERFACE_NAME = "Nokia Netact RAN";
    private static final String HUAWEI_METAMODEL_PATH = "metamodels/NAR_002/huawei.csv";
    private static final String NOKIA_METAMODEL_PATH = "metamodels/NAR_002/nokia.csv";
    private static final String ENODEB_FUNCTION_MODULE = "eNodeBFunctionModule";
    private static final String CELL = "CELL";
    private static final String EUTRANEXTERNALCELL = "EUTRANEXTERNALCELL";
    private static final String EMC = "EMC";
    private static final String MBTS_PORT = "MBTSPort";
    private static final String PLMN = "PLMN";
    private static final String ANR = "ANR";
    private static final String LNADJ = "LNADJ";
    private static final String RMOD = "RMOD";
    private static final String PRODUCTVERSION = "PRODUCTVERSION";
    private static final String CELLRADIUS = "CELLRADIUS";
    private static final String PHYCELLID = "PHYCELLID";
    private static final String DLBANDWIDTH = "DLBANDWIDTH";
    private static final String TAC = "TAC";
    private static final String EMCENABLE = "EMCENABLE";
    private static final String DATE_OF_LAST_SERVICE = "DateOfLastService";
    private static final String VERSION = "version";
    private static final List<String> ANR_ATTRIBUTES_LIST = Arrays.asList("maxNumX2LinksIn", "maxNumX2LinksOut");
    private static final String LNBTS = "LNBTS";
    private static final String APEQM = "APEQM";
    private static final List<String> RMOD_ATTRIBUTES_LIST = Arrays.asList("actPimCancellation", "actSnapshotCollection");
    private static final String EQUAL_TO_OPERATOR = "EQUAL TO";
    private static final String BETWEEN_OR_EQUAL_TO_OPERATOR = "BETWEEN or EQUAL TO";
    private static final String OUTSIDE_OPERATOR = "OUTSIDE";
    private static final String LESS_THAN_OPERATOR = "LESS THAN";
    private static final String MORE_THAN_OPERATOR = "MORE THAN";
    private static final String NOT_FOUND_OPERATOR = "NOT FOUND";
    private static final String NOT_EMPTY_OPERATOR = "NOT EMPTY";
    private static final String PRODUCTVERSION_VALUE1 = "BTS3900 V100R015C10";
    private static final String VERSION_VALUE1 = "1.1";
    private static final String CELLRADIUS_VALUE1 = "9000";
    private static final String PHYCELLID_VALUE1 = "180";
    private static final String DLBANDWIDTH_VALUE1 = "4";
    private static final String TAC_VALUE1 = "500";
    private static final String ENABLE_VALUE = "Enabled commonIcon-SHOW";
    private static final String RMOD_RULE_DEFINITIONS = "actPimCancellation|actSnapshotCollection\n" +
            "0|0";
    private static final String ANR_RULE_DEFINITIONS = "maxNumX2LinksIn|maxNumX2LinksOut\n" +
            "255|255";
    private static final String GROUP_NOT_PRESENT_LOG_PATTERN = "Validation Rules Group with name %s is not available.";
    private static final String GROUP_PRESENT_LOG_PATTERN = "Validation Rules Group with name %s is still available.";
    private static final String RULE_SAVED_MESSAGE = "Rule successfully saved.";
    private static final String HUAWEI_SYSTEM_MESSAGE_PATTERN = "Create Huawei definitions. Checking system message after definition for %s create.";
    private static final String NOKIA_SYSTEM_MESSAGE_PATTERN = "Create Nokia definitions. Checking system message after definition for %s create.";
    private static final String VALIDATION_MESSAGE_IS_PRESENT = "Cannot create rule definition. Validation message is present.";
    private static final String INCORRECT_DEFINITIONS_TOTAL_A1 = "Total number of Simple Values Check rules definitions is incorrect.";
    private static final String INCORRECT_DEFINITIONS_TOTAL_A2 = "Total number of Complex Value Check rules definitions is incorrect.";
    private static final String INCORRECT_DEFINITIONS_TOTAL_B1 = "Total number of Object Cardinality Check rules definitions is incorrect.";
    private static final String INCORRECT_DEFINITIONS_TOTAL_B2 = "Total number of Cardinality and Values Check rules definitions is incorrect.";
    private static final String INCORRECT_VALUE_A1_PATTERN = "Incorrect value in Simple Values Check tab for column %s.";
    private static final String INCORRECT_VALUE_A2_PATTERN = "Incorrect value in Complex Value Check tab for column %s.";
    private static final String INCORRECT_VALUE_B1_PATTERN = "Incorrect value in Object Cardinality Check tab for column %s.";
    private static final String INCORRECT_VALUE_B2_PATTERN = "Incorrect value in Cardinality and Values Check tab for column %s.";
    private static final String LNADJ_CARDINALITY = "37";
    private static final String RMOD_CARDINALITY = "1";
    private static final String CELLRADIUS_VALUE2 = "11000";
    private static final String PHYCELLID_VALUE2 = "200";
    private static final int GROUP_PRIORITY = 1;

    private static String validationRulesManagerUrl;
    private static String metamodelEditorUrl;

    private SoftAssert softAssert;
    private ValidationRulesManagerPage validationRulesManagerPage;
    private MetamodelPage metamodelPage;
    private RuleDefinitionWizardPage ruleDefinitionWizardPage;

    @BeforeClass
    @Description("Open Console")
    public void openConsole() {
        waitForPageToLoad();
        softAssert = new SoftAssert();
    }

    @Test(priority = 1, description = "Create Validation Rules Group")
    @Description("Create Validation Rules Group")
    public void createVRG() {
        SoftAssert createVRGsoftAssert = new SoftAssert();
        homePage.chooseFromLeftSideMenu(VALIDATION_RULES_MANAGER, NETWORK_DISCOVERY_AND_RECONCILIATION, PARAMETERS_AUDIT);
        waitForPageToLoad();
        validationRulesManagerUrl = driver.getCurrentUrl();
        validationRulesManagerPage = new ValidationRulesManagerPage(driver);
        validationRulesManagerPage.openCreateGroupWizard();
        waitForPageToLoad();
        ValidationRulesGroupWizardPage wizard = new ValidationRulesGroupWizardPage(driver);
        wizard.createGroup(HUAWEI_VALIDATION_RULES_GROUP_NAME, GROUP_PRIORITY);
        waitForPageToLoad();
        validationRulesManagerPage.openCreateGroupWizard();
        waitForPageToLoad();
        wizard.createGroup(NOKIA_VALIDATION_RULES_GROUP_NAME, GROUP_PRIORITY);
        waitForPageToLoad();
        createVRGsoftAssert.assertTrue(validationRulesManagerPage.isGroupPresent(HUAWEI_VALIDATION_RULES_GROUP_NAME), String.format(GROUP_NOT_PRESENT_LOG_PATTERN, HUAWEI_VALIDATION_RULES_GROUP_NAME));
        createVRGsoftAssert.assertTrue(validationRulesManagerPage.isGroupPresent(NOKIA_VALIDATION_RULES_GROUP_NAME), String.format(GROUP_NOT_PRESENT_LOG_PATTERN, NOKIA_VALIDATION_RULES_GROUP_NAME));
        createVRGsoftAssert.assertAll();
    }

    @Test(priority = 2, description = "Create Rule Definitions for Huawei", dependsOnMethods = {"createVRG"})
    @Description("Create Rule Definitions for Huawei")
    public void createHuaweiDefinitions() throws URISyntaxException {
        homePage.chooseFromLeftSideMenu(METAMODEL_EDITOR, NETWORK_DISCOVERY_AND_RECONCILIATION);
        waitForPageToLoad();
        metamodelEditorUrl = driver.getCurrentUrl();
        metamodelPage = new MetamodelPage(driver);
        metamodelPage.searchInterfaceByName(HUAWEI_INTERFACE_NAME);
        waitForPageToLoad();
        metamodelPage.importMetamodel(HUAWEI_METAMODEL_PATH);

        searchAndSelectObjectType(ENODEB_FUNCTION_MODULE);
        openA1RuleWizard();
        SimpleValueCheckDefinition def1 = SimpleValueCheckDefinition.builder()
                .setGroupName(HUAWEI_VALIDATION_RULES_GROUP_NAME)
                .setAttributeName(PRODUCTVERSION)
                .setOperator(EQUAL_TO_OPERATOR)
                .setValue1(PRODUCTVERSION_VALUE1)
                .build();
        ruleDefinitionWizardPage = new RuleDefinitionWizardPage(driver);
        ruleDefinitionWizardPage.createSimpleValueCheckDefinition(def1);
        checkMessage(String.format(HUAWEI_SYSTEM_MESSAGE_PATTERN, PRODUCTVERSION));

        searchAndSelectObjectType(CELL);
        openA1RuleWizard();
        SimpleValueCheckDefinition def2 = SimpleValueCheckDefinition.builder()
                .setGroupName(HUAWEI_VALIDATION_RULES_GROUP_NAME)
                .setAttributeName(CELLRADIUS)
                .setOperator(BETWEEN_OR_EQUAL_TO_OPERATOR)
                .setValue1(CELLRADIUS_VALUE1)
                .setValue2(CELLRADIUS_VALUE2)
                .build();
        ruleDefinitionWizardPage.createSimpleValueCheckDefinition(def2);
        checkMessage(String.format(HUAWEI_SYSTEM_MESSAGE_PATTERN, CELLRADIUS));

        openA1RuleWizard();
        SimpleValueCheckDefinition def3 = SimpleValueCheckDefinition.builder()
                .setGroupName(HUAWEI_VALIDATION_RULES_GROUP_NAME)
                .setAttributeName(PHYCELLID)
                .setOperator(OUTSIDE_OPERATOR)
                .setValue1(PHYCELLID_VALUE1)
                .setValue2(PHYCELLID_VALUE2)
                .build();
        ruleDefinitionWizardPage.createSimpleValueCheckDefinition(def3);
        checkMessage(String.format(HUAWEI_SYSTEM_MESSAGE_PATTERN, PHYCELLID));

        openA1RuleWizard();
        SimpleValueCheckDefinition def4 = SimpleValueCheckDefinition.builder()
                .setGroupName(HUAWEI_VALIDATION_RULES_GROUP_NAME)
                .setAttributeName(DLBANDWIDTH)
                .setOperator(LESS_THAN_OPERATOR)
                .setValue1(DLBANDWIDTH_VALUE1)
                .build();
        ruleDefinitionWizardPage.createSimpleValueCheckDefinition(def4);
        checkMessage(String.format(HUAWEI_SYSTEM_MESSAGE_PATTERN, DLBANDWIDTH));

        searchAndSelectObjectType(EUTRANEXTERNALCELL);
        openA1RuleWizard();
        SimpleValueCheckDefinition def5 = SimpleValueCheckDefinition.builder()
                .setGroupName(HUAWEI_VALIDATION_RULES_GROUP_NAME)
                .setAttributeName(TAC)
                .setOperator(MORE_THAN_OPERATOR)
                .setValue1(TAC_VALUE1)
                .build();
        ruleDefinitionWizardPage.createSimpleValueCheckDefinition(def5);
        checkMessage(String.format(HUAWEI_SYSTEM_MESSAGE_PATTERN, TAC));

        searchAndSelectObjectType(EMC);
        openA1RuleWizard();
        SimpleValueCheckDefinition def6 = SimpleValueCheckDefinition.builder()
                .setGroupName(HUAWEI_VALIDATION_RULES_GROUP_NAME)
                .setAttributeName(EMCENABLE)
                .setOperator(NOT_FOUND_OPERATOR)
                .build();
        ruleDefinitionWizardPage.createSimpleValueCheckDefinition(def6);
        checkMessage(String.format(HUAWEI_SYSTEM_MESSAGE_PATTERN, EMCENABLE));

        searchAndSelectObjectType(MBTS_PORT);
        openA1RuleWizard();
        SimpleValueCheckDefinition def7 = SimpleValueCheckDefinition.builder()
                .setGroupName(HUAWEI_VALIDATION_RULES_GROUP_NAME)
                .setAttributeName(DATE_OF_LAST_SERVICE)
                .setOperator(NOT_EMPTY_OPERATOR)
                .build();
        ruleDefinitionWizardPage.createSimpleValueCheckDefinition(def7);
        checkMessage(String.format(HUAWEI_SYSTEM_MESSAGE_PATTERN, DATE_OF_LAST_SERVICE));
    }

    @Test(priority = 3, description = "Create Rule Definitions for Nokia", dependsOnMethods = {"createVRG"})
    @Description("Create Rule Definitions for Nokia")
    public void createNokiaDefinitions() throws URISyntaxException {
        driver.get(metamodelEditorUrl);
        waitForPageToLoad();
        metamodelPage = new MetamodelPage(driver);
        metamodelPage.searchInterfaceByName(NOKIA_INTERFACE_NAME);
        waitForPageToLoad();
        metamodelPage.importMetamodel(NOKIA_METAMODEL_PATH);

        searchAndSelectObjectType(PLMN);
        openA1RuleWizard();
        SimpleValueCheckDefinition def2 = SimpleValueCheckDefinition.builder()
                .setGroupName(NOKIA_VALIDATION_RULES_GROUP_NAME)
                .setAttributeName(VERSION)
                .setOperator(EQUAL_TO_OPERATOR)
                .setValue1(VERSION_VALUE1)
                .build();
        ruleDefinitionWizardPage = new RuleDefinitionWizardPage(driver);
        ruleDefinitionWizardPage.createSimpleValueCheckDefinition(def2);
        checkMessage(String.format(NOKIA_SYSTEM_MESSAGE_PATTERN, VERSION));

        searchAndSelectObjectType(ANR);
        createA2Rule();
        checkMessage(String.format(NOKIA_SYSTEM_MESSAGE_PATTERN, ANR));

        searchAndSelectObjectType(LNADJ);
        createB1Rule();
        checkMessage(String.format(NOKIA_SYSTEM_MESSAGE_PATTERN, LNADJ));

        searchAndSelectObjectType(RMOD);
        createB2Rule();
        checkMessage(String.format(NOKIA_SYSTEM_MESSAGE_PATTERN, RMOD));
    }

    @Test(priority = 4, description = "Check Rules Definitions for Huawei", dependsOnMethods = {"createHuaweiDefinitions"})
    @Description("Check Rules Definitions for Huawei")
    public void checkHuaweiDefinitions() {
        openValidationRulesManager();
        validationRulesManagerPage = new ValidationRulesManagerPage(driver);
        validationRulesManagerPage.searchGroup(HUAWEI_VALIDATION_RULES_GROUP_NAME);
        waitForPageToLoad();
        validationRulesManagerPage.selectGroup(HUAWEI_VALIDATION_RULES_GROUP_NAME);
        waitForPageToLoad();
        SoftAssert huaweiSoftAssert = new SoftAssert();
        Assert.assertEquals(validationRulesManagerPage.getSimpleValueCheckTable().getTotalCount(), 7, INCORRECT_DEFINITIONS_TOTAL_A1);

        int rowNumber = validationRulesManagerPage.getRowNumberFromSimpleValueCheckTable(CELLRADIUS);
        SimpleValueCheckDefinition a1Values1 = validationRulesManagerPage.getSimpleValueCheckTableValues(rowNumber);
        a1Values1.getInterfaceName().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values1.getInterfaceName().get(), HUAWEI_INTERFACE_NAME, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.CM_INTERFACE_NAME_COLUMN)));
        a1Values1.getState().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values1.getState().get(), ENABLE_VALUE, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.STATE_COLUMN)));
        a1Values1.getObjectType().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values1.getObjectType().get(), CELL, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.OBJECT_NAME_COLUMN)));
        a1Values1.getAttributeName().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values1.getAttributeName().get(), CELLRADIUS, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.ATTRIBUTE_NAME_COLUMN)));
        huaweiSoftAssert.assertEquals(a1Values1.getOperator(), BETWEEN_OR_EQUAL_TO_OPERATOR, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.OPERATION_COLUMN));
        a1Values1.getValue1().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values1.getValue1().get(), CELLRADIUS_VALUE1, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.VALUE_1_COLUMN)));
        a1Values1.getValue2().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values1.getValue2().get(), String.valueOf(CELLRADIUS_VALUE2), String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.VALUE_2_COLUMN)));

        rowNumber = validationRulesManagerPage.getRowNumberFromSimpleValueCheckTable(EMCENABLE);
        SimpleValueCheckDefinition a1Values2 = validationRulesManagerPage.getSimpleValueCheckTableValues(rowNumber);
        a1Values2.getInterfaceName().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values2.getInterfaceName().get(), HUAWEI_INTERFACE_NAME, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.CM_INTERFACE_NAME_COLUMN)));
        a1Values2.getState().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values2.getState().get(), ENABLE_VALUE, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.STATE_COLUMN)));
        a1Values2.getObjectType().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values2.getObjectType().get(), EMC, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.OBJECT_NAME_COLUMN)));
        a1Values2.getAttributeName().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values2.getAttributeName().get(), EMCENABLE, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.ATTRIBUTE_NAME_COLUMN)));
        huaweiSoftAssert.assertEquals(a1Values2.getOperator(), NOT_FOUND_OPERATOR, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.OPERATION_COLUMN));
        a1Values2.getValue1().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values2.getValue1().get(), "", String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.VALUE_1_COLUMN)));
        a1Values2.getValue2().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values2.getValue2().get(), "", String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.VALUE_2_COLUMN)));

        rowNumber = validationRulesManagerPage.getRowNumberFromSimpleValueCheckTable(TAC);
        SimpleValueCheckDefinition a1Values3 = validationRulesManagerPage.getSimpleValueCheckTableValues(rowNumber);
        a1Values3.getInterfaceName().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values3.getInterfaceName().get(), HUAWEI_INTERFACE_NAME, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.CM_INTERFACE_NAME_COLUMN)));
        a1Values3.getState().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values3.getState().get(), ENABLE_VALUE, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.STATE_COLUMN)));
        a1Values3.getObjectType().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values3.getObjectType().get(), EUTRANEXTERNALCELL, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.OBJECT_NAME_COLUMN)));
        a1Values3.getAttributeName().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values3.getAttributeName().get(), TAC, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.ATTRIBUTE_NAME_COLUMN)));
        huaweiSoftAssert.assertEquals(a1Values3.getOperator(), MORE_THAN_OPERATOR, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.OPERATION_COLUMN));
        a1Values3.getValue1().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values3.getValue1().get(), TAC_VALUE1, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.VALUE_1_COLUMN)));
        a1Values3.getValue2().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values3.getValue2().get(), "", String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.VALUE_2_COLUMN)));

        rowNumber = validationRulesManagerPage.getRowNumberFromSimpleValueCheckTable(DATE_OF_LAST_SERVICE);
        SimpleValueCheckDefinition a1Values4 = validationRulesManagerPage.getSimpleValueCheckTableValues(rowNumber);
        a1Values4.getInterfaceName().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values4.getInterfaceName().get(), HUAWEI_INTERFACE_NAME, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.CM_INTERFACE_NAME_COLUMN)));
        a1Values4.getState().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values4.getState().get(), ENABLE_VALUE, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.STATE_COLUMN)));
        a1Values4.getObjectType().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values4.getObjectType().get(), MBTS_PORT, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.OBJECT_NAME_COLUMN)));
        a1Values4.getAttributeName().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values4.getAttributeName().get(), DATE_OF_LAST_SERVICE, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.ATTRIBUTE_NAME_COLUMN)));
        huaweiSoftAssert.assertEquals(a1Values4.getOperator(), NOT_EMPTY_OPERATOR, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.OPERATION_COLUMN));
        a1Values4.getValue1().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values4.getValue1().get(), "", String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.VALUE_1_COLUMN)));
        a1Values4.getValue2().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values4.getValue2().get(), "", String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.VALUE_2_COLUMN)));

        rowNumber = validationRulesManagerPage.getRowNumberFromSimpleValueCheckTable(DLBANDWIDTH);
        SimpleValueCheckDefinition a1Values5 = validationRulesManagerPage.getSimpleValueCheckTableValues(rowNumber);
        a1Values5.getInterfaceName().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values5.getInterfaceName().get(), HUAWEI_INTERFACE_NAME, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.CM_INTERFACE_NAME_COLUMN)));
        a1Values5.getState().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values5.getState().get(), ENABLE_VALUE, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.STATE_COLUMN)));
        a1Values5.getObjectType().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values5.getObjectType().get(), CELL, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.OBJECT_NAME_COLUMN)));
        a1Values5.getAttributeName().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values5.getAttributeName().get(), DLBANDWIDTH, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.ATTRIBUTE_NAME_COLUMN)));
        huaweiSoftAssert.assertEquals(a1Values5.getOperator(), LESS_THAN_OPERATOR, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.OPERATION_COLUMN));
        a1Values5.getValue1().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values5.getValue1().get(), DLBANDWIDTH_VALUE1, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.VALUE_1_COLUMN)));
        a1Values5.getValue2().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values5.getValue2().get(), "", String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.VALUE_2_COLUMN)));

        rowNumber = validationRulesManagerPage.getRowNumberFromSimpleValueCheckTable(PRODUCTVERSION);
        SimpleValueCheckDefinition a1Values6 = validationRulesManagerPage.getSimpleValueCheckTableValues(rowNumber);
        a1Values6.getInterfaceName().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values6.getInterfaceName().get(), HUAWEI_INTERFACE_NAME, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.CM_INTERFACE_NAME_COLUMN)));
        a1Values6.getState().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values6.getState().get(), ENABLE_VALUE, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.STATE_COLUMN)));
        a1Values6.getObjectType().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values6.getObjectType().get(), ENODEB_FUNCTION_MODULE, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.OBJECT_NAME_COLUMN)));
        a1Values6.getAttributeName().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values6.getAttributeName().get(), PRODUCTVERSION, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.ATTRIBUTE_NAME_COLUMN)));
        huaweiSoftAssert.assertEquals(a1Values6.getOperator(), EQUAL_TO_OPERATOR, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.OPERATION_COLUMN));
        a1Values6.getValue1().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values6.getValue1().get(), PRODUCTVERSION_VALUE1, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.VALUE_1_COLUMN)));
        a1Values6.getValue2().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values6.getValue2().get(), "", String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.VALUE_2_COLUMN)));

        rowNumber = validationRulesManagerPage.getRowNumberFromSimpleValueCheckTable(PHYCELLID);
        SimpleValueCheckDefinition a1Values7 = validationRulesManagerPage.getSimpleValueCheckTableValues(rowNumber);
        a1Values7.getInterfaceName().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values7.getInterfaceName().get(), HUAWEI_INTERFACE_NAME, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.CM_INTERFACE_NAME_COLUMN)));
        a1Values7.getState().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values7.getState().get(), ENABLE_VALUE, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.STATE_COLUMN)));
        a1Values7.getObjectType().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values7.getObjectType().get(), CELL, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.OBJECT_NAME_COLUMN)));
        a1Values7.getAttributeName().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values7.getAttributeName().get(), PHYCELLID, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.ATTRIBUTE_NAME_COLUMN)));
        huaweiSoftAssert.assertEquals(a1Values7.getOperator(), OUTSIDE_OPERATOR, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.OPERATION_COLUMN));
        a1Values7.getValue1().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values7.getValue1().get(), PHYCELLID_VALUE1, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.VALUE_1_COLUMN)));
        a1Values7.getValue2().ifPresent(value ->
                huaweiSoftAssert.assertEquals(a1Values7.getValue2().get(), String.valueOf(PHYCELLID_VALUE2), String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.VALUE_2_COLUMN)));

        huaweiSoftAssert.assertAll();
        validationRulesManagerPage.selectComplexValueCheckTab();
        waitForPageToLoad();
        Assert.assertTrue(validationRulesManagerPage.getComplexValueCheckTable().hasNoData(), INCORRECT_DEFINITIONS_TOTAL_A2);
        validationRulesManagerPage.selectObjectCardinalityCheckTab();
        waitForPageToLoad();
        Assert.assertTrue(validationRulesManagerPage.getObjectCardinalityCheckTable().hasNoData(), INCORRECT_DEFINITIONS_TOTAL_B1);
        validationRulesManagerPage.selectCardinalityAndValuesCheckTab();
        waitForPageToLoad();
        Assert.assertTrue(validationRulesManagerPage.getCardinalityAndValuesCheckTable().hasNoData(), INCORRECT_DEFINITIONS_TOTAL_B2);
    }

    @Test(priority = 5, description = "Check Rules Definitions for Nokia", dependsOnMethods = {"createNokiaDefinitions"})
    @Description("Check Rules Definitions for Nokia")
    public void checkNokiaDefinitions() {
        openValidationRulesManager();
        validationRulesManagerPage = new ValidationRulesManagerPage(driver);
        validationRulesManagerPage.searchGroup(NOKIA_VALIDATION_RULES_GROUP_NAME);
        waitForPageToLoad();
        validationRulesManagerPage.selectGroup(NOKIA_VALIDATION_RULES_GROUP_NAME);
        waitForPageToLoad();
        SoftAssert nokiaSoftAssert = new SoftAssert();
        Assert.assertEquals(validationRulesManagerPage.getSimpleValueCheckTable().getTotalCount(), 1, INCORRECT_DEFINITIONS_TOTAL_A1);
        SimpleValueCheckDefinition a1Values = validationRulesManagerPage.getSimpleValueCheckTableValues(0);
        a1Values.getInterfaceName().ifPresent(value ->
                nokiaSoftAssert.assertEquals(a1Values.getInterfaceName().get(), NOKIA_INTERFACE_NAME, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.CM_INTERFACE_NAME_COLUMN)));
        a1Values.getState().ifPresent(value ->
                nokiaSoftAssert.assertEquals(a1Values.getState().get(), ENABLE_VALUE, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.STATE_COLUMN)));
        a1Values.getObjectType().ifPresent(value ->
                nokiaSoftAssert.assertEquals(a1Values.getObjectType().get(), PLMN, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.OBJECT_NAME_COLUMN)));
        a1Values.getAttributeName().ifPresent(value ->
                nokiaSoftAssert.assertEquals(a1Values.getAttributeName().get(), VERSION, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.ATTRIBUTE_NAME_COLUMN)));
        nokiaSoftAssert.assertEquals(a1Values.getOperator(), EQUAL_TO_OPERATOR, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.OPERATION_COLUMN));
        a1Values.getValue1().ifPresent(value ->
                nokiaSoftAssert.assertEquals(a1Values.getValue1().get(), VERSION_VALUE1, String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.VALUE_1_COLUMN)));
        a1Values.getValue2().ifPresent(value ->
                nokiaSoftAssert.assertEquals(a1Values.getValue2().get(), "", String.format(INCORRECT_VALUE_A1_PATTERN, ValidationRulesManagerPage.VALUE_2_COLUMN)));

        Assert.assertEquals(validationRulesManagerPage.getComplexValueCheckTable().getTotalCount(), 1, INCORRECT_DEFINITIONS_TOTAL_A2);
        ComplexValueCheckDefinition a2Values = validationRulesManagerPage.getComplexValueCheckTableValues(0);
        a2Values.getInterfaceName().ifPresent(value ->
                nokiaSoftAssert.assertEquals(a2Values.getInterfaceName().get(), NOKIA_INTERFACE_NAME, String.format(INCORRECT_VALUE_A2_PATTERN, ValidationRulesManagerPage.CM_INTERFACE_NAME_COLUMN)));
        a2Values.getState().ifPresent(value ->
                nokiaSoftAssert.assertEquals(a2Values.getState().get(), ENABLE_VALUE, String.format(INCORRECT_VALUE_A2_PATTERN, ValidationRulesManagerPage.STATE_COLUMN)));
        a2Values.getObjectType().ifPresent(value ->
                nokiaSoftAssert.assertEquals(a2Values.getObjectType().get(), ANR, String.format(INCORRECT_VALUE_A2_PATTERN, ValidationRulesManagerPage.OBJECT_TYPE_COLUMN)));
        nokiaSoftAssert.assertEquals(Arrays.stream(a2Values.getRuleDefinitions().split(", ")).sorted().collect(Collectors.toList()), ANR_ATTRIBUTES_LIST, String.format(INCORRECT_VALUE_A2_PATTERN, ValidationRulesManagerPage.CHECKED_ATTRIBUTES_COLUMN));

        Assert.assertEquals(validationRulesManagerPage.getObjectCardinalityCheckTable().getTotalCount(), 1, INCORRECT_DEFINITIONS_TOTAL_B1);
        ObjectCardinalityCheckDefinition b1Values = validationRulesManagerPage.getObjectCardinalityCheckTableValues(0);
        b1Values.getInterfaceName().ifPresent(value ->
                nokiaSoftAssert.assertEquals(b1Values.getInterfaceName().get(), NOKIA_INTERFACE_NAME, String.format(INCORRECT_VALUE_B1_PATTERN, ValidationRulesManagerPage.CM_INTERFACE_NAME_COLUMN)));
        b1Values.getState().ifPresent(value ->
                nokiaSoftAssert.assertEquals(b1Values.getState().get(), ENABLE_VALUE, String.format(INCORRECT_VALUE_B1_PATTERN, ValidationRulesManagerPage.STATE_COLUMN)));
        nokiaSoftAssert.assertEquals(b1Values.getParentObjectType(), LNBTS, String.format(INCORRECT_VALUE_B1_PATTERN, ValidationRulesManagerPage.PARENT_OBJECT_TYPE_COLUMN));
        b1Values.getChildObjectType().ifPresent(value ->
                nokiaSoftAssert.assertEquals(b1Values.getChildObjectType().get(), LNADJ, String.format(INCORRECT_VALUE_B1_PATTERN, ValidationRulesManagerPage.CHILD_OBJECT_TYPE_COLUMN)));
        nokiaSoftAssert.assertEquals(b1Values.getExpectedCardinality(), String.valueOf(LNADJ_CARDINALITY), String.format(INCORRECT_VALUE_B1_PATTERN, ValidationRulesManagerPage.EXPECTED_CARDINALITY_COLUMN));

        Assert.assertEquals(validationRulesManagerPage.getCardinalityAndValuesCheckTable().getTotalCount(), 1, INCORRECT_DEFINITIONS_TOTAL_B2);
        CardinalityAndValueCheckDefinition b2Values = validationRulesManagerPage.getCardinalityAndValuesCheckTableValues(0);
        b2Values.getInterfaceName().ifPresent(value ->
                nokiaSoftAssert.assertEquals(b2Values.getInterfaceName().get(), NOKIA_INTERFACE_NAME, String.format(INCORRECT_VALUE_B2_PATTERN, ValidationRulesManagerPage.CM_INTERFACE_NAME_COLUMN)));
        b2Values.getState().ifPresent(value ->
                nokiaSoftAssert.assertEquals(b2Values.getState().get(), ENABLE_VALUE, String.format(INCORRECT_VALUE_B2_PATTERN, ValidationRulesManagerPage.STATE_COLUMN)));
        nokiaSoftAssert.assertEquals(b2Values.getParentObjectType(), APEQM, String.format(INCORRECT_VALUE_B2_PATTERN, ValidationRulesManagerPage.PARENT_OBJECT_TYPE_COLUMN));
        b2Values.getChildObjectType().ifPresent(value ->
                nokiaSoftAssert.assertEquals(b2Values.getChildObjectType().get(), RMOD, String.format(INCORRECT_VALUE_B2_PATTERN, ValidationRulesManagerPage.CHILD_OBJECT_TYPE_COLUMN)));
        nokiaSoftAssert.assertEquals(Arrays.stream(b2Values.getRuleDefinitions().split(", ")).sorted().collect(Collectors.toList()), RMOD_ATTRIBUTES_LIST, String.format(INCORRECT_VALUE_B2_PATTERN, ValidationRulesManagerPage.CHECKED_ATTRIBUTES_COLUMN));
        b2Values.getExpectedCardinality().ifPresent(value ->
                nokiaSoftAssert.assertEquals(b2Values.getExpectedCardinality().get(), String.valueOf(RMOD_CARDINALITY), String.format(INCORRECT_VALUE_B2_PATTERN, ValidationRulesManagerPage.EXPECTED_CARDINALITY_COLUMN)));

        nokiaSoftAssert.assertAll();
    }

    @Test(priority = 6, description = "Validation Rules Group teardown", dependsOnMethods = {"createVRG"})
    @Description("Validation Rules Group teardown")
    public void teardown() {
        openValidationRulesManager();
        validationRulesManagerPage = new ValidationRulesManagerPage(driver);
        validationRulesManagerPage.deleteGroup(HUAWEI_VALIDATION_RULES_GROUP_NAME);
        waitForPageToLoad();
        validationRulesManagerPage.deleteGroup(NOKIA_VALIDATION_RULES_GROUP_NAME);
        waitForPageToLoad();
        SoftAssert teardownSoftAssert = new SoftAssert();
        teardownSoftAssert.assertFalse(validationRulesManagerPage.isGroupPresent(HUAWEI_VALIDATION_RULES_GROUP_NAME), String.format(GROUP_PRESENT_LOG_PATTERN, HUAWEI_VALIDATION_RULES_GROUP_NAME));
        teardownSoftAssert.assertFalse(validationRulesManagerPage.isGroupPresent(NOKIA_VALIDATION_RULES_GROUP_NAME), String.format(GROUP_PRESENT_LOG_PATTERN, NOKIA_VALIDATION_RULES_GROUP_NAME));
        teardownSoftAssert.assertAll();
    }

    @Test(priority = 7, description = "Checking system message summary")
    @Description("Checking system message summary")
    public void systemMessageSummary() {
        softAssert.assertAll();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void openValidationRulesManager() {
        driver.get(validationRulesManagerUrl);
        waitForPageToLoad();
    }

    private void searchAndSelectObjectType(String objectType) {
        waitForPageToLoad();
        metamodelPage = new MetamodelPage(driver);
        metamodelPage.searchForObject(objectType);
        waitForPageToLoad();
        Assert.assertTrue(metamodelPage.isObjectDisplayed(objectType));
        metamodelPage.selectObjectType(objectType);
        waitForPageToLoad();
    }

    private void openA1RuleWizard() {
        metamodelPage = new MetamodelPage(driver);
        metamodelPage.selectSimpleValueCheckTab();
        waitForPageToLoad();
        metamodelPage.openRuleDefinitionWizard();
        waitForPageToLoad();
    }

    private void createA2Rule() {
        metamodelPage = new MetamodelPage(driver);
        metamodelPage.selectComplexValueCheckTab();
        waitForPageToLoad();
        metamodelPage.openRuleDefinitionWizard();
        waitForPageToLoad();
        ruleDefinitionWizardPage = new RuleDefinitionWizardPage(driver);
        ComplexValueCheckDefinition def = ComplexValueCheckDefinition.builder()
                .setGroupName(NOKIA_VALIDATION_RULES_GROUP_NAME)
                .setRuleDefinitions(ANR_RULE_DEFINITIONS)
                .build();
        ruleDefinitionWizardPage.fillComplexValueCheckDefinition(def);
        ruleDefinitionWizardPage.validate();
        waitForPageToLoad();
        Assert.assertFalse(ruleDefinitionWizardPage.isValidationMessagePresent(), VALIDATION_MESSAGE_IS_PRESENT);
        ruleDefinitionWizardPage.accept();
    }

    private void createB1Rule() {
        metamodelPage = new MetamodelPage(driver);
        metamodelPage.selectObjectCardinalityCheckTab();
        waitForPageToLoad();
        metamodelPage.openRuleDefinitionWizard();
        waitForPageToLoad();
        ruleDefinitionWizardPage = new RuleDefinitionWizardPage(driver);
        ObjectCardinalityCheckDefinition def = ObjectCardinalityCheckDefinition.builder()
                .setGroupName(NOKIA_VALIDATION_RULES_GROUP_NAME)
                .setParentObjectType(LNBTS)
                .setExpectedCardinality(LNADJ_CARDINALITY)
                .build();
        ruleDefinitionWizardPage.createObjectCardinalityCheckDefinition(def);
    }

    private void createB2Rule() {
        metamodelPage = new MetamodelPage(driver);
        metamodelPage.selectCardinalityAndValuesCheckTab();
        waitForPageToLoad();
        metamodelPage.openRuleDefinitionWizard();
        waitForPageToLoad();
        ruleDefinitionWizardPage = new RuleDefinitionWizardPage(driver);
        CardinalityAndValueCheckDefinition def = CardinalityAndValueCheckDefinition.builder()
                .setGroupName(NOKIA_VALIDATION_RULES_GROUP_NAME)
                .setParentObjectType(APEQM)
                .setRuleDefinitions(RMOD_RULE_DEFINITIONS)
                .build();
        ruleDefinitionWizardPage.fillCardinalityAndValueCheckDefinition(def);
        ruleDefinitionWizardPage.validate();
        waitForPageToLoad();
        Assert.assertFalse(ruleDefinitionWizardPage.isValidationMessagePresent(), VALIDATION_MESSAGE_IS_PRESENT);
        ruleDefinitionWizardPage.accept();
    }

    private void checkMessage(String systemMessageLog) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, new WebDriverWait(driver, Duration.ofSeconds(90)));
        Optional<SystemMessageContainer.Message> firstSystemMessage = systemMessage.getFirstMessage();
        softAssert.assertTrue(firstSystemMessage.isPresent(), systemMessageLog);
        if (firstSystemMessage.isPresent()) {
            SystemMessageContainer.Message message = systemMessage.getFirstMessage()
                    .orElseThrow(() -> new RuntimeException("The list is empty"));
            softAssert.assertEquals(message.getMessageType(), SystemMessageContainer.MessageType.SUCCESS, systemMessageLog);
            softAssert.assertEquals(message.getText(), BucOssNar002Test.RULE_SAVED_MESSAGE, systemMessageLog);
            systemMessage.close();
        }
        waitForPageToLoad();
    }
}
