package com.krillsson.sysapi.updatechecker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApiService {
    @GET("repos/{user}/{repo}/releases")
    fun getReleases(@Path("user") user: String, @Path("repo") repo: String): Call<ApiResponse>
}