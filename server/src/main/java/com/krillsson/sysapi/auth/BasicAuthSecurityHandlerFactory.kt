package com.krillsson.sysapi.auth

import org.eclipse.jetty.security.ConstraintMapping
import org.eclipse.jetty.security.ConstraintSecurityHandler
import org.eclipse.jetty.security.HashLoginService
import org.eclipse.jetty.security.SecurityHandler
import org.eclipse.jetty.security.UserStore
import org.eclipse.jetty.util.security.Constraint
import org.eclipse.jetty.util.security.Credential

class BasicAuthSecurityHandlerFactory(private val username: String, private val password: String) {
    fun create() : SecurityHandler {
        val loginService = HashLoginService()
        val userStore = UserStore()
        userStore.addUser(username, Credential.getCredential(password), arrayOf("AUTHENTICATED"))
        loginService.setUserStore(userStore)
        loginService.name = "system-API"
        val constraint = Constraint()
        constraint.name = Constraint.__BASIC_AUTH
        constraint.roles = arrayOf("AUTHENTICATED")
        constraint.authenticate = true
        val cm = ConstraintMapping()
        cm.constraint = constraint
        cm.pathSpec = "/*"
        val csh = ConstraintSecurityHandler()
        csh.authenticator = org.eclipse.jetty.security.authentication.BasicAuthenticator()
        csh.realmName = "system-API"
        csh.addConstraintMapping(cm)
        csh.loginService = loginService
        return csh
    }
}