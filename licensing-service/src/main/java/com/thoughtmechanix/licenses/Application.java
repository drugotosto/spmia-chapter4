package com.thoughtmechanix.licenses;

import com.thoughtmechanix.licenses.utils.UserContextInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableEurekaClient
// Tale annotazione permette di usare il client "DiscoveryClient"
@EnableDiscoveryClient
// Tale annotazione serve per abilitare l'utilizzo dei "FeignClient" e relativa annotazione (@FeignClient) da utilizzare sopra alle interfacce
@EnableFeignClients
// Tale annotazione dichiara che il servizio utilizzerà "Hystrix"
@EnableCircuitBreaker
public class Application {

    /*
        Tale annotazione (in abbinamento con il costruttore richiamato all'interno del metodo) serve per
        essere in grado di utilizzare la versione della classe "RestTemplate" Ribbon-aware client
    */
    /*
        E' necessario anche inettare nel "RestTemplate" una istanza di "UserContextInterceptor"
        per fare in modo che le diverse informazioni di contesto presenti sotto forma di ThreadLocal
        (recupeati inizialmente dagli HEADER HTTP della richiesta client) vengano passati anche al
        microservizio successivo chiamato tramite "RestTemplate".
     */
    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate(){

        RestTemplate template = new RestTemplate();

        List interceptors = template.getInterceptors();
        if (interceptors==null){
            template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
        }
        else{
            interceptors.add(new UserContextInterceptor());
            template.setInterceptors(interceptors);
        }

        return template;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
  }
}
