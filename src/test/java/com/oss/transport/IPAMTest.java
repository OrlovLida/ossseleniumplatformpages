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

    private String identifier = "Identifier";
    private String address = "Address";
    private String highestIPAddress = "Highest IP Address";
    private String broadcastIPAddress = "Broadcast IP Address";
    private String ipNetworkName = "IP network name";
    private String maskLength = "Mask length";
    private String subnetType = "Subnet type";
    private String role = "Role";
    private String percentFree = "Percent free";
    private String childCount = "Child count";

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
        ipAddressManagementViewPage.useContextAction("CreateOperationsForNetwork", "Create IP Network");
        IPNetworkWizardPage ipNetworkWizardPage = new IPNetworkWizardPage(driver);
        ipNetworkWizardPage.createIPNetwork(name, description);
        ipAddressManagementViewPage.closeSystemMessage();
    }

    @Test(priority = 2)
    public void checkIPAMTreeAfterNetworkCreation() {
        ipAddressManagementViewPage.selectTreeRow(name);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue("Name"), name);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue("Description"), description);
    }

    @Test(priority = 3)
    public void createIPSubnets() {
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.useContextAction("CreateOperationsForNetwork", "Create IPv4 Subnet");
        IPSubnetWizardPage ipSubnetWizardPage = new IPSubnetWizardPage(driver);
        ipSubnetWizardPage.ipSubnetWizardSelectStep("126000", "12600128", ">=", "24", 3);
        Map<String, String> firstIPSubnet = new HashMap<>();
        firstIPSubnet.put(subnetType, "Block");
        firstIPSubnet.put(role, "Management - Primary");
        firstIPSubnet.put(description, "description");
        Map<String, String> secondIPSubnet = new HashMap<>();
        secondIPSubnet.put(subnetType, "Network");
        Map<String, String> thirdIPSubnet = new HashMap<>();
        thirdIPSubnet.put(subnetType, "Network");
        ipSubnetWizardPage.ipSubnetWizardPropertiesStep(firstIPSubnet, secondIPSubnet, thirdIPSubnet);
        ipSubnetWizardPage.ipSubnetWizardSummaryStep();
    }

    @Test(priority = 4)
    public void checkIPAMTreeAfterSubnetsCreation() {

        firstIPSubnetProperties.put(identifier, "126.0.0.0/24 ["+name+"]");
        firstIPSubnetProperties.put(address, "126.0.0.0");
        firstIPSubnetProperties.put(highestIPAddress, "126.0.0.254");
        firstIPSubnetProperties.put(broadcastIPAddress, "126.0.0.255");
        firstIPSubnetProperties.put(ipNetworkName, name);
        firstIPSubnetProperties.put(maskLength, "24");
        firstIPSubnetProperties.put(subnetType, "BLOCK");
        firstIPSubnetProperties.put(role, "Management - Primary");
        //firstIPSubnetProperties.put(percentFree, "0%");   bug juz poprawiony czeka na release
        //firstIPSubnetProperties.put(childCount, "2");  bug juz poprawiony czeka na release
        firstIPSubnetProperties.put(description, "description");

        secondIPSubnetProperties.put(identifier, "126.0.0.0/25 ["+name+"]");
        secondIPSubnetProperties.put(address, "126.0.0.0");
        secondIPSubnetProperties.put(highestIPAddress, "126.0.0.126");
        secondIPSubnetProperties.put(broadcastIPAddress, "126.0.0.127");
        secondIPSubnetProperties.put(ipNetworkName, name);
        secondIPSubnetProperties.put(maskLength, "25");
        secondIPSubnetProperties.put(subnetType, "NETWORK");
        secondIPSubnetProperties.put(percentFree, "100%");
        secondIPSubnetProperties.put(childCount, "0");

        thirdIPSubnetProperties.put(identifier, "126.0.0.128/25 ["+name+"]");
        thirdIPSubnetProperties.put(address, "126.0.0.128");
        thirdIPSubnetProperties.put(highestIPAddress, "126.0.0.254");
        thirdIPSubnetProperties.put(broadcastIPAddress, "126.0.0.255");
        thirdIPSubnetProperties.put(ipNetworkName, name);
        thirdIPSubnetProperties.put(maskLength, "25");
        thirdIPSubnetProperties.put(subnetType, "NETWORK");
        thirdIPSubnetProperties.put(percentFree, "100%");
        thirdIPSubnetProperties.put(childCount, "0");

        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.selectTreeRow(name);
        ipAddressManagementViewPage.selectTreeRow(name);
        ipAddressManagementViewPage.expandTreeRow(name);
        DelayUtils.sleep(100);
        checkSubnetAttributesOnIPAMTree(firstIPSubnetProperties);
        ipAddressManagementViewPage.expandTreeRowContains(firstIPSubnetProperties.get(address));
        checkSubnetAttributesOnIPAMTree(secondIPSubnetProperties);
        checkSubnetAttributesOnIPAMTree(thirdIPSubnetProperties);
    }

    private void checkSubnetAttributesOnIPAMTree(Map<String, String> subnetProperties){
        ipAddressManagementViewPage.selectTreeRowContains(subnetProperties.get(address)+"/"+subnetProperties.get(maskLength));
        Set<String> keySet = subnetProperties.keySet();
        for(String key: keySet){
            Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(key), subnetProperties.get(key));
        }
        ipAddressManagementViewPage.selectTreeRowContains(subnetProperties.get(address)+"/"+subnetProperties.get(maskLength));
    }

    // bug OSSTPT-30469
    @Test(priority = 5)
    public void editIPNetwork() {
//        ipAddressManagementViewPage.selectTreeRow(name);
//        ipAddressManagementViewPage.useContextAction("EditOperationsForNetwork", "Edit IP Network");
//        IPNetworkWizardPage ipNetworkWizardPage = new IPNetworkWizardPage(driver);
//        ipNetworkWizardPage.editIPNetwork(nameUpdated, descriptionUpdated);
    }

    // bug OSSTPT-30469
    @Test(priority = 6)
    public void checkIPAMTreeAfterNetworkEdition() {
//        firstIPSubnetProperties.put(identifier, firstSubnet+ "["+nameUpdated+"]");
//        firstIPSubnetProperties.put(ipNetworkName, nameUpdated);
//        secondIPSubnetProperties.put(identifier, secondSubnet+ "["+nameUpdated+"]");
//        secondIPSubnetProperties.put(ipNetworkName, nameUpdated);
//        thirdIPSubnetProperties.put(identifier, thirdSubnet+ "["+nameUpdated+"]");
//        thirdIPSubnetProperties.put(ipNetworkName, nameUpdated);
//
//        ipAddressManagementViewPage.selectTreeRow(nameUpdated);
//        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue("Name"), nameUpdated);
//        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue("Description"), descriptionUpdated);
//        ipAddressManagementViewPage.selectTreeRow(nameUpdated);
//        ipAddressManagementViewPage.expandTreeRow(nameUpdated);
//        ipAddressManagementViewPage.selectTreeRowContains(firstSubnet);
//        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(identifier), firstIPSubnetProperties.get(identifier));
//        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(ipNetworkName), firstIPSubnetProperties.get(ipNetworkName));
//        ipAddressManagementViewPage.selectTreeRowContains(firstSubnet);
//        ipAddressManagementViewPage.expandTreeRowContains(firstSubnet);
//        ipAddressManagementViewPage.selectTreeRowContains(secondSubnet);
//        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(identifier), secondIPSubnetProperties.get(identifier));
//        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(ipNetworkName), secondIPSubnetProperties.get(ipNetworkName));
//        ipAddressManagementViewPage.selectTreeRowContains(secondSubnet);
//        ipAddressManagementViewPage.expandTreeRowContains(secondSubnet);
//        ipAddressManagementViewPage.selectTreeRowContains(thirdSubnet);
//        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(identifier), thirdIPSubnetProperties.get(identifier));
//        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue(ipNetworkName), thirdIPSubnetProperties.get(ipNetworkName));
    }

    // zamienic name na nameUpdated po poprawie buga OSSTPT-30469
    @Test (priority = 7)
    public void checkInventoryViewForIPNetwork() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "IPNetwork");
        newInventoryViewPage.openFilterPanel().setValue(Input.ComponentType.TEXT_FIELD, "name", name).applyFilter();
        newInventoryViewPage.getMainTable().selectFirstRow();
        DelayUtils.sleep(100);
        Assert.assertEquals(newInventoryViewPage.getPropertyPanel().getPropertyValue("name"), name);
        Assert.assertEquals(newInventoryViewPage.getPropertyPanel().getPropertyValue("description"), description);
    }

    // bug
    // zamienic name na nameUpdated po poprawie buga OSSTPT-30469
    @Test (priority = 8)
    public void checkInventoryViewForIPSubnet() {
//        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "IPv4Subnet");
//        newInventoryViewPage.openFilterPanel().setValue(Input.ComponentType.TEXT_FIELD, "networkName", name).applyFilter();
//        checkSubnetAttributesOnNewIV(firstIPSubnetProperties);
//        checkSubnetAttributesOnNewIV(secondIPSubnetProperties);
//        checkSubnetAttributesOnNewIV(thirdIPSubnetProperties);
    }

    private void checkSubnetAttributesOnNewIV(Map<String, String> subnetProperties){
        newInventoryViewPage.getMainTable().typeIntoSearch(subnetProperties.get(identifier));
        newInventoryViewPage.getMainTable().selectFirstRow();
        //newInventoryViewPage.getTableWidget().selectRowByAttributeValue(identifier, firstIPSubnetProperties.get(identifier));
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
        ipAddressManagementViewPage.expandTreeRowContains(firstIPSubnetProperties.get(address));
        ipAddressManagementViewPage.deleteIPSubnet(secondIPSubnetProperties.get(address)+"/"+secondIPSubnetProperties.get(maskLength));
        ipAddressManagementViewPage.expandTreeRow(name);
        ipAddressManagementViewPage.expandTreeRowContains(firstIPSubnetProperties.get(address));
        ipAddressManagementViewPage.deleteIPSubnet(thirdIPSubnetProperties.get(address));
        ipAddressManagementViewPage.expandTreeRow(name);
        ipAddressManagementViewPage.deleteIPSubnet(firstIPSubnetProperties.get(address));
    }

    // zamienic name na nameUpdated po poprawie buga OSSTPT-30469
    @Test(priority = 10)
    public void deleteIPNetwork() {
        ipAddressManagementViewPage.deleteObject(name);
    }
}