/*
 * Sys-Api (https://github.com/Krillsson/sys-api)
 *
 * Copyright 2017 Christian Jensen / Krillsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Maintainers:
 * contact[at]christian-jensen[dot]se
 */
package com.krillsson.sysapi.rest

import com.krillsson.sysapi.auth.BasicAuthorizer
import com.krillsson.sysapi.config.UserConfiguration
import com.krillsson.sysapi.core.domain.filesystem.FileSystem
import com.krillsson.sysapi.core.domain.filesystem.FileSystemLoad
import com.krillsson.sysapi.core.domain.history.HistoryEntry
import com.krillsson.sysapi.core.history.LegacyHistoryManager
import com.krillsson.sysapi.core.metrics.FileSystemMetrics
import io.dropwizard.auth.Auth
import java.time.OffsetDateTime
import javax.annotation.security.RolesAllowed
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("filesystems")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
class FileSystemsResource(
    private val provider: FileSystemMetrics,
    private val historyManager: LegacyHistoryManager
) {
    @GET
    fun getRoot(): List<FileSystem> {
        return provider.fileSystems()
    }

    @GET
    @Path("{name}")
    fun getFileSystemByName(@PathParam("name") name: String): FileSystem {
        return provider.fileSystemByName(name) ?: throw WebApplicationException(
            String.format(
                "No fileSystem with name %s was found.",
                name
            ), Response.Status.NOT_FOUND
        )
    }

    @GET
    @Path("loads/{name}")
    fun getFileSystemLoadByName(@PathParam("name") name: String): FileSystemLoad {
        return provider.fileSystemLoadByName(name) ?: throw WebApplicationException(
            String.format(
                "No fileSystem with name %s was found.",
                name
            ), Response.Status.NOT_FOUND
        )
    }

    @GET
    @Path("loads")
    fun getLoads(@Auth user: UserConfiguration?): List<FileSystemLoad> {
        return provider.fileSystemLoads()
    }

    @GET
    @Path("loads/history")
    fun getLoadHistory(
        @Auth user: UserConfiguration?,
        @QueryParam("fromDate") fromDate: OffsetDateTime?,
        @QueryParam("toDate") toDate: OffsetDateTime?
    ): List<HistoryEntry<List<FileSystemLoad>>> {
        return historyManager.fileSystemLoadHistory(fromDate, toDate)
    }
}