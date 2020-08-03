package com.oss.pages.exportguiwizard;

import com.oss.framework.data.Data;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class FillServerDataPage extends ExportGuiWizardPage{

    public FillServerDataPage(WebDriver driver){super(driver);getWizard();}

    private String PROTOCOLE_TYPE_ID = "exportgui-components-protocoletypechoose";
    private String SERVER_ADDRESS_ID = "exportgui-components-serveraddresstext";
    private String REMOTE_DIRECTORY_PATH_ID = "exportgui-components-directorypathtext";
    private String USER_NAME_ID = "exportgui-components-remoteusernametext";
    private String PASSWORD_ID = "exportgui-components-remotepasswordtext";

    @Step("Type Server Address")
    public FillServerDataPage typeServerAddress(String serverAddress) {
        setValueOnTextField(SERVER_ADDRESS_ID,Data.createSingleData(serverAddress));
        return this;
    }

    @Step("Type Remote Directory Path")
    public FillServerDataPage typeRemoteDirectoryPath(String remoteDirectoryPath) {
        setValueOnTextField(REMOTE_DIRECTORY_PATH_ID,Data.createSingleData(remoteDirectoryPath));
        return this;
    }

    @Step("Type User Name")
    public FillServerDataPage typeUserName(String userName) {
        setValueOnTextField(USER_NAME_ID,Data.createSingleData(userName));
        return this;
    }

    @Step("Type password")
    public FillServerDataPage typePassword(String password) {
        setValueOnTextField(PASSWORD_ID,Data.createSingleData(password));
        return this;
    }

    @Step("Choose Protocole Type")
    public FillServerDataPage chooseProtocoleType(String type){
        waitForComponent("//input[contains (@id,'" + PROTOCOLE_TYPE_ID + "')]");
        setValueOnCombobox(PROTOCOLE_TYPE_ID, type);
        return this;
    }
}
