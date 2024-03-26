CREATE TABLE DiskLoad
(
    id                  char(36)     NOT NULL,
    historyId           char(36)     NULL,
    name                VARCHAR(255) NULL,
    serial              VARCHAR(255) NULL,
    `reads`             BIGINT       NOT NULL,
    readBytes           BIGINT       NOT NULL,
    writes              BIGINT       NOT NULL,
    writeBytes          BIGINT       NOT NULL,
    readBytesPerSecond  BIGINT       NOT NULL,
    writeBytesPerSecond BIGINT       NOT NULL,
    CONSTRAINT pk_diskload PRIMARY KEY (id),
    CONSTRAINT FK_DISKLOAD_ON_HISTORYID FOREIGN KEY (historyId) REFERENCES HistorySystemLoadEntity (id)
);

CREATE TABLE FileSystemLoad
(
    id                  char(36)     NOT NULL,
    historyId           char(36)     NULL,
    name                VARCHAR(255) NULL,
    freeSpaceBytes      BIGINT       NOT NULL,
    usableSpaceBytes    BIGINT       NOT NULL,
    totalSpaceBytes     BIGINT       NOT NULL,
    CONSTRAINT pk_driveload PRIMARY KEY (id),
    CONSTRAINT FK_DRIVELOAD_ON_HISTORYID FOREIGN KEY (historyId) REFERENCES HistorySystemLoadEntity (id)
);