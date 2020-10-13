package com.oss.transport;

import com.oss.BaseTestCase;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.transport.IPAddressManagementViewPage;
import com.oss.pages.transport.IPNetworkWizardPage;
import com.oss.pages.transport.IPSubnetWizardPage;
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
    private String name = "IPam2SeleniumTest";
    private String description = "Description";
    private String nameUpdated = "IPamSeleniumTest2";
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

    private static final String CREATE_OPERATION_FOR_NETWORK_GROUP = "CreateOperationsForNetwork";
    private static final String EDIT_OPERATION_FOR_NETWORK_GROUP = "EditOperationsForNetwork";
    private static final String CREATE_IP_NETWORK_ACTION = "Create IP Network";
    private static final String EDIT_IP_NETWORK_ACTION = "Edit IP Network";
    private static final String CREATE_IPV4_SUBNET_ACTION = "Create IPv4 Subnet";

    private Map<String, String> firstIPSubnetProperties = new HashMap<>();
    private Map<String, String> secondIPSubnetProperties = new HashMap<>();
    private Map<String, String> thirdIPSubnetProperties = new HashMap<>();

    @BeforeClass
    public void prepareTest() {
        DelayUtils.sleep(1000);
        ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementViewPageLive(driver, BASIC_URL);
    }

    @Test(priority = 1)
    public void createIPNetwork() {
        ipAddressManagementViewPage.selectFirstTreeRow();
        ipAddressManagementViewPage.useContextAction(CREATE_OPERATION_FOR_NETWORK_GROUP, CREATE_IP_NETWORK_ACTION);
        IPNetworkWizardPage ipNetworkWizardPage = new IPNetworkWizardPage(driver);
        ipNetworkWizardPage.createIPNetwork(name, description);
        ipAddressManagementViewPage.closeSystemMessage();
    }

    @Test(priority = 2)
    public void checkIPAMTreeAfterNetworkCreation() {
        ipAddressManagementViewPage.selectTreeRow(name);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(NETWORK_PROPERTY_NAME), name);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(NETWORK_PROPERTY_DESCRIPTION), description);
    }

    @Test(priority = 3)
    public void createIPSubnets() {
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.useContextAction(CREATE_OPERATION_FOR_NETWORK_GROUP, CREATE_IPV4_SUBNET_ACTION);
        String[] subnetSelectStepFilterData = {"126000", "12600128", ">=", "24"};
        IPSubnetWizardPage ipSubnetWizardPage = new IPSubnetWizardPage(driver);
        ipSubnetWizardPage.ipSubnetWizardSelectStep(subnetSelectStepFilterData, 3);
        Map<String, String> firstIPSubnet = new HashMap<>();
        firstIPSubnet.put(SUBNET_PROPERTY_SUBNET_TYPE, "Block");
        firstIPSubnet.put(SUBNET_PROPERTY_ROLE, "Management - Primary");
        firstIPSubnet.put(SUBNET_PROPERTY_DESCRIPTION, "description");
        Map<String, String> secondIPSubnet = new HashMap<>();
        secondIPSubnet.put(SUBNET_PROPERTY_SUBNET_TYPE, "Network");
        Map<String, String> thirdIPSubnet = new HashMap<>();
        thirdIPSubnet.put(SUBNET_PROPERTY_SUBNET_TYPE, "Network");
        ipSubnetWizardPage.ipSubnetWizardPropertiesStep(firstIPSubnet, secondIPSubnet, thirdIPSubnet);
        ipSubnetWizardPage.ipSubnetWizardSummaryStep();
    }

    @Test(priority = 4)
    public void checkIPAMTreeAfterSubnetsCreation() {

        firstIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, "126.0.0.0/24 ["+name+"]");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_ADDRESS, "126.0.0.0");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_HIGHEST_IP_ADDRESS, "126.0.0.254");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_BROADCAST_IP_ADDRESS, "126.0.0.255");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, name);
        firstIPSubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, "24");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "BLOCK");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_ROLE, "Management - Primary");
        //firstIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "0%");   bug juz poprawiony czeka na release
        //firstIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "2");  bug juz poprawiony czeka na release
        firstIPSubnetProperties.put(SUBNET_PROPERTY_DESCRIPTION, "description");

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

        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.selectTreeRow(name);
        ipAddressManagementViewPage.selectTreeRow(name);
        ipAddressManagementViewPage.expandTreeRow(name);
        DelayUtils.sleep(100);
        checkSubnetAttributesOnIPAMTree(firstIPSubnetProperties);
        ipAddressManagementViewPage.expandTreeRowContains(firstIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS));
        checkSubnetAttributesOnIPAMTree(secondIPSubnetProperties);
        checkSubnetAttributesOnIPAMTree(thirdIPSubnetProperties);
    }

    private void checkSubnetAttributesOnIPAMTree(Map<String, String> subnetProperties){
        ipAddressManagementViewPage.selectTreeRowContains(subnetProperties.get(SUBNET_PROPERTY_ADDRESS)+"/"+subnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH));
        Set<String> keySet = subnetProperties.keySet();
        for(String key: keySet){
            Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(key), subnetProperties.get(key));
        }
        ipAddressManagementViewPage.selectTreeRowContains(subnetProperties.get(SUBNET_PROPERTY_ADDRESS)+"/"+subnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH));
    }

    // bug OSSTPT-30469
    @Test(priority = 5)
    public void editIPNetwork() {
//        ipAddressManagementViewPage.selectTreeRow(name);
//        ipAddressManagementViewPage.useContextAction(EDIT_OPERATION_FOR_NETWORK_GROUP, EDIT_IP_NETWORK_ACTION);
//        IPNetworkWizardPage ipNetworkWizardPage = new IPNetworkWizardPage(driver);
//        ipNetworkWizardPage.editIPNetwork(nameUpdated, descriptionUpdated);
    }

    // bug OSSTPT-30469
    @Test(priority = 6)
    public void checkIPAMTreeAfterNetworkEdition() {
//        firstIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, firstIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS)+firstIPSubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH) +"["+nameUpdated+"]");
//        firstIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, nameUpdated);
//        secondIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, secondIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS)+secondIPSubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH)+ "["+nameUpdated+"]");
//        secondIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, nameUpdated);
//        thirdIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, thirdIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS)+thirdIPSubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH)+ "["+nameUpdated+"]");
//        thirdIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, nameUpdated);
//
//        ipAddressManagementViewPage.selectTreeRow(nameUpdated);
//        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(NETWORK_PROPERTY_NAME), nameUpdated);
//        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(NETWORK_PROPERTY_DESCRIPTION), descriptionUpdated);
//        ipAddressManagementViewPage.selectTreeRow(nameUpdated);
//        ipAddressManagementViewPage.expandTreeRow(nameUpdated);
//        ipAddressManagementViewPage.selectTreeRowContains(firstIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS));
//        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(SUBNET_PROPERTY_IDENTIFIER), firstIPSubnetProperties.get(SUBNET_PROPERTY_IDENTIFIER));
//        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(SUBNET_PROPERTY_IP_NETWORK_NAME), firstIPSubnetProperties.get(SUBNET_PROPERTY_IP_NETWORK_NAME));
//        ipAddressManagementViewPage.selectTreeRowContains(firstIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS));
//        ipAddressManagementViewPage.expandTreeRowContains(firstIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS));
//        ipAddressManagementViewPage.selectTreeRowContains(secondIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS)+"/"+secondIPSubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH));
//        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(SUBNET_PROPERTY_IDENTIFIER), secondIPSubnetProperties.get(SUBNET_PROPERTY_IDENTIFIER));
//        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(SUBNET_PROPERTY_IP_NETWORK_NAME), secondIPSubnetProperties.get(SUBNET_PROPERTY_IP_NETWORK_NAME));
//        ipAddressManagementViewPage.selectTreeRowContains(secondIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS)+"/"+secondIPSubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH));
//        ipAddressManagementViewPage.selectTreeRowContains(thirdIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS));
//        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(SUBNET_PROPERTY_IDENTIFIER), thirdIPSubnetProperties.get(SUBNET_PROPERTY_IDENTIFIER));
//        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(SUBNET_PROPERTY_IP_NETWORK_NAME), thirdIPSubnetProperties.get(SUBNET_PROPERTY_IP_NETWORK_NAME));
    }

    // zamienic name na nameUpdated po poprawie buga OSSTPT-30469
    @Test (priority = 7)
    public void checkInventoryViewForIPNetwork() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, NETWORK_INVENTORY_TYPE);
        newInventoryViewPage.openFilterPanel().setValue(Input.ComponentType.TEXT_FIELD, NETWORK_INVENTORY_PROPERTY_NAME, name).applyFilter();
        newInventoryViewPage.getMainTable().selectFirstRow();
        DelayUtils.sleep(100);
        Assert.assertEquals(newInventoryViewPage.getPropertyPanel().getPropertyValue(NETWORK_INVENTORY_PROPERTY_NAME), name);
        Assert.assertEquals(newInventoryViewPage.getPropertyPanel().getPropertyValue(NETWORK_INVENTORY_PROPERTY_DESCRIPTION), description);
    }

    // bug
    // zamienic name na nameUpdated po poprawie buga OSSTPT-30469
    @Test (priority = 8)
    public void checkInventoryViewForIPSubnet() {
//        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, IPV4_SUBNET_INVENTORY_TYPE);
//        newInventoryViewPage.openFilterPanel().setValue(Input.ComponentType.TEXT_FIELD, NETWORK_NAME_FILTER_FOR_SUBNET_INVENTORY_VIEW, name).applyFilter();
//        checkSubnetAttributesOnNewIV(firstIPSubnetProperties);
//        checkSubnetAttributesOnNewIV(secondIPSubnetProperties);
//        checkSubnetAttributesOnNewIV(thirdIPSubnetProperties);
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

    // zamienic name na nameUpdated po poprawie buga OSSTPT-30469
    @Test(priority = 9)
    public void deleteIPSubnets() {
        ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementViewPageLive(driver, BASIC_URL);
        ipAddressManagementViewPage.expandTreeRow(name);
        ipAddressManagementViewPage.expandTreeRowContains(firstIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS));
        ipAddressManagementViewPage.deleteIPSubnet(secondIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS)+"/"+secondIPSubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH));
        ipAddressManagementViewPage.expandTreeRow(name);
        ipAddressManagementViewPage.expandTreeRowContains(firstIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS));
        ipAddressManagementViewPage.deleteIPSubnet(thirdIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS));
        ipAddressManagementViewPage.expandTreeRow(name);
        ipAddressManagementViewPage.deleteIPSubnet(firstIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS));
    }

    // zamienic name na nameUpdated po poprawie buga OSSTPT-30469
    @Test(priority = 10)
    public void deleteIPNetwork() {
        ipAddressManagementViewPage.deleteObject(name);
    }
}