package org.sjpool;

import java.util.concurrent.Callable;

public class TestRunnable implements Callable<Integer> {
    private final TestDAO testDAO;

    TestRunnable(TestDAO testDAO) {
        this.testDAO = testDAO;
    }

    @Override
    public Integer call() throws Exception {
        return testDAO.readDB().size();
    }
}
