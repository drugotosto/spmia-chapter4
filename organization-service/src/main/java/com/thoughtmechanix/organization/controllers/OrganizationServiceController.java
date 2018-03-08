package com.thoughtmechanix.organization.controllers;


import com.thoughtmechanix.organization.model.Organization;
import com.thoughtmechanix.organization.services.OrganizationService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping(value="v1/organizations")
@CrossOrigin()
public class OrganizationServiceController {
    @Autowired
    private OrganizationService orgService;



    @RequestMapping(value = "/" , method = RequestMethod.GET)
    public List<Organization> getAllOrganizations(){
        return orgService.getAllOrgs();
    }

    // Endpoint che verrà richimato dal microservizio "licensingService" per recuperare le informazioni della compagnia richiesta
    @RequestMapping(value="/{organizationId}",method = RequestMethod.GET)
    public Organization getOrganization( @PathVariable("organizationId") String organizationId) {
        return orgService.getOrg(organizationId);
    }

    @RequestMapping(value="/{organizationId}",method = RequestMethod.PUT)
    public void updateOrganization( @PathVariable("organizationId") String orgId, @RequestBody Organization org) {
        orgService.updateOrg( org );
    }

    /*
        Ritorno un JSON che contiene l'ID dell'organizzaziine appena creata
     */
    @RequestMapping(value="/{organizationId}",method = RequestMethod.POST)
    public JSONObject saveOrganization(@RequestBody Organization org) {
       return orgService.saveOrg( org );
    }

    @RequestMapping(value="/{organizationId}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization( @PathVariable("orgId") String orgId,  @RequestBody Organization org) {
        orgService.deleteOrg( org );
    }
}
