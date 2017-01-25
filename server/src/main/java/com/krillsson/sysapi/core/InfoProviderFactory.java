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
package com.krillsson.sysapi.core;

import com.krillsson.sysapi.config.SystemApiConfiguration;
import com.krillsson.sysapi.core.windows.WindowsInfoProvider;
import oshi.PlatformEnum;

public class InfoProviderFactory {
    private final PlatformEnum os;
    private final SystemApiConfiguration configuration;

    public InfoProviderFactory(PlatformEnum os, SystemApiConfiguration configuration) {
        this.os = os;
        this.configuration = configuration;
    }

    public InfoProvider provide() {
        switch (os) {
            case WINDOWS:
                if (configuration.windows() == null || configuration.windows().enableOhmJniWrapper()) {
                    WindowsInfoProvider windowsInfoProvider = new WindowsInfoProvider();
                    if (windowsInfoProvider.canProvide()) {
                        return windowsInfoProvider;
                    }
                }
            case LINUX:
            case MACOSX:
                //https://github.com/Chris911/iStats
            case FREEBSD:
            case SOLARIS:
            case UNKNOWN:
            default:
                return new DefaultInfoProvider();

        }
    }

}