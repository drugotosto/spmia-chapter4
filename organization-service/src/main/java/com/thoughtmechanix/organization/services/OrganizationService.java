package com.thoughtmechanix.organization.services;

import com.thoughtmechanix.organization.events.source.SimpleSourceBean;
import com.thoughtmechanix.organization.model.Organization;
import com.thoughtmechanix.organization.repository.OrganizationRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository orgRepository;

    @Autowired
    SimpleSourceBean simpleSourceBean;

    public List<Organization> getAllOrgs() {
        return orgRepository.findAll();
    }

    public Organization getOrg(String organizationId) {
        return orgRepository.findByOrganizationId(organizationId);
    }

    public JSONObject saveOrg(Organization org){

        // Inserisco la nuova organizzazione sul DB
        String organizationId = UUID.randomUUID().toString();
        org.setOrganizationId(organizationId);
        orgRepository.save(org);
        // Ritorno un JSON object che contiene il solo campo relativo all'ID dell'organizzazione appena creata. Verrà utilizzata lato client..
        JSONObject obj=new JSONObject();
        obj.put("organizationId",organizationId);

        // Pubblicazione del messaggio sul message broker relativo all'evento "Save" della nuova Organizzazione
        simpleSourceBean.publishOrgChange("SAVE", org.getOrganizationId());

        return obj;
    }

    public void updateOrg(Organization org){

        orgRepository.save(org);
        // Pubblicazione del messaggio sul message broker relativo all'evento "UPDATE" dell'Organizzazione
        simpleSourceBean.publishOrgChange("UPDATE", org.getOrganizationId());
    }

    public void deleteOrg(Organization org){

        orgRepository.delete( org.getOrganizationId());
        // Pubblicazione del messaggio sul message broker relativo all'evento "DELETE" dell'Organizzazione
        simpleSourceBean.publishOrgChange("DELETE", org.getOrganizationId());
    }

}
