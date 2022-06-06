package com.oss.pages.exportguiwizard;

import com.oss.framework.utils.DelayUtils;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class FillServerDataPage extends ExportGuiWizardPage{

    public FillServerDataPage(WebDriver driver){super(driver);getWizard();}

    private static final String PROTOCOLE_TYPE_ID = "exportgui-components-protocoletypechoose";
    private static final String SERVER_ADDRESS_ID = "exportgui-components-serveraddresstext";
    private static final String REMOTE_DIRECTORY_PATH_ID = "exportgui-components-directorypathtext";
    private static final String USER_NAME_ID = "exportgui-components-remoteusernametext";
    private static final String PASSWORD_ID = "exportgui-components-remotepasswordtext";

    @Step("Type Server Address")
    public FillServerDataPage typeServerAddress(String serverAddress) {
        setTextValue(SERVER_ADDRESS_ID,serverAddress);
        return this;
    }

    @Step("Type Remote Directory Path")
    public FillServerDataPage typeRemoteDirectoryPath(String remoteDirectoryPath) {
        setTextValue(REMOTE_DIRECTORY_PATH_ID, remoteDirectoryPath);
        return this;
    }

    @Step("Type User Name")
    public FillServerDataPage typeUserName(String userName) {
        setTextValue(USER_NAME_ID, userName);
        return this;
    }

    @Step("Type password")
    public FillServerDataPage typePassword(String password) {
        setTextValue(PASSWORD_ID,password);
        return this;
    }

    @Step("Choose Protocole Type")
    public FillServerDataPage chooseProtocoleType(String type){
        DelayUtils.waitForPageToLoad(driver, wait);
        setComboboxValue(PROTOCOLE_TYPE_ID, type);
        return this;
    }
}
