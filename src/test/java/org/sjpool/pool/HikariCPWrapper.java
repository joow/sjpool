package org.sjpool.pool;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Future;

class HikariCPWrapper implements PoolWrapper {
    private final HikariPool hikariPool;

    HikariCPWrapper(PoolProperties poolProperties) throws SQLException {
        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(poolProperties.getDabaseUrl());
        config.setMaximumPoolSize(poolProperties.getNbMaxConnections());
        hikariPool = new HikariPool(config);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return hikariPool.getConnection();
    }

    @Override
    public Future<Connection> getFutureConnection() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNbConnectionsAllocated() {
        return hikariPool.getMetricsTracker().getTotalConnections();
    }
}
