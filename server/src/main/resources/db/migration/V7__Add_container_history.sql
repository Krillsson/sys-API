CREATE TABLE ContainerStatisticsEntity
(
    id                  VARCHAR(255) NOT NULL,
    timestamp           datetime     NOT NULL,
    currentPid          BIGINT       NOT NULL,
    usagePercentPerCore DOUBLE       NOT NULL,
    usagePercentTotal   DOUBLE       NOT NULL,
    periods             BIGINT       NOT NULL,
    throttledPeriods    BIGINT       NOT NULL,
    throttledTime       BIGINT       NOT NULL,
    bytesReceived       BIGINT       NOT NULL,
    bytesTransferred    BIGINT       NOT NULL,
    bytesWritten        BIGINT       NOT NULL,
    bytesRead           BIGINT       NOT NULL,
    usageBytes          BIGINT       NOT NULL,
    usagePercent        DOUBLE       NOT NULL,
    limitBytes          BIGINT       NOT NULL,
    CONSTRAINT pk_containerstatisticsentity PRIMARY KEY (id)
);

create index if not exists ContainerStatisticsEntityTimestamp on ContainerStatisticsEntity (id, timestamp);