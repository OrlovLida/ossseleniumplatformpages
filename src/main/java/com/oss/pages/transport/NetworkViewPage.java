package com.oss.pages.transport;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageContainer.Message;
import com.oss.framework.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.dockedPanel.DockedPanel;
import com.oss.framework.widgets.dockedPanel.DockedPanelInterface;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class NetworkViewPage extends BasePage {

    public NetworkViewPage(WebDriver driver) {
        super(driver);
    }

    @Step("Expand docked panel")
    public void expandDockedPanel(String position) {
        DelayUtils.waitForPageToLoad(driver, wait);
        DockedPanelInterface dockedPanel = DockedPanel.createDockedPanelByPosition(driver, wait, position);
        dockedPanel.expandDockedPanel(position);
    }

    @Step("Select object in view content")
    public void selectObjectInViewContent(String name, String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, "leftPanelTab");
        table.selectRowByAttributeValueWithLabel(name, value);
    }

    @Step("Use context action")
    public void useContextAction(String group, String action) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ActionsInterface actionsContainer = OldActionsContainer.createFromParent(driver, wait, driver.findElement(By.xpath("//div[@class='OssWindow']//div[@class='windowIcons']")));
        actionsContainer.callAction(group, action);
    }

    @Step("Check system message")
    public void checkSystemMessage() {
        DelayUtils.waitForPageToLoad(driver, wait);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, wait);
        List<Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(MessageType.SUCCESS);
    }

}
