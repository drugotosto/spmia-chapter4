package com.thoughtmechanix.licenses.hystrix;

import com.thoughtmechanix.licenses.utils.UserContext;
import com.thoughtmechanix.licenses.utils.UserContextHolder;

import java.util.concurrent.Callable;


/*
* Tale classe sarà quella che andrà effettivamente a propagare il contesto del thread padre (UserContext) al thread pool di Hystrix
* */
public final class DelegatingUserContextCallable<V> implements Callable<V> {

    // Rappresenta uno pseudonimo relativo al metodo protetto dalla @HystrixCommand
    private final Callable<V> delegate;

    private UserContext originalUserContext;

    public DelegatingUserContextCallable(Callable<V> delegate, UserContext userContext) {
        this.delegate = delegate;
        this.originalUserContext = userContext;
    }

    public V call() throws Exception {

        // Viene settato il contesto thread del padre che contiene come valori il Correlation ID
        UserContextHolder.setContext( originalUserContext );

        try {
            // Invocazione del metodo protetto dal @HystrixCommand
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