package com.oss.nfv.onboardNetworkService;

import com.comarch.oss.logical.function.api.dto.LogicalFunctionSyncIdentificationDTO;
import com.comarch.oss.logical.function.api.dto.LogicalFunctionViewDTO;
import com.comarch.oss.logical.function.v2.api.dto.LogicalFunctionBulkDTO;
import com.oss.BaseTestCase;
import com.oss.services.LogicalFunctionClient;
import com.oss.untils.Environment;
import com.oss.utils.TestListener;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import java.io.IOException;
import java.util.Optional;

import static com.oss.nfv.onboardNetworkService.OnboardNetworkServiceConstants.NFVO_NAME;

@Listeners({TestListener.class})
public class BaseOnboardNetworkServiceTest extends BaseTestCase {

    protected Environment env = Environment.getInstance();
    private LogicalFunctionClient logicalFunctionClient;

    @BeforeClass
    public void prepareData() throws IOException {
        logicalFunctionClient = LogicalFunctionClient.getInstance(env);
        deleteLogicalFunctions();
        createRequiredLogicalFunctions();
    }

    public void createRequiredLogicalFunctions(){
        createLogicalFunction(OnboardNetworkServiceResource.buildNFVOLogicalFunctionBulkDTO());
    }

    private LogicalFunctionSyncIdentificationDTO createLogicalFunction(LogicalFunctionBulkDTO logicalFunctionBulkDTO) {
        return logicalFunctionClient
                .createLogicalFunctionBulk(logicalFunctionBulkDTO)
                .getLogicalFunctionsIdentifications()
                .get(0);
    }

    @AfterClass(alwaysRun = true)
    public void cleanData() {
        deleteLogicalFunctions();
    }

    private void deleteLogicalFunctions() {
        deleteLogicalFunctionsByName(NFVO_NAME);
    }

    private void deleteLogicalFunctionsByName(String masterOSSName) {
        logicalFunctionClient.getLogicalFunctionByName(masterOSSName).stream()
                .map(LogicalFunctionViewDTO::getId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(logicalFunctionClient::deleteLogicalFunction);
    }

}
