package com.krillsson.sysapi.util

import java.io.File


object FileSystem {
    val data = File("data")

//    /**
//     * Gets the base location of the given class.
//     *
//     *
//     * If the class is directly on the file system (e.g.,
//     * "/path/to/my/package/MyClass.class") then it will return the base directory
//     * (e.g., "file:/path/to").
//     *
//     *
//     *
//     * If the class is within a JAR file (e.g.,
//     * "/path/to/my-jar.jar!/my/package/MyClass.class") then it will return the
//     * path to the JAR (e.g., "file:/path/to/my-jar.jar").
//     *
//     *
//     * @param c The class whose location is desired.
//     * @see FileUtils.urlToFile
//     */
//    fun getLocation(c: Class<*>?): URL? {
//        if (c == null) return null // could not load the class
//
//        // try the easy way first
//        try {
//            val codeSourceLocation = c.protectionDomain.codeSource.location
//            if (codeSourceLocation != null) return codeSourceLocation
//        } catch (e: SecurityException) {
//            // NB: Cannot access protection domain.
//        } catch (e: NullPointerException) {
//            // NB: Protection domain or code source is null.
//        }
//
//        // NB: The easy way failed, so we try the hard way. We ask for the class
//        // itself as a resource, then strip the class's path from the URL string,
//        // leaving the base path.
//
//        // get the class's raw resource path
//        val classResource = c.getResource(c.simpleName + ".class") ?: return null
//        // cannot find class resource
//        val url = classResource.toString()
//        val suffix = c.canonicalName.replace('.', '/') + ".class"
//        if (!url.endsWith(suffix)) return null // weird URL
//
//        // strip the class's path from the URL string
//        val base = url.substring(0, url.length - suffix.length)
//        var path = base
//
//        // remove the "jar:" prefix and "!/" suffix, if present
//        if (path.startsWith("jar:")) path = path.substring(4, path.length - 2)
//        return try {
//            URL(path)
//        } catch (e: MalformedURLException) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//    /**
//     * Converts the given [URL] to its corresponding [File].
//     *
//     *
//     * This method is similar to calling `new File(url.toURI())` except that
//     * it also handles "jar:file:" URLs, returning the path to the JAR file.
//     *
//     *
//     * @param url The URL to convert.
//     * @return A file path suitable for use with e.g. [FileInputStream]
//     * @throws IllegalArgumentException if the URL does not correspond to a file.
//     */
//    fun urlToFile(url: URL?): File? {
//        return if (url == null) null else urlToFile(url.toString())
//    }
//
//    /**
//     * Converts the given URL string to its corresponding [File].
//     *
//     * @param url The URL to convert.
//     * @return A file path suitable for use with e.g. [FileInputStream]
//     * @throws IllegalArgumentException if the URL does not correspond to a file.
//     */
//    fun urlToFile(url: String): File? {
//        var path = url
//        if (path.startsWith("jar:")) {
//            // remove "jar:" prefix and "!/" suffix
//            val index = path.indexOf("!/")
//            path = path.substring(4, index)
//        }
//        try {
//            if (PlatformUtils.isWindows() && path.matches("file:[A-Za-z]:.*")) {
//                path = "file:/" + path.substring(5)
//            }
//            return File(URL(path).toURI())
//        } catch (e: MalformedURLException) {
//            // NB: URL is not completely well-formed.
//        } catch (e: URISyntaxException) {
//            // NB: URL is not completely well-formed.
//        }
//        if (path.startsWith("file:")) {
//            // pass through the URL as-is, minus "file:" prefix
//            path = path.substring(5)
//            return File(path)
//        }
//        throw IllegalArgumentException("Invalid URL: $url")
//    }

}