package com.oss.bpm;

import com.oss.BaseTestCase;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.pages.bpm.Milestone;
import com.oss.pages.bpm.ProcessInstancesPage;
import com.oss.pages.bpm.ProcessModelsPage;
import com.oss.utils.TestListener;
import javafx.scene.control.ComboBox;
import org.apache.commons.logging.Log;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;


@Listeners({TestListener.class})
public class ExportModelTest extends BaseTestCase {

    private static final String DOMAIN = "Inventory Processes";
    private static final String PROCESS_NAME = "GK Milestones";
    private static final String FILE_NAME = PROCESS_NAME.replaceAll(" ","+");
    private static final String EXPORT_AS_BAR = "export-bar";
    private static final String EXPORT_WITH_CONFIG = "export-with-configuration-files";
    private static final String DEFAULT_PATH = "C:\\DANE\\ossseleniumplatformpages\\target\\downloadFiles";


    @BeforeClass
    public void openBrw(){
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }


    @Test (priority = 1)
    public void exportModelAsBar(){
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        ProcessModelsPage processModelsPage = ProcessModelsPage.goToProcessModelsPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        processModelsPage.chooseDomain(DOMAIN);
        processModelsPage.selectProcessModelByName(PROCESS_NAME);
        processModelsPage.callContextAction(EXPORT_AS_BAR);
        Assert.assertTrue(processModelsPage.isFileDownloaded(DEFAULT_PATH,FILE_NAME));

    }
}
