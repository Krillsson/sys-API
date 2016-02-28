# System Api
System Api is a REST-interface to your computers hardware. it uses [Sigar](https://github.com/hyperic/sigar) as well as [OpenHardwareMonitor](https://github.com/openhardwaremonitor/openhardwaremonitor) to gather information about your
system and publishes that information with the help of [Dropwizard](https://github.com/dropwizard/dropwizard).
You can run this application on a bunch of different platforms. Take a look at [Sigars wiki](https://support.hyperic.com/display/SIGAR/Home) to get an idea if you can run this on your system.

Feel free to create issues or make pull requests.

# Usage

## Running
Download the [latest release](https://github.com/Krillsson/sys-api/releases/latest).

- Windows: unzip the package somewhere convenient and then right click the `.bat` file and choose _Run as administrator_
- *nix: untar or unzip the package and run the `.sh` file from a terminal

## Development
The project uses [Maven](https://maven.apache.org/) so assuming you have mvn and git on your path:

```sh
git clone [this repo] sys-api
```
```sh
mvn clean install
```

This will download the necessary Sigar native files and extract them under server/target/lib

You should now be able to:


```sh
cd server
java -jar target/system-api.jar server dev.yml
```
If you got everything working you should now be able to:

```sh
curl -i --user user:password -H "Accept: application/json" -X GET http://localhost:8080/v1/system
```

If you want to package everything needed to run this application in a *.zip*, *tar.gz* and *tar.bz2*:

```sh
mvn clean package
```

And the resulting files should be located in */server/target/*

## Configuration reference
The dev.yml file is a Dropwizard configuration file. Have a look at the [Dropwizard configuration reference](https://dropwizard.github.io/dropwizard/manual/configuration.html).

Apart from the standard Dropwizard configurations you need to specify what username and password to be used for the Basic Authentication like so:

    user:
        username: [change me]
        password: [change me]

The application will attempt to resolve the location of the Sigar native files by itself. So the native files need to be contained in a directory named lib in the same directory as the .jar executable. 
This has been tested on Mac OSX Yosemite and Windows 8.1. However, if you get *UnsatisfiedLinkError*'s you can override the location by specifying:

    sigarLocation: [absolute path to lib folder]

### But I want HTTPS?

Take a look at this guide: [Dropwizard and SSL](http://clearthehaze.com/2014/09/dropwizard-ssl/)

And then you can use this configuration to forward HTTP requests to HTTPS:

    forwardHttps: true

## What can you do with it?
As of now you can query _sys-api_ for

- Overall cpu usage
- Cpu usage per core
- Available space on filesystems
- List all network interfaces
- Get the current speed of a network (a bit hacky)
- Get available swap and ram
- General information about the system, like users, uptime and hostname
- List all current processes along with ram usage, cpu-time and arguments.

Additionally, on a Windows machine you can do the following:

- Read temperatures from the CPU, GPU as well as the motherboard
- Fan usage and RPM on the CPU, GPU and motherboard
- Disk read/write rates

### Warning
Due to the fact that OpenHardwareMonitor extend the functionality of the REST api, some elements in the JSON will be empty or contain zero/-1 values other systems. For instance the temperature will always be 0 or the DriveLoad object will always be empty. Code accordingly.

## Endpoints

    GET     /cpu
    GET     /cpu/{core}
    GET     /drives
    GET     /drives/type/{fsTypeName}
    GET     /drives/{id}
    GET     /memory
    GET     /memory/ram
    GET     /memory/swap
    GET     /networks
    GET     /networks/{id}
    GET     /networks/{id}/speed
    GET     /processes
    GET     /processes/statistics
    GET     /processes/{pid}
    GET     /system
    GET     /system/jvm
    GET     /system/operatingsystem
    GET     /system/uptime

## Windows only:

    GET     /motherboard
    GET     /gpus

# Prerequisites
- [Maven 3](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)
- [Java 8 JRE](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

# Credits
- [cb372 - metrics-sigar](https://github.com/cb372/metrics-sigar)
- [Sigar](https://github.com/hyperic/sigar)
- [Dropwizard](https://github.com/dropwizard/dropwizard)

All of the projects above are licensed under the [Apache License, Version 2](http://www.apache.org/licenses/LICENSE-2.0)