package com.thoughtmechanix.zuulsvr.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
* Rappresenta il Zuul Pre-Filter che ha il compito di verificare e nel caso generare il Correlation ID che
* serve per implementare una forma di tracciamento della richiesta utente tra le varie chiamate ai microservizi
*/
@Component
public class TrackingFilter extends ZuulFilter{

    private static final int      FILTER_ORDER =  1;
    private static final boolean  SHOULD_FILTER=true;
    private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);

    /*
        Custom Utility Class che racchiude le funzioni più utilizzate da tutte le diverse tipologie di filtri.
        Tra i metodi utilizzato dal TrackingFilter ci sono quelli per verificare la presenza del Correlation ID
        come attributo HEADER della richiesta HTTP client e nel caso inserirla.
    */
    @Autowired
    FilterUtils filterUtils;

    // Dfinisce la tipologia di filtro a cui questa classe appartiene (Pre-filter, Route-filter, Post-filter)
    @Override
    public String filterType() {
        return FilterUtils.PRE_FILTER_TYPE;
    }

    // Definisce l'ordine che dovrà avere tale filtro rispetto agli altri della sua stessa tipologia
    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    // Attiva o Disattiva il Filtro
    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    /*
        Codice che contiene la logica di business che viene eseguita ogni volta, prima che la chiamata arrivi al microservizio target
        Si controlla che il Correlation ID sia presente nell'HEADER e nel caso lo si setta tra gli HEADERS
    */
    @Override
    public Object run() {

        if (isCorrelationIdPresent()) {
           logger.debug("tmx-correlation-id found in tracking filter: {}. ", filterUtils.getCorrelationId());
        }
        else{
            filterUtils.setCorrelationId(generateCorrelationId());
            logger.debug("tmx-correlation-id generated in tracking filter: {}.", filterUtils.getCorrelationId());
        }

        RequestContext ctx = RequestContext.getCurrentContext();
        logger.debug("Processing incoming request for {}.",  ctx.getRequest().getRequestURI());
        return null;
    }

    private boolean isCorrelationIdPresent(){

        if (filterUtils.getCorrelationId() !=null) {
            return true;
        }

        return false;
    }

    // Si genera un nuovo Correlation ID random che verrà settato come attributo HEADER della richiesta HTTP client
    private String generateCorrelationId(){
        return java.util.UUID.randomUUID().toString();
    }
}