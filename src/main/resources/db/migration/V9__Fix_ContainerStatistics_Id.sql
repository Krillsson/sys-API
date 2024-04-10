CREATE TABLE ContainerStatisticsEntityCopy
(
    id               char(36)     NOT NULL,
    containerId      VARCHAR(255) NOT NULL,
    timestamp        datetime     NOT NULL,
    currentPid       BIGINT       NOT NULL,
    usagePercentPerCore DOUBLE NOT NULL,
    usagePercentTotal DOUBLE NOT NULL,
    periods          BIGINT       NOT NULL,
    throttledPeriods BIGINT       NOT NULL,
    throttledTime    BIGINT       NOT NULL,
    bytesReceived    BIGINT       NOT NULL,
    bytesTransferred BIGINT       NOT NULL,
    bytesWritten     BIGINT       NOT NULL,
    bytesRead        BIGINT       NOT NULL,
    usageBytes       BIGINT       NOT NULL,
    usagePercent DOUBLE NOT NULL,
    limitBytes       BIGINT       NOT NULL,
    CONSTRAINT pk_containerstatisticsentity PRIMARY KEY (id)
);
INSERT INTO ContainerStatisticsEntityCopy (id, containerId, timestamp, currentPid, usagePercentPerCore, usagePercentTotal, periods,
                                           throttledPeriods, throttledTime, bytesReceived, bytesTransferred,
                                           bytesWritten, bytesRead, usageBytes, usagePercent, limitBytes)
SELECT lower(
               hex(randomblob(4)) || '-' || hex(randomblob(2)) || '-' || '4' ||
               substr(hex(randomblob(2)), 2) || '-' ||
               substr('AB89', 1 + (abs(random()) % 4), 1) ||
               substr(hex(randomblob(2)), 2) || '-' ||
               hex(randomblob(6))
       ) entityId, id, timestamp, currentPid, usagePercentPerCore, usagePercentTotal, periods, throttledPeriods, throttledTime, bytesReceived, bytesTransferred, bytesWritten, bytesRead, usageBytes, usagePercent, limitBytes
FROM ContainerStatisticsEntity;
DROP TABLE ContainerStatisticsEntity;
ALTER TABLE ContainerStatisticsEntityCopy RENAME TO ContainerStatisticsEntity;