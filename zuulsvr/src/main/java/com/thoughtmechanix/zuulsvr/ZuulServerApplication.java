package com.thoughtmechanix.zuulsvr;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
// Questa annotazione abilita il tale servizio ad essere un Zuul service gateway
/*
    La configurazione automatica (senza configurazioni custom) utilizzata da Zuul prevede di comunicare con Eureka
    per effettuare la ricerca dei microservizi utilizzando il loro service IDs  e di utilizzare Ribbon per
    implementare il client-side load balancing.
 */
@EnableZuulProxy
public class ZuulServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulServerApplication.class, args);
    }
}

