package com.oss.nfv.networkService;

import com.comarch.oss.logical.function.api.dto.LogicalFunctionIdentificationDTO;
import com.comarch.oss.resourcecatalog.tmf.api.dto.ResourceSpecificationDTO;
import com.oss.BaseTestCase;
import com.oss.services.LogicalFunctionClient;
import com.oss.services.nfv.networkservice.NetworkServiceApiClient;
import com.oss.services.nfv.networkservice.OnboardClient;
import com.oss.services.nfv.vnf.VNFSpecificationClient;
import com.oss.services.resourcecatalog.tmf.TMFCatalogClient;
import com.oss.untils.Environment;
import com.oss.utils.TestListener;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.oss.nfv.networkService.CreateNetworkServiceTestConstants.NETWORK_SERVICE_IDENTIFIER;
import static com.oss.nfv.networkService.CreateNetworkServiceTestConstants.VNF_IDENTIFIER;

@Listeners({TestListener.class})
public abstract class BaseNetworkServiceTest extends BaseTestCase {

    private final String NETWORK_SERVICE_YAML_FILE_PATH_TO_TEST = "src/test/resources/nfv/networkService/specificationDescriptorNetworkService.yaml";
    private final String VNF_YAML_FILE_PATH_TO_TEST = "src/test/resources/nfv/networkService/specificationDescriptorVNF.yaml";

    protected Environment env = Environment.getInstance();

    @BeforeClass
    public void prepareData() throws IOException {
        deleteAnyNetworkServiceInstances();
        deleteVNFSpecifications();
        deleteNetworkServiceSpecifications();

        onboardNewVNFSpecificationsStructure();
        onboardNewNetworkServiceSpecificationsStructure();
    }

    @AfterClass(alwaysRun = true)
    public void cleanData() {
        deleteAnyNetworkServiceInstances();
        deleteVNFSpecifications();
        deleteNetworkServiceSpecifications();
    }

    private void onboardNewVNFSpecificationsStructure() throws IOException {
        String yamlContent = new String(Files.readAllBytes(Paths.get(VNF_YAML_FILE_PATH_TO_TEST)));
        com.oss.services.nfv.vnf.OnboardClient.getInstance(env).uploadResourceSpecificationYAMLAndGetVNFId(yamlContent);
    }

    private void onboardNewNetworkServiceSpecificationsStructure() throws IOException {
        String yamlContent = new String(Files.readAllBytes(Paths.get(NETWORK_SERVICE_YAML_FILE_PATH_TO_TEST)));
        OnboardClient.getInstance(env).uploadResourceSpecificationAndGetNetworkServiceId(yamlContent);
    }

    private void deleteAnyNetworkServiceInstances() {
        NetworkServiceApiClient logicalFunctionClient = NetworkServiceApiClient.getInstance(env);
        LogicalFunctionClient.getInstance(env).getLogicalFunctionBySpecification(NETWORK_SERVICE_IDENTIFIER)
                .getLogicalFunctionsIdentifications().stream().map(LogicalFunctionIdentificationDTO::getId)
                .forEach(logicalFunctionClient::deleteNetworkServiceById);
    }

    private void deleteNetworkServiceSpecifications() {
        TMFCatalogClient.getInstance(env)
                .deleteResourceSpecification(NETWORK_SERVICE_IDENTIFIER, true);
    }

    private void deleteVNFSpecifications() {
        TMFCatalogClient.getInstance(env).getResourceSpecification(VNF_IDENTIFIER)
                .flatMap(ResourceSpecificationDTO::getXId)
                .ifPresent(VNFSpecificationClient.getInstance(env)::deleteVnfSpecificationById);
    }

}
