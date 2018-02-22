package com.thoughtmechanix.licenses.model;

public class Organization {
    String organizationId;
    String organizationName;
    String contactName;
    String contactEmail;
    String contactPhone;


    public String getOrganizationId() {
        return organizationId;
    }


    public void setOrganizationId(String id) {
        this.organizationId = id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String name) {
        this.organizationName = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }


}