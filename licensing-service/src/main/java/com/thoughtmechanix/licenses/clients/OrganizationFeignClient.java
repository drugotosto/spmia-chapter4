package com.thoughtmechanix.licenses.clients;


import com.thoughtmechanix.licenses.model.Organization;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// Identifica a quale application ID (microservizio) ci si riferisce e verso il quale si andranno a fare le chiamate REST (sarà presente quindi una interfaccia per ogni microservizio che occorre richiamare)
@FeignClient("organizationservice")
public interface OrganizationFeignClient {

    /*
        La sola interfaccia con tanto di metodo ripreso dalla classe OrganizationServiceController basta per inovcare il relativo end/point
        Sarà compito di Spring Cloud framework generare dinamicamente una classe proxy che si occuperà di effettuare la vera e propria chiamata.
        In pratica le operazioni che venivano eseguire in "OrganizationRestTemplateClient" vengono invece fatte dalla classe proxy
    */
    /*
        Con l'utilizzo del "Feign Client" ogni status code HTTP 4xx-5xx ritornato dal servizio chiamato sarà mappato in
        una classe "FeignException" che conterrà un JSON che potrà essere parsificato per capirne l'errore specifico
     */
    @RequestMapping(method= RequestMethod.GET, value="/v1/organizations/{organizationId}", consumes="application/json")
    Organization getOrganization(@PathVariable("organizationId") String organizationId);
}
