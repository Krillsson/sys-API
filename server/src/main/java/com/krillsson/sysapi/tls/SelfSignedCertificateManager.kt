package com.krillsson.sysapi.tls

import com.google.common.net.HostSpecifier
import com.google.common.net.InetAddresses
import com.krillsson.sysapi.util.logger
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.BasicConstraints
import org.bouncycastle.asn1.x509.Extension
import org.bouncycastle.asn1.x509.GeneralName
import org.bouncycastle.asn1.x509.GeneralNames
import org.bouncycastle.cert.X509v3CertificateBuilder
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.jce.X509KeyUsage
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openssl.jcajce.JcaMiscPEMGenerator
import org.bouncycastle.operator.ContentSigner
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.bouncycastle.util.io.pem.PemObjectGenerator
import org.bouncycastle.util.io.pem.PemWriter
import java.io.*
import java.math.BigInteger
import java.security.*
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*


/**
 *
 * @author Kai Kreuzer - Initial contribution
 */
class SelfSignedCertificateManager {

    companion object {
        private val logger by logger()
        private const val JETTY_KEYSTORE_PATH_PROPERTY = "keystorewww.jks"
        private const val KEYSTORE_PASSWORD = "sys-api"
        private const val KEYSTORE_ENTRY_ALIAS = "sys-api-key"
        private const val KEYSTORE_CA_ENTRY_ALIAS = "sys-api-ca-key"
        private const val KEYSTORE_JKS_TYPE = "JKS"
        private const val SHA256_RSA = "SHA256WithRSAEncryption"
    }

    private val provider = BouncyCastleProvider()
    private val random = SecureRandom()

    private data class Certificate(
        val certificate: X509Certificate,
        val keypair: KeyPair
    )

    private data class CertificateKeyStore(val store: KeyStore, val file: File)


    @Throws(Exception::class)
    fun start(names: CertificateNamesCreator.Names) {
        try {
            val certificateKeyStore = ensureKeystore()
            if (!isCertificateInKeystore(certificateKeyStore.store)) {
                logger.debug("{} alias not found. Generating a new certificate.", KEYSTORE_ENTRY_ALIAS)
                val caCertificate = generateX509CACertificate()
                val serverCertificate =
                    generateX509ServerCertificate(caCertificate, names.commonName, names.subjectAlternativeNames)
                certificateKeyStore.store.saveCertificate(caCertificate, KEYSTORE_CA_ENTRY_ALIAS)
                certificateKeyStore.store.saveCertificate(serverCertificate, KEYSTORE_ENTRY_ALIAS)
                certificateKeyStore.store.writeToFile(certificateKeyStore.file, KEYSTORE_PASSWORD)
                writeCertificateToPemFile("self_signed_ca.crt", caCertificate.certificate)
                writeCertificateToPemFile("self_signed_server.crt", serverCertificate.certificate)
            } else {
                logger.debug("{} alias found. Use it for SSL", KEYSTORE_ENTRY_ALIAS)
                readCertificateFromStore(certificateKeyStore.store, KEYSTORE_CA_ENTRY_ALIAS)
            }
        } catch (e: CertificateException) {
            logger.error("Failed to generate a new SSL Certificate.", e)
        } catch (e: KeyStoreException) {
            logger.error("Failed to generate a new SSL Certificate.", e)
        }
    }

    private fun readCertificateFromStore(keystore: KeyStore, alias: String): X509Certificate =
        keystore.getCertificate(alias) as X509Certificate

    @Throws(Exception::class)
    fun stop() {
        // Nothing to do.
    }

    /**
     * Ensure that the keystore exist and is readable. If not, create a new one.
     *
     * @throws KeyStoreException if the creation of the keystore fails or if it is not readable.
     */
    @Throws(KeyStoreException::class)
    private fun ensureKeystore(): CertificateKeyStore {
        val keystoreFile = File(JETTY_KEYSTORE_PATH_PROPERTY)
        val keyStore = KeyStore.getInstance(KEYSTORE_JKS_TYPE)
        if (!keystoreFile.exists()) {
            try {
                logger.debug("No keystore found. Creation of {}", keystoreFile.absolutePath)
                val newFileCreated = keystoreFile.createNewFile()
                if (newFileCreated) {
                    keyStore.load(null, null)
                } else {
                    throw IOException("Keystore file creation failed.")
                }
            } catch (e: IOException) {
                throw KeyStoreException("Failed to create the keystore " + keystoreFile.absolutePath, e)
            } catch (e: NoSuchAlgorithmException) {
                throw KeyStoreException("Failed to create the keystore " + keystoreFile.absolutePath, e)
            } catch (e: CertificateException) {
                throw KeyStoreException("Failed to create the keystore " + keystoreFile.absolutePath, e)
            }
        } else {
            try {
                FileInputStream(keystoreFile).use { keystoreStream ->
                    logger.debug("Keystore found. Trying to load {}", keystoreFile.absolutePath)
                    keyStore.load(keystoreStream, KEYSTORE_PASSWORD.toCharArray())
                }
            } catch (e: NoSuchAlgorithmException) {
                throw KeyStoreException("Failed to load the keystore " + keystoreFile.absolutePath, e)
            } catch (e: CertificateException) {
                throw KeyStoreException("Failed to load the keystore " + keystoreFile.absolutePath, e)
            } catch (e: IOException) {
                throw KeyStoreException("Failed to load the keystore " + keystoreFile.absolutePath, e)
            }
        }
        return CertificateKeyStore(keyStore, keystoreFile)
    }

    private fun isCertificateInKeystore(keystore: KeyStore): Boolean {
        return keystore.getCertificate(KEYSTORE_ENTRY_ALIAS) != null &&
                keystore.getCertificate(KEYSTORE_CA_ENTRY_ALIAS) != null
    }

    private fun KeyStore.saveCertificate(certificate: Certificate, alias: String) {
        setKeyEntry(
            alias,
            certificate.keypair.private,
            KEYSTORE_PASSWORD.toCharArray(),
            arrayOf(certificate.certificate)
        )
    }

    private fun KeyStore.writeToFile(file: File, password: String) =
        store(FileOutputStream(file), password.toCharArray())

    private fun writeCertificateToPemFile(fileName: String, certificate: X509Certificate) {
        val file = File("./$fileName")
        if (file.exists()) {
            logger.info("$fileName already exists. Delete it if you want to re-write it.")
            return
        }
        file.createNewFile()
        file.takeIf { it.canRead() && it.canWrite() }?.let {
            logger.info("Writing $fileName")
            PemWriter(FileWriter(it)).use { pw ->
                val gen: PemObjectGenerator = JcaMiscPEMGenerator(certificate)
                pw.writeObject(gen)
            }
        }
    }

    private fun generateX509CACertificate(): Certificate {
        val now = LocalDateTime.now().atZone(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS)

        val notBefore = Date(now.minusDays(30).toEpochSecond())
        val notAfter = Date(now.plusYears(3).toEpochSecond())

        val caKeyPair = generateKeypair()
        val owner = X500Name("CN=sys-API-CA, O=sys-api, C=SE")
        val builder: X509v3CertificateBuilder = JcaX509v3CertificateBuilder(
            owner,
            BigInteger(64, random),
            notBefore,
            notAfter,
            owner,
            caKeyPair.public
        )
            .addExtension(Extension.basicConstraints, false, BasicConstraints(true))

        val signer: ContentSigner = JcaContentSignerBuilder(SHA256_RSA).build(caKeyPair.private)
        val certHolder = builder.build(signer)
        val cert: X509Certificate = JcaX509CertificateConverter().setProvider(provider).getCertificate(certHolder)

        return Certificate(
            cert,
            caKeyPair
        )
    }

    private fun generateX509ServerCertificate(
        caCertificate: Certificate,
        commonName: String,
        subjectAlternativeNames: List<String>
    ): Certificate {
        val now = LocalDateTime.now().atZone(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS)

        val notBefore = Date(now.minusDays(30).toInstant().toEpochMilli())
        val notAfter = Date(now.plusYears(4).toInstant().toEpochMilli())

        val keyPair = generateKeypair()
        val owner = X500Name("CN=$commonName, O=sys-api, C=SE")
        val keyUsage = X509KeyUsage(
            X509KeyUsage.digitalSignature
                    or X509KeyUsage.dataEncipherment or X509KeyUsage.keyAgreement or X509KeyUsage.keyEncipherment
        )
        val builder: X509v3CertificateBuilder = JcaX509v3CertificateBuilder(
            caCertificate.certificate,
            BigInteger(64, random),
            notBefore,
            notAfter,
            owner,
            keyPair.public
        )
            .addExtension(Extension.keyUsage, false, keyUsage)
            .apply {
                val names: Array<GeneralName> = subjectAlternativeNames.mapNotNull { name ->
                    if (InetAddresses.isInetAddress(name)) {
                        GeneralName(GeneralName.iPAddress, name)
                    } else if (HostSpecifier.isValid(name)) {
                        GeneralName(GeneralName.dNSName, name)
                    } else {
                        logger.warn("$name is neither a valid URL nor a valid IP")
                        null
                    }
                }.toTypedArray()
                if (names.isNotEmpty()) {
                    addExtension(
                        Extension.subjectAlternativeName,
                        false,
                        GeneralNames(names)
                    )
                }
            }

        val signer: ContentSigner = JcaContentSignerBuilder(SHA256_RSA).build(caCertificate.keypair.private)
        val certHolder = builder.build(signer)
        val cert: X509Certificate = JcaX509CertificateConverter().setProvider(provider).getCertificate(certHolder)
        return Certificate(
            cert,
            keyPair
        )
    }

    private fun generateKeypair(): KeyPair {
        val random = SecureRandom()
        return try {
            val keyGen = KeyPairGenerator.getInstance("RSA")
            keyGen.initialize(2048, random)
            keyGen.generateKeyPair()
        } catch (e: NoSuchAlgorithmException) {
            // Should not reach here because every Java implementation must have RSA key pair generator.
            throw Error(e)
        }
    }
}