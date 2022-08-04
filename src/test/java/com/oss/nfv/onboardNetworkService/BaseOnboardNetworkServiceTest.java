package com.oss.nfv.onboardNetworkService;

import com.comarch.oss.logical.function.api.dto.LogicalFunctionSyncIdentificationDTO;
import com.comarch.oss.logical.function.api.dto.LogicalFunctionViewDTO;
import com.comarch.oss.logical.function.v2.api.dto.LogicalFunctionBulkDTO;
import com.oss.BaseTestCase;
import com.oss.services.LogicalFunctionCoreClient;
import com.oss.untils.Environment;
import com.oss.utils.TestListener;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import java.io.IOException;
import java.util.Optional;

import static com.oss.nfv.onboardNetworkService.OnboardNetworkServiceConstants.ERICSSON_NFVO_NAME;

@Listeners({TestListener.class})
public class BaseOnboardNetworkServiceTest extends BaseTestCase {

    protected Environment env = Environment.getInstance();
    private LogicalFunctionCoreClient logicalFunctionCoreClient;

    @BeforeClass
    public void prepareData() throws IOException {
        logicalFunctionCoreClient = LogicalFunctionCoreClient.getInstance(env);
        deleteLogicalFunctions();
        createRequiredLogicalFunctions();
    }

    public void createRequiredLogicalFunctions(){
        createLogicalFunction(OnboardNetworkServiceDTOBuilder.buildNFVOLogicalFunctionBulkDTO());
    }

    private LogicalFunctionSyncIdentificationDTO createLogicalFunction(LogicalFunctionBulkDTO logicalFunctionBulkDTO) {
        return logicalFunctionCoreClient
                .createLogicalFunctionBulk(logicalFunctionBulkDTO)
                .getLogicalFunctionsIdentifications()
                .get(0);
    }

    @AfterClass(alwaysRun = true)
    public void cleanData() {
        deleteLogicalFunctions();
    }

    private void deleteLogicalFunctions() {
        deleteLogicalFunctionsByName(ERICSSON_NFVO_NAME);
    }

    private void deleteLogicalFunctionsByName(String masterManagementSystemName) {
        logicalFunctionCoreClient.getLogicalFunctionByName(masterManagementSystemName).stream()
                .map(LogicalFunctionViewDTO::getId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(logicalFunctionCoreClient::deleteLogicalFunction);
    }

}
