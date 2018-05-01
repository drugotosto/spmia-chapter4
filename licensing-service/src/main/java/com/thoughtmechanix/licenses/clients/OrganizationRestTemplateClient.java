package com.thoughtmechanix.licenses.clients;

import com.thoughtmechanix.licenses.model.Organization;
import com.thoughtmechanix.licenses.utils.UserContextHolder;
import org.apache.log4j.Logger;
//import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrganizationRestTemplateClient {

    private static final Logger logger = Logger.getLogger(OrganizationRestTemplateClient.class);

    /*
        Il RestTemplate bean Ribbon-aware definito in Application.java (con tanto di annotazione @LoadBalanced) permette di
        avere un client che fa uso di una classe RestTemplate migliorata in grado di:
        - Utilizzare l'Eureka service ID in fase di costruzione dell'url da utilizzare poi nella richiesta GET (nella chiamata di RestTemplate.excahnge())
        - Effettivo utilizzo del client-side load balancing offerto da Ribbon
     */
    /*
        Da notare che adesso l'URL utilizzato fa uso del Zuul Proxy Service
     */
    @Autowired
    RestTemplate restTemplate;

    public Organization getOrganization(String organizationId){
        logger.info(String.format(">>> In Licensing Service.getOrganization: {}. Thread Id: {}", UserContextHolder.getContext().getCorrelationId(), Thread.currentThread().getId()));

        ResponseEntity<Organization> restExchange = restTemplate.exchange(
                        "http://zuulservice/api/organization/v1/organizations/{organizationId}",
                        HttpMethod.GET,null, Organization.class, organizationId);

        return restExchange.getBody();
    }
}
