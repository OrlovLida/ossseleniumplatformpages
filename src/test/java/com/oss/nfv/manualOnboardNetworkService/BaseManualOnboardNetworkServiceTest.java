package com.oss.nfv.manualOnboardNetworkService;

import java.io.IOException;
import java.util.Optional;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import com.comarch.oss.logical.function.api.dto.LogicalFunctionSyncIdentificationDTO;
import com.comarch.oss.logical.function.api.dto.LogicalFunctionViewDTO;
import com.comarch.oss.resourcecatalog.tmf.api.dto.ResourceSpecificationDTO;
import com.oss.BaseTestCase;
import com.oss.services.LogicalFunctionClient;
import com.oss.services.resourcecatalog.tmf.TMFCatalogClient;
import com.oss.untils.Environment;
import com.oss.utils.TestListener;

import static com.oss.nfv.manualOnboardNetworkService.ManualOnboardNetworkServiceConstants.ERICSSON_NFVO_NAME;
import static com.oss.nfv.manualOnboardNetworkService.ManualOnboardNetworkServiceConstants.NS_PACKAGE_IDENTIFIER;

@Listeners({TestListener.class})
public class BaseManualOnboardNetworkServiceTest extends BaseTestCase {

    private final Environment env = Environment.getInstance();
    private final LogicalFunctionClient logicalFunctionClient = LogicalFunctionClient.getInstance(env);
    private final TMFCatalogClient tmfCatalogClient = TMFCatalogClient.getInstance(env);

    @BeforeClass
    public void prepareData() throws IOException {
        deleteData();
        createData();
    }

    @AfterClass(alwaysRun = true)
    public void cleanData() {
        deleteData();
    }

    private void createData() {
        createEricssonNFVOLogicalFunction();
        createNSPackageSpecification();
    }

    private void deleteData() {
        deleteEricssonNFVOLogicalFunction();
        deleteNSPackageSpecification();
    }

    private LogicalFunctionSyncIdentificationDTO createEricssonNFVOLogicalFunction() {
        return logicalFunctionClient
                .createLogicalFunctionBulk(ManualOnboardNetworkServiceDTOBuilder.buildNFVOLogicalFunctionBulkDTO())
                .getLogicalFunctionsIdentifications()
                .get(0);
    }

    private void createNSPackageSpecification() {
        Optional<ResourceSpecificationDTO> resourceSpecification = tmfCatalogClient.getResourceSpecification(NS_PACKAGE_IDENTIFIER);
        if(!resourceSpecification.isPresent()) {
            tmfCatalogClient.createResourceSpecification(ManualOnboardNetworkServiceDTOBuilder.buildNSPackageSpecification());
        }
    }

    private void deleteEricssonNFVOLogicalFunction() {
        logicalFunctionClient.getLogicalFunctionByName(ERICSSON_NFVO_NAME).stream()
                .map(LogicalFunctionViewDTO::getId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(logicalFunctionClient::deleteLogicalFunction);
    }

    private void deleteNSPackageSpecification() {
        tmfCatalogClient.deleteResourceSpecification(NS_PACKAGE_IDENTIFIER, true);
    }
}
