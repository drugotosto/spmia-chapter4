package com.thoughtmechanix.licenses.hystrix;

import com.thoughtmechanix.licenses.utils.UserContext;
import com.thoughtmechanix.licenses.utils.UserContextHolder;

import java.util.concurrent.Callable;


/*
    Questa classe come tutte quelle presenti all'interno del pacchetto 'hystrix' vengono utilizzare per risolvere
    il problema di propagazione delle informazioni di contesto tra il thread padre relativo alla richiesta HTTP
    e il thread pool gestito da Hystrix
    Questa classe nello specifico andrà effettivamente a propagare il contesto del thread padre (UserContext)
    al thread pool di Hystrix
*/
public final class DelegatingUserContextCallable<V> implements Callable<V> {

    // Rappresenta uno pseudonimo relativo al metodo protetto dalla @HystrixCommand
    private final Callable<V> delegate;

    private UserContext originalUserContext;

    public DelegatingUserContextCallable(Callable<V> delegate, UserContext userContext) {
        this.delegate = delegate;
        this.originalUserContext = userContext;
    }

    // Viene eseguito prima che di eseguire il metodo originale protetto da @HystrixCommand
    public V call() throws Exception {

        // Viene eseguita la vera e propria propagazione del contesto dal thread padre al thread gestito da Hystrix
        UserContextHolder.setContext( originalUserContext );

        try {
            // Invocazione del metodo originale protetto dal @HystrixCommand
            return delegate.call();
        }
        finally {
            this.originalUserContext = null;
        }
    }

    public static <V> Callable<V> create(Callable<V> delegate, UserContext userContext) {
        return new DelegatingUserContextCallable<V>(delegate, userContext);
    }
}