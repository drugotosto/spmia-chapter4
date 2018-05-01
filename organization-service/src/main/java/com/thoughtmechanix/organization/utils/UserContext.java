package com.thoughtmechanix.organization.utils;

import org.springframework.stereotype.Component;

/*
    Classe che ha lo scopo di contenere le cosidette informazioni di contesto recuperate da ogni richiesta HTTP in arrivo
    Tra le varie informazioni recuperate troviamo il "Correlation ID" che servirà per tracciare la transazione utente
    tra le diverse chiamate fatte ai vari microservizi.

* Nel capitolo successivo (Capitolo 6) sarà compito dei vari Zuul Filters settare (alla PRIMA chiamata verso
* un microservizio da parte di una richiesta HTTP client) e controllare (per le chiamate successive
* ai vari microservizi da parte della stessa richiesta HTTP client) che i valori dei vari attributi HEADER
* (che formano le informazioni di contesto) siano presenti e possano essere recuperati dai vari microservizi.
*
* Il Zuul TrackingFiler ad esempio si preoccupa di settare il Correlation ID (tramite 'generateCorrelationId()')
* quando la richiesta HTTP client effettua la prima chiamata ad un microservizio.

*/

@Component
public class UserContext {

    public static final String CORRELATION_ID = "tmx-correlation-id";
    public static final String AUTH_TOKEN     = "tmx-auth-token";
    public static final String USER_ID        = "tmx-user-id";
    public static final String ORG_ID         = "tmx-org-id";

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