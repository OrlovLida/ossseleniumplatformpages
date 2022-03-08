package com.oss.nfv.vnf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import com.comarch.oss.logical.function.api.dto.LogicalFunctionIdentificationDTO;
import com.comarch.oss.resourcecatalog.tmf.api.dto.ResourceSpecificationDTO;
import com.oss.BaseTestCase;
import com.oss.services.LogicalFunctionClient;
import com.oss.services.nfv.vnf.OnboardClient;
import com.oss.services.nfv.vnf.VNFApiClient;
import com.oss.services.nfv.vnf.VNFSpecificationClient;
import com.oss.services.resourcecatalog.tmf.TMFCatalogClient;
import com.oss.untils.Environment;
import com.oss.utils.TestListener;

import static com.oss.nfv.vnf.CreateVNFTestConstants.VNF_INSTANTIATION_LEVEL_0_IDENTIFIER;
import static com.oss.nfv.vnf.CreateVNFTestConstants.VNF_ROOT_IDENTIFIER;

@Listeners({TestListener.class})
public abstract class BaseVNFTest extends BaseTestCase {

    private final String XML_FILE_PATH_TO_TEST = "src/test/resources/nfv/vnf/specificationDescriptor.xml";

    protected Environment env = Environment.getInstance();

    @BeforeClass
    public void prepareData() throws IOException {
        deleteAnyVNFInstances();
        deleteVNFSpecifications();
        onboardNewVNFSpecificationsStructure();
    }

    @AfterClass(alwaysRun = true)
    public void cleanData() {
        deleteAnyVNFInstances();
        deleteVNFSpecifications();
    }

    private void onboardNewVNFSpecificationsStructure() throws IOException {
        String xmlContent = new String(Files.readAllBytes(Paths.get(XML_FILE_PATH_TO_TEST)));
        OnboardClient.getInstance(env).uploadResourceSpecificationAndGetVNFId(xmlContent);
    }

    private void deleteAnyVNFInstances() {
        VNFApiClient vnfApiClient = VNFApiClient.getInstance(env);
        LogicalFunctionClient.getInstance(env).getLogicalFunctionBySpecification(VNF_INSTANTIATION_LEVEL_0_IDENTIFIER)
                .getLogicalFunctionsIdentifications().stream().map(LogicalFunctionIdentificationDTO::getId)
                .forEach(id -> vnfApiClient.deleteVnfById(id.toString()));
    }

    private void deleteVNFSpecifications() {
        Optional<ResourceSpecificationDTO> vnfRootSpec = TMFCatalogClient.getInstance(env).getResourceSpecification(VNF_ROOT_IDENTIFIER);
        vnfRootSpec.map(ResourceSpecificationDTO::getXId).ifPresent(VNFSpecificationClient.getInstance(env)::deleteVnfSpecificationById);
    }

}
