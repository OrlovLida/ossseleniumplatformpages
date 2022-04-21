package com.oss.template.infrastructure.template.folder;

import java.util.Arrays;
import java.util.Optional;

import com.comarch.oss.cm.templatefiller.api.dto.TemplateFolderDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.transport.infrastructure.EnvironmentRequestClient;

/**
 * Created by Bartłomiej Jędrzejczyk on 2022-04-15
 */
public class TemplateFolderClient {

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

    private Optional<String> getTemplateFolderId(String folderName) {
        return Arrays.stream(
                requestClient.getTemplateFillerCoreSpecification()
                    .given()
                    .queryParam("filter.folderName", folderName)
                    .get("template-folder-repository/filtered-folders")
                    .then().log().status().log()
                    .body().contentType(ContentType.JSON).extract().as(TemplateFolderDTO[].class))
            .findFirst()
            .map(TemplateFolderDTO::getId);
    }

    private void deleteTemplateFolder(String folderId) {
        String path = String.format("template-folder-repository/delete-folder/%s", folderId);
        requestClient.getTemplateFillerCoreSpecification()
            .delete(path)
            .then().log()
            .status().log();
    }
}
