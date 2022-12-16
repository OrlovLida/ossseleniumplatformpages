package com.oss.services;

import java.util.NoSuchElementException;

import javax.ws.rs.core.Response;

import org.assertj.core.util.Lists;

import com.comarch.oss.transport.ip.ethernet.ethernetlink.api.dto.BulkEthernetLinkResultDTO;
import com.comarch.oss.transport.ip.ethernet.ethernetlink.api.dto.EthernetLinkBulkDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

public class EthernetCoreClient {

    private static final String ETHERNET_LINK_PATH = "/ethernet-link/createOrUpdate/bulk";
    private static final String DELETE_ETHERNET_LINK_PATH = "/ethernet-link/byIds";
    private static EthernetCoreClient instance;
    private final Environment env;

    public EthernetCoreClient(Environment environment) {
        env = environment;
    }

    public static EthernetCoreClient getInstance(Environment pEnvironment) {
        if (instance != null) {
            return instance;
        }
        instance = new EthernetCoreClient(pEnvironment);
        return instance;
    }

    public Long createEthernetLink(EthernetLinkBulkDTO ethernetLink) {
        return env.getEthernetCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .contentType(ContentType.JSON)
                .body(Lists.newArrayList(ethernetLink))
                .when()
                .post(ETHERNET_LINK_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(BulkEthernetLinkResultDTO.class)
                .getCreatedTrails()
                .stream()
                .findFirst().orElseThrow(() -> new NoSuchElementException("No Ethernet Link created."))
                .getId().orElseThrow(() -> new NoSuchElementException("Cannot get ID from Ethernet Link."));
    }

    public void deleteEthernetLink(Long ethernetLinkId) {
        env.getEthernetCoreRequestSpecification()
                .given()
                .queryParam(Constants.ID, ethernetLinkId)
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .when()
                .delete(DELETE_ETHERNET_LINK_PATH)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()).assertThat();
    }
}
