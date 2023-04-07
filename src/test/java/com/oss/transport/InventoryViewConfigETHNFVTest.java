package com.oss.transport;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.comarch.oss.web.pages.NewInventoryViewPage;
import com.comarch.oss.web.pages.SearchObjectTypePage;
import com.oss.BaseTestCase;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.navigation.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;

import io.qameta.allure.Description;

/**
 * @author Szymon Masarczyk
 */

public class InventoryViewConfigETHNFVTest extends BaseTestCase {

    private static final String RESOURCE_INVENTORY_SIDE_MENU = "Resource Inventory";
    private static final String INVENTORY_VIEW_SIDE_MENU = "Inventory View";
    private static final String MAIN_TABLE_ETHERNET_LINK_CONFIGURATION = "TableWidgetEthernetLinkDefault";
    private static final List<String> ETHERNET_LINK_MAIN_TABLE = List.of("Name", "Label", "Identifier", "Object Type", "Layer", "Latency [ms]", "Is Trunk", "Role", "Effective Capacity", "Capacity", "Description", "Lifecycle State", "Start Location", "Start Physical Device", "Start Logical Function", "Start Card", "Start Port", "Start Termination Point", "End Location", "End Physical Device", "End Logical Function", "End Card", "End Port", "End Termination Point", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String TABS_ETHERNET_LINK_CONFIGURATION = "TabsEthernetLinkDefault";
    private static final List<String> DEFAULT_TRAIL_TABS_LIST = List.of("Properties", "Termination", "Routing - 1st level", "Element Routing", "Utilization - 1st level", "End to end", "Planning Info", "Interests", "IP Subnets");
    private static final String ETHERNET_LINK_PROPERTIES_CONFIGURATION = "PropertyTabEthernetLinkDefault";
    private static final List<String> ETHERNET_LINK_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "Label", "Identifier", "Object Type", "Layer", "Latency [ms]", "Is Trunk", "Role", "Effective Capacity", "Capacity", "Routing Status", "Termination Status", "Adaptation Type", "Topology", "Lifecycle State", "Start Location", "Start Physical Device", "Start Logical Function", "Start Card", "Start Port", "Start Termination Point", "End Location", "End Physical Device", "End Logical Function", "End Card", "End Port", "End Termination Point", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String MAIN_TABLE_AGGREGATED_ETHERNET_LINK_CONFIGURATION = "TableWidgetAggregatedEthernetLinkDefault";
    private static final List<String> AGGREGATED_ETHERNET_LINK_MAIN_TABLE = List.of("Name", "Label", "Identifier", "Object Type", "Layer", "Aggregation Protocol", "Speed Mbps", "Capacity", "Effective Capacity", "Description", "Lifecycle State", "Start Location", "Start Physical Device", "Start Logical Function", "Start Card", "Start Port", "Start Termination Point", "End Location", "End Physical Device", "End Logical Function", "End Card", "End Port", "End Termination Point", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String TABS_AGGREGATED_ETHERNET_LINK_CONFIGURATION = "TabsAggregatedEthernetLinksDefault";
    private static final String AGGREGATED_ETHERNET_LINK_PROPERTIES_CONFIGURATION = "PropertyTabAggregatedEthernetLinkDefault";
    private static final List<String> AGGREGATED_ETHERNET_LINK_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "Label", "Identifier", "Object Type", "Layer", "Aggregation Protocol", "Capacity", "Effective Capacity", "Speed Mbps", "Routing Status", "Termination Status", "Adaptation Type", "Topology", "Lifecycle State", "Start Location", "Start Physical Device", "Start Logical Function", "Start Card", "Start Port", "Start Termination Point", "End Location", "End Physical Device", "End Logical Function", "End Card", "End Port", "End Termination Point", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String MAIN_TABLE_ELINE_CONFIGURATION = "TableWidgetELineDefault";
    private static final List<String> ELINE_MAIN_TABLE = List.of("Name", "Label", "Identifier", "Object Type", "Layer", "VLAN ID", "Service Provider", "Service Creation Date", "Capacity", "Description", "Lifecycle State", "Start Location", "Start Physical Device", "Start Logical Function", "Start Card", "Start Port", "Start Termination Point", "End Location", "End Physical Device", "End Logical Function", "End Card", "End Port", "End Termination Point", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String TABS_ELINE_CONFIGURATION = "TabsELineDefault";
    private static final String ELINE_PROPERTIES_CONFIGURATION = "PropertyTabELineDefault";
    private static final List<String> ELINE_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "Label", "Identifier", "Object Type", "Layer", "VLAN ID", "Service Provider", "Capacity", "Routing Status", "Termination Status", "Adaptation Type", "Topology", "Lifecycle State", "Start Location", "Start Physical Device", "Start Logical Function", "Start Card", "Start Port", "Start Termination Point", "End Location", "End Physical Device", "End Logical Function", "End Card", "End Port", "End Termination Point", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String MAIN_TABLE_VLAN_CONFIGURATION = "TableWidgetVLANDefault";
    private static final List<String> VLAN_MAIN_TABLE = List.of("Name", "Label", "Identifier", "Calculated Name", "Object Type", "Layer", "VLAN ID", "Service Type", "Network Role", "Capacity", "Description", "Lifecycle State", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String TABS_VLAN_CONFIGURATION = "TabsVLANDefault";
    private static final String VLAN_PROPERTIES_CONFIGURATION = "PropertyTabVLANDefault";
    private static final List<String> VLAN_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "Label", "Identifier", "Calculated Name", "Object Type", "Layer", "VLAN ID", "VLAN Type", "Service Type", "Network Role", "Capacity", "Routing Status", "Termination Status", "Adaptation Type", "Topology", "Lifecycle State", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String MAIN_TABLE_ETHERWAY_CONFIGURATION = "TableWidgetEtherwayDefault";
    private static final List<String> ETHERWAY_MAIN_TABLE = List.of("Name", "Label", "Identifier", "Object Type", "Layer", "Etherway ID", "Etherway Type", "Application ID", "Line Speed [Mbps]", "Description", "Lifecycle State", "Start Location", "Start Physical Device", "Start Logical Function", "Start Card", "Start Port", "Start Termination Point", "End Location", "End Physical Device", "End Logical Function", "End Card", "End Port", "End Termination Point", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String TABS_ETHERWAY_CONFIGURATION = "TabsEtherwayDefault";
    private static final String ETHERWAY_PROPERTIES_CONFIGURATION = "PropertyTabEtherwayDefault";
    private static final List<String> ETHERWAY_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "Label", "Identifier", "Object Type", "Layer", "Etherway ID", "Etherway Type", "Application ID", "Line Speed [Mbps]", "Description", "Lifecycle State", "Start Location", "Start Physical Device", "Start Logical Function", "Start Card", "Start Port", "Start Termination Point", "End Location", "End Physical Device", "End Logical Function", "End Card", "End Port", "End Termination Point", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String MAIN_TABLE_ETHERFLOW_CONFIGURATION = "TableWidgetEtherflowDefault";
    private static final List<String> ETHERFLOW_MAIN_TABLE = List.of("Name", "Label", "Identifier", "Object Type", "Layer", "Etherflow ID", "Etherflow Type", "Application ID", "Description", "Lifecycle State", "Start Location", "Start Physical Device", "Start Logical Function", "Start Card", "Start Port", "Start Termination Point", "End Location", "End Physical Device", "End Logical Function", "End Card", "End Port", "End Termination Point", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String TABS_ETHERFLOW_CONFIGURATION = "TabsEtherflowDefault";
    private static final String ETHERFLOW_PROPERTIES_CONFIGURATION = "PropertyTabEtherflowDefault";
    private static final List<String> ETHERFLOW_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "Label", "Identifier", "Object Type", "Layer", "Etherflow ID", "Etherflow Type", "Application ID", "Description", "Lifecycle State", "Start Location", "Start Physical Device", "Start Logical Function", "Start Card", "Start Port", "Start Termination Point", "End Location", "End Physical Device", "End Logical Function", "End Card", "End Port", "End Termination Point", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String MAIN_TABLE_VIRTUAL_NETWORK_CONFIGURATION = "TableWidgetVirtualNetworkDefault";
    private static final List<String> VIRTUAL_NETWORK_MAIN_TABLE = List.of("Name", "Label", "Identifier", "Object Type", "Layer", "Network Type", "Segmentation ID", "Physical Network", "Capacity", "Description", "Lifecycle State", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String TABS_VIRTUAL_NETWORK_CONFIGURATION = "TabsVirtualNetworkDefault";
    private static final String VIRTUAL_NETWORK_PROPERTIES_CONFIGURATION = "PropertyTabVirtualNetworkDefault";
    private static final List<String> VIRTUAL_NETWORK_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "Label", "Identifier", "Object Type", "Layer", "Network Type", "Segmentation ID", "Physical Network", "Capacity", "Routing Status", "Termination Status", "Adaptation Type", "Topology", "Lifecycle State", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String MAIN_TABLE_EVPN_CONFIGURATION = "TableWidgetEVPNDefault";
    private static final List<String> EVPN_MAIN_TABLE = List.of("Name", "Label", "Identifier", "Object Type", "Layer", "EVI", "Capacity", "Description", "Lifecycle State", "Create User", "Create Date", "Modify User", "Modify Date");
    private static final String TABS_EVPN_CONFIGURATION = "TabsEVPNDefault";
    private static final String EVPN_PROPERTIES_CONFIGURATION = "PropertyTabEVPNDefault";
    private static final List<String> EVPN_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "Label", "Identifier", "Object Type", "Layer", "EVI", "Capacity", "Routing Status", "Termination Status", "Adaptation Type", "Topology", "Lifecycle State", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String MAIN_TABLE_VXLAN_CONFIGURATION = "TableWidgetVxLANDefault";
    private static final List<String> VXLAN_MAIN_TABLE = List.of("Name", "Label", "Identifier", "Object Type", "Layer", "Tunnel Type", "Address Family", "VNI", "Tenant", "Capacity", "Description", "Lifecycle State", "Create User", "Create Date", "Modify User", "Modify Date");
    private static final String TABS_VXLAN_CONFIGURATION = "TabsVxLANDefault";
    private static final String VXLAN_PROPERTIES_CONFIGURATION = "PropertyTabVxLANDefault";
    private static final List<String> VXLAN_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "Label", "Identifier", "Object Type", "Layer", "Tunnel Type", "Address Family", "VNI", "Tenant", "Capacity", "Routing Status", "Termination Status", "Adaptation Type", "Topology", "Lifecycle State", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String MAIN_TABLE_ETHERNET_INTERFACE_CONFIGURATION = "TableWidgetEthernetInterfaceDefault";
    private static final List<String> ETHERNET_INTERFACE_MAIN_TABLE = List.of("Name", "Identifier", "Number", "Location", "Physical Device", "Port", "Pluggable Module", "Logical Location", "Logical Function", "Access Function", "Administrative Duplex Mode", "Administrative Speed", "Administrative State", "Auto-Negotiation", "Auto Advertisement", "Bandwidth", "Encapsulation", "Flow Control", "LACP Short Period", "Maximum Frame Size", "MTU", "Operational Duplex Mode", "Operational Speed", "Operational State", "Physical Address", "Switchport", "Switchport mode", "Role", "Usage State", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String TABS_ETHERNET_INTERFACE_CONFIGURATION = "TabsEthernetInterfaceDefault";
    private static final List<String> ETHERNET_INTERFACE_TABS = List.of("Properties", "Client Termination Points", "Server Termination Points", "Host Termination Point Relations", "Terminated Trails", "IP Addresses", "VRF");
    private static final String ETHERNET_INTERFACE_PROPERTIES_CONFIGURATION = "PropertyTabEthernetInterfaceDefault";
    private static final List<String> ETHERNET_INTERFACE_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "Identifier", "ID", "Short Identifier", "Number", "Location", "Physical Device", "Port", "Pluggable Module", "Logical Location", "Logical Function", "Access Function", "Administrative Duplex Mode", "Administrative Speed", "Administrative State", "Auto-Negotiation", "Auto Advertisement", "Bandwidth", "Encapsulation", "Flow Control", "LACP Short Period", "Maximum Frame Size", "MTU", "Operational Duplex Mode", "Operational Speed", "Operational State", "Physical Address", "Switchport", "Switchport mode", "Role", "Usage State", "Is Blocked", "Is Reserved", "Description", "Is Configured", "Is Obsolete", "Create User", "Create Date", "Modify User", "Modify Date");
    private static final String MAIN_TABLE_AGGREGATED_ETHERNET_INTERFACE_CONFIGURATION = "TableWidgetAggregatedEthernetInterfaceDefault";
    private static final List<String> AGGREGATED_ETHERNET_INTERFACE_MAIN_TABLE = List.of("Name", "Identifier", "Number", "Location", "Physical Device", "Logical Location", "Logical Function", "Aggregation Factor", "Aggregation Protocol", "Aggregation Rate", "Administrative State", "Operational State", "Encapsulation", "LACP Mode", "LACP Short Period", "Minimum Active Links", "Minimum Bandwidth", "MTU", "Physical Address", "Usage State", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String TABS_AGGREGATED_ETHERNET_INTERFACE_CONFIGURATION = "TabsAggregatedEthernetInterfaceDefault";
    private static final List<String> AGGREGATED_ETHERNET_INTERFACE_TABS = List.of("Properties", "Client Termination Points", "Server Termination Points", "Terminated Trails", "IP Addresses", "VRF");
    private static final String AGGREGATED_ETHERNET_INTERFACE_PROPERTIES_CONFIGURATION = "PropertyTabAggregatedEthernetInterfaceDefault";
    private static final List<String> AGGREGATED_ETHERNET_INTERFACE_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "Identifier", "ID", "Short Identifier", "Number", "Location", "Physical Device", "Logical Location", "Logical Function", "Aggregation Factor", "Aggregation Protocol", "Aggregation Rate", "Administrative State", "Operational State", "Encapsulation", "LACP Mode", "LACP Short Period", "Minimum Active Links", "Minimum Bandwidth", "MTU", "Physical Address", "Usage State", "Is Blocked", "Is Reserved", "Description", "Is Configured", "Is Obsolete", "Create User", "Create Date", "Modify User", "Modify Date");
    private static final String MAIN_TABLE_LOOPBACK_INTERFACE_CONFIGURATION = "TableWidgetLoopbackInterfaceDefault";
    private static final List<String> LOOPBACK_INTERFACE_MAIN_TABLE = List.of("Name", "Identifier", "Number", "Location", "Physical Device", "Administrative State", "Operational State", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String TABS_LOOPBACK_INTERFACE_CONFIGURATION = "TabsLoopbackInterfaceDefault";
    private static final List<String> LOOPBACK_INTERFACE_TABS = List.of("Properties", "IP Addresses");
    private static final String LOOPBACK_INTERFACE_PROPERTIES_CONFIGURATION = "PropertyTabLoopbackInterfaceDefault";
    private static final List<String> LOOPBACK_INTERFACE_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "Identifier", "ID", "Short Identifier", "Number", "Location", "Physical Device", "Administrative State", "Operational State", "Usage State", "Is Blocked", "Is Reserved", "Description", "Is Configured", "Is Obsolete", "Create User", "Create Date", "Modify User", "Modify Date");
    private static final String MAIN_TABLE_IRB_INTERFACE_CONFIGURATION = "TableWidgetIRBInterfaceDefault";
    private static final List<String> IRB_INTERFACE_MAIN_TABLE = List.of("Name", "Identifier", "Location", "Physical Device", "VLAN ID", "MTU", "Administrative State", "Operational State", "Usage State", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String TABS_IRB_INTERFACE_CONFIGURATION = "TabsIRBInterfaceDefault";
    private static final List<String> IRB_INTERFACE_TABS = List.of("Properties", "Client Termination Points", "Terminated Trails", "IP Addresses", "VRF");
    private static final String IRB_INTERFACE_PROPERTIES_CONFIGURATION = "PropertyTabIRBInterfaceDefault";
    private static final List<String> IRB_INTERFACE_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "Identifier", "ID", "Short Identifier", "Number", "Location", "Physical Device", "VLAN ID", "MTU", "Administrative State", "Operational State", "Usage State", "Usage State", "Is Blocked", "Is Reserved", "Description", "Is Configured", "Is Obsolete", "Create User", "Create Date", "Modify User", "Modify Date");
    private static final String MAIN_TABLE_VLAN_INTERFACE_CONFIGURATION = "TableWidgetVLANInterfaceDefault";
    private static final List<String> VLAN_INTERFACE_MAIN_TABLE = List.of("Name", "Identifier", "Location", "Physical Device", "Logical Location", "Logical Function", "Parent Interface", "VLAN Interface Type", "Outer VLAN ID", "Inner VLAN ID", "Native VLAN ID", "Subinterface ID", "Bandwidth", "MTU", "Administrative State", "Operational State", "Interface Role", "Usage State", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String TABS_VLAN_INTERFACE_CONFIGURATION = "TabsVLANInterfaceDefault";
    private static final List<String> VLAN_INTERFACE_TABS = List.of("Properties", "Client Termination Points", "Server Termination Points", "Association Termination Point Relations", "Terminated Trails", "IP Addresses", "VRF");
    private static final String VLAN_INTERFACE_PROPERTIES_CONFIGURATION = "PropertyTabVLANInterfaceDefault";
    private static final List<String> VLAN_INTERFACE_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "Identifier", "ID", "Short Identifier", "Location", "Physical Device", "Logical Location", "Logical Function", "Parent Interface", "VLAN Interface Type", "Outer VLAN ID", "Inner VLAN ID", "Native VLAN ID", "Subinterface ID", "Bandwidth", "MTU", "Administrative State", "Operational State", "Interface Role", "Usage State", "Is Blocked", "Is Reserved", "Description", "Is Configured", "Is Obsolete", "Create User", "Create Date", "Modify User", "Modify Date");
    private static final String MAIN_TABLE_TRAFFIC_CLASS_CONFIGURATION = "TableWidgetTrafficClassDefault";
    private static final List<String> TRAFFIC_CLASS_MAIN_TABLE = List.of("Name", "Description", "Location", "Physical Device", "Physical Device ID", "Logical Location", "Logical Function", "Logical Function ID", "Input Interface ID", "Match", "IP Precedence", "MPLS Experimental", "MPLS Experimental Topmost", "IP DSCP", "CIR Ingress", "CIR Egress", "PIR Ingress", "PIR Egress", "Access List", "Protocol", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String TABS_TRAFFIC_CLASS_CONFIGURATION = "TabsTrafficClassDefault";
    private static final List<String> TRAFFIC_CLASS_TABS = List.of("Properties");
    private static final String TRAFFIC_CLASS_PROPERTIES_CONFIGURATION = "PropertyTabTrafficClassDefault";
    private static final List<String> TRAFFIC_CLASS_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "Description", "Location", "Physical Device", "Physical Device ID", "Logical Location", "Logical Function", "Logical Function ID", "Input Interface ID", "Match", "IP Precedence", "MPLS Experimental", "MPLS Experimental Topmost", "IP DSCP", "CIR Ingress", "CIR Egress", "PIR Ingress", "PIR Egress", "Access List", "Protocol", "Is Obsolete", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String MAIN_TABLE_TRAFFIC_POLICY_CONFIGURATION = "TableWidgetTrafficPolicyDefault";
    private static final List<String> TRAFFIC_POLICY_MAIN_TABLE = List.of("Name", "Description", "Identifier", "Location", "Physical Device", "Physical Device ID", "Logical Location", "Logical Function", "Logical Function ID", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String TABS_TRAFFIC_POLICY_CONFIGURATION = "TabsTrafficPolicyDefault";
    private static final List<String> TRAFFIC_POLICY_TABS = List.of("Properties", "Traffic Policy Entries", "Traffic Policy Assignments");
    private static final String TRAFFIC_POLICY_PROPERTIES_CONFIGURATION = "PropertyTabTrafficPolicyDefault";
    private static final List<String> TRAFFIC_POLICY_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "Description", "Identifier", "Location", "Physical Device", "Physical Device ID", "Logical Location", "Logical Function", "Logical Function ID", "Is Obsolete", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String MAIN_TABLE_VLAN_POOL_CONFIGURATION = "TableWidgetVLANPoolDefault";
    private static final List<String> VLAN_POOL_MAIN_TABLE = List.of("Name", "Description", "Lifecycle State", "Identifier", "VLAN Range Name", "VLAN Range", "Used IDs", "Free IDs", "Usage", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String TABS_VLAN_POOL_CONFIGURATION = "TabsVLANPoolDefault";
    private static final List<String> VLAN_POOL_TABS = List.of("Properties", "VLAN Parent Object", "Physical Device Parent Object");
    private static final String VLAN_POOL_PROPERTIES_CONFIGURATION = "PropertyTabVLANPoolDefault";
    private static final List<String> VLAN_POOL_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "Description", "Lifecycle State", "Identifier", "VLAN Range Name", "VLAN Range", "VLAN Range ID", "Used IDs", "Free IDs", "Usage", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String MAIN_TABLE_VLAN_RANGE_CONFIGURATION = "TableWidgetVLANRangeDefault";
    private static final List<String> VLAN_RANGE_MAIN_TABLE = List.of("Name", "Description", "Lifecycle State", "Identifier", "VLAN Range", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String TABS_VLAN_RANGE_CONFIGURATION = "TabsVLANRangeDefault";
    private static final List<String> VLAN_RANGE_TABS = List.of("Properties", "VLAN Pools");
    private static final String VLAN_RANGE_PROPERTIES_CONFIGURATION = "PropertyTabVLANRangeDefault";
    private static final List<String> VLAN_RANGE_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "Description", "Lifecycle State", "Identifier", "VLAN Range", "Create User", "Create Date", "Modify User", "Modify Date", "ID");
    private static final String MAIN_TABLE_VXLAN_INSTANCE_CONFIGURATION = "TableWidgetVxLANInstanceDefault";
    private static final List<String> VXLAN_INSTANCE_MAIN_TABLE = List.of("Name", "Identifier", "ID", "VTEP", "IP Network Element", "VNI", "VNI Type", "Discovery Mode", "Bridge Domain ID", "VRF ID", "Tenant", "Multicast Group ID", "Static MAC Address", "ARP Suppression", "Replication Mode", "Replication Protocol", "Static Replication Peer", "Type", "Description", "Create User", "Create Date", "Modify User", "Modify Date");
    private static final String TABS_VXLAN_INSTANCE_CONFIGURATION = "TabsVxLANInstanceDefault";
    private static final List<String> VXLAN_INSTANCE_TABS = List.of("Properties", "Access Interfaces", "Connections");
    private static final String VXLAN_INSTANCE_PROPERTIES_CONFIGURATION = "PropertyTabVxLANInstanceDefault";
    private static final List<String> VXLAN_INSTANCE_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "Identifier", "ID", "VTEP", "IP Network Element", "VNI", "VNI Type", "Discovery Mode", "Bridge Domain ID", "VRF ID", "Tenant", "Multicast Group ID", "Static MAC Address", "ARP Suppression", "Replication Mode", "Replication Protocol", "Static Replication Peer", "Type", "Description", "Create User", "Create Date", "Modify User", "Modify Date");
    private static final String MAIN_TABLE_EVPN_INSTANCE_CONFIGURATION = "TableWidgetEVPNInstanceDefault";
    private static final List<String> EVPN_INSTANCE_MAIN_TABLE = List.of("Name", "Identifier", "ID", "IP Network Element", "EVI", "Route Distinguisher", "ARP Proxy", "ARP Suppression", "ND Proxy", "Nd Suppression", "Advertise Mac", "MAC Limit", "Source Address", "Import Route Targets IPv4", "Import Route Targets IPv6", "Export Route Targets IPv4", "Export Route Targets IPv6", "Type", "Description", "Create User", "Create Date", "Modify User", "Modify Date");
    private static final String TABS_EVPN_INSTANCE_CONFIGURATION = "TabsEVPNInstanceDefault";
    private static final List<String> EVPN_INSTANCE_TABS = List.of("Properties", "Grouped Termination Points", "Connections", "Static Routes", "BGP Services");
    private static final String EVPN_INSTANCE_PROPERTIES_CONFIGURATION = "PropertyTabEVPNInstanceDefault";
    private static final List<String> EVPN_INSTANCE_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "Identifier", "ID", "IP Network Element", "EVI", "Route Distinguisher", "ARP Proxy", "ARP Suppression", "ND Proxy", "Nd Suppression", "Advertise Mac", "MAC Limit", "Source Address", "Import Route Targets IPv4", "Import Route Targets IPv6", "Export Route Targets IPv4", "Export Route Targets IPv6", "Type", "Description", "Create User", "Create Date", "Modify User", "Modify Date");
    private static final String MAIN_TABLE_IP_NETWORK_ELEMENT_CONFIGURATION = "TableWidgetIPNetworkElementDefault";
    private static final List<String> IP_NETWORK_ELEMENT_MAIN_TABLE = List.of("Name", "Identifier", "Type", "UUID", "Logical Location", "Detail Logical Location Type", "NE Role", "Management System", "Management System", "Description", "Lifecycle State", "Resource Specification", "Base Specification", "Create User", "Create Date", "Update User", "Update Time", "ID");
    private static final String TABS_IP_NETWORK_ELEMENT_CONFIGURATION = "TabsIPNetworkElementDefault";
    private static final List<String> IP_NETWORK_ELEMENT_TABS = List.of("Properties", "Composed Resources", "VxLAN Instances", "EVPN Instances", "IP Addresses", "Static Routes", "BGP Services");
    private static final String IP_NETWORK_ELEMENT_PROPERTIES_CONFIGURATION = "PropertyTabIPNetworkElementDefault";
    private static final List<String> IP_NETWORK_ELEMENT_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "Identifier", "Type", "UUID", "Logical Location", "Detail Logical Location Type", "NE Role", "Management System", "Management System", "Description", "Lifecycle State", "Resource Specification", "Base Specification", "Create User", "Create Date", "Update User", "Update Time", "ID");
    private static final String MAIN_TABLE_VTEP_CONFIGURATION = "TableWidgetVTEPDefault";
    private static final List<String> VTEP_MAIN_TABLE = List.of("Name", "ID", "Source IP", "MTU", "Global Multicast IP (Layer 2)", "Global Multicast IP (Layer 3)", "PIM Rendevous IP", "Inner VLAN Handling Mode", "Type", "Description", "IP Network Element", "Source Interface", "Resource Specification", "Base Specification", "Create User", "Create Date", "Modify User", "Modify Date");
    private static final String TABS_VTEP_CONFIGURATION = "TabsVTEPDefault";
    private static final List<String> VTEP_TABS = List.of("Properties", "VxLAN Instances");
    private static final String VTEP_PROPERTIES_CONFIGURATION = "PropertyTabVTEPDefault";
    private static final List<String> VTEP_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "ID", "Source IP", "MTU", "Global Multicast IP (Layer 2)", "Global Multicast IP (Layer 3)", "PIM Rendevous IP", "Inner VLAN Handling Mode", "Type", "Description", "IP Network Element", "Source Interface", "Resource Specification", "Base Specification", "Create User", "Create Date", "Modify User", "Modify Date");
    private static final String MAIN_TABLE_L3OUT_CONFIGURATION = "TableWidgetL3OutDefault";
    private static final List<String> L3OUT_MAIN_TABLE = List.of("Name", "Identifier", "ID", "Description", "Name Alias", "Provider Label", "Consumer Label", "PIM", "Route Control Enforcement", "Type", "Master Tenant", "Create User", "Create Date", "Modify User", "Modify Date");
    private static final String TABS_L3OUT_CONFIGURATION = "TabsL3OutDefault";
    private static final List<String> L3OUT_TABS = List.of("Properties", "Grouped VxLAN Instances", "Grouped Trails", "Termination Points");
    private static final String L3OUT_PROPERTIES_CONFIGURATION = "PropertyTabL3OutDefault";
    private static final List<String> L3OUT_PROPERTIES_ATTRIBUTES_LIST = List.of("Name", "Identifier", "ID", "Description", "Name Alias", "Provider Label", "Consumer Label", "PIM", "Route Control Enforcement", "Type", "Master Tenant", "Create User", "Create Date", "Modify User", "Modify Date");

    @BeforeClass
    public void openInventoryView() {
        openInventoryViewPage();
        waitForPageToLoad();
    }

    @Test(priority = 1)
    @Description("Ethernet Link Configuration")
    public void ethernetLinkIVVerification() {
        searchForObjectType("Ethernet Link");
        changeMainTableConfiguration(MAIN_TABLE_ETHERNET_LINK_CONFIGURATION);
        assertMainTableColumns(ETHERNET_LINK_MAIN_TABLE);
        changeTabsConfiguration(TABS_ETHERNET_LINK_CONFIGURATION);
        assertTabsConfiguration(DEFAULT_TRAIL_TABS_LIST);
        changePropertiesTabConfiguration(ETHERNET_LINK_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(ETHERNET_LINK_PROPERTIES_ATTRIBUTES_LIST);
    }

    @Test(priority = 2)
    @Description("Aggregated Ethernet Link Configuration")
    public void aggregatedEthernetLinkIVVerification() {
        openInventoryView();
        searchForObjectType("Aggregated Ethernet Link");
        changeMainTableConfiguration(MAIN_TABLE_AGGREGATED_ETHERNET_LINK_CONFIGURATION);
        assertMainTableColumns(AGGREGATED_ETHERNET_LINK_MAIN_TABLE);
        changeTabsConfiguration(TABS_AGGREGATED_ETHERNET_LINK_CONFIGURATION);
        assertTabsConfiguration(DEFAULT_TRAIL_TABS_LIST);
        changePropertiesTabConfiguration(AGGREGATED_ETHERNET_LINK_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(AGGREGATED_ETHERNET_LINK_PROPERTIES_ATTRIBUTES_LIST);
    }

    @Test(priority = 3)
    @Description("E-line Configuration")
    public void elineIVVerification() {
        openInventoryView();
        searchForObjectType("E-Line");
        changeMainTableConfiguration(MAIN_TABLE_ELINE_CONFIGURATION);
        assertMainTableColumns(ELINE_MAIN_TABLE);
        changeTabsConfiguration(TABS_ELINE_CONFIGURATION);
        assertTabsConfiguration(DEFAULT_TRAIL_TABS_LIST);
        changePropertiesTabConfiguration(ELINE_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(ELINE_PROPERTIES_ATTRIBUTES_LIST);
    }

    @Test(priority = 4)
    @Description("VLAN Configuration")
    public void vlanIVVerification() {
        openInventoryView();
        searchForObjectType("VLAN");
        changeMainTableConfiguration(MAIN_TABLE_VLAN_CONFIGURATION);
        assertMainTableColumns(VLAN_MAIN_TABLE);
        changeTabsConfiguration(TABS_VLAN_CONFIGURATION);
        assertTabsConfiguration(DEFAULT_TRAIL_TABS_LIST);
        changePropertiesTabConfiguration(VLAN_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(VLAN_PROPERTIES_ATTRIBUTES_LIST);
    }

    @Test(priority = 5)
    @Description("Etherway Leased Line Configuration")
    public void etherwayIVVerification() {
        openInventoryView();
        searchForObjectType("Etherway Leased Line");
        changeMainTableConfiguration(MAIN_TABLE_ETHERWAY_CONFIGURATION);
        assertMainTableColumns(ETHERWAY_MAIN_TABLE);
        changeTabsConfiguration(TABS_ETHERWAY_CONFIGURATION);
        assertTabsConfiguration(DEFAULT_TRAIL_TABS_LIST);
        changePropertiesTabConfiguration(ETHERWAY_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(ETHERWAY_PROPERTIES_ATTRIBUTES_LIST);
    }

    @Test(priority = 6)
    @Description("Etherflow Leased Line Configuration")
    public void etherflowIVVerification() {
        openInventoryView();
        searchForObjectType("Etherflow Leased Line");
        changeMainTableConfiguration(MAIN_TABLE_ETHERFLOW_CONFIGURATION);
        assertMainTableColumns(ETHERFLOW_MAIN_TABLE);
        changeTabsConfiguration(TABS_ETHERFLOW_CONFIGURATION);
        assertTabsConfiguration(DEFAULT_TRAIL_TABS_LIST);
        changePropertiesTabConfiguration(ETHERFLOW_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(ETHERFLOW_PROPERTIES_ATTRIBUTES_LIST);
    }

    @Test(priority = 7)
    @Description("Virtual Network Configuration")
    public void virtualNetworkIVVerification() {
        openInventoryView();
        searchForObjectType("VirtualNetwork");
        changeMainTableConfiguration(MAIN_TABLE_VIRTUAL_NETWORK_CONFIGURATION);
        assertMainTableColumns(VIRTUAL_NETWORK_MAIN_TABLE);
        changeTabsConfiguration(TABS_VIRTUAL_NETWORK_CONFIGURATION);
        assertTabsConfiguration(DEFAULT_TRAIL_TABS_LIST);
        changePropertiesTabConfiguration(VIRTUAL_NETWORK_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(VIRTUAL_NETWORK_PROPERTIES_ATTRIBUTES_LIST);
    }

    @Test(priority = 8)
    @Description("EVPN Configuration")
    public void evpnIVVerification() {
        openInventoryView();
        searchForObjectType("EVPN");
        changeMainTableConfiguration(MAIN_TABLE_EVPN_CONFIGURATION);
        assertMainTableColumns(EVPN_MAIN_TABLE);
        changeTabsConfiguration(TABS_EVPN_CONFIGURATION);
        assertTabsConfiguration(DEFAULT_TRAIL_TABS_LIST);
        changePropertiesTabConfiguration(EVPN_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(EVPN_PROPERTIES_ATTRIBUTES_LIST);
    }

    @Test(priority = 9)
    @Description("VxLAN Configuration")
    public void vxlanIVVerification() {
        openInventoryView();
        searchForObjectType("VxLAN");
        changeMainTableConfiguration(MAIN_TABLE_VXLAN_CONFIGURATION);
        assertMainTableColumns(VXLAN_MAIN_TABLE);
        changeTabsConfiguration(TABS_VXLAN_CONFIGURATION);
        assertTabsConfiguration(DEFAULT_TRAIL_TABS_LIST);
        changePropertiesTabConfiguration(VXLAN_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(VXLAN_PROPERTIES_ATTRIBUTES_LIST);
    }

    @Test(priority = 10)
    @Description("Ethernet Interface Configuration")
    public void ethernetInterfaceIVVerification() {
        openInventoryView();
        searchForObjectType("Ethernet Interface");
        changeMainTableConfiguration(MAIN_TABLE_ETHERNET_INTERFACE_CONFIGURATION);
        assertMainTableColumns(ETHERNET_INTERFACE_MAIN_TABLE);
        changeTabsConfiguration(TABS_ETHERNET_INTERFACE_CONFIGURATION);
        assertTabsConfiguration(ETHERNET_INTERFACE_TABS);
        changePropertiesTabConfiguration(ETHERNET_INTERFACE_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(ETHERNET_INTERFACE_PROPERTIES_ATTRIBUTES_LIST);
    }

    @Test(priority = 11)
    @Description("Aggregated Ethernet Interface Configuration")
    public void aggregatedEthernetInterfaceIVVerification() {
        openInventoryView();
        searchForObjectType("Aggregated Ethernet Interface");
        changeMainTableConfiguration(MAIN_TABLE_AGGREGATED_ETHERNET_INTERFACE_CONFIGURATION);
        assertMainTableColumns(AGGREGATED_ETHERNET_INTERFACE_MAIN_TABLE);
        changeTabsConfiguration(TABS_AGGREGATED_ETHERNET_INTERFACE_CONFIGURATION);
        assertTabsConfiguration(AGGREGATED_ETHERNET_INTERFACE_TABS);
        changePropertiesTabConfiguration(AGGREGATED_ETHERNET_INTERFACE_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(AGGREGATED_ETHERNET_INTERFACE_PROPERTIES_ATTRIBUTES_LIST);
    }

    @Test(priority = 12)
    @Description("Loopback Interface Configuration")
    public void loopbackInterfaceIVVerification() {
        openInventoryView();
        searchForObjectType("Loopback Interface");
        changeMainTableConfiguration(MAIN_TABLE_LOOPBACK_INTERFACE_CONFIGURATION);
        assertMainTableColumns(LOOPBACK_INTERFACE_MAIN_TABLE);
        changeTabsConfiguration(TABS_LOOPBACK_INTERFACE_CONFIGURATION);
        assertTabsConfiguration(LOOPBACK_INTERFACE_TABS);
        changePropertiesTabConfiguration(LOOPBACK_INTERFACE_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(LOOPBACK_INTERFACE_PROPERTIES_ATTRIBUTES_LIST);
    }

    @Test(priority = 13)
    @Description("IRB Interface Configuration")
    public void irbInterfaceIVVerification() {
        openInventoryView();
        searchForObjectType("IRB Interface");
        changeMainTableConfiguration(MAIN_TABLE_IRB_INTERFACE_CONFIGURATION);
        assertMainTableColumns(IRB_INTERFACE_MAIN_TABLE);
        changeTabsConfiguration(TABS_IRB_INTERFACE_CONFIGURATION);
        assertTabsConfiguration(IRB_INTERFACE_TABS);
        changePropertiesTabConfiguration(IRB_INTERFACE_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(IRB_INTERFACE_PROPERTIES_ATTRIBUTES_LIST);
    }

    @Test(priority = 14)
    @Description("VLAN Interface Configuration")
    public void vlanInterfaceIVVerification() {
        openInventoryView();
        searchForObjectType("VLAN Interface");
        changeMainTableConfiguration(MAIN_TABLE_VLAN_INTERFACE_CONFIGURATION);
        assertMainTableColumns(VLAN_INTERFACE_MAIN_TABLE);
        changeTabsConfiguration(TABS_VLAN_INTERFACE_CONFIGURATION);
        assertTabsConfiguration(VLAN_INTERFACE_TABS);
        changePropertiesTabConfiguration(VLAN_INTERFACE_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(VLAN_INTERFACE_PROPERTIES_ATTRIBUTES_LIST);
    }

    @Test(priority = 15)
    @Description("Traffic Class Configuration")
    public void trafficClassIVVerification() {
        openInventoryView();
        searchForObjectType("Traffic Class");
        changeMainTableConfiguration(MAIN_TABLE_TRAFFIC_CLASS_CONFIGURATION);
        assertMainTableColumns(TRAFFIC_CLASS_MAIN_TABLE);
        changeTabsConfiguration(TABS_TRAFFIC_CLASS_CONFIGURATION);
        assertTabsConfiguration(TRAFFIC_CLASS_TABS);
        changePropertiesTabConfiguration(TRAFFIC_CLASS_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(TRAFFIC_CLASS_PROPERTIES_ATTRIBUTES_LIST);
    }

    @Test(priority = 16)
    @Description("Traffic Policy Configuration")
    public void trafficPolicyIVVerification() {
        openInventoryView();
        searchForObjectType("Traffic Policy");
        changeMainTableConfiguration(MAIN_TABLE_TRAFFIC_POLICY_CONFIGURATION);
        assertMainTableColumns(TRAFFIC_POLICY_MAIN_TABLE);
        changeTabsConfiguration(TABS_TRAFFIC_POLICY_CONFIGURATION);
        assertTabsConfiguration(TRAFFIC_POLICY_TABS);
        changePropertiesTabConfiguration(TRAFFIC_POLICY_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(TRAFFIC_POLICY_PROPERTIES_ATTRIBUTES_LIST);
    }

    @Test(priority = 17)
    @Description("VLAN Pool Configuration")
    public void vlanPoolIVVerification() {
        openInventoryView();
        searchForObjectType("VLAN Pool");
        changeMainTableConfiguration(MAIN_TABLE_VLAN_POOL_CONFIGURATION);
        assertMainTableColumns(VLAN_POOL_MAIN_TABLE);
        changeTabsConfiguration(TABS_VLAN_POOL_CONFIGURATION);
        assertTabsConfiguration(VLAN_POOL_TABS);
        changePropertiesTabConfiguration(VLAN_POOL_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(VLAN_POOL_PROPERTIES_ATTRIBUTES_LIST);
    }

    @Test(priority = 18)
    @Description("VLAN Range Configuration")
    public void vlanRangeIVVerification() {
        openInventoryView();
        searchForObjectType("VLAN Range");
        changeMainTableConfiguration(MAIN_TABLE_VLAN_RANGE_CONFIGURATION);
        assertMainTableColumns(VLAN_RANGE_MAIN_TABLE);
        changeTabsConfiguration(TABS_VLAN_RANGE_CONFIGURATION);
        assertTabsConfiguration(VLAN_RANGE_TABS);
        changePropertiesTabConfiguration(VLAN_RANGE_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(VLAN_RANGE_PROPERTIES_ATTRIBUTES_LIST);
    }

    @Test(priority = 19)
    @Description("VxLAN Instance Configuration")
    public void vxlanInstanceIVVerification() {
        openInventoryView();
        searchForObjectType("VxLAN Instance");
        changeMainTableConfiguration(MAIN_TABLE_VXLAN_INSTANCE_CONFIGURATION);
        assertMainTableColumns(VXLAN_INSTANCE_MAIN_TABLE);
        changeTabsConfiguration(TABS_VXLAN_INSTANCE_CONFIGURATION);
        assertTabsConfiguration(VXLAN_INSTANCE_TABS);
        changePropertiesTabConfiguration(VXLAN_INSTANCE_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(VXLAN_INSTANCE_PROPERTIES_ATTRIBUTES_LIST);
    }

    @Test(priority = 20)
    @Description("EVPN Instance Configuration")
    public void evpnInstanceIVVerification() {
        openInventoryView();
        searchForObjectType("EVPN Instance");
        changeMainTableConfiguration(MAIN_TABLE_EVPN_INSTANCE_CONFIGURATION);
        assertMainTableColumns(EVPN_INSTANCE_MAIN_TABLE);
        changeTabsConfiguration(TABS_EVPN_INSTANCE_CONFIGURATION);
        assertTabsConfiguration(EVPN_INSTANCE_TABS);
        changePropertiesTabConfiguration(EVPN_INSTANCE_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(EVPN_INSTANCE_PROPERTIES_ATTRIBUTES_LIST);
    }

    @Test(priority = 21)
    @Description("IP Network Element Configuration")
    public void ipNetworkElementIVVerification() {
        openInventoryView();
        searchForObjectType("IP Network Element");
        changeMainTableConfiguration(MAIN_TABLE_IP_NETWORK_ELEMENT_CONFIGURATION);
        assertMainTableColumns(IP_NETWORK_ELEMENT_MAIN_TABLE);
        changeTabsConfiguration(TABS_IP_NETWORK_ELEMENT_CONFIGURATION);
        assertTabsConfiguration(IP_NETWORK_ELEMENT_TABS);
        changePropertiesTabConfiguration(IP_NETWORK_ELEMENT_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(IP_NETWORK_ELEMENT_PROPERTIES_ATTRIBUTES_LIST);
    }

    @Test(priority = 22)
    @Description("VTEP Configuration")
    public void vtepIVVerification() {
        openInventoryView();
        searchForObjectType("VTEP");
        changeMainTableConfiguration(MAIN_TABLE_VTEP_CONFIGURATION);
        assertMainTableColumns(VTEP_MAIN_TABLE);
        changeTabsConfiguration(TABS_VTEP_CONFIGURATION);
        assertTabsConfiguration(VTEP_TABS);
        changePropertiesTabConfiguration(VTEP_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(VTEP_PROPERTIES_ATTRIBUTES_LIST);
    }

    @Test(priority = 23)
    @Description("L3Out Configuration")
    public void l3outIVVerification() {
        openInventoryView();
        searchForObjectType("L3Out Apic");
        changeMainTableConfiguration(MAIN_TABLE_L3OUT_CONFIGURATION);
        assertMainTableColumns(L3OUT_MAIN_TABLE);
        changeTabsConfiguration(TABS_L3OUT_CONFIGURATION);
        assertTabsConfiguration(L3OUT_TABS);
        changePropertiesTabConfiguration(L3OUT_PROPERTIES_CONFIGURATION);
        assertPropertiesTabConfiguration(L3OUT_PROPERTIES_ATTRIBUTES_LIST);
    }

    private void openInventoryViewPage() {
        SideMenu sidemenu = SideMenu.create(driver, webDriverWait);
        sidemenu.callActionByLabel(INVENTORY_VIEW_SIDE_MENU, RESOURCE_INVENTORY_SIDE_MENU);
    }

    private void searchForObjectType(String objectType) {
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType(objectType);
    }

    private void changeMainTableConfiguration(String tableConfigurationName) {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.applyConfigurationForMainTable(tableConfigurationName);
        newInventoryViewPage.selectFirstRow();
    }

    private void assertMainTableColumns(List<String> expectedColumnHeaders) {
        TableComponent tableComponent = TableComponent.createById(driver, webDriverWait, "table-MainTableWidget");
        List<String> actualColumnHeaders = tableComponent.getColumnHeaders();
        List<String> diffList = new ArrayList<>();
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(actualColumnHeaders.size(), expectedColumnHeaders.size(), "List sizes do not match");

        for (int i = 0; i < expectedColumnHeaders.size(); i++) {
            if (!actualColumnHeaders.contains(expectedColumnHeaders.get(i))) {
                diffList.add(expectedColumnHeaders.get(i));
            } else if (!expectedColumnHeaders.contains(actualColumnHeaders.get(i))) {
                diffList.add(actualColumnHeaders.get(i));
            }
        }

        softAssert.assertTrue(diffList.isEmpty(), "Different elements in Column Headers lists: " + diffList);
        softAssert.assertAll();
    }

    private void changeTabsConfiguration(String tabsConfigurationName) {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.applyConfigurationForTabs(tabsConfigurationName);
    }

    private void assertTabsConfiguration(List<String> tabsList) {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        SoftAssert softAssert = new SoftAssert();
        for (String tab : tabsList) {
            softAssert.assertTrue(newInventoryViewPage.isTabVisible(tab), String.format("Missing tab: %s", tab));
        }
        softAssert.assertAll();
    }

    private void changePropertiesTabConfiguration(String propertiesConfigurationName) {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.applyConfigurationForProperties(0, "PropertyPanelWidget", propertiesConfigurationName);
    }

    private void assertPropertiesTabConfiguration(List<String> propertiesAttributesList) {
        PropertyPanel propertyPanel = PropertyPanel.createById(driver, webDriverWait, "PropertyPanelWidget");
        List<String> actualPropertiesAttributesList = propertyPanel.getPropertyLabels();

        List<String> diffList = new ArrayList<>();
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(actualPropertiesAttributesList.size(), propertiesAttributesList.size(), "List sizes do not match");

        for (int i = 0; i < propertiesAttributesList.size(); i++) {
            if (!actualPropertiesAttributesList.contains(propertiesAttributesList.get(i))) {
                diffList.add(propertiesAttributesList.get(i));
            } else if (!propertiesAttributesList.contains(actualPropertiesAttributesList.get(i))) {
                diffList.add(actualPropertiesAttributesList.get(i));
            }
        }

        softAssert.assertTrue(diffList.isEmpty(), "Different elements in Properties Tab lists: " + diffList);
        softAssert.assertAll();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}
