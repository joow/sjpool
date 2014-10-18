package org.sjpool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestDAO {
    private final Pool pool;

    public TestDAO(Pool pool) {
        this.pool = pool;
    }

    public void initializeDB() throws SQLException {
        createDB();
        populateDB();
    }

    private void createDB() throws SQLException {
        try (final Connection connection = pool.getConnection(); final Statement statement = connection.createStatement()) {
            statement.executeUpdate(("CREATE TABLE TEST(ID INTEGER NOT NULL)"));
        }
    }

    private void populateDB() throws SQLException {
        try (final Connection connection = pool.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO TEST VALUES (?)")) {
            for (int i = 0; i < 10000; i++) {
                preparedStatement.setInt(1, i);
                preparedStatement.executeUpdate();
            }
        }
    }

    public List<Integer> readDB() throws SQLException {
        final List<Integer> integers = new ArrayList<>();
        try (final Connection connection = pool.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID FROM TEST");
             final ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                integers.add(resultSet.getInt(1));
            }
        }

        return integers;
    }
}
