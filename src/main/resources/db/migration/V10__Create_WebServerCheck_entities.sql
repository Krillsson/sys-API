CREATE TABLE WebServerCheckEntity
(
    id  char(36)     NOT NULL,
    url VARCHAR(255) NOT NULL,
    CONSTRAINT pk_websitecheckentity PRIMARY KEY (id)
);

CREATE TABLE WebServerCheckHistoryEntity
(
    id             char(36)     NOT NULL,
    websiteCheckId char(36)     NOT NULL,
    timestamp      datetime     NOT NULL,
    responseCode   INT          NOT NULL,
    message        VARCHAR(255) NOT NULL,
    errorBody      VARCHAR(255) NULL,
    CONSTRAINT pk_websitecheckhistoryentry PRIMARY KEY (id)
);

create index if not exists WebServerCheckHistoryEntry on WebServerCheckHistoryEntity (websiteCheckId, timestamp);