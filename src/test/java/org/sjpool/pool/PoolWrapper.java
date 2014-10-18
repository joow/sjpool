package org.sjpool.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Future;

interface PoolWrapper {
    Connection getConnection() throws SQLException;

    Future<Connection> getFutureConnection();

    int getNbConnectionsAllocated();
}
