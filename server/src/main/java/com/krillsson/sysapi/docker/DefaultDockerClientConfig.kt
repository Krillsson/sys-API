package com.krillsson.sysapi.docker

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.dockerjava.api.exception.DockerClientException
import com.github.dockerjava.api.model.AuthConfig
import com.github.dockerjava.api.model.AuthConfigurations
import com.github.dockerjava.core.*
import com.google.common.base.Preconditions
import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.Serializable
import java.net.URI
import java.util.*

class DefaultDockerClientConfig constructor(
    dockerHost: URI?,
    dockerConfigPath: String?,
    apiVersion: String?,
    registryUrl: String?,
    registryUsername: String?,
    registryPassword: String?,
    registryEmail: String?,
    sslConfig: SSLConfig?,
    objectMapper: ObjectMapper?
) : Serializable, DockerClientConfig {
    companion object {
        private const val serialVersionUID = 1L
        const val DOCKER_HOST = "DOCKER_HOST"
        const val DOCKER_TLS_VERIFY = "DOCKER_TLS_VERIFY"
        const val DOCKER_CONFIG = "DOCKER_CONFIG"
        const val DOCKER_CERT_PATH = "DOCKER_CERT_PATH"
        const val API_VERSION = "api.version"
        const val REGISTRY_USERNAME = "registry.username"
        const val REGISTRY_PASSWORD = "registry.password"
        const val REGISTRY_EMAIL = "registry.email"
        const val REGISTRY_URL = "registry.url"
        private const val DOCKER_JAVA_PROPERTIES = "docker-java.properties"
        private val CONFIG_KEYS: MutableSet<String> = HashSet()
        val DEFAULT_PROPERTIES = Properties()
        private fun loadIncludedDockerProperties(systemProperties: Properties): Properties {
            val p = Properties()
            p.putAll(DEFAULT_PROPERTIES)
            try {
                DefaultDockerClientConfig::class.java.getResourceAsStream("/$DOCKER_JAVA_PROPERTIES").use { `is` ->
                    if (`is` != null) {
                        p.load(`is`)
                    }
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
            replaceProperties(p, systemProperties)
            return p
        }

        private fun replaceProperties(properties: Properties, replacements: Properties) {
            for (objectKey in properties.keys) {
                val key = objectKey.toString()
                properties.setProperty(key, replaceProperties(properties.getProperty(key), replacements))
            }
        }

        private fun replaceProperties(s: String, replacements: Properties): String {
            var s = s
            for ((key1, value) in replacements) {
                val key = "\${$key1}"
                while (s.contains(key)) {
                    s = s.replace(key, value.toString())
                }
            }
            return s
        }

        /**
         * Creates a new Properties object containing values overridden from ${user.home}/.docker.io.properties
         *
         * @param p
         * The original set of properties to override
         * @return A copy of the original Properties with overridden values
         */
        private fun overrideDockerPropertiesWithSettingsFromUserHome(
            p: Properties,
            systemProperties: Properties
        ): Properties {
            val overriddenProperties = Properties()
            overriddenProperties.putAll(p)
            val usersDockerPropertiesFile = File(
                systemProperties.getProperty("user.home"),
                ".$DOCKER_JAVA_PROPERTIES"
            )
            if (usersDockerPropertiesFile.isFile) {
                try {
                    FileInputStream(usersDockerPropertiesFile).use { `in` -> overriddenProperties.load(`in`) }
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }
            return overriddenProperties
        }

        private fun overrideDockerPropertiesWithEnv(properties: Properties, env: Map<String, String>): Properties {
            val overriddenProperties = Properties()
            overriddenProperties.putAll(properties)

            // special case which is a sensible default
            if (env.containsKey(DOCKER_HOST)) {
                val value = env[DOCKER_HOST]
                if (value != null && value.trim { it <= ' ' }.isNotEmpty()) {
                    overriddenProperties.setProperty(DOCKER_HOST, value)
                }
            }
            for ((envKey, value) in env) {
                if (CONFIG_KEYS.contains(envKey)) {
                    if (value != null && value.trim { it <= ' ' }.isNotEmpty()) {
                        overriddenProperties.setProperty(envKey, value)
                    }
                }
            }
            return overriddenProperties
        }

        /**
         * Creates a new Properties object containing values overridden from the System properties
         *
         * @param p
         * The original set of properties to override
         * @return A copy of the original Properties with overridden values
         */
        private fun overrideDockerPropertiesWithSystemProperties(
            p: Properties,
            systemProperties: Properties
        ): Properties {
            val overriddenProperties = Properties()
            overriddenProperties.putAll(p)
            for (key in CONFIG_KEYS) {
                if (systemProperties.containsKey(key)) {
                    overriddenProperties.setProperty(key, systemProperties.getProperty(key))
                }
            }
            return overriddenProperties
        }

        fun createDefaultConfigBuilder(objectMapper: ObjectMapper): Builder {
            return createDefaultConfigBuilder(
                System.getenv(),
                System.getProperties().clone() as Properties,
                objectMapper
            )
        }

        /**
         * Allows you to build the config without system environment interfering for more robust testing
         */
        fun createDefaultConfigBuilder(
            env: Map<String, String>,
            systemProperties: Properties,
            objectMapper: ObjectMapper
        ): Builder {
            var properties = loadIncludedDockerProperties(systemProperties)
            properties = overrideDockerPropertiesWithSettingsFromUserHome(properties, systemProperties)
            properties = overrideDockerPropertiesWithEnv(properties, env)
            properties = overrideDockerPropertiesWithSystemProperties(properties, systemProperties)
            return Builder().withProperties(properties).withObjectMapper(objectMapper)
        }

        init {
            CONFIG_KEYS.add(DOCKER_HOST)
            CONFIG_KEYS.add(DOCKER_TLS_VERIFY)
            CONFIG_KEYS.add(DOCKER_CONFIG)
            CONFIG_KEYS.add(DOCKER_CERT_PATH)
            CONFIG_KEYS.add(API_VERSION)
            CONFIG_KEYS.add(REGISTRY_USERNAME)
            CONFIG_KEYS.add(REGISTRY_PASSWORD)
            CONFIG_KEYS.add(REGISTRY_EMAIL)
            CONFIG_KEYS.add(REGISTRY_URL)
            DEFAULT_PROPERTIES[DOCKER_HOST] = "unix:///var/run/docker.sock"
            DEFAULT_PROPERTIES[DOCKER_CONFIG] = "\${user.home}/.docker"
            DEFAULT_PROPERTIES[REGISTRY_URL] = "https://index.docker.io/v1/"
            DEFAULT_PROPERTIES[REGISTRY_USERNAME] = "\${user.name}"
        }
    }

    private val dockerHost: URI
    private val registryUsername: String?
    private val registryPassword: String?
    private val registryEmail: String?
    private val registryUrl: String?
    val dockerConfigPath: String?
    private val sslConfig: SSLConfig?
    private val apiVersion: RemoteApiVersion?
    private val realObjectMapper: ObjectMapper?

    var dockerConfig: DockerConfigFile? = null
        get() {
            if (field == null) {
                field = try {
                    DockerConfigFile.loadConfig(objectMapper, dockerConfigPath)
                } catch (e: IOException) {
                    throw DockerClientException("Failed to parse docker configuration file", e)
                }
            }
            return field
        }
        private set

    private fun checkDockerHostScheme(dockerHost: URI): URI {
        return when (dockerHost.scheme) {
            "tcp", "unix", "npipe" -> dockerHost
            else -> throw DockerClientException("Unsupported protocol scheme found: '$dockerHost")
        }
    }

    override fun getDockerHost(): URI {
        return dockerHost
    }

    override fun getApiVersion(): RemoteApiVersion? {
        return apiVersion
    }

    override fun getRegistryUsername(): String? {
        return registryUsername
    }

    override fun getRegistryPassword(): String? {
        return registryPassword
    }

    override fun getRegistryEmail(): String? {
        return registryEmail
    }

    override fun getRegistryUrl(): String? {
        return registryUrl
    }

    private val authConfig: AuthConfig?
        private get() {
            var authConfig: AuthConfig? = null
            if (getRegistryUsername() != null && getRegistryPassword() != null && getRegistryUrl() != null) {
                authConfig = AuthConfig()
                    .withUsername(getRegistryUsername())
                    .withPassword(getRegistryPassword())
                    .withEmail(getRegistryEmail())
                    .withRegistryAddress(getRegistryUrl())
            }
            return authConfig
        }

    override fun effectiveAuthConfig(imageName: String): AuthConfig {
        val authConfig = authConfig
        if (authConfig != null) {
            return authConfig
        }
        val dockerCfg = dockerConfig!!
        val reposTag = NameParser.parseRepositoryTag(imageName)
        val hostnameReposName = NameParser.resolveRepositoryName(reposTag.repos)
        return dockerCfg.resolveAuthConfig(hostnameReposName.hostname)!!
    }

    override fun getAuthConfigurations(): AuthConfigurations {
        return dockerConfig!!.authConfigurations
    }

    override fun getSSLConfig(): SSLConfig? {
        return sslConfig
    }

    override fun getObjectMapper(): ObjectMapper? {
        return realObjectMapper
    }

    override fun equals(o: Any?): Boolean {
        return EqualsBuilder.reflectionEquals(this, o)
    }

    override fun hashCode(): Int {
        return HashCodeBuilder.reflectionHashCode(this)
    }

    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE)
    }

    class Builder {
        private var dockerHost: URI? = null
        private var apiVersion: String? = null
        private var registryUsername: String? = null
        private var registryPassword: String? = null
        private var registryEmail: String? = null
        private var registryUrl: String? = null
        private var dockerConfig: String? = null
        private var dockerCertPath: String? = null
        private var dockerTlsVerify: Boolean? = null
        private var customSslConfig: SSLConfig? = null
        private var objectMapper: ObjectMapper? = null

        /**
         * This will set all fields in the builder to those contained in the Properties object. The Properties object should contain the
         * following docker-java configuration keys: DOCKER_HOST, DOCKER_TLS_VERIFY, api.version, registry.username, registry.password,
         * registry.email, DOCKER_CERT_PATH, and DOCKER_CONFIG.
         */
        fun withProperties(p: Properties): Builder {
            return withDockerHost(p.getProperty(DOCKER_HOST))
                .withDockerTlsVerify(p.getProperty(DOCKER_TLS_VERIFY))
                .withDockerConfig(p.getProperty(DOCKER_CONFIG))
                .withDockerCertPath(p.getProperty(DOCKER_CERT_PATH))
                .withApiVersion(p.getProperty(API_VERSION))
                .withRegistryUsername(p.getProperty(REGISTRY_USERNAME))
                .withRegistryPassword(p.getProperty(REGISTRY_PASSWORD))
                .withRegistryEmail(p.getProperty(REGISTRY_EMAIL))
                .withRegistryUrl(p.getProperty(REGISTRY_URL))
        }

        /**
         * configure DOCKER_HOST
         */
        fun withDockerHost(dockerHost: String): Builder {
            Preconditions.checkNotNull(dockerHost, "uri was not specified")
            this.dockerHost = URI.create(dockerHost)
            return this
        }

        fun withApiVersion(apiVersion: RemoteApiVersion): Builder {
            this.apiVersion = apiVersion.version
            return this
        }

        fun withApiVersion(apiVersion: String?): Builder {
            this.apiVersion = apiVersion
            return this
        }

        fun withRegistryUsername(registryUsername: String?): Builder {
            this.registryUsername = registryUsername
            return this
        }

        fun withRegistryPassword(registryPassword: String?): Builder {
            this.registryPassword = registryPassword
            return this
        }

        fun withRegistryEmail(registryEmail: String?): Builder {
            this.registryEmail = registryEmail
            return this
        }

        fun withRegistryUrl(registryUrl: String?): Builder {
            this.registryUrl = registryUrl
            return this
        }

        fun withDockerCertPath(dockerCertPath: String?): Builder {
            this.dockerCertPath = dockerCertPath
            return this
        }

        fun withDockerConfig(dockerConfig: String?): Builder {
            this.dockerConfig = dockerConfig
            return this
        }

        fun withObjectMapper(objectMapper: ObjectMapper?): Builder {
            this.objectMapper = objectMapper
            return this
        }


        fun withDockerTlsVerify(dockerTlsVerify: String?): Builder {
            if (dockerTlsVerify != null) {
                val trimmed = dockerTlsVerify.trim { it <= ' ' }
                this.dockerTlsVerify = "true".equals(trimmed, ignoreCase = true) || "1" == trimmed
            } else {
                this.dockerTlsVerify = false
            }
            return this
        }

        fun withDockerTlsVerify(dockerTlsVerify: Boolean?): Builder {
            this.dockerTlsVerify = dockerTlsVerify
            return this
        }

        /**
         * Overrides the default [SSLConfig] that is used when calling [Builder.withDockerTlsVerify] and
         * [Builder.withDockerCertPath]. This way it is possible to pass a custom [SSLConfig] to the resulting
         * [DockerClientConfig] that may be created by other means than the local file system.
         */
        fun withCustomSslConfig(customSslConfig: SSLConfig?): Builder {
            this.customSslConfig = customSslConfig
            return this
        }

        fun build(): DefaultDockerClientConfig {
            var sslConfig: SSLConfig? = null
            if (customSslConfig == null) {
                if (BooleanUtils.isTrue(dockerTlsVerify)) {
                    dockerCertPath = checkDockerCertPath(dockerCertPath)
                    sslConfig = LocalDirectorySSLConfig(dockerCertPath)
                }
            } else {
                sslConfig = customSslConfig
            }
            return DefaultDockerClientConfig(
                dockerHost,
                dockerConfig,
                apiVersion,
                registryUrl,
                registryUsername,
                registryPassword,
                registryEmail,
                sslConfig,
                objectMapper
            )
        }

        private fun checkDockerCertPath(dockerCertPath: String?): String? {
            if (StringUtils.isEmpty(dockerCertPath)) {
                throw DockerClientException(
                    "Enabled TLS verification (DOCKER_TLS_VERIFY=1) but certifate path (DOCKER_CERT_PATH) is not defined."
                )
            }
            val certPath = File(dockerCertPath)
            if (!certPath.exists()) {
                throw DockerClientException(
                    "Enabled TLS verification (DOCKER_TLS_VERIFY=1) but certificate path (DOCKER_CERT_PATH) '"
                            + dockerCertPath + "' doesn't exist."
                )
            } else if (!certPath.isDirectory) {
                throw DockerClientException(
                    "Enabled TLS verification (DOCKER_TLS_VERIFY=1) but certificate path (DOCKER_CERT_PATH) '"
                            + dockerCertPath + "' doesn't point to a directory."
                )
            }
            return dockerCertPath
        }
    }

    init {
        this.dockerHost = checkDockerHostScheme(dockerHost!!)
        this.dockerConfigPath = dockerConfigPath
        this.apiVersion = RemoteApiVersion.parseConfigWithDefault(apiVersion)
        this.sslConfig = sslConfig
        this.registryUsername = registryUsername
        this.registryPassword = registryPassword
        this.registryEmail = registryEmail
        this.registryUrl = registryUrl
        this.realObjectMapper = objectMapper
    }
}