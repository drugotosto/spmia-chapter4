package com.thoughtmechanix.licenses.utils;


import org.springframework.util.Assert;

/*
    Classe che fa da contenitore dello "UserContext" utilizzando la classe "ThreadLocal".
    Ogni differente richiesta HTTP sarà gestita da un thread differente il quale grazie all'utilizzo
    della classe "UserContextHolder" avrà accesso ad uno "UserCotext" differente ognuno contenente i
    valori che specifici della richiesta HTTP che il thread gestisce.
*/
public class UserContextHolder {

    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<UserContext>();

    public static final UserContext getContext(){
        UserContext context = userContext.get();

        if (context == null) {
            context = createEmptyContext();
            userContext.set(context);

        }
        return userContext.get();
    }

    public static final UserContext createEmptyContext(){
        return new UserContext();
    }

    public static final void setContext(UserContext context) {
        Assert.notNull(context, "Only non-null UserContext instances are permitted");
        userContext.set(context);
    }
}
