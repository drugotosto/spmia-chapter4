package com.thoughtmechanix.licenses.clients;

import com.thoughtmechanix.licenses.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrganizationRestTemplateClient {

    /*
        Il RestTemplate bean Ribbon-aware definito in Application.java (con tanto di annotazione @LoadBalanced) permette di
        avere un client che fa uso di una classe RestTemplate migliorata in grado di:
        - Utilizzare l'Eureka service ID in fase di costruzione dell'url da utilizzare poi nella richiesta GET (nella chiamata di RestTemplate.excahnge())
        - Effettivo utilizzo del client-side load balancing offerto da Ribbon
     */
    @Autowired
    RestTemplate restTemplate;

    public Organization getOrganization(String organizationId){
        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        "http://organizationservice/v1/organizations/{organizationId}",
                        HttpMethod.GET,null, Organization.class, organizationId);

        return restExchange.getBody();
    }
}
