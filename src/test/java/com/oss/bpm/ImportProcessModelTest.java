package com.oss.bpm;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.ProcessInstancesPage;
import com.oss.pages.bpm.ProcessModelsPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.utils.TestListener;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

@Listeners({TestListener.class})
public class ImportProcessModelTest extends BaseTestCase {

    private static final String DOMAIN_NAME = "Inventory Processes";
    private static final String MODEL_NAME = "pr_proces";


    @BeforeClass
    public void openProcessModelsPage(){
        ProcessModelsPage processModelsPage = ProcessModelsPage.goToProcessModelsPage(driver, BASIC_URL);
        DelayUtils.sleep(2000);
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        processModelsPage.chooseDomain(DOMAIN_NAME);
    }

    @Test (priority = 1)
    public void importModel(){
        try {
            ProcessModelsPage processModelsPage = new ProcessModelsPage(driver);
            URL resource = ImportProcessModelTest.class.getClassLoader().getResource("bpm/pr_proces.bar");
            String absolutePatch = Paths.get(resource.toURI()).toFile().getAbsolutePath();
            processModelsPage.importModel(absolutePatch);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cannot load file", e);
        }
        DelayUtils.waitForPageToLoad(driver,webDriverWait);

    }

    @Test (priority = 2)
    public void deleteModel(){
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        ProcessModelsPage processModelsPage = new ProcessModelsPage(driver);
        processModelsPage.deleteModel(MODEL_NAME);
    }
}
