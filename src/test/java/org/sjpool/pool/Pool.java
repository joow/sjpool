package org.sjpool.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Future;

public class Pool {
    public static enum PoolType {
        SIMPLE_JAVA_POOL, HIKARI_CP;
    }

    private final PoolWrapper wrapper;

    public Pool(PoolProperties poolProperties) throws SQLException {
        if (poolProperties.getPoolType() == PoolType.SIMPLE_JAVA_POOL) {
            wrapper = new SimpleJavaPoolWrapper(poolProperties);
        } else if (poolProperties.getPoolType() == PoolType.HIKARI_CP) {
            wrapper = new HikariCPWrapper(poolProperties);
        } else {
            throw new IllegalArgumentException(String.format("Unknown pool type: %s", poolProperties.getPoolType()));
        }
    }

    public Connection getConnection() throws SQLException {
        return wrapper.getConnection();
    }

    public Future<Connection> getFutureConnection() {
        return wrapper.getFutureConnection();
    }

    public int getNbConnectionsAllocated() {
        return wrapper.getNbConnectionsAllocated();
    }
}
