package com.krillsson.sysapi.tls

import retrofit2.Call
import retrofit2.http.GET

interface IfConfigMe {
    @GET("/ip")
    fun getMyIp(): Call<String>
}