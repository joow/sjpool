package org.sjpool;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Naive implementation of a pool of JDBC connections.
 */
public class SimpleJdbcPool {
    private int nbMinConnections;

    private int nbMaxConnections;

    private int acquireIncrement;

    private String url;

    private String user;

    private String password;

    private TransferQueue<Connection> connections;

    private final AtomicInteger nbConnectionsAllocated = new AtomicInteger();

    private static final ExecutorService getAsyncConnectionService = Executors.newFixedThreadPool(100);

    private SimpleJdbcPool initializePool() throws SQLException {
        connections = new LinkedTransferQueue<>();
        createConnections(nbMinConnections);

        return this;
    }

    private void createConnections(int nbConnectionsToCreate) throws SQLException {
        nbConnectionsAllocated.addAndGet(nbConnectionsToCreate);

        for (int i = 0; i < nbConnectionsToCreate; i++) {
            final Connection connection = DriverManager.getConnection(url, user, password);
            final ConnectionProxy connectionProxy = new ConnectionProxy(connection, this);
            final Object proxy = Proxy.newProxyInstance(
                    ClassLoader.getSystemClassLoader(), new Class[]{Connection.class}, connectionProxy);
            connections.offer((Connection) proxy);

        }
    }

    public Connection getConnection() throws SQLException {
        if (connections.size() == 0) {
            expandPool();
        }

        try {
            return connections.take();
        } catch (InterruptedException e) {
            throw new SQLException(e);
        }
    }

    private void expandPool() throws SQLException {
        final int nbConnectionsToCreate = Math.min(acquireIncrement, nbMaxConnections - nbConnectionsAllocated.get());

        if (nbConnectionsToCreate > 0) {
            createConnections(nbConnectionsToCreate);
        }
    }

    public Future<Connection> getAsyncConnection() {
        return getAsyncConnectionService.submit(new Callable<Connection>() {
            @Override
            public Connection call() throws SQLException {
                return getConnection();
            }
        });
    }

    void close(final Object connectionProxy) {
        /*
         * Try to transfer the closed connection to another thread waiting for an available connection.
         */
        if (!connections.hasWaitingConsumer() || !connections.tryTransfer((Connection) connectionProxy)) {
            connections.offer((Connection) connectionProxy);
        }
    }

    public int getNbConnectionsAllocated() {
        return nbConnectionsAllocated.get();
    }

    /**
     * Builder class for {@link org.sjpool.SimpleJdbcPool} objects.
     */
    public static class Builder {
        private int nbMinConnections = 1;

        private int nbMaxConnections = 100;

        private int acquireIncrement = 5;

        private String url;

        public Builder nbConnectionsMin(int nbMinConnections) {
            this.nbMinConnections = nbMinConnections;
            return this;
        }

        public Builder nbConnectionsMax(int nbMaxConnections) {
            this.nbMaxConnections = nbMaxConnections;
            return this;
        }

        public Builder acquireIncrement(int acquireIncrement) {
            this.acquireIncrement = acquireIncrement;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public SimpleJdbcPool build() throws SQLException {
            final SimpleJdbcPool pool = new SimpleJdbcPool();
            pool.nbMinConnections = nbMinConnections;
            pool.nbMaxConnections = nbMaxConnections;
            pool.acquireIncrement = acquireIncrement;
            pool.url = url;

            return pool.initializePool();
        }
    }
}
