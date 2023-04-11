package com.oss.template.infrastructure.template.folder;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import com.comarch.oss.cm.templatefiller.api.dto.ParameterValidationDTO;
import com.comarch.oss.cm.templatefiller.api.dto.TemplateDTO;
import com.comarch.oss.cm.templatefiller.api.dto.TemplateFolderDTO;
import com.comarch.oss.cm.templatefiller.api.dto.TemplateParameterContentDTO;
import com.comarch.oss.cm.templatefiller.api.dto.TemplateParameterDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.serviceClient.EnvironmentRequestClient;

/**
 * Created by Bartłomiej Jędrzejczyk on 2022-04-15
 */
public class TemplateFolderClient {

    private static final String TEMPLATE_FOLDER_REPOSITORY = "template-folder-repository/";
    private static final String TEMPLATE_REPOSITORY = "template-repository";
    private static TemplateFolderClient instance;
    private final EnvironmentRequestClient requestClient;

    private TemplateFolderClient(EnvironmentRequestClient environmentRequestClient) {
        this.requestClient = environmentRequestClient;
    }

    public static TemplateFolderClient getInstance(EnvironmentRequestClient requestClient) {
        if (instance != null) {
            return instance;
        }
        instance = new TemplateFolderClient(requestClient);
        return instance;
    }

    public void deleteFolderPermanently(String folderName) {
        getTemplateFolderId(folderName)
                .ifPresent(this::deleteTemplateFolder);
    }

    public boolean isFolderPresent(String folderName) {
        return !getTemplateFolderList(folderName).isEmpty();
    }

    public boolean isTemplatePresent(String businessKey) {
        businessKey = businessKey.replace("/", "%2F");
        return requestClient.getTemplateFillerCoreSpecification()
                .given()
                .get(TEMPLATE_REPOSITORY + "/" + businessKey)
                .then()
                .log()
                .status()
                .extract().response().getStatusCode() == Response.Status.OK.getStatusCode();
    }

    public void createFolder(String folderName) {
        TemplateFolderDTO templateFolderDTO = TemplateFolderDTO.builder()
                .name(folderName)
                .id(UUID.randomUUID().toString())
                .isRemoved(false)
                .build();

        requestClient.getTemplateFillerCoreSpecification()
                .given()
                .body(templateFolderDTO)
                .when()
                .post(TEMPLATE_FOLDER_REPOSITORY + "create")
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat();
    }

    public void createTemplate(String businessKey, String content) {
        String[] parts = businessKey.split("[/;]");
        String folderName = parts[0];
        String templateName = parts[1];
        String vendorName = parts[2];
        String type = parts[3];

        TemplateDTO templateDTO = TemplateDTO.builder()
                .name(templateName)
                .content(content)
                .vendor(vendorName)
                .folderName(folderName)
                .type(TemplateDTO.TypeEnum.valueOf(type))
                .interpreter("python")
                .configurationDeviceSource(TemplateDTO.ConfigurationDeviceSourceEnum.INVENTORY_NEW)
                .configurationDeviceType("Router")
                .configurationExecutionTarget(TemplateDTO.ConfigurationExecutionTargetEnum.DEPLOY_TO_SERVER)
                .deviceCommunicationType(TemplateDTO.DeviceCommunicationType.DEFAULT)
                .isRemoved(false)
                .isRollback(false)
                .verified(true)
                .addParameters(templateParametersBuilder("Password", TemplateParameterDTO.ValidationTypeEnum.PASSWORD, TemplateParameterDTO.TypeEnum.SYSTEM, ParameterValidationDTO.TypeEnum.PASSWORD, null, null))
                .addParameters(templateParametersBuilder("DeployDirectory", TemplateParameterDTO.ValidationTypeEnum.NO_VALIDATION, TemplateParameterDTO.TypeEnum.SYSTEM, null, null, "../../data/TemplateCM/deployTest"))
                .addParameters(templateParametersBuilder("Port", TemplateParameterDTO.ValidationTypeEnum.NO_VALIDATION, TemplateParameterDTO.TypeEnum.SYSTEM, null, null, "22"))
                .addParameters(templateParametersBuilder("InterfaceName", TemplateParameterDTO.ValidationTypeEnum.NO_VALIDATION, TemplateParameterDTO.TypeEnum.USER, null, null, null))
                .addParameters(templateParametersBuilder("Protocol", TemplateParameterDTO.ValidationTypeEnum.ALLOWABLE_VALUES, TemplateParameterDTO.TypeEnum.SYSTEM, ParameterValidationDTO.TypeEnum.ALLOWABLE_VALUES, "sftp://", "sftp://"))
                .addParameters(templateParametersBuilder("DeployFileName", TemplateParameterDTO.ValidationTypeEnum.NO_VALIDATION, TemplateParameterDTO.TypeEnum.SYSTEM, null, null, "ExampleScript.cli"))
                .addParameters(templateParametersBuilder("Username", TemplateParameterDTO.ValidationTypeEnum.NO_VALIDATION, TemplateParameterDTO.TypeEnum.SYSTEM, null, null, "oss"))
                .addParameters(templateParametersBuilder("Host", TemplateParameterDTO.ValidationTypeEnum.NO_VALIDATION, TemplateParameterDTO.TypeEnum.SYSTEM, null, null, "10.132.118.207"))
                .addParameters(templateParametersBuilder("IPAddress", TemplateParameterDTO.ValidationTypeEnum.NO_VALIDATION, TemplateParameterDTO.TypeEnum.USER, null, null, "10.101.10.10"))
                .addParameters(templateParametersBuilder("Mask", TemplateParameterDTO.ValidationTypeEnum.NO_VALIDATION, TemplateParameterDTO.TypeEnum.USER, null, null, "255.255.255.240"))
                .folderId(getTemplateFolderId(folderName).orElseThrow(() -> new NoSuchElementException("Cannot get ID from template folder")))
                .build();

        requestClient.getTemplateFillerCoreSpecification()
                .given()
                .body(templateDTO)
                .when()
                .post(TEMPLATE_REPOSITORY)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat();
    }

    private TemplateParameterDTO templateParametersBuilder(String name, TemplateParameterDTO.ValidationTypeEnum validationTypeEnum, TemplateParameterDTO.TypeEnum typeEnum, ParameterValidationDTO.TypeEnum validationType, String validation, String content) {
        ParameterValidationDTO parameterValidationDTO = null;
        if (validationType != null) {
            if (validation != null) {
                parameterValidationDTO = ParameterValidationDTO.builder()
                        .type(validationType)
                        .addAllowableValues(validation)
                        .build();
            } else {
                parameterValidationDTO = ParameterValidationDTO.builder()
                        .type(validationType)
                        .build();
            }
        }

        TemplateParameterContentDTO templateParameterContentDTO = TemplateParameterContentDTO.builder()
                .content(Optional.ofNullable(content))
                .build();

        return TemplateParameterDTO.builder()
                .name(name)
                .validationType(validationTypeEnum)
                .type(typeEnum)
                .addContent(templateParameterContentDTO)
                .validation(Optional.ofNullable(parameterValidationDTO))
                .build();
    }

    private Optional<String> getTemplateFolderId(String folderName) {
        return getTemplateFolderList(folderName).stream().findFirst().map(TemplateFolderDTO::getId);
    }

    private List<TemplateFolderDTO> getTemplateFolderList(String folderName) {
        return Arrays.stream(
                requestClient.getTemplateFillerCoreSpecification()
                        .given()
                        .queryParam("filter.folderName", folderName)
                        .get(TEMPLATE_FOLDER_REPOSITORY + "filtered-folders")
                        .then().log().status().log()
                        .body()
                        .contentType(ContentType.JSON)
                        .extract()
                        .as(TemplateFolderDTO[].class)).collect(Collectors.toList());
    }

    private void deleteTemplateFolder(String folderId) {
        String path = String.format(TEMPLATE_FOLDER_REPOSITORY + "delete-folder/%s", folderId);
        requestClient.getTemplateFillerCoreSpecification()
                .delete(path)
                .then().log()
                .status().log();
    }
}
