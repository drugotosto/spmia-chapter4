package com.thoughtmechanix.licenses.clients;


import com.thoughtmechanix.licenses.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class OrganizationDiscoveryClient {

    /* Tale oggetto sarà iniettato automaticamente all'interno della classe (grazie all'utilizzo dell'anotazione @EnableDiscoveryClient presente nella classe Application.java)
       e permete di interagire con le Ribbon libraries per ottenere una lista di tutte le istanze di un certo microservizio
    */
    @Autowired
    private DiscoveryClient discoveryClient;

    public Organization getOrganization(String organizationId) {
        RestTemplate restTemplate = new RestTemplate();
        /*
            Viene recuperata una lista di istanze relative al microservizio passato come argomento tramite le Ribbon libraries.
            Ogni oggetto della classe "ServiceInstance" contiene informazioni relative ad ogni singola istanza del microservizio quali: hostname, port, URI ...
        */
        List<ServiceInstance> instances = discoveryClient.getInstances("organizationservice");

        /*
            Viene recuperato l'end-point da richiamare associato alla prima istanza del microservizio organizationService
         */
        if (instances.size()==0) return null;
        String serviceUri = String.format("%s/v1/organizations/%s",instances.get(0).getUri().toString(), organizationId);
        System.out.println("!!!! SERVICE URI:  " + serviceUri);

        /*
            Tramite la classe RestTemplate viene Fatta una semplice chiamata REST all'end-point della prima istanza del microservizio recuperato precendentemente
         */
        ResponseEntity< Organization > restExchange =
                restTemplate.exchange(serviceUri, HttpMethod.GET, null, Organization.class, organizationId);
        
        return restExchange.getBody();
    }
}
