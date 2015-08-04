# System Api
System Api is a REST-interface to your computers hardware. it uses [Sigar](https://github.com/hyperic/sigar) to gather information about your system and publishes that information in the JSON format with the help of [Dropwizard](https://github.com/dropwizard/dropwizard)

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

# Endpoints

    GET     /cpus (com.krillsson.sysapi.resources.CpuResource)
    GET     /cpus/cpu/{core} (com.krillsson.sysapi.resources.CpuResource)
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