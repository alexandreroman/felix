/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.felix.converter.impl;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.service.converter.Adapter;
import org.osgi.service.converter.Converter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ConverterTest {
    private Converter converter;

    @Before
    public void setUp() {
        converter = new ConverterImpl();
    }

    @After
    public void tearDown() {
        converter = null;
    }

    @Test
    public void testSimpleConversions() {
        assertEquals(Integer.valueOf(123), converter.convert("123").to(Integer.class));
        //assertEquals(Integer.valueOf(123), c.convert("123").to(int.class));
        assertEquals(Long.valueOf(123), converter.convert("123").to(Long.class));
//        assertEquals(Character.valueOf(123), c.convert("123").to(Character.class));
        assertEquals(Byte.valueOf((byte) 123), converter.convert("123").to(Byte.class));
        assertEquals(Float.valueOf("12.3"), converter.convert("12.3").to(Float.class));
        assertEquals(Double.valueOf("12.3"), converter.convert("12.3").to(Double.class));
        assertEquals("123", converter.convert(123).to(String.class));
    }

    @Test
    public void testStandardStringArrayConversion() {
        String[] sa = {"A", "B"};
        assertEquals("A", converter.convert(sa).toString());
        assertEquals("A", converter.convert(sa).to(String.class));

        String[] sa2 = {"A"};
        assertArrayEquals(sa2, converter.convert("A").to(String[].class));
    }

    @Test
    public void testCustomStringArrayConverstion() {
        Adapter adapter = converter.getAdapter();
        adapter.rule(String[].class, String.class,
                v -> Stream.of(v).collect(Collectors.joining(",")),
                v -> v.split(","));

        String[] sa = {"A", "B"};
        assertEquals("A,B", adapter.convert(sa).to(String.class));
        assertArrayEquals(sa, adapter.convert("A,B").to(String[].class));
    }
}
