package com.oss.pages.exportguiwizard;

import com.oss.framework.data.Data;
import org.openqa.selenium.WebDriver;

public class FillServerDataPage extends ExportGuiWizardPage{

    public FillServerDataPage(WebDriver driver){super(driver);getWizard();}

    private String PROTOCOLE_TYPE_ID = "exportgui-components-protocoletypechoose";
    private String SERVER_ADDRESS_ID = "exportgui-components-serveraddresstext";
    private String REMOTE_DIRECTORY_PATH_ID = "exportgui-components-directorypathtext";
    private String USER_NAME_ID = "exportgui-components-remoteusernametext";
    private String PASSWORD_ID = "exportgui-components-remotepasswordtext";

    public FillServerDataPage typeServerAddress(String serverAddress) {
        setValueOnTextField(SERVER_ADDRESS_ID,Data.createSingleData(serverAddress));
        return this;
    }

    public FillServerDataPage typeRemoteDirectoryPath(String remoteDirectoryPath) {
        setValueOnTextField(REMOTE_DIRECTORY_PATH_ID,Data.createSingleData(remoteDirectoryPath));
        return this;
    }

    public FillServerDataPage typeUserName(String userName) {
        setValueOnTextField(USER_NAME_ID,Data.createSingleData(userName));
        return this;
    }

    public FillServerDataPage typePassword(String password) {
        setValueOnTextField(PASSWORD_ID,Data.createSingleData(password));
        return this;
    }

    public FillServerDataPage chooseProtocoleType(String type){
        waitForComponent("//input[contains (@id,'" + PROTOCOLE_TYPE_ID + "')]");
        setValueOnCombobox(PROTOCOLE_TYPE_ID, type);
        return this;
    }
}
