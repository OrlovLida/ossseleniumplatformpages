package com.oss.pages.bpm;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.pages.BasePage;
import com.oss.pages.dms.AttachFileWizardPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;

public class ProcessModelsPage extends BasePage {
    private final static String IMPORT_BUTTON = "bpm_models_view_import-model-popup_model-import-button-1";


    public ProcessModelsPage(WebDriver driver) {
        super(driver);
    }

    public static ProcessModelsPage goToProcessModelsPage(WebDriver driver, WebDriverWait webDriverWait) {
        SideMenu sidemenu = SideMenu.create(driver, webDriverWait);
        if (driver.getPageSource().contains("BPM and Planning"))
            sidemenu.callActionByLabel("Process Models", "BPM and Planning", "Business Process Management");
        else
            sidemenu.callActionByLabel("Process Models", "Views", "Business Process Management");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return new ProcessModelsPage(driver);
    }

    public static ProcessModelsPage goToProcessModelsPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/bpm/models" +
                "?perspective=LIVE", basicURL));

        return new ProcessModelsPage(driver);
    }

    public void chooseDomain(String domain) {
        Input domain_input = ComponentFactory.create("domain-chooser--domain-combobox", Input.ComponentType.COMBOBOX, driver, wait);
        domain_input.setSingleStringValue(domain);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public TableInterface getModelsTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createByComponentDataAttributeName(driver, wait, "bpm_models_view_app-model-list");
    }

    public void selectProcessModelByName(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getModelsTable().searchByAttributeWithLabel("Name", Input.ComponentType.TEXT_FIELD, name);
        getModelsTable().selectRow(0);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void callContextAction(String actionId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getModelsTable().callAction(actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }
    public boolean isFileDownloaded(String downloadPath, String fileName) {
        File dir = new File(downloadPath);
        File[] dirContents = dir.listFiles();

        for (int i = 0; i < dirContents.length; i++) {
            if (dirContents[i].getName().contains(fileName)) {
                // File has been found, it can now be deleted:
                dirContents[i].delete();
                return true;
            }
        }
        return false;
    }

    public void importModel(String filePath){
        callContextAction("import");
        Wizard importWizard = Wizard.createByComponentId(driver, wait, "bpm_models_view_import-model-popup");
        Input component = importWizard.getComponent("bpm_models_view_import-model-popup_model-import-form", Input.ComponentType.FILE_CHOOSER);
        component.setSingleStringValue(filePath);
        importWizard.clickActionById(IMPORT_BUTTON);
    }

    public void deleteModel(String modelName){
        OldTable modelsList = OldTable.createByComponentDataAttributeName(driver, wait, "bpm_models_view_app-model-list");
        modelsList.searchByAttributeWithLabel("Name", Input.ComponentType.TEXT_FIELD,modelName);
        modelsList.selectRow(0);
        DelayUtils.sleep(1000);
        modelsList.callAction("delete");
        Wizard deleteWizard = Wizard.createByComponentId(driver,wait,"bpm_models_view_delete-model-popup");
        deleteWizard.clickActionById("ConfirmationBox_bpm_models_view_delete-model-popup_DELETE_MODEL_CONFIRMATION_BOX_VIEW_ID_action_button");
    }

}
