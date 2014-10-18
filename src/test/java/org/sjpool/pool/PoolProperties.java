package org.sjpool.pool;

public class PoolProperties {
    private final String databaseUrl;

    private final int nbMinConnections;

    private final int nbMaxConnections;

    private final int acquireIncrements;

    private final Pool.PoolType poolType;

    public PoolProperties(String databaseUrl, int nbMinConnections, int nbMaxConnections, int acquireIncrements,
                          Pool.PoolType poolType) {
        super();
        this.databaseUrl = databaseUrl;
        this.nbMinConnections = nbMinConnections;
        this.nbMaxConnections = nbMaxConnections;
        this.acquireIncrements = acquireIncrements;
        this.poolType = poolType;
    }

    public String getDabaseUrl() {
        return databaseUrl;
    }

    public int getNbMinConnections() {
        return nbMinConnections;
    }

    public int getNbMaxConnections() {
        return nbMaxConnections;
    }

    public int getAcquireIncrements() {
        return acquireIncrements;
    }

    public Pool.PoolType getPoolType() {
        return poolType;
    }
}
