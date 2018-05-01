package com.thoughtmechanix.organization.utils;


import org.springframework.util.Assert;

/*
    Utility Class che fa da contenitore per lo "UserContext" andando a sfruttare il "ThreadLocal" per fare in modo che ogni
    thread associato ad una nuova richeista abbia uno "UserContext" differente e quindi un Correlation ID differente.
    Si genera in questa maniera le informazioni di contesto per ogni thread relativo ad una specifica richiesta HTTP in arrivo
*/
public final class UserContextHolder {

    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<UserContext>();

    private UserContextHolder(){
    }

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
