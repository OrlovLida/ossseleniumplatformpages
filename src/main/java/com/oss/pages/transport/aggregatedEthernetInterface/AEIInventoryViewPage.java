package com.oss.pages.transport.aggregatedEthernetInterface;

import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.pages.BasePage;
import com.oss.pages.platform.OldInventoryViewPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kamil Jacko
 */
public class AEIInventoryViewPage extends BasePage {

    private final TabsInterface tabsInterface;
    private final ActionsInterface actionsInterface;
    private Map<String, String> propertiesToValuesMap = new HashMap<>();

    private static final String NUMBER = "Number";
    private static final String DEVICE_NAME = "Name";
    private static final String DESCRIPTION = "Description";
    private static final String AGGREGATION_PROTOCOL = "Aggregation Protocol";
    private static final String LACP_MODE = "LACP Mode";
    private static final String LACP_SHORT_PERIOD = "LACP Short Period";
    private static final String MINIMUM_ACTIVE_LINKS = "Minimum Links";
    private static final String MINIMUM_BANDWIDTH = "Minimum Bandwidth [mbps]";
    private static final String MTU = "MTU";
    private static final String MAC_ADDRESS = "MAC address";
    private static final String ADMINISTRATIVE_STATE = "Administrative State";
    private static final String ENCAPSULATION = "Encapsulation";

    private static final String OK_BUTTON_TEXT = "Ok";
    private static final String REMOVE_AEI_MESSAGE = "Aggregated Ethernet Interface was removed successfully";

    private static final String PROPERTIES_TAB = "Properties";
    private static final String DETAIL_SERVER_NODES_TAB = "Server Termination Points";

    private static final String SELECT_AEI_PATH = "//div[contains(@class, 'Cell Row_AggregatedEthernetInterface')]";
    private static final String INTERFACE_NAME_PATTERN = "//div[@class='OSSTableColumn Col_Name(Node) center draggable']//div[text()='%s']";

    public AEIInventoryViewPage(WebDriver driver) {
        super(driver);
        DelayUtils.waitForPageToLoad(driver, wait);
        actionsInterface = ActionsContainer.createFromParent(driver.findElement(By.xpath("//div[@class='windowContent']")), driver, wait);
        tabsInterface = TabWindowWidget.create(driver, wait);
    }

    public void selectAEI() {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement aggregatedEthernetInterfaceBar = driver.findElement(By.xpath(SELECT_AEI_PATH));
        aggregatedEthernetInterfaceBar.click();
    }

    public AEIWizardPage editAEI() {
        actionsInterface.callActionByLabel("EDIT", "Edit Aggregated Ethernet Interface");
        return new AEIWizardPage(driver);
    }

    public void removeAEI() {
        actionsInterface.callActionByLabel("EDIT", "Delete Aggregated Ethernet Interface");
        clickOk();
    }

    public void clickOk() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button removeAEIButton = Button.create(driver, OK_BUTTON_TEXT);
        removeAEIButton.click();
    }

    public boolean isRemoveMessageCorrect() {
        DelayUtils.waitForPageToLoad(driver, wait);
        SystemMessageInterface message = SystemMessageContainer.create(driver, wait);
        return message.getFirstMessage().get().getText().equals(REMOVE_AEI_MESSAGE);
    }

    public void obtainPropertyValues() {
        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        propertiesToValuesMap = oldInventoryViewPage.getTableWidget().getPropertyNamesToValues();
    }

    public String getNumberValue() {
        return propertiesToValuesMap.get(NUMBER);
    }

    public String getDeviceNameValue() {
        return propertiesToValuesMap.get(DEVICE_NAME);
    }

    public String getDescriptionValue() {
        return propertiesToValuesMap.get(DESCRIPTION);
    }

    public String getAggregationProtocolValue() {
        return propertiesToValuesMap.get(AGGREGATION_PROTOCOL);
    }

    public String getLacpModeValue() {
        return propertiesToValuesMap.get(LACP_MODE);
    }

    public String getLacpShortPeriodValue() {
        return propertiesToValuesMap.get(LACP_SHORT_PERIOD);
    }

    public String getMinimumActiveLinksValue() {
        return propertiesToValuesMap.get(MINIMUM_ACTIVE_LINKS);
    }

    public String getMinimumBandwidthValue() {
        return propertiesToValuesMap.get(MINIMUM_BANDWIDTH);
    }

    public String getMTUValue() {
        return propertiesToValuesMap.get(MTU);
    }

    public String getMacAddressValue() {
        return propertiesToValuesMap.get(MAC_ADDRESS);
    }

    public String getAdministrativeStateValue() {
        return propertiesToValuesMap.get(ADMINISTRATIVE_STATE);
    }

    public String getEncapsulationValue() {
        return propertiesToValuesMap.get(ENCAPSULATION);
    }

    public void openPropertiesTab() {
        DelayUtils.waitForPageToLoad(driver, wait);
        tabsInterface.selectTabByLabel(PROPERTIES_TAB);
    }

    public void openInterfaceAssignmentTab() {
        DelayUtils.waitForPageToLoad(driver, wait);
        tabsInterface.selectTabByLabel(DETAIL_SERVER_NODES_TAB);
    }

    public String getInterface(String assignedInterfaceName) {
        String pattern = String.format(INTERFACE_NAME_PATTERN, assignedInterfaceName);
        WebElement element = driver.findElement(By.xpath(pattern));
        return element.getText();
    }
}
