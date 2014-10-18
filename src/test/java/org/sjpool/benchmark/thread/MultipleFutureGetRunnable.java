package org.sjpool.benchmark.thread;

import org.sjpool.pool.Pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MultipleFutureGetRunnable implements Runnable {
    private final Pool pool;

    private final int nbConnectionsRequested;

    private final long delay;

    public MultipleFutureGetRunnable(Pool pool, int nbConnectionsRequested, long delay) {
        this.pool = pool;
        this.nbConnectionsRequested = nbConnectionsRequested;
        this.delay = delay;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < nbConnectionsRequested; i++) {
                final Future<Connection> futureConnection1 = pool.getFutureConnection();
                final Future<Connection> futureConnection2 = pool.getFutureConnection();
                final Future<Connection> futureConnection3 = pool.getFutureConnection();
                final Connection connection1 = futureConnection1.get();
                final Connection connection2 = futureConnection2.get();
                final Connection connection3 = futureConnection3.get();
                Thread.sleep(delay);
                connection1.close();
                connection2.close();
                connection3.close();
            }
        } catch (SQLException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
