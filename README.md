:computer: System API
=====================
[![Build Status](https://travis-ci.org/Krillsson/sys-api.svg)](https://travis-ci.org/Krillsson/sys-api)
[![Coverage Status](https://coveralls.io/repos/github/Krillsson/sys-api/badge.svg?branch=develop)](https://coveralls.io/github/Krillsson/sys-api?branch=develop)
[![SonarQube Quality Gate](https://sonarqube.com/api/badges/gate?key=com.krillsson:sys-api)](https://sonarqube.com/dashboard?id=com.krillsson%3Asys-api)

System API (sys-api) is a RESTful API to your computers hardware. 

Sys-api publishes metrics from [OSHI](https://github.com/oshi/oshi) with the help of [Dropwizard](https://github.com/dropwizard/dropwizard). On Windows the information is supplemented with
[OpenHardwareMonitor](https://github.com/openhardwaremonitor/openhardwaremonitor) with a little help from [OhmJni4Net](https://github.com/Krillsson/ohmjni4net).

## What can it do?
Use the */system* endpoint to get a summary of the system health.

- CPU usage & info
- Memory usage
- List running processes
- List all network interfaces
- Show info from sensors and fans
- Motherboard information

## Endpoints

```
    GET /cpu
    GET /cpu/ticks
    GET /gpus
    GET /memory
    GET /motherboard
    GET /nics
    GET /nics/{id}
    GET /powersources
    GET /processes
    GET /processes/{pid}
    GET /sensors
    GET /storage
    GET /storage/{name}
    GET /system
```

## Running
Download the [latest release](https://github.com/Krillsson/sys-api/releases/latest).

- Windows: unzip the package somewhere convenient and then right click the `.bat` file and choose _Run as administrator_
- *nix: untar or unzip the package and run the `.sh` file from a terminal

A GUI in JavaFX is in the making

## Configuration
The configuration.yml file is a [Dropwizard configuration file](https://dropwizard.github.io/dropwizard/manual/configuration.html).

Basic Authentication:

    user:
        username: [change me]
        password: [change me]
        
Disable the OhmJni extension on Windows:

    windows:
      enableOhmJniWrapper: false

### Enable HTTPS

Forward HTTP to HTTPS:

    forwardHttps: true

## Development
Setup
```sh
git clone [this repo] sys-api
```
```sh
mvn clean install
```
```sh
cd server
java -jar target/system-api.jar server configuration.yml
```
Test

```sh
curl -i --user user:password -H "Accept: application/json" -X GET http://localhost:8080/v1/system
```

Package for distribution in a *.zip*, *tar.gz* and *tar.bz2*:

```sh
mvn clean package
```

And the resulting files should be located in */server/target/*

License
-------

    Copyright 2014 Krillsson
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
