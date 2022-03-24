package com.oss.pages.languageservice;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.BasePage;
import com.oss.pages.exportguiwizard.ExportGuiWizardPage;
import com.oss.pages.platform.NotificationWrapperPage;

import io.qameta.allure.Step;

public class LanguageServicePage extends BasePage {

    private static String WIDGET_ID = "TranslationsView_MainWidget";

    @Step("Open Language Service Page")
    public static LanguageServicePage goToLanguageServicePage(WebDriver driver, String baseURL) {
        driver.get(String.format("%s/#/views/languagesservice/views/translations" +
                "?perspective=LIVE", baseURL));
        return new LanguageServicePage(driver);
    }

    public LanguageServicePage(WebDriver driver) {
        super(driver);
    }

    private static final String EXPORT_BUTTON_ID = "exportButton";

    @Step("Open Export File Wizard")
    public ExportGuiWizardPage openExportFileWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        tableWidget().callAction(ActionsContainer.KEBAB_GROUP_ID, TableWidget.EXPORT_ACTION_ID);
        return new ExportGuiWizardPage(driver);
    }

    @Step("Clear Notifications")
    public LanguageServicePage clearNotifications() {
        openNotificationPanel()
                .clearNotifications()
                .close();
        return this;
    }

    public int howManyNotifications() {
        int amountOfNotifications = openNotificationPanel()
                .waitForExportFinish()
                .amountOfNotifications();
        closeNotificationPanel();
        return amountOfNotifications;
    }

    @Step("Type ID of First Service in Search")
    public LanguageServicePage typeIdOfFirstServiceInSearch() {
        DelayUtils.waitForPageToLoad(driver, wait);
        String idOfFirstElement = tableWidget().getCellValue(0, "code");
        tableWidget().fullTextSearch(idOfFirstElement);
        return this;
    }

    private LanguageServicePage closeNotificationPanel() {
        new NotificationWrapperPage(driver).close();
        return this;
    }

    private TableWidget tableWidget() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return TableWidget.createById(driver, WIDGET_ID, wait);
    }
}

