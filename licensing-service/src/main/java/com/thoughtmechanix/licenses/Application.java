package com.thoughtmechanix.licenses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
// Tale annotazione permette di utilizzare il DiscoveryClient e le Ribbon libraries
@EnableDiscoveryClient
// Tale annotazione serve per abilitare l'utilizzo dei FeignClient e relativa annotazione da utilizzare sopra alle interfacce
@EnableFeignClients
public class Application {

    /*
        Tale annotazione (in abbinamento con il costruttore richiamato all'interno del metodo) serve per
        essere in grado di utilizzare la versione della classe RestTemplate Ribbon-aware
    */
    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate(){
        // Una volta richiamato il costruttore della classe "normale" RestTemplate tramite l'annotazione verrà trasformata in una versione Ribbon-aware
        return new RestTemplate();
  }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
  }
}
