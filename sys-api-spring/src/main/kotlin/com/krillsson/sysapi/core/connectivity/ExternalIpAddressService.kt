package com.krillsson.sysapi.core.connectivity

import retrofit2.Call
import retrofit2.http.GET

interface ExternalIpAddressService {
    @GET("/ip")
    fun getMyIp(): Call<String>
}