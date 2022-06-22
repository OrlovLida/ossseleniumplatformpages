package com.oss.nfv.networkSliceSubnet;

import com.comarch.oss.logical.function.api.dto.LogicalFunctionViewDTO;
import com.comarch.oss.resourcecatalog.tmf.api.dto.ResourceSpecificationCreationDTO;
import com.oss.BaseTestCase;
import com.oss.services.LogicalFunctionCoreClient;
import com.oss.services.LogicalInventoryViewClient;
import com.oss.services.nfv.networkslice.NetworkSliceApiClient;
import com.oss.services.resourcecatalog.tmf.TMFCatalogClient;
import com.oss.untils.Environment;
import com.oss.utils.TestListener;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static com.oss.nfv.networkSliceSubnet.CreateNetworkSliceSubnetConstants.NETWORK_SLICE_SUBNET_NAME;
import static com.oss.nfv.networkSliceSubnet.CreateNetworkSliceSubnetConstants.NETWORK_SLICE_SUBNET_SPECIFICATION_IDENTIFIER;

@Listeners({TestListener.class})
public abstract class BaseNetworkSliceSubnetTest extends BaseTestCase {

    private final String CUSTOM_WIZARD_FILE_PATH_TO_TEST =
            "src/test/resources/nfv/networkSliceSubnet/networkSliceSubnetCustomWizard.json";

    protected Environment env = Environment.getInstance();

    @BeforeClass
    public void prepareData() throws IOException {
        cleanData();
        createNetworkSliceSubnetSpecification();
        saveCustomWizard();
    }

    @AfterClass(alwaysRun = true)
    public void cleanData() {
        deleteAnyNetworkSliceSubnetInstancesByName();
        deleteNetworkSliceSubnetSpecification();
        deleteCustomWizard();
    }

    private void saveCustomWizard() throws IOException {
        String jsonContent = new String(Files.readAllBytes(Paths.get(CUSTOM_WIZARD_FILE_PATH_TO_TEST)));
        LogicalInventoryViewClient.getInstance(env)
                .saveCustomWizard(jsonContent);
    }
    private void deleteCustomWizard() {
        LogicalInventoryViewClient.getInstance(env)
                .deleteCustomWizard(NETWORK_SLICE_SUBNET_SPECIFICATION_IDENTIFIER);
    }

    private void createNetworkSliceSubnetSpecification() {
        TMFCatalogClient tmfCatalogClient = TMFCatalogClient.getInstance(env);
        ResourceSpecificationCreationDTO resourceSpecificationCreationDTO = NetworkSliceSubnetResource
                .buildNetworkSliceSubnetResourceSpecificationCreationDTO();
        tmfCatalogClient.createResourceSpecification(resourceSpecificationCreationDTO);
    }

    private void deleteNetworkSliceSubnetSpecification() {
        TMFCatalogClient.getInstance(env)
                .deleteResourceSpecification(NETWORK_SLICE_SUBNET_SPECIFICATION_IDENTIFIER, true);
    }

    private void deleteAnyNetworkSliceSubnetInstancesByName() {
        NetworkSliceApiClient networkSliceApiClient = NetworkSliceApiClient.getInstance(env);
        LogicalFunctionCoreClient.getInstance(env).getLogicalFunctionByName(NETWORK_SLICE_SUBNET_NAME).stream()
                .map(LogicalFunctionViewDTO::getId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(networkSliceApiClient::deleteNetworkSliceSubnet);
    }

}
