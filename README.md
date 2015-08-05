# System Api
System Api is a REST-interface to your computers hardware. it uses [Sigar](https://github.com/hyperic/sigar) to gather information about your system and publishes that information in the JSON format with the help of [Dropwizard](https://github.com/dropwizard/dropwizard).
You can run this application on a bunch of different platforms. Take a look at [Sigars wiki](https://support.hyperic.com/display/SIGAR/Home) to get an idea if you can run this on your system.

Feel free to create issues or make pull requests.

# Usage
The project uses [Maven](https://maven.apache.org/) so assuming you have mvn and git on your path:

```sh
git clone [this repo] sys-api
```
```sh
mvn clean install
```

This will download the necessary Sigar native files and extract them under /target/lib

You should now be able to:

```sh
java -jar target/system-api.jar server dev.yml
```
## Configuration reference
The dev.yml file is a Dropwizard configuration file. Have a look at the [Dropwizard configuration reference](https://dropwizard.github.io/dropwizard/manual/configuration.html).

Apart from the standard Dropwizard configurations you need to specify what username and password to be used for the Basic Authentication like so:

    user:
        username: [change me]
        password: [change me]

The application will attempt to resolve the location of the Sigar native files by itself. So the native files need to be contained in a directory named lib in the same directory as the .jar executable. 
This has been tested on Mac OSX Yosemite and Windows 8.1. However, if you get *UnsatisfiedLinkError*'s you can override the location by specifying:

    sigarLocation: [absolute path to lib folder]

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

## Endpoints

    GET     /cpus (com.krillsson.sysapi.resources.CpuResource)
    GET     /cpus/{core} (com.krillsson.sysapi.resources.CpuResource)
    GET     /filesystems (com.krillsson.sysapi.resources.FilesystemResource)
    GET     /filesystems/type/{fsTypeName} (com.krillsson.sysapi.resources.FilesystemResource)
    GET     /filesystems/{id} (com.krillsson.sysapi.resources.FilesystemResource)
    GET     /memory (com.krillsson.sysapi.resources.MemoryResource)
    GET     /memory/ram (com.krillsson.sysapi.resources.MemoryResource)
    GET     /memory/swap (com.krillsson.sysapi.resources.MemoryResource)
    GET     /system (com.krillsson.sysapi.resources.SystemResource)
    GET     /networks (com.krillsson.sysapi.resources.NetworkResource)
    GET     /networks/{id} (com.krillsson.sysapi.resources.NetworkResource)
    GET     /networks/{id}/speed (com.krillsson.sysapi.resources.NetworkResource)
    GET     /processes (com.krillsson.sysapi.resources.ProcessResource)
    GET     /processes/statistics (com.krillsson.sysapi.resources.ProcessResource)
    GET     /processes/{pid} (com.krillsson.sysapi.resources.ProcessResource)


# Requirements
- [Java 8 JRE](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

# Credits
- [cb372 - metrics-sigar](https://github.com/cb372/metrics-sigar)
- [Sigar](https://github.com/hyperic/sigar)
- [Dropwizard](https://github.com/dropwizard/dropwizard)

All of the projects above are licensed under the [Apache License, Version 2](http://www.apache.org/licenses/LICENSE-2.0)