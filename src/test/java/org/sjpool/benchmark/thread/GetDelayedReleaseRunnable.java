package org.sjpool.benchmark.thread;

import org.sjpool.pool.Pool;

import java.sql.Connection;
import java.sql.SQLException;

public class GetDelayedReleaseRunnable implements Runnable {
    private final Pool pool;

    private final int nbConnectionsRequested;

    private final long delay;

    public GetDelayedReleaseRunnable(Pool pool, int nbConnectionsRequested, long delay) {
        this.pool = pool;
        this.nbConnectionsRequested = nbConnectionsRequested;
        this.delay = delay;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < nbConnectionsRequested; i++) {
                final Connection connection = pool.getConnection();
                Thread.sleep(delay);
                connection.close();
            }
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
