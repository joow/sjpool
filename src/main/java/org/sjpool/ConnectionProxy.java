package org.sjpool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;

class ConnectionProxy implements InvocationHandler {
    private static final String CLOSE_METHOD_NAME = "close";

    private final Connection connection;

    private final SimpleJdbcPool pool;

    ConnectionProxy(final Connection connection, final SimpleJdbcPool pool) {
        this.connection = connection;
        this.pool = pool;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (CLOSE_METHOD_NAME.equals(method.getName())) {
            close(proxy);
            return null;
        }

        try {
            return method.invoke(connection, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    private void close(final Object connectionProxy) {
        pool.close(connectionProxy);
    }
}
