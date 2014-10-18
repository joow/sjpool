package org.sjpool;

import java.util.concurrent.Callable;

public class TestRunnable implements Callable<Integer> {
    public TestRunnable(TestDAO testDAO) {
    }

    @Override
    public Integer call() throws Exception {
        return testDAO.readDB().size();
    }
}
