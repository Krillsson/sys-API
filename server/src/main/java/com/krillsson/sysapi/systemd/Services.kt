package com.krillsson.sysapi.systemd

class Services : ArrayList<Services.ServicesItem>(){
    data class ServicesItem(
        val active: String,
        val description: String,
        val load: String,
        val sub: String,
        val unit: String
    )
}