package com.thoughtmechanix.organization.events.source;

import com.thoughtmechanix.organization.events.models.OrganizationChangeModel;
import com.thoughtmechanix.organization.utils.UserContext;
import com.thoughtmechanix.organization.utils.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;


@Component
public class SimpleSourceBean {
    private Source source;

    private static final Logger logger = LoggerFactory.getLogger(SimpleSourceBean.class);

    @Autowired
    public SimpleSourceBean(Source source){
        this.source = source;
    }

    /*
        Racchiude tutta la logica per pubblicare i messaggi sul message broker che contengono:
        - L'ambito a cui appartiene l'evento generato
        - Il tipo di evento specifico generato
        - ID dell'organizzazione alla quale ci si riferisce
        - Il Correlation ID che fa riferimento all'operazione richiesta dal client
    */
    public void publishOrgChange(String action,String orgId){

        // Costruzione del POJO che corrisponde al messaggio da inviare
        OrganizationChangeModel change =  new OrganizationChangeModel(
                OrganizationChangeModel.class.getTypeName(),
                action,
                orgId,
                UserContextHolder.getContext().getCorrelationId()
        );

        /*
            Avviene la vera e propria pubblicazione del messaggio sul canale (MessageChannel class).
            La "helper class" MessageBuilder serve per convertire il POJO precedentemente creato in un oggetto "Message class"
        */
        source.output().send(MessageBuilder.withPayload(change).build());

        logger.debug("Invio del messaggio di '{}' a Kafka relativo all'Organization Id '{}'", action, orgId);

    }
}
