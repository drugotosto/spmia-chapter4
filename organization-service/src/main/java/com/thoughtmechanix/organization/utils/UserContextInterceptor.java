package com.thoughtmechanix.organization.utils;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

//import org.slf4j.Logger;

/*
    Si preoccupa di fare in modo che le informazioni di contesto siano presenti all'interno degli HEADERS
    della richiesta HTTP successiva che sarà inviata al micorservizio seguente all'interno della stessa transazione
 */
public class UserContextInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = Logger.getLogger(UserContextInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        HttpHeaders headers = request.getHeaders();
        headers.add(UserContext.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
        headers.add(UserContext.AUTH_TOKEN, UserContextHolder.getContext().getAuthToken());

        return execution.execute(request, body);
    }
}