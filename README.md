# System Api
System Api is a REST-interface to your computers hardware. it uses [OSHI](https://github.com/oshi/oshi) as well as [OpenHardwareMonitor](https://github.com/openhardwaremonitor/openhardwaremonitor) to gather information about your
system and publishes that information with the help of [Dropwizard](https://github.com/dropwizard/dropwizard).
You can run this application on every platform Oshi supports (Windows, Linux, Mac OS X & Unix (Solaris, FreeBSD))

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

You should now be able to:


```sh
cd server
java -jar target/system-api.jar server configuration.yml
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
The configuration.yml file is a Dropwizard configuration file. Have a look at the [Dropwizard configuration reference](https://dropwizard.github.io/dropwizard/manual/configuration.html).

Apart from the standard Dropwizard configurations you need to specify what username and password to be used for the Basic Authentication like so:

    user:
        username: [change me]
        password: [change me]
        
On Windows systems you can disable the OhmJniWrapper feature with

    windows:
      enableOhmJniWrapper: false

### Enable HTTPS

Take a look at this guide: [Dropwizard and SSL](http://clearthehaze.com/2014/09/dropwizard-ssl/)

And then you can use this configuration to forward HTTP requests to HTTPS:

    forwardHttps: true

## What can you do with it?
As of now you can query _sys-api_ for

- Overall cpu usage
- Cpu usage per core
- Available space on filesystems
- List all network interfaces
- Get available swap and ram
- List all current processes along with ram usage and cpu-time.

## Endpoints

    GET     /disks (com.krillsson.sysapi.resources.DiskStoresResource)
    GET     /gpus (com.krillsson.sysapi.resources.GpuResource)
    GET     /memory (com.krillsson.sysapi.resources.MemoryResource)
    GET     /meta/version (com.krillsson.sysapi.resources.MetaInfoResource)
    GET     /motherboard (com.krillsson.sysapi.resources.MotherboardResource)
    GET     /networkinterfaces (com.krillsson.sysapi.resources.NetworkInterfacesResource)
    GET     /powersources (com.krillsson.sysapi.resources.PowerSourcesResource)
    GET     /processes (com.krillsson.sysapi.resources.ProcessesResource)
    GET     /processor (com.krillsson.sysapi.resources.CpuResource)
    GET     /sensors (com.krillsson.sysapi.resources.SensorsResource)
    GET     /system (com.krillsson.sysapi.resources.SystemResource)
    GET     /system/jvm (com.krillsson.sysapi.resources.SystemResource)

# Prerequisites
- [Maven 3](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)
- [Java 8 JRE](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

# Credits
- [OSHI](https://github.com/oshi/oshi)
- [Dropwizard](https://github.com/dropwizard/dropwizard)

All of the projects above are licensed under the [Apache License, Version 2](http://www.apache.org/licenses/LICENSE-2.0)