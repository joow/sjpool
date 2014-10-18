package org.sjpool.benchmark.thread;

import org.sjpool.pool.Pool;

import java.sql.Connection;
import java.sql.SQLException;

public class GetReleaseRunnable implements Runnable {
    private final Pool pool;

    private final int nbConnectionsRequested;

    public GetReleaseRunnable(Pool pool, int nbConnectionsRequested) {
        this.pool = pool;
        this.nbConnectionsRequested = nbConnectionsRequested;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < nbConnectionsRequested; i++) {
                final Connection connection = pool.getConnection();
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
