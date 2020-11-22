### 0.14.1

**REST API Breaking changes**

 - Optimize GraphQL layer
 - Convert more classes to Kotlin

### 0.14

**REST API Breaking changes**

 - Migrated project to Gradle
 - Add PhysicalMemory to MemoryInfo
 - Remove as much nullability from GraphQL schema as possible
 - Events persistence
 - Update dependencies
 
### 0.13
 
 - Migration to Kotlin
 - GraphQL support
 - Events persistence
 
### 0.12

 - Dates are now serialized as: `2019-02-04T22:08:42.048+01:00`
 - Latest dropwizard
 - Added `GET /monitors/{id}/events` endpoint (get events for a monitor)
 

### 0.11

**REST API Breaking changes**

- OSX: Fixes related to drives migrated to APFS 
- Network speed is now included in NetworkInterface object (/system/ & /nics/)
- Drive object now has a sizeBytes property (/system/ & /drives/)
- /system/load now includes top ten memory consuming processes by default. Configurable via query parameter.
- Every /history/ endpoint now has optional query parameters to limit the output. E.g: `v2/system/load/history?fromDate=2018-09-23T15:11:55.661&toDate=2018-09-23T15:21:25.659`

### 0.10

Lot's of new features! **And unfortunately an REST API breaking release.**

Changelog: 
- Monitoring
- Split static information from system load information
- Load history
- Caching of values so that each server call does not mean a system call
- Latest dropwizard and OSHI
- More flexible configuration of polling and caching

*Please keep in mind that the API is still in it's early stages and is subject to change*

### 0.9

- Network tx/rx and Disk r/w are now fetched from OpenHardwareMonitor on Windows
- Add support for hot reloading SSL certs (i.e Let's Encrypt) [Guide](https://github.com/Krillsson/sys-API/wiki/Let's-Encrypt)

*Please keep in mind that the API is still in it's early stages and is subject to change*

### 0.8

- New feature: GET /CPU/ now returns detailed load per core
- The deliverable now includes a Postman collection that covers most of the functionality of the API
- Fixes issue where APFS storage did not have a OSFileStore
- Updated Dropwizard, OSHI and Jackson

*Please keep in mind that the API is still in it's early stages and is subject to change*

### 0.7

- Fix bug where osFileStore was sometimes missing from JSON payload
- Updated Dropwizard and OSHI

*Please keep in mind that the API is still in it's early stages and is subject to change*

### 0.6

- Changed information source from Sigar to OSHI
- Better calculation of speed (nic dl/upl, disk r/w)
- GUI version is now included

*Please keep in mind that the API is still in it's early stages and is subject to change*

### 0.5

- Support for Raspberry Pi
- Added id's to Filesystems

*Please keep in mind that the API is still in it's early stages and is subject to change*

### 0.4

- Added GET /meta/version and GET /meta/pid
- GET /cpu/ now includes process statistics
- Fixed nasty crash in Sigar
- Fixed NullReferenceException in OpenHardwareMonitorLib.dll

*Please keep in mind that the API is still in it's early stages and is subject to change*

### 0.3

- Added OpenHardwareMonitor support on Windows
- Added configuration option to forward HTTP requests to HTTPS

*Please keep in mind that the API is still in it's early stages and is subject to change*

### 0.2 

- This is the first release of System Api. Keep in mind that this is a very early version.

*Please keep in mind that the API is still in it's early stages and is subject to change*