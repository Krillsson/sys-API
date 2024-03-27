CREATE TABLE Connectivity
(
    id                 char(36)     NOT NULL,
    externalIp         VARCHAR(255) NULL,
    previousExternalIp VARCHAR(255) NULL,
    connected          BIT(1)       NOT NULL,
    CONSTRAINT pk_connectivity PRIMARY KEY (id)
);

CREATE TABLE CoreLoad
(
    id         char(36) NOT NULL,
    cpuLoadId  char(36) NULL,
    percentage DOUBLE   NOT NULL,
    CONSTRAINT pk_coreload PRIMARY KEY (id),
    CONSTRAINT FK_CORELOAD_ON_CPULOADID FOREIGN KEY (cpuLoadId) REFERENCES CpuLoad (id)
);

CREATE TABLE CpuLoad
(
    id                char(36) NOT NULL,
    usagePercentage   DOUBLE   NOT NULL,
    systemLoadAverage DOUBLE   NOT NULL,
    processCount      INT      NOT NULL,
    threadCount       INT      NOT NULL,
    voltage           DOUBLE   NOT NULL,
    fanRpm            DOUBLE   NOT NULL,
    fanPercent        DOUBLE   NOT NULL,
    CONSTRAINT pk_cpuload PRIMARY KEY (id)
);

CREATE TABLE CpuLoad_temperatures
(
    CpuLoad_id   char(36) NOT NULL,
    temperatures DOUBLE   NULL,
    CONSTRAINT fk_cpuload_temperatures_on_cpu_load FOREIGN KEY (CpuLoad_id) REFERENCES CpuLoad (id)
);

CREATE TABLE DriveHealthData
(
    id            char(36)     NOT NULL,
    driveLoadId   char(36)     NULL,
    `description` VARCHAR(255) NULL,
    data          DOUBLE       NOT NULL,
    dataType      INT          NULL,
    CONSTRAINT pk_drivehealthdata PRIMARY KEY (id),
    CONSTRAINT FK_DRIVEHEALTHDATA_ON_DRIVELOADID FOREIGN KEY (driveLoadId) REFERENCES DriveLoad (id)
);

CREATE TABLE DriveLoad
(
    id                  char(36)     NOT NULL,
    historyId           char(36)     NULL,
    name                VARCHAR(255) NULL,
    serial              VARCHAR(255) NULL,
    temperature         DOUBLE       NOT NULL,
    usableSpace         BIGINT       NOT NULL,
    totalSpace          BIGINT       NOT NULL,
    openFileDescriptors BIGINT       NOT NULL,
    maxFileDescriptors  BIGINT       NOT NULL,
    `reads`             BIGINT       NOT NULL,
    readBytes           BIGINT       NOT NULL,
    writes              BIGINT       NOT NULL,
    writeBytes          BIGINT       NOT NULL,
    readBytesPerSecond  BIGINT       NOT NULL,
    writeBytesPerSecond BIGINT       NOT NULL,
    CONSTRAINT pk_driveload PRIMARY KEY (id),
    CONSTRAINT FK_DRIVELOAD_ON_HISTORYID FOREIGN KEY (historyId) REFERENCES HistorySystemLoadEntity (id)
);

CREATE TABLE GpuLoad
(
    id          char(36)     NOT NULL,
    historyId   char(36)     NULL,
    name        VARCHAR(255) NULL,
    coreLoad    DOUBLE       NOT NULL,
    memoryLoad  DOUBLE       NOT NULL,
    fanRpm      DOUBLE       NOT NULL,
    fanPercent  DOUBLE       NOT NULL,
    temperature DOUBLE       NOT NULL,
    CONSTRAINT pk_gpuload PRIMARY KEY (id),
    CONSTRAINT FK_GPULOAD_ON_HISTORYID FOREIGN KEY (historyId) REFERENCES HistorySystemLoadEntity (id)
);

CREATE TABLE HealthData
(
    id            char(36)     NOT NULL,
    historyId     char(36)     NULL,
    `description` VARCHAR(255) NULL,
    data          DOUBLE       NOT NULL,
    dataType      INT          NULL,
    CONSTRAINT pk_healthdata PRIMARY KEY (id),
    CONSTRAINT FK_HEALTHDATA_ON_HISTORYID FOREIGN KEY (historyId) REFERENCES HistorySystemLoadEntity (id)
);

CREATE TABLE HistorySystemLoadEntity
(
    id                char(36) NOT NULL,
    date              datetime NULL,
    uptime            BIGINT   NOT NULL,
    systemLoadAverage DOUBLE   NOT NULL,
    cpuLoad_id        char(36) NULL,
    connectivity_id   char(36) NULL,
    memory_id         char(36) NULL,
    CONSTRAINT pk_historysystemloadentity PRIMARY KEY (id),
    CONSTRAINT FK_HISTORYSYSTEMLOADENTITY_ON_CONNECTIVITY FOREIGN KEY (connectivity_id) REFERENCES Connectivity (id),
    CONSTRAINT FK_HISTORYSYSTEMLOADENTITY_ON_CPULOAD FOREIGN KEY (cpuLoad_id) REFERENCES CpuLoad (id),
    CONSTRAINT FK_HISTORYSYSTEMLOADENTITY_ON_MEMORY FOREIGN KEY (memory_id) REFERENCES MemoryLoad (id)
);

CREATE TABLE MemoryLoad
(
    id                char(36) NOT NULL,
    numberOfProcesses INT      NOT NULL,
    swapTotalBytes    BIGINT   NOT NULL,
    swapUsedBytes     BIGINT   NOT NULL,
    totalBytes        BIGINT   NOT NULL,
    availableBytes    BIGINT   NOT NULL,
    usedPercent       DOUBLE   NOT NULL,
    CONSTRAINT pk_memoryload PRIMARY KEY (id)
);

CREATE TABLE NetworkInterfaceLoad
(
    id                    char(36)     NOT NULL,
    historyId             char(36)     NULL,
    name                  VARCHAR(255) NULL,
    mac                   VARCHAR(255) NULL,
    isUp                  BIT(1)       NOT NULL,
    speed                 BIGINT       NOT NULL,
    bytesReceived         BIGINT       NOT NULL,
    bytesSent             BIGINT       NOT NULL,
    packetsReceived       BIGINT       NOT NULL,
    packetsSent           BIGINT       NOT NULL,
    inErrors              BIGINT       NOT NULL,
    outErrors             BIGINT       NOT NULL,
    receiveBytesPerSecond BIGINT       NOT NULL,
    sendBytesPerSecond    BIGINT       NOT NULL,
    CONSTRAINT pk_networkinterfaceload PRIMARY KEY (id),
    CONSTRAINT FK_NETWORKINTERFACELOAD_ON_HISTORYID FOREIGN KEY (historyId) REFERENCES HistorySystemLoadEntity (id)
);