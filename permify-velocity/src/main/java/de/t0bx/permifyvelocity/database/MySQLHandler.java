package de.t0bx.permifyvelocity.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.t0bx.permifyvelocity.PermifyPlugin;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class MySQLHandler implements IMySQLHandler, AutoCloseable {
    private HikariDataSource dataSource;
    private final Executor executor;
    private final Logger logger;

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    private final int poolSize;
    private final long connectionTimeout;
    private final long idleTimeout;
    private final long maxLifetime;

    private static final int DEFAULT_POOL_SIZE = 10;
    private static final int DEFAULT_CONNECTION_TIMEOUT = 30000;
    private static final int DEFAULT_IDLE_TIMEOUT = 600000;
    private static final int DEFAULT_MAX_LIFETIME = 1800000;

    public MySQLHandler(String host, int port, String database, String username, String password) {
        this(host, port, username, password, database, DEFAULT_POOL_SIZE, DEFAULT_CONNECTION_TIMEOUT, DEFAULT_IDLE_TIMEOUT, DEFAULT_MAX_LIFETIME);
    }

    public MySQLHandler(String host, int port, String database, String username, String password, int poolSize, long connectionTimeout, long idleTimeout, long maxLifetime) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;

        this.poolSize = poolSize;
        this.connectionTimeout = connectionTimeout;
        this.idleTimeout = idleTimeout;
        this.maxLifetime = maxLifetime;

        this.executor = Executors.newFixedThreadPool(Math.max(2, poolSize / 2));
        this.logger = PermifyPlugin.getInstance().getLogger();
    }

    @Override
    public void connect() throws SQLException {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database +
                    "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");

            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(poolSize);
            config.setConnectionTimeout(connectionTimeout);
            config.setIdleTimeout(idleTimeout);
            config.setMaxLifetime(maxLifetime);

            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.addDataSourceProperty("useServerPrepStmts", "true");
            config.addDataSourceProperty("useLocalSessionState", "true");
            config.addDataSourceProperty("rewriteBatchedStatements", "true");
            config.addDataSourceProperty("cacheResultSetMetadata", "true");
            config.addDataSourceProperty("cacheServerConfiguration", "true");
            config.addDataSourceProperty("elideSetAutoCommits", "true");
            config.addDataSourceProperty("maintainTimeStats", "true");

            this.dataSource = new HikariDataSource(config);
            this.logger.info("Connected to MySQL using HikariCP with a pool size of " + poolSize);
        } catch (Exception exception) {
            throw new SQLException("Failed to initialize MySQL connection pool", exception);
        }
    }

    @Override
    public void disconnect() {
        if (this.dataSource != null && !this.dataSource.isClosed()) {
            this.dataSource.close();
            this.logger.info("HikariCP connection pool has been closed");
        }
    }

    @Override
    public void close() {
        this.disconnect();
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (this.dataSource == null || this.dataSource.isClosed()) {
            throw new SQLException("Connection pool has not been initialized or has been closed!");
        }
        return this.dataSource.getConnection();
    }

    @Override
    public void update(String query, Object... params) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            this.setParameters(preparedStatement, params);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public CompletableFuture<Void> updateAsync(String query, Object... params) {
        return CompletableFuture.runAsync(() -> {
           try {
               this.update(query, params);
           } catch (SQLException exception) {
               this.logger.error("Error while executing async update", exception);
               throw new RuntimeException("Error while updating MySQL", exception);
           }
        }, this.executor);
    }

    @Override
    public <T> List<T> query(String query, Function<ResultSet, T> resultHandler, Object... params) throws SQLException {
        List<T> results = new ArrayList<>();

        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            this.setParameters(preparedStatement, params);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    results.add(resultHandler.apply(resultSet));
                }
            }
        }

        return results;
    }

    @Override
    public <T> CompletableFuture<List<T>> queryAsync(String query, Function<ResultSet, T> resultHandler, Object... params) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return this.query(query, resultHandler, params);
            } catch (SQLException exception) {
                this.logger.error("Error while executing async query", exception);
                throw new RuntimeException("Error while executing async query", exception);
            }
        }, this.executor);
    }

    @Override
    public int[] batchUpdate(String query, List<Object[]> batchParams) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            for (Object[] batchParam : batchParams) {
                this.setParameters(preparedStatement, batchParam);
                preparedStatement.addBatch();
            }

            return preparedStatement.executeBatch();
        }
    }

    @Override
    public CompletableFuture<int[]> batchUpdateAsync(String query, List<Object[]> batchParams) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return this.batchUpdate(query, batchParams);
            } catch (SQLException exception) {
                this.logger.error("Error while executing async batch update", exception);
                throw new RuntimeException("Error while executing async batch update", exception);
            }
        }, this.executor);
    }

    @Override
    public <T> Optional<T> queryFirst(String query, Function<ResultSet, T> resultHandler, Object... params) throws SQLException {
        List<T> results = this.query(query, resultHandler, params);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }

    @Override
    public <T> CompletableFuture<Optional<T>> queryFirstAsync(String query, Function<ResultSet, T> resultHandler, Object... params) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return this.queryFirst(query, resultHandler, params);
            } catch (SQLException exception) {
                this.logger.error("Error while executing async query", exception);
                throw new RuntimeException("Error while executing async query", exception);
            }
        }, this.executor);
    }

    @Override
    public <T> T queryScalar(String query, Class<T> type, Object... params) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            this.setParameters(preparedStatement, params);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getObject(1, type);
                }

                return null;
            }
        }
    }

    @Override
    public <T> CompletableFuture<T> queryScalarAsync(String query, Class<T> type, Object... params) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                 return this.queryScalar(query, type, params);
            } catch (SQLException exception) {
                this.logger.error("Error while executing async query", exception);
                throw new RuntimeException("Error while executing async query", exception);
            }
        }, this.executor);
    }

    @Override
    public void transaction(TransactionCallback callback) throws SQLException {
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            callback.execute(connection);

            connection.commit();
        } catch (Exception exception) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    this.logger.error("Error while rolling back transaction", rollbackException);
                }
            }
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new SQLException("Error while executing transaction", exception);
            }
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException closeException) {
                    this.logger.error("Error while closing connection", closeException);
                }
            }
        }
    }

    @Override
    public CompletableFuture<Void> transactionAsync(TransactionCallback callback) {
        return CompletableFuture.runAsync(() -> {
            try {
                this.transaction(callback);
            } catch (SQLException exception) {
                this.logger.error("Error while executing transaction", exception);
                throw new RuntimeException("Error while executing transaction", exception);
            }
        }, this.executor);
    }

    @Override
    public long count(String table, String whereClause, Object... params) throws SQLException {
        String query = "SELECT COUNT(*) FROM" + table;
        if (whereClause != null && !whereClause.isEmpty()) {
            query += " WHERE " + whereClause;
        }

        return this.queryScalar(query, Long.class, params);
    }

    @Override
    public CompletableFuture<Long> countAsync(String table, String whereClause, Object... params) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return this.count(table, whereClause, params);
            } catch (SQLException exception) {
                this.logger.error("Error while executing count", exception);
                throw new RuntimeException("Error while executing count", exception);
            }
        }, this.executor);
    }

    @Override
    public boolean exists(String table, String whereClause, Object... params) throws SQLException {
        return this.count(table, whereClause, params) > 0;
    }

    @Override
    public CompletableFuture<Boolean> existsAsync(String table, String whereClause, Object... params) {
        return this.countAsync(table, whereClause, params).thenApply(count -> count > 0);
    }

    @Override
    public HikariPoolStats getPoolStats() {
        if (this.dataSource == null) {
            throw new IllegalStateException("Connection pool has not been initialized");
        }

        return new HikariPoolStats(
                this.dataSource.getHikariPoolMXBean().getActiveConnections(),
                this.dataSource.getHikariPoolMXBean().getIdleConnections(),
                this.dataSource.getHikariPoolMXBean().getTotalConnections(),
                this.dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection()
        );
    }

    private void setParameters(PreparedStatement preparedStatement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
    }
}