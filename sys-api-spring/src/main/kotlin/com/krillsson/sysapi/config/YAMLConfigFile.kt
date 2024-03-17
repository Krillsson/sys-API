/*
 * Sys-Api (https://github.com/Krillsson/sys-api)
 *
 * Copyright 2017 Christian Jensen / Krillsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Maintainers:
 * contact[at]christian-jensen[dot]se
 */
package com.krillsson.sysapi.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@ConfigurationProperties
@PropertySource(value = ["classpath:config/configuration.yml"], factory = YamlPropertySourceFactory::class)
class YAMLConfigFile{
    var authentication: UserConfiguration = UserConfiguration("user", "password")
    var metricsConfig: MetricsConfiguration = MetricsConfiguration()
    var windows: WindowsConfiguration = WindowsConfiguration()
    var processes: ProcessesConfiguration = ProcessesConfiguration()
    var linux: LinuxConfiguration = LinuxConfiguration()
    var connectivityCheck: ConnectivityCheckConfiguration = ConnectivityCheckConfiguration(true, "https://ifconfig.me")
    var updateCheck: UpdateCheckConfiguration = UpdateCheckConfiguration()
    var docker: DockerConfiguration = DockerConfiguration()
    var logReader: LogReaderConfiguration = LogReaderConfiguration()
    var selfSignedCertificates: SelfSignedCertificateConfiguration = SelfSignedCertificateConfiguration(
        true,
        true,
        true
    )
    var mDNS: MdnsConfiguration = MdnsConfiguration(false)
    var upnp: UpnpIgdConfiguration = UpnpIgdConfiguration(false)
}