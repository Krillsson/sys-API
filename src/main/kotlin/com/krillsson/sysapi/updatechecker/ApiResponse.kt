package com.krillsson.sysapi.updatechecker

class ApiResponse : ArrayList<ApiResponse.Release>() {
    data class Release(
        val body: String,
        val created_at: String,
        val id: Int,
        val name: String,
        val published_at: String,
        val tag_name: String,
        val url: String,
        val zipball_url: String
    )
}