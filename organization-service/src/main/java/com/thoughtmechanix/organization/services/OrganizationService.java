package com.thoughtmechanix.organization.services;

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


    public List<Organization> getAllOrgs() {
        return orgRepository.findAll();
    }

    public Organization getOrg(String organizationId) {
        return orgRepository.findByOrganizationId(organizationId);
    }

    public JSONObject saveOrg(Organization org){
        String organizationId = UUID.randomUUID().toString();
        org.setOrganizationId(organizationId);
        orgRepository.save(org);


        JSONObject obj=new JSONObject();
        obj.put("organizationId",organizationId);
        return obj;
    }

    public void updateOrg(Organization org){
        orgRepository.save(org);
    }

    public void deleteOrg(Organization org){
        orgRepository.delete( org.getOrganizationId());
    }

}
