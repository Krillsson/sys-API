:computer: System API
=====================
[![Coverage Status](https://coveralls.io/repos/github/Krillsson/sys-api/badge.svg?branch=develop)](https://coveralls.io/github/Krillsson/sys-api?branch=develop)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.krillsson%3Asys-api&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=com.krillsson%3Asys-api)

System API (sys-API) provide both GraphQL and RESTful API's to your computers hardware.

It publishes and monitors values from [OSHI](https://github.com/oshi/oshi) with the help of [Dropwizard](https://github.com/dropwizard/dropwizard). On Windows the information is supplemented with
[OpenHardwareMonitor](https://github.com/openhardwaremonitor/openhardwaremonitor) with a bit of help from [OhmJni4Net](https://github.com/Krillsson/ohmjni4net).

## What can it do?

- Threshold-based monitoring of system values
- List ongoing threshold violations (events)
- CPU usage & info
- Memory usage
- List running processes
- List all network interfaces
- Show info from sensors and fans
- Motherboard information

### GraphQL

GraphQL is available through the `/graphql` endpoint. It supports the exact same features as the REST-API. Checkout the [schema](server/src/main/resources/schema.graphqls).

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

### Building UIs

The general idea is that each item has a root endpoint that contain static information, like a processors name or the names of the disks. Then each and every item has a `/load` endpoint that gives you the current
load of that item and a `/load/history` that gives you the load history over time. Disks or network interfaces always have mappable names or ID's between the root and load/history content.

History endpoints also have the ability to limit it's output. 

Example: `v2/system/load/history?fromDate=2018-09-23T15:11:55.661&toDate=2018-09-23T15:21:25.659`

So if you are building a UI on top of this API you can query the root endpoint once or seldom. And then if you want to display load over time you can query the history endpoint once and
then continuously poll the load endpoint and append the results to the history collections.

### Monitoring

Currently, you can monitor

 - CPU load
 - CPU temperature
 - Memory usage
 - Network up or down
 
#### Configuring monitors
 
You add a monitor by doing a POST call to `/monitors` with the body of `com.krillsson.sysapi.dto.monitor.Monitor`:

```json
{
  "inertiaInSeconds": "10",
  "type": "CPU|CPU_TEMP|DRIVE|GPU|MEMORY|NETWORK_UP",
  "threshold": "123"
}
```

If you exclude the `"id"` field from the JSON the API will create a new monitor. If you include an ID the API will update the monitor of that ID if it exists or return NOT FOUND/404 if it does not exist. 

The response for this call will be the UUID of the monitor that was created:

```json
{
    "id": "4bd27b5a-b3a2-4716-a7bb-8f0bf9815849"
}
```

You can then use this ID to delete or update a monitor.

_Inertia_: the amount of time in seconds the threshold should be crossed for before triggering a event. The same goes for when
transitioning back to below the threshold.

_Note_: The `NETWORK_UP` ignores the threshold and uses 1 as value for up and 0 for down.

_Note_: The cpu, gpu and cpu temp monitors checks if the load goes _above_ the thresholds while the drive and memory onces
monitors considers a violation to be when the value drops below a threshold.

#### Querying for events

Call the `/events` endpoints to get the currently ongoing threshold violations (events).

The response will be an array of `com.krillsson.sysapi.dto.monitor.MonitorEvent`:

```json
[
    {
        "id": "9723c720-4fa0-4497-a08d-8117aa343713",
        "monitorId": "4bd27b5a-b3a2-4716-a7bb-8f0bf9815849",
        "time": 1532602649608,
        "type": "CPU|CPU_TEMP|DRIVE|GPU|MEMORY|NETWORK_UP",
        "monitorStatus": "START|STOP",
        "threshold": 123,
        "value": 122
    }
]
```

The `"id"` in the response above can be used to delete the events associated with that ID. If you remove a _START_ event
with a particular ID, the _STOP_ event for that event will be discarded. 

## Running
Download the [latest release](https://github.com/Krillsson/sys-api/releases/latest).

- Windows: unzip the package somewhere convenient and then right click the `.bat` file and choose _Run as administrator_
- *nix: untar or unzip the package and run the `.sh` file from a terminal

A GUI in JavaFX is in the making

### Concerned about memory usage

Change the launcher script to this: `java -Xmx256m -Xms128m -jar system-api.jar server configuration.yml`

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
curl -i --user user:password -H "Accept: application/json" -X GET http://localhost:8080/v2/system
```

Package for distribution in a *.zip*, *tar.gz* and *tar.bz2*:

```sh
mvn clean package
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