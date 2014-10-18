package org.sjpool.benchmark;

import org.sjpool.pool.Pool;
import org.sjpool.pool.PoolProperties;
import org.sjpool.benchmark.thread.GetDelayedReleaseRunnable;
import org.sjpool.benchmark.thread.GetReleaseRunnable;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Benchmark {
    private static final String DB_URL = "jdbc:h2:mem:test";

    public static void main(String[] args) throws SQLException {
        for (final Pool.PoolType poolType : Pool.PoolType.values()) {
            new Benchmark().bench(poolType);
        }
    }

    private void bench(final Pool.PoolType poolType) throws SQLException {
        System.out.println(String.format("Pool type: %s", poolType));

        final PoolProperties poolProperties = new PoolProperties(DB_URL, 1, 100, 5, poolType);

        long runtime = runGetRelease(1, 1_000_000, poolProperties);
        System.out.println(String.format("Benchmark #1: %d ms.", runtime));

        runtime = runGetRelease(100, 10_000, poolProperties);
        System.out.println(String.format("Benchmark #2: %d ms.", runtime));

        runtime = runGetRelease(500, 10_000, poolProperties);
        System.out.println(String.format("Benchmark #3: %d ms.", runtime));

        runtime = runGetDelayedRelease(500, 100, 10, poolProperties);
        System.out.println(String.format("Benchmark #4: %d ms.", runtime));

        runtime = runGetDelayedRelease(200, 50, 100, poolProperties);
        System.out.println(String.format("Benchmark #5: %d ms.", runtime));

        final PoolProperties pownedPoolProperties = new PoolProperties(DB_URL, 10, 10, 1, poolType);
        runtime = runGetRelease(20, 2_000, pownedPoolProperties);
        System.out.println(String.format("Benchmark #P0WN3D: %d ms.", runtime));

        final PoolProperties productionPoolProperties = new PoolProperties(DB_URL, 3, 24, 3, poolType);
        runtime = runGetDelayedRelease(25, 10, 500, productionPoolProperties);
        System.out.println(String.format("Benchmark #productionRuntime: %d ms.", runtime));

        final PoolProperties deadPoolProperties = new PoolProperties(DB_URL, 5, 100, 5, poolType);
        runtime = runGetDelayedRelease(10_000, 1, 75, deadPoolProperties);
        System.out.println(String.format("Benchmark #deathRuntime: %d ms.", runtime));
    }

    private long runGetRelease(int nbThreads, int nbConnectionsRequestedPerThread, PoolProperties poolProperties) throws SQLException {
        final Pool pool = new Pool(poolProperties);
        final long begin = System.nanoTime();
        final ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < nbThreads; i++) {
            executorService.submit(new GetReleaseRunnable(pool, nbConnectionsRequestedPerThread));
        }

        executorService.shutdown();
        while (!executorService.isTerminated())
            ;

        final long end = System.nanoTime();

        System.out.println(String.format("Connections allocated: %d", pool.getNbConnectionsAllocated()));

        return (end - begin) / 1_000_000;
    }

    private long runGetDelayedRelease(int nbThreads, int nbConnectionsRequestedPerThread, long delay, PoolProperties poolProperties)
            throws SQLException {
        final Pool pool = new Pool(poolProperties);
        final long begin = System.nanoTime();
        final ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < nbThreads; i++) {
            executorService.submit(new GetDelayedReleaseRunnable(pool, nbConnectionsRequestedPerThread, delay));
        }

        executorService.shutdown();
        while (!executorService.isTerminated())
            ;

        final long end = System.nanoTime();

        System.out.println(String.format("Connections allocated: %d", pool.getNbConnectionsAllocated()));

        return (end - begin) / 1_000_000;
    }
}
