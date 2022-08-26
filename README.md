![header](header.png)
[![Docker hub](https://badgen.net/badge/icon/docker?icon=docker&label)](https://hub.docker.com/r/krillsson/sys-api)
[![Coverage Status](https://coveralls.io/repos/github/Krillsson/sys-api/badge.svg?branch=develop)](https://coveralls.io/github/Krillsson/sys-api?branch=develop)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.krillsson%3Asys-api&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=com.krillsson%3Asys-api)

System API (sys-API) provide both GraphQL and RESTful API's to your computers hardware.

It publishes and monitors values from [OSHI](https://github.com/oshi/oshi) with the help of [Dropwizard](https://github.com/dropwizard/dropwizard). On Windows the information is supplemented with
[OpenHardwareMonitor](https://github.com/openhardwaremonitor/openhardwaremonitor) with a bit of help from [OhmJni4Net](https://github.com/Krillsson/ohmjni4net).

>This is the server backend for the Android app Monitee.
>
>:warning: Latest compatible version is [0.17.1](https://github.com/Krillsson/sys-API/releases/tag/0.17.1) :warning:
>
><a href="https://play.google.com/store/apps/details?id=com.krillsson.monitee"><img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" alt="Get it on Play Store" height="80"></a>

## What can it do?

Query for:
- CPU usage & info
- Memory usage
- List running processes
- List all network interfaces
- Show info from sensors and fans
- Motherboard information
- List docker containers
- Start/stop docker containers

### Monitoring

Currently, you can monitor

- CPU load
- CPU temperature
- Memory usage
- Network up
- Network upload/download rate
- Drive space
- Drive read/write rate
- Process ID CPU or Memory
- Docker container in running state
- Connectivity
- External IP changed

### GraphQL

GraphQL is available through the `/graphql` endpoint. Checkout the [schema](server/src/main/resources/schema.graphqls). There's also a set of sample queries in the _sample-queries_ directory

A web-UI for trying out the GraphQL-API is also available at `<IP>:8080/`. If you don't want to expose this functionality. It can be disabled via the configuration.

```yaml
graphQLPlayGround:
  enabled: false
```

If the server is protected by Basic Auth, you need to configure GraphQL Playground to send the `authorization` header. Here's an example with the default credentials:

```json
{
  "authorization": "Basic dXNlcjpwYXNzd29yZA=="
}
```

## REST-API

The `/system` endpoint contains accumulated information about the whole system. Then if you want information for just a specific part of the system you can use the part specific endpoint.
So for example the cpu temperature and usage is under `/cpu`. Some things are excluded from `/system`, such as `/system/jvm` and `/processes` since they contain so much information.

## Running
Download the [latest release](https://github.com/Krillsson/sys-api/releases/latest).

- Windows: unzip the package somewhere convenient and then right click the `.bat` file and choose _Run as administrator_
- *nix: untar or unzip the package and run the `.sh` file from a terminal
  - Use `nohup` to run the process disconnected from the terminal:
  - `$ nohup ./run.sh &`
  - press CTRL+C
  - Verify that it started successfully:
  - `$ tail -f nohup.out`

## Running using docker
Make sure you have [docker compose](https://docs.docker.com/compose/install/) installed.

 - Navigate to a directory on your machine that you want to install sys-API docker container in
 - Create the two directories so sys-API can persist your environment specific stuff outside the container
   - `$ mkdir data`
   - `$ mkdir config`
 - Download the compose file to your root directory
   - For example: `$ wget https://raw.githubusercontent.com/Krillsson/sys-API/master/docker-compose.yml`
 - Make the appropriate edits to the docker-compose file. See the comments in there.
 - Start the container
   - `$ docker compose -f docker-compose.yml up`

docker compose -f docker-compose-private.yml up

## Configuration
The configuration.yml file is a [Dropwizard configuration file](https://www.dropwizard.io/en/latest/manual/configuration.html).

Basic Authentication:

    user:
        username: [change me]
        password: [change me]

### Self-signed certificates
By default, sys-API will generate a self-signed certificate to enable HTTPS. This is to lower the barrier for encrypted traffic between the client and the server.
Please note that using properly signed certificates is better. Let's Encrypt is a free and good alternative. Refer [wiki page](https://github.com/Krillsson/sys-API/wiki/Let's-Encrypt) on how to set it up.

For convenience, the certificates are persisted using a java keystore. If you wish to re-generate the certificates, delete the _keystorewww.jks_ file. But note that this will require re-adding the server in Monitee.

## Development
Setup
```sh
git clone [this repo] sys-api
```
```sh
./gradlew :server:run
```
Test

```sh
curl -i --user user:password -H "Accept: application/json" -X GET http://localhost:8080/api/system
```

Package for distribution in a *.zip*:

```sh
./gradlew :server:shadowDistZip
```

And the resulting files should be located in */server/target/*

License
-------

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
