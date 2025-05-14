package de.t0bx.permifyvelocity.database;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class HikariPoolStats {
    private final int activeConnections;
    private final int idleConnections;
    private final int totalConnections;
    private final int waitingThread;

    @Override
    public String toString() {
        return "HikariPoolStats{" +
                "activeConnections= " + activeConnections +
                ", idleConnections= " + idleConnections +
                ", totalConnections= " + totalConnections +
                ", waitingThreads= " + waitingThread +
                "}";
    }
}
