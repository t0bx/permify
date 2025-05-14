package de.t0bx.permifyvelocity.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface IMySQLHandler {

    void connect() throws SQLException;

    void disconnect();

    void update(String query, Object... params) throws SQLException;

    CompletableFuture<Void> updateAsync(String query, Object... params);

    <T> List<T> query(String query, Function<ResultSet, T> resultHandler, Object... params) throws SQLException;

    <T> CompletableFuture<List<T>> queryAsync(String query, Function<ResultSet, T> resultHandler, Object... params);

    int[] batchUpdate(String query, List<Object[]> batchParams) throws SQLException;

    CompletableFuture<int[]> batchUpdateAsync(String query, List<Object[]> batchParams);

    <T> Optional<T> queryFirst(String query, Function<ResultSet, T> resultHandler, Object... params) throws SQLException;

    <T> CompletableFuture<Optional<T>> queryFirstAsync(String query, Function<ResultSet, T> resultHandler, Object... params);

    <T> T queryScalar(String query, Class<T> type, Object... params) throws SQLException;

    <T> CompletableFuture<T> queryScalarAsync(String query, Class<T> type, Object... params);

    void transaction(TransactionCallback callback) throws SQLException;

    CompletableFuture<Void> transactionAsync(TransactionCallback callback);

    long count(String table, String whereClause, Object... params) throws SQLException;

    CompletableFuture<Long> countAsync(String table, String whereClause, Object... params);

    boolean exists(String table, String whereClause, Object... params) throws SQLException;

    CompletableFuture<Boolean> existsAsync(String table, String whereClause, Object... params);

    HikariPoolStats getPoolStats();

    Connection getConnection() throws SQLException;

    interface TransactionCallback {
        void execute(Connection connection) throws SQLException;
    }
}
