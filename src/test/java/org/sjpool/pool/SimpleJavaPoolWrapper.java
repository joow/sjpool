package org.sjpool.pool;

import org.sjpool.SimpleJdbcPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Future;

class SimpleJavaPoolWrapper implements PoolWrapper {
    private final SimpleJdbcPool simpleJavaPool;

    SimpleJavaPoolWrapper(PoolProperties poolProperties) throws SQLException {
        simpleJavaPool = new SimpleJdbcPool.Builder().nbConnectionsMin(poolProperties.getNbMinConnections())
                .nbConnectionsMax(poolProperties.getNbMaxConnections()).acquireIncrement(poolProperties.getAcquireIncrements())
                .url(poolProperties.getDabaseUrl()).build();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return simpleJavaPool.getConnection();
    }

    @Override
    public Future<Connection> getFutureConnection() {
        return simpleJavaPool.getAsyncConnection();
    }

    @Override
    public int getNbConnectionsAllocated() {
        return simpleJavaPool.getNbConnectionsAllocated();
    }
}
