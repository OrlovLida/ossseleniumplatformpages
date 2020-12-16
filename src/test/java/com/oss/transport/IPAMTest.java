package com.oss.transport;

import com.oss.BaseTestCase;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.transport.IPAddressManagementViewPage;
import com.oss.pages.transport.IPSubnetWizardPage;
import com.oss.pages.transport.helper.IPSubnetFilterProperties;
import com.oss.pages.transport.helper.IPSubnetWizardProperties;
import com.oss.utils.TestListener;
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
    public void createIPNetwork() {
        ipAddressManagementViewPage.selectFirstTreeRow();
        ipAddressManagementViewPage.createIPNetwork(name, description);
    }

    @Test(priority = 2)
    public void checkIPAMTreeAfterNetworkCreation() {
        ipAddressManagementViewPage.selectTreeRow(name);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(NETWORK_PROPERTY_NAME), name);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(NETWORK_PROPERTY_DESCRIPTION), description);
    }

    @Test(priority = 3)
    public void createIPSubnets() {
        IPSubnetWizardPage ipSubnetWizardPage = ipAddressManagementViewPage.createIPv4Subnet();
        IPSubnetFilterProperties subnetFilterProperties = new IPSubnetFilterProperties(FILTER_SUBNETS_START_IP, FILTER_SUBNETS_END_IP_FOR_CREATION, OPERATOR_HIGHER_OR_EQUAL, HIGHEST_IP_SUBNET_MASK);
        ipSubnetWizardPage.ipSubnetWizardSelectStep(subnetFilterProperties, AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_CREATION);
        IPSubnetWizardProperties firstIpSubnetWizardProperties = new IPSubnetWizardProperties(BLOCK_SUBNET_TYPE, MANAGEMENT_PRIMARY_ROLE, SUBNET_DESCRIPTION);
        IPSubnetWizardProperties secondIpSubnetWizardProperties = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE);
        IPSubnetWizardProperties thirdIpSubnetWizardProperties = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE);
        ipSubnetWizardPage.ipSubnetWizardPropertiesStep(firstIpSubnetWizardProperties, secondIpSubnetWizardProperties, thirdIpSubnetWizardProperties);
        ipSubnetWizardPage.ipSubnetWizardSummaryStep();

        putInitialSubnetsProperties();
    }

    @Test(priority = 4)
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

    // bug OSSTPT-30469
    @Test(priority = 5, enabled = false)
    public void editIPNetwork() {
        ipAddressManagementViewPage.editIPNetwork(name, nameUpdated, descriptionUpdated);
        updateSubnetsPropertiesAfterIPNetworkEdition();
    }

    // bug OSSTPT-30469
    @Test(priority = 6, enabled = false)
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

    @Test(priority = 7)
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

    @Test(priority = 8)
    public void checkIPAMTreeAfterSubnetSplit() {
        checkSubnetAttributesOnIPAMTree(secondIPSubnetProperties);
        checkSubnetAttributesOnIPAMTree(fourthIPSubnetProperties);
        ipAddressManagementViewPage.expandTreeRowContains(getSubnetAddressAndMask(fourthIPSubnetProperties));
        checkSubnetAttributesOnIPAMTree(fifthIPSubnetProperties);
    }

    @Test(priority = 9)
    public void mergeIPSubnet() {
        IPSubnetWizardPage ipSubnetWizardPage = ipAddressManagementViewPage
                .mergeIPv4Subnet(getSubnetAddressAndMask(secondIPSubnetProperties),
                        getSubnetAddressAndMask(fourthIPSubnetProperties), getSubnetAddressAndMask(fifthIPSubnetProperties));
        ipSubnetWizardPage.ipSubnetWizardSelectStep(AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_MERGE);
        IPSubnetWizardProperties firstIpSubnetWizardProperties = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE, MANAGEMENT_PRIMARY_ROLE, SUBNET_DESCRIPTION_UPDATED);
        ipSubnetWizardPage.ipSubnetWizardPropertiesStep(firstIpSubnetWizardProperties);
        ipSubnetWizardPage.ipSubnetWizardSummaryStep();

        updateSubnetsAfterIPSubnetsMerge();
    }

    @Test(priority = 10)
    public void checkIPAMTreeAfterSubnetMerge() {
        ipAddressManagementViewPage.expandTreeRow(name);
        ipAddressManagementViewPage.selectTreeRow(name);
        ipAddressManagementViewPage.selectTreeRow(name);
        ipAddressManagementViewPage.expandTreeRowContains(getSubnetAddressAndMask(firstIPSubnetProperties));
        checkSubnetAttributesOnIPAMTree(secondIPSubnetProperties);
    }

    @Test(priority = 11) // bug OSSTPT-31077
    public void editIPSubnet() {
        ipAddressManagementViewPage.changeIPSubnetTypeToBlock(getSubnetAddressAndMask(thirdIPSubnetProperties));
//        ipAddressManagementViewPage.selectTreeRow(name);
//        ipAddressManagementViewPage.selectTreeRow(name);
//        ipAddressManagementViewPage.expandTreeRow(name);
//        ipAddressManagementViewPage.expandTreeRowContains(getSubnetAddressAndMask(firstIPSubnetProperties));
//        ipAddressManagementViewPage.editIPv4Subnet(getSubnetAddressAndMask(secondIPSubnetProperties), "26", "Standard", "DESCR");
        updateSubnetsPropertiesAfterIPSubnetsEdition();
    }

    @Test(priority = 12) // bug OSSTPT-31077
    public void checkIPAMTreeAfterSubnetEdition() {
        ipAddressManagementViewPage.selectTreeRow(name);
        ipAddressManagementViewPage.selectTreeRow(name);
        ipAddressManagementViewPage.expandTreeRow(name);
        ipAddressManagementViewPage.expandTreeRowContains(getSubnetAddressAndMask(firstIPSubnetProperties));
//        checkSubnetAttributesOnIPAMTree(secondIPSubnetProperties);
        checkSubnetAttributesOnIPAMTree(thirdIPSubnetProperties);
    }

    // zamienic name na nameUpdated po poprawie buga OSSTPT-30469
    @Test (priority = 13)
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
    @Test(priority = 14, enabled = false)
    public void checkInventoryViewForIPSubnet() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, IPV4_SUBNET_INVENTORY_TYPE);
        newInventoryViewPage.openFilterPanel().setValue(Input.ComponentType.TEXT_FIELD, NETWORK_NAME_FILTER_FOR_SUBNET_INVENTORY_VIEW, name).applyFilter();
        checkSubnetAttributesOnNewIV(firstIPSubnetProperties);
        checkSubnetAttributesOnNewIV(secondIPSubnetProperties);
        checkSubnetAttributesOnNewIV(thirdIPSubnetProperties);
    }

    // zamienic name na nameUpdated po poprawie buga OSSTPT-30469
    @Test(priority = 15)
    public void deleteIPSubnets() {
        ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementViewPageLive(driver, BASIC_URL);
        ipAddressManagementViewPage.expandTreeRow(name);
        ipAddressManagementViewPage.deleteObject(getSubnetAddressAndMask(firstIPSubnetProperties));
        ipAddressManagementViewPage.expandTreeRow(name);
        ipAddressManagementViewPage.deleteObject(getSubnetAddressAndMask(thirdIPSubnetProperties));
        ipAddressManagementViewPage.expandTreeRow(name);
        ipAddressManagementViewPage.deleteObject(getSubnetAddressAndMask(secondIPSubnetProperties));
    }

    // zamienic name na nameUpdated po poprawie buga OSSTPT-30469
    @Test(priority = 16)
    public void deleteIPNetwork() {
        ipAddressManagementViewPage.deleteObject(name);
    }

    private void putInitialSubnetsProperties(){
        firstIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, "126.0.0.0/24 ["+name+"]");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_ADDRESS, "126.0.0.0");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_HIGHEST_IP_ADDRESS, "126.0.0.254");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_BROADCAST_IP_ADDRESS, "126.0.0.255");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, name);
        firstIPSubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, "24");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "BLOCK");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_ROLE, MANAGEMENT_PRIMARY_ROLE);
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