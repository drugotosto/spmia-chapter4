package com.thoughtmechanix.organization.utils;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/*
* Questo rappresenta un Filtro (base) utilizzato per intercettare qualunque chiamata in  arrivo dal
* Zuul Gateway verso il qui presente REST Licensing Service con lo scopo di recuperare le infomazioni
* contenute nell'Header HTTP riguaradanti:
*
* - Correlation ID: Serve per tracciare la richiesta HTTP originale/transazione tra le varie chiamate
*   all'interno dei diversi microservizi per portarla a termine.
*
* - Authentication Token: ...
*
* Tali informazioni una volta prelevate dall'header HTTP saranno inserite in un "UserContext" memorizzato
* da parte dello "UserContextHolder" all'interno di un oggetto "ThreadLocal" in maniera tale da poter essere
* utilizzate tramite Hystrix
*
*/
@Component
public class UserContextFilter implements Filter {
    private static final Logger logger = Logger.getLogger(UserContextFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        UserContextHolder.getContext().setCorrelationId(  httpServletRequest.getHeader(UserContext.CORRELATION_ID) );
        UserContextHolder.getContext().setUserId(httpServletRequest.getHeader(UserContext.USER_ID));
        UserContextHolder.getContext().setAuthToken(httpServletRequest.getHeader(UserContext.AUTH_TOKEN));
        UserContextHolder.getContext().setOrgId(httpServletRequest.getHeader(UserContext.ORG_ID));

        logger.info(String.format("UserContextFilter Correlation id: %s", UserContextHolder.getContext().getCorrelationId()));

        filterChain.doFilter(httpServletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}