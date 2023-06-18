package com.krillsson.sysapi.systemd

class SystemCtlServicesOutput : ArrayList<SystemCtlServicesOutput.Item>(){
    data class Item(
        val active: String,
        val description: String,
        val load: String,
        val sub: String,
        val unit: String
    )
}