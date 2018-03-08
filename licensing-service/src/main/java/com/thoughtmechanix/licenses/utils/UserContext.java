package com.thoughtmechanix.licenses.utils;

import org.springframework.stereotype.Component;

/*
    Classe che contiene le informazioni di contesto relative ad ogni singola originale richiesta HTTP in arrivo
    da parte di un certo utente.
 */
@Component
public class UserContext {

    public static final String CORRELATION_ID = "correlation-id";
    public static final String AUTH_TOKEN     = "auth-token";
    public static final String USER_ID        = "user-id";
    public static final String ORG_ID         = "org-id";

    private String correlationId= new String();
    private String authToken= new String();
    private String userId = new String();
    private String orgId = new String();

    public String getCorrelationId() { return correlationId;}
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getAuthToken() {
        return authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

}