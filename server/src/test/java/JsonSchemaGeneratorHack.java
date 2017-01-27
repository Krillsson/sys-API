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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceMixin;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.NetworkInterface;
import java.util.HashSet;
import java.util.Set;

/*import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;*/

public class JsonSchemaGeneratorHack {

    @Test
    @Ignore
    public void name() throws Exception {

        Set<Class<?>> classes = listClassesInPackage("com.krillsson.sysapi.core.domain.motherboard");
        for (Class<?> aClass : classes) {
            String jsonSchemaForClass = getJsonSchemaForClass(aClass);
            try (PrintWriter out = new PrintWriter(aClass.getSimpleName(), "UTF-8")) {
                System.out.println("Writing " + jsonSchemaForClass);
                out.write(jsonSchemaForClass);
            }
        }
    }

    private Set<Class<?>> listClassesInPackage(String s) {
        return new HashSet<>();
    }

    private String getJsonSchemaForClass(Class clazz) throws IOException {
        System.out.println("Get schema for " + clazz.getSimpleName());
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixInAnnotations(NetworkInterface.class, NetworkInterfaceMixin.class);
        FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("networkInterface filter", SimpleBeanPropertyFilter.serializeAllExcept("name", "displayName", "inetAddresses", "interfaceAddresses", "mtu", "subInterfaces"));
        mapper.setFilters(filterProvider);
        //There are other configuration options you can set.  This is the one I needed.

        JsonSchema schema = mapper.generateJsonSchema(clazz);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema);
    }

    /*private String getJsonSchemaForClass(Class klazz){
        ObjectMapper mapper = new ObjectMapper();
        JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);
        try {
            JsonSchema schema = schemaGen.generateSchema(klazz);
            return schema.get$schema();
        } catch (JsonMappingException e) {

        }
        return null;
    }

    private Set<Class<?>> listClassesInPackage(String packageName){
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName))));
        return reflections.getSubTypesOf(Object.class);
    }*/
}
