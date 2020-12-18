package com.oss.transport;

import com.oss.BaseTestCase;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.transport.ipam.IPAddressManagementViewPage;
import com.oss.pages.transport.ipam.IPSubnetWizardPage;
import com.oss.pages.transport.helper.IPSubnetFilterProperties;
import com.oss.pages.transport.helper.IPSubnetWizardProperties;
import com.oss.pages.transport.ipam.RoleViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Ewa Frączek
 */

@Listeners({ TestListener.class })
public class IPAMTest extends BaseTestCase {
    protected IPAddressManagementViewPage ipAddressManagementViewPage;
    protected NewInventoryViewPage newInventoryViewPage;
    private String name = "IPam4SeleniumTest";
    private String description = "Description";
    private String nameUpdated = "IPamSeleniumTestUpdated";
    private String descriptionUpdated = "DescriptionUpdated";

    private static final String NETWORK_INVENTORY_TYPE = "IPNetwork";
    private static final String IPV4_SUBNET_INVENTORY_TYPE = "IPv4Subnet";
    private static final String NETWORK_NAME_FILTER_FOR_SUBNET_INVENTORY_VIEW = "networkName";

    private static final String NETWORK_PROPERTY_NAME = "Name";
    private static final String NETWORK_PROPERTY_DESCRIPTION = "Description";
    private static final String NETWORK_INVENTORY_PROPERTY_NAME = "name";
    private static final String NETWORK_INVENTORY_PROPERTY_DESCRIPTION = "description";

    private static final String ROLE_NAME = "IPAMSeleniumTest1";
    private static final String ROLE_NAME_UPDATED = "IPAMSeleniumTestUpdated1";

    private static final String SUBNET_PROPERTY_IDENTIFIER = "Identifier";
    private static final String SUBNET_PROPERTY_ADDRESS = "Address";
    private static final String SUBNET_PROPERTY_HIGHEST_IP_ADDRESS = "Highest IP Address";
    private static final String SUBNET_PROPERTY_BROADCAST_IP_ADDRESS = "Broadcast IP Address";
    private static final String SUBNET_PROPERTY_IP_NETWORK_NAME = "IP network name";
    private static final String SUBNET_PROPERTY_MASK_LENGTH = "Mask length";
    private static final String SUBNET_PROPERTY_SUBNET_TYPE = "Subnet type";
    private static final String SUBNET_PROPERTY_ROLE = "Role";
    private static final String SUBNET_PROPERTY_PERCENT_FREE = "Percent free";
    private static final String SUBNET_PROPERTY_CHILD_COUNT = "Child count";
    private static final String SUBNET_PROPERTY_DESCRIPTION = "Description";

    private static final int AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_CREATION = 3;
    private static final int AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_SPLIT = 2;
    private static final int AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_MERGE = 3;
    private static final String NETWORK_SUBNET_TYPE = "Network";
    private static final String BLOCK_SUBNET_TYPE = "Block";
    private static final String MANAGEMENT_PRIMARY_ROLE = "Management - Primary";
    private static final String MANAGEMENT_SECONDARY_ROLE = "Management - Secondary";
    private static final String SUBNET_DESCRIPTION = "description";
    private static final String SUBNET_DESCRIPTION_UPDATED = "description2";
    private static final String FILTER_SUBNETS_START_IP = "126000";
    private static final String FILTER_SUBNETS_END_IP_FOR_CREATION = "12600128";
    private static final String FILTER_SUBNETS_END_IP_FOR_SPLIT = "126000";
    private static final String OPERATOR_HIGHER_OR_EQUAL = ">=";
    private static final String HIGHEST_IP_SUBNET_MASK = "24";

    private Map<String, String> firstIPSubnetProperties = new HashMap<>();
    private Map<String, String> secondIPSubnetProperties = new HashMap<>();
    private Map<String, String> thirdIPSubnetProperties = new HashMap<>();
    private Map<String, String> fourthIPSubnetProperties = new HashMap<>();
    private Map<String, String> fifthIPSubnetProperties = new HashMap<>();

    @BeforeClass
    public void prepareTest() {
        DelayUtils.sleep(1000);
        ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementViewPageLive(driver, BASIC_URL);
    }

    @Test(priority = 1)
    @Description("Create Role")
    public void createRole() {
        RoleViewPage roleViewPage = ipAddressManagementViewPage.openRoleView();
        roleViewPage.createRole(ROLE_NAME);
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
        Assert.assertTrue(roleViewPage.doesRoleNameExist(ROLE_NAME));
        roleViewPage.exitRoleView();
    }

    @Test(priority = 2)
    @Description("Create IP Network")
    public void createIPNetwork() {
        ipAddressManagementViewPage.selectFirstTreeRow();
        ipAddressManagementViewPage.createIPNetwork(name, description);
        DelayUtils.sleep(100);
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
    }

    @Test(priority = 3)
    @Description("Check IPAM Tree after Network Creation")
    public void checkIPAMTreeAfterNetworkCreation() {
        ipAddressManagementViewPage.selectTreeRow(name);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(NETWORK_PROPERTY_NAME), name);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(NETWORK_PROPERTY_DESCRIPTION), description);
    }

    @Test(priority = 4)
    @Description("Create IP Subnets")
    public void createIPSubnets() {
        IPSubnetWizardPage ipSubnetWizardPage = ipAddressManagementViewPage.createIPv4Subnet();
        IPSubnetFilterProperties subnetFilterProperties = new IPSubnetFilterProperties(FILTER_SUBNETS_START_IP, FILTER_SUBNETS_END_IP_FOR_CREATION, OPERATOR_HIGHER_OR_EQUAL, HIGHEST_IP_SUBNET_MASK);
        ipSubnetWizardPage.ipSubnetWizardSelectStep(subnetFilterProperties, AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_CREATION);
        IPSubnetWizardProperties firstIpSubnetWizardProperties = new IPSubnetWizardProperties(BLOCK_SUBNET_TYPE, ROLE_NAME, SUBNET_DESCRIPTION);
        IPSubnetWizardProperties secondIpSubnetWizardProperties = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE);
        IPSubnetWizardProperties thirdIpSubnetWizardProperties = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE);
        ipSubnetWizardPage.ipSubnetWizardPropertiesStep(firstIpSubnetWizardProperties, secondIpSubnetWizardProperties, thirdIpSubnetWizardProperties);
        ipSubnetWizardPage.ipSubnetWizardSummaryStep();

        putInitialSubnetsProperties();
    }

    @Test(priority = 5)
    @Description("Check IPAM tree after Subnets creation")
    public void checkIPAMTreeAfterSubnetsCreation() {
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.selectTreeRow(name);
        ipAddressManagementViewPage.selectTreeRow(name);
        ipAddressManagementViewPage.expandTreeRow(name);
        DelayUtils.sleep(100);
        checkSubnetAttributesOnIPAMTree(firstIPSubnetProperties);
        ipAddressManagementViewPage.expandTreeRowContains(getSubnetAddressAndMask(firstIPSubnetProperties));
        checkSubnetAttributesOnIPAMTree(secondIPSubnetProperties);
        checkSubnetAttributesOnIPAMTree(thirdIPSubnetProperties);
    }

    @Test(priority = 6)
    @Description("Edit Role")
    public void editRole() {
        RoleViewPage roleViewPage = ipAddressManagementViewPage.openRoleView();
        roleViewPage.editRole(ROLE_NAME, ROLE_NAME_UPDATED);
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
        Assert.assertTrue(roleViewPage.doesRoleNameExist(ROLE_NAME_UPDATED));
        Assert.assertFalse(roleViewPage.doesRoleNameExist(ROLE_NAME));
        roleViewPage.exitRoleView();

        updateSubnetPropertiesAfterRoleEdition();
    }

    @Test(priority = 7)
    @Description("Check IPAM Tree after role edition")
    public void checkIpamTreeAfterRoleEdition() {
        ipAddressManagementViewPage.selectTreeRowContains(getSubnetAddressAndMask(firstIPSubnetProperties));
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(SUBNET_PROPERTY_ROLE), firstIPSubnetProperties.get(SUBNET_PROPERTY_ROLE));
        ipAddressManagementViewPage.selectTreeRowContains(getSubnetAddressAndMask(firstIPSubnetProperties));
    }

    // bug OSSTPT-30469
    @Test(priority = 8, enabled = false)
    @Description("Edit IP Network")
    public void editIPNetwork() {
        ipAddressManagementViewPage.editIPNetwork(name, nameUpdated, descriptionUpdated);
        updateSubnetsPropertiesAfterIPNetworkEdition();
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
    }

    // bug OSSTPT-30469
    @Test(priority = 9, enabled = false)
    @Description("Check IPAM Tree after IP Network Edition")
    public void checkIPAMTreeAfterNetworkEdition() {
        ipAddressManagementViewPage.selectTreeRow(nameUpdated);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(NETWORK_PROPERTY_NAME), nameUpdated);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(NETWORK_PROPERTY_DESCRIPTION), descriptionUpdated);

        ipAddressManagementViewPage.selectTreeRow(nameUpdated);
        ipAddressManagementViewPage.expandTreeRow(nameUpdated);
        checkSubnetAttributesOnIPAMTree(firstIPSubnetProperties);
        ipAddressManagementViewPage.expandTreeRowContains(getSubnetAddressAndMask(firstIPSubnetProperties));
        checkSubnetAttributesOnIPAMTree(secondIPSubnetProperties);
        checkSubnetAttributesOnIPAMTree(thirdIPSubnetProperties);
    }

    @Test(priority = 10)
    @Description("Split IP Subnet")
    public void splitIPSubnet() {
        IPSubnetWizardPage ipSubnetWizardPage = ipAddressManagementViewPage.splitIPv4Subnet(getSubnetAddressAndMask(secondIPSubnetProperties));
        IPSubnetFilterProperties subnetFilterProperties = new IPSubnetFilterProperties(FILTER_SUBNETS_START_IP, FILTER_SUBNETS_END_IP_FOR_SPLIT);
        ipSubnetWizardPage.ipSubnetWizardSelectStep(subnetFilterProperties, AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_SPLIT);
        IPSubnetWizardProperties firstIpSubnetWizardProperties = new IPSubnetWizardProperties(BLOCK_SUBNET_TYPE, MANAGEMENT_SECONDARY_ROLE, SUBNET_DESCRIPTION);
        IPSubnetWizardProperties secondIpSubnetWizardProperties = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE);
        ipSubnetWizardPage.ipSubnetWizardPropertiesStep(firstIpSubnetWizardProperties, secondIpSubnetWizardProperties);
        ipSubnetWizardPage.ipSubnetWizardSummaryStep();

        updateSubnetsAfterIPSubnetsSplit();
    }

    @Test(priority = 11)
    @Description("Check IPAM Tree after Subnet Split")
    public void checkIPAMTreeAfterSubnetSplit() {
        checkSubnetAttributesOnIPAMTree(secondIPSubnetProperties);
        checkSubnetAttributesOnIPAMTree(fourthIPSubnetProperties);
        ipAddressManagementViewPage.expandTreeRowContains(getSubnetAddressAndMask(fourthIPSubnetProperties));
        checkSubnetAttributesOnIPAMTree(fifthIPSubnetProperties);
    }

    @Test(priority = 12)
    @Description("Merge IP Subnets")
    public void mergeIPSubnets() {
        IPSubnetWizardPage ipSubnetWizardPage = ipAddressManagementViewPage
                .mergeIPv4Subnet(getSubnetAddressAndMask(secondIPSubnetProperties),
                        getSubnetAddressAndMask(fourthIPSubnetProperties), getSubnetAddressAndMask(fifthIPSubnetProperties));
        ipSubnetWizardPage.ipSubnetWizardSelectStep(AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_MERGE);
        IPSubnetWizardProperties firstIpSubnetWizardProperties = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE, MANAGEMENT_PRIMARY_ROLE, SUBNET_DESCRIPTION_UPDATED);
        ipSubnetWizardPage.ipSubnetWizardPropertiesStep(firstIpSubnetWizardProperties);
        ipSubnetWizardPage.ipSubnetWizardSummaryStep();

        updateSubnetsAfterIPSubnetsMerge();
    }

    @Test(priority = 13)
    @Description("Check IPAM Tree after Subnets Merge")
    public void checkIPAMTreeAfterSubnetMerge() {
        ipAddressManagementViewPage.expandTreeRow(name);
        ipAddressManagementViewPage.selectTreeRow(name);
        ipAddressManagementViewPage.selectTreeRow(name);
        ipAddressManagementViewPage.expandTreeRowContains(getSubnetAddressAndMask(firstIPSubnetProperties));
        checkSubnetAttributesOnIPAMTree(secondIPSubnetProperties);
    }

    @Test(priority = 14) // bug OSSTPT-31077
    @Description("Edit IP Subnets")
    public void editIPSubnets() {
        ipAddressManagementViewPage.changeIPSubnetTypeToBlock(getSubnetAddressAndMask(thirdIPSubnetProperties));
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
//        ipAddressManagementViewPage.selectTreeRow(name);
//        ipAddressManagementViewPage.selectTreeRow(name);
//        ipAddressManagementViewPage.expandTreeRow(name);
//        ipAddressManagementViewPage.expandTreeRowContains(getSubnetAddressAndMask(firstIPSubnetProperties));
//        ipAddressManagementViewPage.editIPv4Subnet(getSubnetAddressAndMask(secondIPSubnetProperties), "26", "Standard", "DESCR");
        updateSubnetsPropertiesAfterIPSubnetsEdition();
    }

    @Test(priority = 15) // bug OSSTPT-31077
    @Description("Check IPAM Tree after Subnets Edition")
    public void checkIPAMTreeAfterSubnetEdition() {
        ipAddressManagementViewPage.selectTreeRow(name);
        ipAddressManagementViewPage.selectTreeRow(name);
        ipAddressManagementViewPage.expandTreeRow(name);
        ipAddressManagementViewPage.expandTreeRowContains(getSubnetAddressAndMask(firstIPSubnetProperties));
//        checkSubnetAttributesOnIPAMTree(secondIPSubnetProperties);
        checkSubnetAttributesOnIPAMTree(thirdIPSubnetProperties);
    }

    // zamienic name na nameUpdated po poprawie buga OSSTPT-30469
    @Test (priority = 16)
    @Description("Check New Inventory View for IP Network")
    public void checkInventoryViewForIPNetwork() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, NETWORK_INVENTORY_TYPE);
        newInventoryViewPage.openFilterPanel().setValue(Input.ComponentType.TEXT_FIELD, NETWORK_INVENTORY_PROPERTY_NAME, name).applyFilter();
        newInventoryViewPage.getMainTable().selectFirstRow();
        DelayUtils.sleep(100);
        Assert.assertEquals(newInventoryViewPage.getPropertyPanel().getPropertyValue(NETWORK_INVENTORY_PROPERTY_NAME), name);
        Assert.assertEquals(newInventoryViewPage.getPropertyPanel().getPropertyValue(NETWORK_INVENTORY_PROPERTY_DESCRIPTION), description);
    }

    // bug OSSTPT-31074
    // zamienic name na nameUpdated po poprawie buga OSSTPT-30469
    @Test(priority = 17, enabled = false)
    @Description("Check New Inventory View for IP Subnets")
    public void checkInventoryViewForIPSubnets() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, IPV4_SUBNET_INVENTORY_TYPE);
        newInventoryViewPage.openFilterPanel().setValue(Input.ComponentType.TEXT_FIELD, NETWORK_NAME_FILTER_FOR_SUBNET_INVENTORY_VIEW, name).applyFilter();
        checkSubnetAttributesOnNewIV(firstIPSubnetProperties);
        checkSubnetAttributesOnNewIV(secondIPSubnetProperties);
        checkSubnetAttributesOnNewIV(thirdIPSubnetProperties);
    }

    // zamienic name na nameUpdated po poprawie buga OSSTPT-30469
    @Test(priority = 18)
    @Description("Delete IP Subnets")
    public void deleteIPSubnets() {
        ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementViewPageLive(driver, BASIC_URL);
        ipAddressManagementViewPage.expandTreeRow(name);
        ipAddressManagementViewPage.deleteObject(getSubnetAddressAndMask(firstIPSubnetProperties));
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
        ipAddressManagementViewPage.expandTreeRow(name);
        ipAddressManagementViewPage.deleteObject(getSubnetAddressAndMask(thirdIPSubnetProperties));
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
        ipAddressManagementViewPage.expandTreeRow(name);
        ipAddressManagementViewPage.deleteObject(getSubnetAddressAndMask(secondIPSubnetProperties));
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
    }

    // zamienic name na nameUpdated po poprawie buga OSSTPT-30469
    @Test(priority = 19)
    @Description("Delete IP Network")
    public void deleteIPNetwork() {
        ipAddressManagementViewPage.deleteObject(name);
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
    }

    @Test(priority = 20)
    @Description("Delete Role")
    public void deleteRole() {
        RoleViewPage roleViewPage = ipAddressManagementViewPage.openRoleView();
        roleViewPage.deleteRole(ROLE_NAME_UPDATED);
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
        Assert.assertFalse(roleViewPage.doesRoleNameExist(ROLE_NAME_UPDATED));
        roleViewPage.exitRoleView();
    }

    private void putInitialSubnetsProperties(){
        firstIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, "126.0.0.0/24 ["+name+"]");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_ADDRESS, "126.0.0.0");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_HIGHEST_IP_ADDRESS, "126.0.0.254");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_BROADCAST_IP_ADDRESS, "126.0.0.255");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, name);
        firstIPSubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, "24");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "BLOCK");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_ROLE, ROLE_NAME);
        firstIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "0%");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "2");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_DESCRIPTION, SUBNET_DESCRIPTION);

        secondIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, "126.0.0.0/25 ["+name+"]");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_ADDRESS, "126.0.0.0");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_HIGHEST_IP_ADDRESS, "126.0.0.126");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_BROADCAST_IP_ADDRESS, "126.0.0.127");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, name);
        secondIPSubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, "25");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "NETWORK");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "100%");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "0");

        thirdIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, "126.0.0.128/25 ["+name+"]");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_ADDRESS, "126.0.0.128");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_HIGHEST_IP_ADDRESS, "126.0.0.254");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_BROADCAST_IP_ADDRESS, "126.0.0.255");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, name);
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, "25");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "NETWORK");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "100%");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "0");
    }

    private void updateSubnetPropertiesAfterRoleEdition(){
        firstIPSubnetProperties.put(SUBNET_PROPERTY_ROLE, ROLE_NAME_UPDATED);
    }

    private void updateSubnetsPropertiesAfterIPNetworkEdition(){
        firstIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, firstIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS)+firstIPSubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH) +"["+nameUpdated+"]");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, nameUpdated);
        secondIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, secondIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS)+secondIPSubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH)+ "["+nameUpdated+"]");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, nameUpdated);
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, thirdIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS)+thirdIPSubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH)+ "["+nameUpdated+"]");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, nameUpdated);
    }

    private void updateSubnetsPropertiesAfterIPSubnetsEdition(){
//        secondIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, "126.0.0.0/26 ["+name+"]");
//        secondIPSubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, "26");
//        secondIPSubnetProperties.put(SUBNET_PROPERTY_ROLE, "Standard");
//        secondIPSubnetProperties.put(SUBNET_PROPERTY_DESCRIPTION, "DESCR");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "BLOCK");
    }

    private void updateSubnetsAfterIPSubnetsSplit(){
        secondIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "50%");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "1");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "BLOCK");

        fourthIPSubnetProperties = new HashMap<>();
        fourthIPSubnetProperties.put(SUBNET_PROPERTY_ADDRESS, "126.0.0.0");
        fourthIPSubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, "26");
        fourthIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "BLOCK");
        fourthIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "1");
        fourthIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "50%");
        fourthIPSubnetProperties.put(SUBNET_PROPERTY_DESCRIPTION, SUBNET_DESCRIPTION);
        fourthIPSubnetProperties.put(SUBNET_PROPERTY_ROLE, MANAGEMENT_SECONDARY_ROLE);

        fifthIPSubnetProperties = new HashMap<>();
        fifthIPSubnetProperties.put(SUBNET_PROPERTY_ADDRESS, "126.0.0.0");
        fifthIPSubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, "27");
        fifthIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "NETWORK");
        fifthIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "0");
        fifthIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "100%");
        fifthIPSubnetProperties.put(SUBNET_PROPERTY_DESCRIPTION, "");
    }

    private void updateSubnetsAfterIPSubnetsMerge() {
        secondIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "100%");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "0");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "NETWORK");
        fourthIPSubnetProperties.put(SUBNET_PROPERTY_DESCRIPTION, SUBNET_DESCRIPTION_UPDATED);
        fourthIPSubnetProperties.put(SUBNET_PROPERTY_ROLE, MANAGEMENT_PRIMARY_ROLE);
    }

    private void checkSubnetAttributesOnIPAMTree(Map<String, String> subnetProperties){
        ipAddressManagementViewPage.selectTreeRowContains(subnetProperties.get(SUBNET_PROPERTY_ADDRESS)+"/"+subnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH));
        Set<String> keySet = subnetProperties.keySet();
        for(String key: keySet){
            Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(key), subnetProperties.get(key));
        }
        ipAddressManagementViewPage.selectTreeRowContains(subnetProperties.get(SUBNET_PROPERTY_ADDRESS)+"/"+subnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH));
    }

    private void checkSubnetAttributesOnNewIV(Map<String, String> subnetProperties){
        newInventoryViewPage.getMainTable().typeIntoSearch(subnetProperties.get(SUBNET_PROPERTY_IDENTIFIER));
        newInventoryViewPage.getMainTable().selectFirstRow();
        DelayUtils.sleep(100);
        Set<String> keySet = subnetProperties.keySet();
        for(String key: keySet){
            if(!newInventoryViewPage.getPropertyPanel().getPropertyValue(key).isEmpty()){
                Assert.assertEquals(newInventoryViewPage.getPropertyPanel().getPropertyValue(key), subnetProperties.get(key));
            }
        }
    }

    private String getSubnetAddressAndMask(Map<String, String> subnetProperties){
        return subnetProperties.get(SUBNET_PROPERTY_ADDRESS)+"/"+subnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH);
    }
}