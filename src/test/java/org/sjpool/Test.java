package org.sjpool;

import org.sjpool.pool.Pool;
import org.sjpool.pool.PoolProperties;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Test {
    private static final int NB_CLIENTS = 1000;

    public static void main(String[] args) throws SQLException, InterruptedException, ExecutionException {

        final TestDAO testDAO = new TestDAO(new Pool(new PoolProperties("jdbc:h2:mem:test", 3, 25, 3, Pool.PoolType.SIMPLE_JAVA_POOL)));
        testDAO.initializeDB();

        final long begin = System.currentTimeMillis();
        final ExecutorService executorService = Executors.newFixedThreadPool(100);
        final List<Future<Integer>> futures = new ArrayList<>();

        for (int i = 0; i < NB_CLIENTS; i++) {
            futures.add(executorService.submit(new TestRunnable(testDAO)));
        }

        executorService.shutdown();

        for (int i = 0; i < NB_CLIENTS; i++) {
            int result = futures.get(i).get();
            if (!(result == 10000)) {
                throw new RuntimeException("Wrong result !");
            }
        }

        final long end = System.currentTimeMillis();

        System.out.println(String.format("done in: %d ms", end - begin));
    }
}
