package com.oss.pages.bpm;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.framework.widgets.tablewidget.TableRow;
import com.oss.pages.BasePage;
import com.oss.pages.dms.AttachFileWizardPage;
import org.apache.logging.log4j.core.util.Assert;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.List;

/**
 * @author Paweł Rother
 */
public class ProcessModelsPage extends BasePage {


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

    private TableInterface getModelsTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createByComponentDataAttributeName(driver, wait, "bpm_models_view_app-model-list");
    }

    public void selectProcessModelByName(String name) {
        getModelsTable().searchByAttributeWithLabel("Name", Input.ComponentType.TEXT_FIELD, name);
        getModelsTable().selectRow(0);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void callAction(String actionId) {
        getModelsTable().callAction(actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void callAction(String groupId, String actionId) {
        getModelsTable().callAction(groupId, actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public boolean isFileDownloaded(String downloadPath, String fileName) {
        File dir = new File(downloadPath);
        File[] dirContents = dir.listFiles();
        if (dirContents == null) {
            return false;
        }

        for (int i = 0; i < dirContents.length; i++) {
            if (dirContents[i].getName().contains(fileName)) {
                // File has been found, it can now be deleted:
                dirContents[i].delete();
                return true;
            }
        }
        return false;
    }


    public void deleteModel(String modelName) {
        TableInterface modelsList = getModelsTable();
        modelsList.searchByAttributeWithLabel("Name", Input.ComponentType.TEXT_FIELD, modelName);
        modelsList.selectRow(0);
        DelayUtils.sleep(1000);
        modelsList.callAction("grouping-action-model-operations", "delete");
        Wizard deleteWizard = Wizard.createByComponentId(driver, wait, "bpm_models_view_delete-model-popup");
        deleteWizard.clickActionById("ConfirmationBox_bpm_models_view_delete-model-popup_DELETE_MODEL_CONFIRMATION_BOX_VIEW_ID_action_button");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void cloneModel(String baseModelName, String clonedModelName, String clonedIdentifier, String clonedDescription) {
        TableInterface modelsList = getModelsTable();
        modelsList.searchByAttributeWithLabel("Name", Input.ComponentType.TEXT_FIELD, baseModelName);
        modelsList.selectRow(0);
        DelayUtils.sleep(1000);
        modelsList.callAction("grouping-action-model-operations", "clone");
        Wizard cloneWizard = Wizard.createByComponentId(driver, wait, "bpm_models_view_clone-model-popup");
        Input name = cloneWizard.getComponent("name-field", Input.ComponentType.TEXT_FIELD);
        Input identifier = cloneWizard.getComponent("identifier-field", Input.ComponentType.TEXT_FIELD);
        Input description = cloneWizard.getComponent("description-field", Input.ComponentType.TEXT_FIELD);
        name.clear();
        name.setSingleStringValue(clonedModelName);
        identifier.clear();
        identifier.setSingleStringValue(clonedIdentifier);
        description.clear();
        description.setSingleStringValue(clonedDescription);
        cloneWizard.clickActionById("bpm_models_view_clone-model-popup_clone-model-buttons-1");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void setKeyword(String keyword) {

    }

}
