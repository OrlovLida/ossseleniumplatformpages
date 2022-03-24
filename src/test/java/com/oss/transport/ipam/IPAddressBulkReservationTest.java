package com.oss.transport.ipam;

import com.oss.BaseTestCase;
import com.oss.pages.transport.ipam.IPAddressManagementViewPage;
import com.oss.pages.transport.ipam.IPSubnetWizardPage;
import com.oss.pages.transport.ipam.helper.IPSubnetFilterProperties;
import com.oss.pages.transport.ipam.helper.IPSubnetWizardProperties;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.text.MessageFormat;
import java.util.HashMap;

import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.*;

@Listeners({TestListener.class})
public class IPAddressBulkReservationTest extends BaseTestCase {
    private static final String NETWORK_NAME = "BULK_ADDRESS_RESERVATION_SELENIUM_TEST";
    private static final String NETWORK_TYPE_SUBNET = "Network";
    private static final String DESCRIPTION = "IPAddressBulkReservation";
    private static final String FIRST_FIXED_IPv4_ADDRESS = "10.000";
    private static final String SECOND_FIXED_IPv4_ADDRESS = "20.000";
    private static final String FIRST_IPv6_ADDRESS = "::10:0";
    private static final String SECOND_IPv6_ADDRESS = "::20:0";
    private static final String IPv4_NETWORK_MASK = "28";
    private static final String IPv6_NETWORK_MASK = "124";
    private static final String FIRST_IPv4_SUBNET_ADDRESS = "10.0.0.0/28";
    private static final String SEC0ND_IPv4_SUBNET_ADDRESS = "20.0.0.0/28";
    private static final String FIRST_IPv6_SUBNET_ADDRESS = "::10:0/124";
    private static final String SEC0ND_IPv6_SUBNET_ADDRESS = "::20:0/124";
    private static final String FIRST_IPv4_SUBNET_HOST = "10.0.0.7";
    private static final String FIRST_IPv4_SUBNET_HOST_ROW_NAME = "10.0.0.7/28";
    private static final String FIRST_IPv6_SUBNET_HOST = "::10:7";
    private static final String FIRST_IPv6_SUBNET_HOST_ROW_NAME = "::10:7/124";
    private static final String EQUALS = "=";
    private static final int AMOUNT_OF_SUBNET_TO_CREATE = 1;
    private static final boolean DO_NOT_RESERVE_CONSECUTIVE = false;
    private static final boolean RESERVE_CONSECUTIVE = true;
    private static final String ONE_CHILD = "1";
    private static final String ZERO_CHILDREN = "0";
    private static final String ONE_HUNDRED_PERCENT = "100%";
    private static final String NINETY_THREE_PERCENT = "93%";
    private static final String NINE_ADDRESSES_TO_RESERVE = "9";
    private static final String TWENTY_NINE_PERCENT = "29%";
    private static final String TEN_CHILDREN = "10";
    private static final String TEN_ADDRESSES_TO_RESERVE = "10";
    private static final String THIRTY_THREE_PERCENT = "33%";
    private static final String EIGHT_ADDRESSES_TO_RESERVE = "8";
    private static final String ZERO_PERCENT = "0%";
    private static final String FOURTEEN_CHILDREN = "14";
    private static final String FIFTEEN_CHILDREN = "15";
    private static final String HIGHER_BOUNDARY_ADDRESS = "HIGHER_BOUNDARY_ADDRESS";
    private static final String LOWER_BOUNDARY_ADDRESS = "LOWER_BOUNDARY_ADDRESS";
    private static final String FIRST_IPv4_ADDRESS_TO_FORMAT = "10.0.0.{0}";
    private static final String SECOND_IPv4_ADDRESS_TO_FORMAT = "20.0.0.{0}";
    private static final String FIRST_IPv6_ADDRESS_TO_FORMAT = "::10:{0}";
    private static final String SECOND_IPv6_ADDRESS_TO_FORMAT = "::20:{0}";
    private static final String TEN_IN_LAST_OCTET = "10";
    private static final String ONE_IN_LAST_OCTET = "1";
    private static final String ONE_IN_LAST_HEXTET = "1";
    private static final String A_IN_LAST_HEXTET = "a";
    private static final String FOURTEEN_IN_LAST_OCTET = "14";
    private static final String F_IN_LAST_HEXTET = "f";
    private static IPAddressManagementViewPage ipAddressManagementViewPage;
    private static IPSubnetFilterProperties FIRST_IPv4_SUBNET_PROPERTIES;
    private static IPSubnetFilterProperties SECOND_IPv4_SUBNET_PROPERTIES;
    private static IPSubnetFilterProperties FIRST_IPv6_SUBNET_PROPERTIES;
    private static IPSubnetFilterProperties SECOND_IPv6_SUBNET_PROPERTIES;
    private static IPSubnetWizardProperties NETWORK_SUBNET_WIZARD_PROPERTIES;
    private static SubnetProperties firstIPv4SubnetProperties;
    private static SubnetProperties secondIPv4SubnetProperties;
    private static SubnetProperties firstIPv6SubnetProperties;
    private static SubnetProperties secondIPv6SubnetProperties;


    @BeforeClass
    public void prepareData() {
        FIRST_IPv4_SUBNET_PROPERTIES = new IPSubnetFilterProperties(FIRST_FIXED_IPv4_ADDRESS, FIRST_FIXED_IPv4_ADDRESS, EQUALS, IPv4_NETWORK_MASK);
        FIRST_IPv6_SUBNET_PROPERTIES = new IPSubnetFilterProperties(FIRST_IPv6_ADDRESS, FIRST_IPv6_ADDRESS, EQUALS, IPv6_NETWORK_MASK);
        SECOND_IPv4_SUBNET_PROPERTIES = new IPSubnetFilterProperties(SECOND_FIXED_IPv4_ADDRESS, SECOND_FIXED_IPv4_ADDRESS, EQUALS, IPv4_NETWORK_MASK);
        SECOND_IPv6_SUBNET_PROPERTIES = new IPSubnetFilterProperties(SECOND_IPv6_ADDRESS, SECOND_IPv6_ADDRESS, EQUALS, IPv6_NETWORK_MASK);
        NETWORK_SUBNET_WIZARD_PROPERTIES = new IPSubnetWizardProperties(NETWORK_TYPE_SUBNET);
        firstIPv4SubnetProperties = prepareSubnetProperties(ONE_CHILD, NINETY_THREE_PERCENT);
        firstIPv4SubnetProperties.addBoundaryAddressProperties(FIRST_IPv4_ADDRESS_TO_FORMAT, IPv4_NETWORK_MASK, "7", HIGHER_BOUNDARY_ADDRESS);
        secondIPv4SubnetProperties = prepareSubnetProperties(ZERO_CHILDREN, ONE_HUNDRED_PERCENT);
        firstIPv6SubnetProperties = prepareSubnetProperties(ONE_CHILD, NINETY_THREE_PERCENT);
        firstIPv6SubnetProperties.addBoundaryAddressProperties(FIRST_IPv6_ADDRESS_TO_FORMAT, IPv6_NETWORK_MASK, "7", HIGHER_BOUNDARY_ADDRESS);
        secondIPv6SubnetProperties = prepareSubnetProperties(ZERO_CHILDREN, ONE_HUNDRED_PERCENT);
    }

    @Test(priority = 1)
    @Description("Create IP Network with IPv4/v6 subnets")
    public void createIPNetworkWithSubnets() {
        ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementPage(driver, BASIC_URL);
        ipAddressManagementViewPage.createIPNetwork(NETWORK_NAME, DESCRIPTION);
        createIPSubnets(FIRST_IPv4_SUBNET_PROPERTIES);
        createIPSubnets(SECOND_IPv4_SUBNET_PROPERTIES);
        createIPSubnets(FIRST_IPv6_SUBNET_PROPERTIES);
        createIPSubnets(SECOND_IPv6_SUBNET_PROPERTIES);
        unselectTreeRow(NETWORK_NAME);
    }

    @Test(priority = 2)
    @Description("Reserve all required in scenario addresses")
    public void reserveNecessaryHostAddresses() {
        ipAddressManagementViewPage.expandTreeRowContains(NETWORK_NAME);
        ipAddressManagementViewPage.reserveGivenIPv4HostAddress(FIRST_IPv4_SUBNET_ADDRESS, FIRST_IPv4_SUBNET_HOST);
        unselectTreeRow(FIRST_IPv4_SUBNET_HOST_ROW_NAME);
        ipAddressManagementViewPage.reserveGivenIPv6HostAddress(FIRST_IPv6_SUBNET_ADDRESS, FIRST_IPv6_SUBNET_HOST);
        unselectTreeRow(FIRST_IPv6_SUBNET_HOST_ROW_NAME);
    }

    @Test(priority = 3)
    @Description("Check correctness before scenario")
    public void checkCorrectnessBeforeScenario() {
        checkCorrectness(FIRST_IPv4_SUBNET_ADDRESS, firstIPv4SubnetProperties);
        checkCorrectness(SEC0ND_IPv4_SUBNET_ADDRESS, secondIPv4SubnetProperties);
        checkCorrectness(FIRST_IPv6_SUBNET_ADDRESS, firstIPv6SubnetProperties);
        checkCorrectness(SEC0ND_IPv6_SUBNET_ADDRESS, secondIPv6SubnetProperties);
    }

    @Test(priority = 4)
    @Description("Reserve in first IPv4 subnet 9 host addresses with unselected checkbox")
    public void nineHostAddressReservationInFirstIPv4Subnet() {
        ipAddressManagementViewPage.bulkIPv4AddressReservation(NINE_ADDRESSES_TO_RESERVE, DO_NOT_RESERVE_CONSECUTIVE, FIRST_IPv4_SUBNET_ADDRESS);
        updateSubnetProperties(firstIPv4SubnetProperties, TWENTY_NINE_PERCENT, TEN_CHILDREN);
        firstIPv4SubnetProperties.addBoundaryAddressProperties(FIRST_IPv4_ADDRESS_TO_FORMAT, IPv4_NETWORK_MASK, TEN_IN_LAST_OCTET, HIGHER_BOUNDARY_ADDRESS);
        firstIPv4SubnetProperties.addBoundaryAddressProperties(FIRST_IPv4_ADDRESS_TO_FORMAT, IPv4_NETWORK_MASK, ONE_IN_LAST_OCTET, LOWER_BOUNDARY_ADDRESS);
        checkCorrectness(FIRST_IPv4_SUBNET_ADDRESS, firstIPv4SubnetProperties);
    }

    @Test(priority = 5)
    @Description("Reserve 10 host addresses with selected checkbox while two IPv4 subnets are selected")
    public void tenHostAddressReservationInTwoIPv4Subnets() {
        ipAddressManagementViewPage.bulkIPv4AddressReservation(TEN_ADDRESSES_TO_RESERVE, RESERVE_CONSECUTIVE, FIRST_IPv4_SUBNET_ADDRESS, SEC0ND_IPv4_SUBNET_ADDRESS);
        updateSubnetProperties(secondIPv4SubnetProperties, TWENTY_NINE_PERCENT, TEN_CHILDREN);
        secondIPv4SubnetProperties.addBoundaryAddressProperties(SECOND_IPv4_ADDRESS_TO_FORMAT, IPv4_NETWORK_MASK, ONE_IN_LAST_OCTET, LOWER_BOUNDARY_ADDRESS);
        secondIPv4SubnetProperties.addBoundaryAddressProperties(SECOND_IPv4_ADDRESS_TO_FORMAT, IPv4_NETWORK_MASK, TEN_IN_LAST_OCTET, HIGHER_BOUNDARY_ADDRESS);
        checkCorrectness(FIRST_IPv4_SUBNET_ADDRESS, firstIPv4SubnetProperties);
        checkCorrectness(SEC0ND_IPv4_SUBNET_ADDRESS, secondIPv4SubnetProperties);
    }

    @Test(priority = 6)
    @Description("Reserve in first IPv6 subnet 9 host addresses with unselected checkbox")
    public void nineHostAddressReservationInFirstIPv6Subnet() {
        ipAddressManagementViewPage.bulkIPv6AddressReservation(NINE_ADDRESSES_TO_RESERVE, DO_NOT_RESERVE_CONSECUTIVE, FIRST_IPv6_SUBNET_ADDRESS);
        updateSubnetProperties(firstIPv6SubnetProperties, THIRTY_THREE_PERCENT, TEN_CHILDREN);
        firstIPv6SubnetProperties.addBoundaryAddressProperties(FIRST_IPv6_ADDRESS_TO_FORMAT, IPv6_NETWORK_MASK, ONE_IN_LAST_HEXTET, LOWER_BOUNDARY_ADDRESS);
        firstIPv6SubnetProperties.addBoundaryAddressProperties(FIRST_IPv6_ADDRESS_TO_FORMAT, IPv6_NETWORK_MASK, A_IN_LAST_HEXTET, HIGHER_BOUNDARY_ADDRESS);
        checkCorrectness(FIRST_IPv6_SUBNET_ADDRESS, firstIPv6SubnetProperties);
    }

    @Test(priority = 7)
    @Description("Reserve 10 host addresses with selected checkbox while two IPv6 subnets are selected")
    public void tenHostAddressReservationInTwoIPv6Subnets() {
        ipAddressManagementViewPage.bulkIPv6AddressReservation(TEN_ADDRESSES_TO_RESERVE, RESERVE_CONSECUTIVE, FIRST_IPv6_SUBNET_ADDRESS, SEC0ND_IPv6_SUBNET_ADDRESS);
        updateSubnetProperties(secondIPv6SubnetProperties, THIRTY_THREE_PERCENT, TEN_CHILDREN);
        secondIPv6SubnetProperties.addBoundaryAddressProperties(SECOND_IPv6_ADDRESS_TO_FORMAT, IPv6_NETWORK_MASK, ONE_IN_LAST_OCTET, LOWER_BOUNDARY_ADDRESS);
        secondIPv6SubnetProperties.addBoundaryAddressProperties(SECOND_IPv6_ADDRESS_TO_FORMAT, IPv6_NETWORK_MASK, A_IN_LAST_HEXTET, HIGHER_BOUNDARY_ADDRESS);
        checkCorrectness(FIRST_IPv6_SUBNET_ADDRESS, firstIPv6SubnetProperties);
        checkCorrectness(SEC0ND_IPv6_SUBNET_ADDRESS, secondIPv6SubnetProperties);
    }

    @Test(priority = 8)
    @Description("Reserve 8 host addresses with unselected checkbox while two IPv4 subnets are selected")
    public void eightHostAddressReservationInTwoIPv4Subnets() {
        ipAddressManagementViewPage.bulkIPv4AddressReservation(EIGHT_ADDRESSES_TO_RESERVE, DO_NOT_RESERVE_CONSECUTIVE, FIRST_IPv4_SUBNET_ADDRESS, SEC0ND_IPv4_SUBNET_ADDRESS);
        updateSubnetProperties(firstIPv4SubnetProperties, ZERO_PERCENT, FOURTEEN_CHILDREN);
        updateSubnetProperties(secondIPv4SubnetProperties, ZERO_PERCENT, FOURTEEN_CHILDREN);
        firstIPv4SubnetProperties.addBoundaryAddressProperties(FIRST_IPv4_ADDRESS_TO_FORMAT, IPv4_NETWORK_MASK, FOURTEEN_IN_LAST_OCTET, HIGHER_BOUNDARY_ADDRESS);
        secondIPv4SubnetProperties.addBoundaryAddressProperties(SECOND_IPv4_ADDRESS_TO_FORMAT, IPv4_NETWORK_MASK, FOURTEEN_IN_LAST_OCTET, HIGHER_BOUNDARY_ADDRESS);
        checkCorrectness(FIRST_IPv4_SUBNET_ADDRESS, firstIPv4SubnetProperties);
        checkCorrectness(SEC0ND_IPv4_SUBNET_ADDRESS, secondIPv4SubnetProperties);
    }

    @Test(priority = 9)
    @Description("Reserve 8 host addresses with unselected checkbox while two IPv6 subnets are selected")
    public void eightHostAddressReservationInTwoIPv6Subnets() {
        ipAddressManagementViewPage.bulkIPv6AddressReservation(TEN_ADDRESSES_TO_RESERVE, DO_NOT_RESERVE_CONSECUTIVE, FIRST_IPv6_SUBNET_ADDRESS, SEC0ND_IPv6_SUBNET_ADDRESS);
        updateSubnetProperties(firstIPv6SubnetProperties, ZERO_PERCENT, FIFTEEN_CHILDREN);
        updateSubnetProperties(secondIPv6SubnetProperties, ZERO_PERCENT, FIFTEEN_CHILDREN);
        firstIPv6SubnetProperties.addBoundaryAddressProperties(FIRST_IPv6_ADDRESS_TO_FORMAT, IPv6_NETWORK_MASK, F_IN_LAST_HEXTET, HIGHER_BOUNDARY_ADDRESS);
        secondIPv6SubnetProperties.addBoundaryAddressProperties(SECOND_IPv6_ADDRESS_TO_FORMAT, IPv6_NETWORK_MASK, F_IN_LAST_HEXTET, HIGHER_BOUNDARY_ADDRESS);
        checkCorrectness(FIRST_IPv6_SUBNET_ADDRESS, firstIPv6SubnetProperties);
        checkCorrectness(SEC0ND_IPv6_SUBNET_ADDRESS, secondIPv6SubnetProperties);
    }

    @Test(priority = 10)
    @Description("Delete test data")
    public void deleteTestData() {
        deleteCreatedData();
    }

    private void createIPSubnets(IPSubnetFilterProperties ipSubnetFilterProperties) {
        IPSubnetWizardPage ipSubnetWizardPage;
        if (ipSubnetFilterProperties.equals(FIRST_IPv4_SUBNET_PROPERTIES) || ipSubnetFilterProperties.equals(SECOND_IPv4_SUBNET_PROPERTIES))
            ipSubnetWizardPage = ipAddressManagementViewPage.createIPv4Subnet();
        else
            ipSubnetWizardPage = ipAddressManagementViewPage.createIPv6Subnet();
        ipSubnetWizardPage.ipSubnetWizardSelectStep(ipSubnetFilterProperties, AMOUNT_OF_SUBNET_TO_CREATE);
        ipSubnetWizardPage.ipSubnetWizardPropertiesStep(NETWORK_SUBNET_WIZARD_PROPERTIES);
        ipSubnetWizardPage.ipSubnetWizardSummaryStep();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
    }

    private void checkCorrectness(String ipSubnetAddress, SubnetProperties subnetProperties) {
        ipAddressManagementViewPage.selectTreeRowContains(ipSubnetAddress);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(SUBNET_PROPERTY_CHILD_COUNT), subnetProperties.getChildCount());
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(SUBNET_PROPERTY_PERCENT_FREE), subnetProperties.getPercentFree());
        ipAddressManagementViewPage.unselectTreeRow(ipSubnetAddress);
        subnetProperties.getBoundaryHostAddressPropertiesMap().forEach((boundaryAddress, addressProperties) -> {
            try {
                ipAddressManagementViewPage.selectTreeRowContains(addressProperties.getHostAddressRowName());
            } catch (NoSuchElementException e) {
                ipAddressManagementViewPage.expandTreeRowContains(ipSubnetAddress);
                ipAddressManagementViewPage.selectTreeRowContains(addressProperties.getHostAddressRowName());
            }
            Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(HOST_PROPERTY_IP_NETWORK_NAME), addressProperties.getIpNetwork());
            Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(HOST_PROPERTY_IDENTIFIER), addressProperties.getIdentifier());
            ipAddressManagementViewPage.unselectTreeRow(addressProperties.getHostAddressRowName());
        });
    }

    private SubnetProperties prepareSubnetProperties(String childCount, String percentFree) {
        SubnetProperties subnetProperties = new SubnetProperties();
        subnetProperties.setChildCount(childCount);
        subnetProperties.setPercentFree(percentFree);
        return subnetProperties;
    }

    private void updateSubnetProperties(SubnetProperties subnetProperties, String percentFree, String childCount) {
        subnetProperties.setPercentFree(percentFree);
        subnetProperties.setChildCount(childCount);
    }

    private void deleteCreatedData() {
        ipAddressManagementViewPage.deleteIPv6SubnetTypeOfNetwork(SEC0ND_IPv6_SUBNET_ADDRESS);
        unselectTreeRow(NETWORK_NAME);
        ipAddressManagementViewPage.expandTreeRowContains(NETWORK_NAME);
        ipAddressManagementViewPage.deleteIPv6SubnetTypeOfNetwork(FIRST_IPv6_SUBNET_ADDRESS);
        unselectTreeRow(NETWORK_NAME);
        ipAddressManagementViewPage.expandTreeRowContains(NETWORK_NAME);
        ipAddressManagementViewPage.deleteIPv4SubnetTypeOfNetwork(SEC0ND_IPv4_SUBNET_ADDRESS);
        unselectTreeRow(NETWORK_NAME);
        ipAddressManagementViewPage.expandTreeRowContains(NETWORK_NAME);
        ipAddressManagementViewPage.deleteIPv4SubnetTypeOfNetwork(FIRST_IPv4_SUBNET_ADDRESS);
        ipAddressManagementViewPage.deleteIPNetwork(NETWORK_NAME);
    }

    private void unselectTreeRow(String rowName) {
        ipAddressManagementViewPage.selectTreeRowContains(rowName);
        ipAddressManagementViewPage.unselectTreeRow(rowName);
    }

    private static class SubnetProperties {

        private String percentFree;
        private String childCount;
        private final HashMap<String, HostAddressProperties> hostAddressPropertiesMap = new HashMap<>();

        public void setPercentFree(String percentFree) {
            this.percentFree = percentFree;
        }

        public void setChildCount(String childCount) {
            this.childCount = childCount;
        }

        public void addBoundaryAddressProperties(String ipAddressToFormat, String networkMask, String lastOctetOrHextet, String boundaryAddress) {
            String ipAddressRowName = MessageFormat.format(ipAddressToFormat, lastOctetOrHextet) + "/" + networkMask;
            String hostAddressIdentifier = ipAddressRowName + " [" + NETWORK_NAME + "]";
            hostAddressPropertiesMap.put(boundaryAddress, new HostAddressProperties(hostAddressIdentifier, ipAddressRowName));
        }

        public String getPercentFree() {
            return percentFree;
        }

        public String getChildCount() {
            return childCount;
        }

        public HashMap<String, HostAddressProperties> getBoundaryHostAddressPropertiesMap() {
            return hostAddressPropertiesMap;
        }
    }

    private static class HostAddressProperties {
        private final String IDENTIFIER;
        private static final String IP_NETWORK = NETWORK_NAME;
        private final String HOST_ADDRESS_ROW_NAME;

        public HostAddressProperties(String identifier, String hostAddressRowName) {
            this.IDENTIFIER = identifier;
            this.HOST_ADDRESS_ROW_NAME = hostAddressRowName;
        }

        public String getIdentifier() {
            return IDENTIFIER;
        }

        public String getIpNetwork() {
            return IP_NETWORK;
        }

        public String getHostAddressRowName() {
            return HOST_ADDRESS_ROW_NAME;
        }

    }

}
