package com.thoughtmechanix.licenses.controllers;

import com.thoughtmechanix.licenses.services.DiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="v1/tools")
public class ToolsController {

    @Autowired
    private DiscoveryService discoveryService;

    /*
        Metodo che per ogni istanza di microservizio registrata con Eureka restituisce il nome del microservizio stesso e relative info su indirizzo IP e porta del server associata
    */
    @RequestMapping(value="/eureka/services",method = RequestMethod.GET)
    public List<String> getEurekaServices() {

        return discoveryService.getEurekaServices();
    }
}
