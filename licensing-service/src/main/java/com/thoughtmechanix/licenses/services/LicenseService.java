package com.thoughtmechanix.licenses.services;

//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
//import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.annotation.*;
import com.thoughtmechanix.licenses.clients.OrganizationDiscoveryClient;
import com.thoughtmechanix.licenses.clients.OrganizationFeignClient;
import com.thoughtmechanix.licenses.clients.OrganizationRestTemplateClient;
import com.thoughtmechanix.licenses.config.ServiceConfig;
import com.thoughtmechanix.licenses.model.License;
import com.thoughtmechanix.licenses.model.Organization;
import com.thoughtmechanix.licenses.repository.LicenseRepository;
import com.thoughtmechanix.licenses.utils.UserContextHolder;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class LicenseService {

    private static final Logger logger = Logger.getLogger(LicenseService.class);

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    ServiceConfig config;

    @Autowired
    OrganizationFeignClient organizationFeignClient;

    @Autowired
    OrganizationRestTemplateClient organizationRestClient;

    @Autowired
    OrganizationDiscoveryClient organizationDiscoveryClient;

    /*
         Implementazione del CIRCUT BREAKER (avanzata con controllo del trip circuit) e BULKHEAD patterns tramite Hystrix nel momento in cui viene fatta una chiamata ad una risorsa esterna (nella fattispecie una chiamata ad un servizio remoto)
         Attraverso tale annotazione ogni chiamata al metodo sarà wrappata con l'Hystrix circuit breaker.
         Se la chiamata impiega troppo tempo per essere completata allora questa verrà terminata lanciando l'eccezzione "HystrixRuntimeException"
         restituendo un messaggio di errore al client.
      */
    @HystrixCommand(
            // Tale attributo serve per informare Hystrix che si vuole creare un nuovo thread pool associato alla chiamta di tale risorsa dandogli tale nome (BULKHEAD pattern)
            threadPoolKey = "retrieveOrgInfoThreadPool",
            // Vengono definite delle attributi per customizzare il comportamento del thread pool creato (BULKHEAD)
            threadPoolProperties = {
                    // Settaggio del numero massimo di thread da assegnare al thread pool
                    @HystrixProperty(name = "coreSize",value="30"),
                    //Settaggio della grandezza massima della coda utilizzata per contenere le richieste in arrivo (il valore di -1 fa si che non vengano concessi più richieste in entrata rispetto al numero di threads disponibili)
                    @HystrixProperty(name="maxQueueSize", value="10")},
            // Le properties che seguono servono per eseguire il settaggio "fine-tune" di Hystrix "Circuit Breaker" nel momento in cui si decide di eseguire il tripping del circuit implementando il "fail-fast"
            commandProperties = {
                    // Numero di volte necessarie all'interno della finestra utilizzata da Hystrix prima questo consideri la possibilità di eseguire il trip del circuito
                    @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="10"),
                    // Percentuali media delle chiamate che devono fallire prima che venga azionato il trip del circuito
                    @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="75"),
                    // Dopo che si è verificato il trip del circuito determina il Tempo in cui Hystrix rimarrà "addormentato" prima di controllare nuovamente se il servizio è nuovamente disponibile
                    @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="7000"),
                    // Grandezza temporale della prima finestra utilizzata da Hystrix
                    @HystrixProperty(name="metrics.rollingStats.timeInMilliseconds", value="15000"),
                    // Numero di buckets utilizzati per memorizzare le statistiche all'interno di una finestra
                    @HystrixProperty(name="metrics.rollingStats.numBuckets", value="5")
            }
    )
    private Organization retrieveOrgInfo(String organizationId, String clientType){
        Organization organization = null;

        switch (clientType) {
            case "feign":
                System.out.println("I am using the feign client");
                organization = organizationFeignClient.getOrganization(organizationId);
                break;
            case "rest":
                System.out.println("I am using the rest client");
                organization = organizationRestClient.getOrganization(organizationId);
                break;
            case "discovery":
                System.out.println("I am using the discovery client");
                organization = organizationDiscoveryClient.getOrganization(organizationId);
                break;
            default:
                organization = organizationRestClient.getOrganization(organizationId);
        }

        return organization;
    }

    public License getLicense(String organizationId,String licenseId, String clientType) {

        logger.info(String.format("LicenseService.getLicensesByOrg  Correlation id: %s", UserContextHolder.getContext().getCorrelationId()));

        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

        // Metodo che effettua una chiamata remota che utilizza Hystrix
        Organization org = retrieveOrgInfo(organizationId, clientType);

        return license
                .withOrganizationName( org.getOrganizationName())
                .withContactName( org.getContactName())
                .withContactEmail( org.getContactEmail() )
                .withContactPhone( org.getContactPhone() )
                .withComment(config.getExampleProperty());
    }

    /*
        Implementazione del CIRCUT BREAKER con FALLBACK patterns tramite Hystrix nel momento in cui viene fatta una chiamata ad una risorsa esterna (nella fattispecie ad un DB remoto)
        Attraverso tale annotazione ogni chiamata al metodo sarà wrappata con l'Hystrix circuit breaker.
        Se la chiamata impiega troppo tempo per essere in grado di portarla a termine verrà eseguito il metodo alternativo "buildFallbackLicenseList"
     */
    @HystrixCommand(
            // Viene definito il fallback metodo da utilizzare per implemetare il FALLBACK PROCESSING nel momento in cui la chiamata al servizio fallisce o è andata in timeout
            fallbackMethod = "buildFallbackLicenseList",
            // Vengono definite delle properties personalizzate da utilizzare per customizzare Hystrix
            commandProperties = {
            // Viene stabilito il tempo di timeout da utilizzare prima che una chiamata effetuata da Hystrix venga interrotta
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000"),
    })
    public List<License> getLicensesByOrg(String organizationId){
        // Chiamata che una 1 su 3 (distribuzione uniforme) "addormenta" per 5 secondi la chiamata mimando un rallentamento
//        randomlyRunLong();

        logger.info(String.format("LicenseService.getLicensesByOrg  Correlation id: %s", UserContextHolder.getContext().getCorrelationId()));

        return licenseRepository.findByOrganizationId( organizationId );
    }

    /*
        Metodi privati che servono per "mimare" il rallentamento di un servizio in maniera tale da generare un timeout e azionare la FALLBACK PROCESSING
    */
    private void randomlyRunLong(){
        Random rand = new Random();

        int randomNum = rand.nextInt((3 - 1) + 1) + 1;

        if (randomNum==3) sleep();
    }

    private void sleep(){
        // Dal momento che Hystrix ha stabilito un tempo di timeout di 2000 la "sleep" verrà interrotta da Hystrix stesso che gestirà la chiamata azionando così la "catch" e relativa stampa dello stack
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("Histrix ha interrotto la sleep. Stampa di tutto lo stack chiamante: ");
            e.printStackTrace();
        }
    }

    /*
        Metodo che verrà utilizzato per implemetare il FALLBACK PROCESSING relativo alla chiamata di recupero
        delle licenze per una certa organizzazione. Tale metodo è importante che abbia la stessa signature del
        metodo originale per cui è stata fatta richiesta (stessi parametri input stesso output).
        In questo caso verrà restituita una lista fittizia cn valori hard-code. Nella realtà invece sarà plausibile
        andare a prelevare i valori da un'altra fonte dati.
      */
    private List<License> buildFallbackLicenseList(String organizationId){
        List<License> fallbackList = new ArrayList<>();
        License license = new License()
                .withId("0000000-00-00000")
                .withOrganizationId( organizationId )
                .withProductName("Sorry no licensing information currently available");

        fallbackList.add(license);
        return fallbackList;
    }


    public JSONObject saveLicense(License license){
        String licenseId = UUID.randomUUID().toString();
        license.withId(licenseId);
        licenseRepository.save(license);

        JSONObject obj=new JSONObject();
        obj.put("licenseId",licenseId);
        return obj;
    }

    public void updateLicense(License license){
      licenseRepository.save(license);
    }

    public void deleteLicense(License license){
        licenseRepository.delete( license.getLicenseId());
    }

}
