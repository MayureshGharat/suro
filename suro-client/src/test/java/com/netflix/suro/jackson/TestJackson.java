package com.netflix.suro.jackson;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class TestJackson {
    @Test
    public void test() throws IOException {
       String spec = "{\"a\":\"aaa\", \"b\":\"bbb\"}";

        ObjectMapper mapper = new ObjectMapper();
        final Map<String, Object> injectables = Maps.newHashMap();

        injectables.put("test", "test");
        mapper.setInjectableValues(new InjectableValues() {
            @Override
            public Object findInjectableValue(
                    Object valueId, DeserializationContext ctxt, BeanProperty forProperty, Object beanInstance
            ) {
                return injectables.get(valueId);
            }
        });

        TestClass test = mapper.readValue(spec, new TypeReference<TestClass>(){});
        assertEquals(test.getTest(), "test");
    }

    public static class TestClass {
        @JacksonInject("test")
        private String test;

        private String a;
        private String b;

        @JsonCreator
        public TestClass(
                @JsonProperty("a") String a,
                @JsonProperty("b") @JacksonInject("b") String b) {
            this.a = a;
            this.b = b;
        }
        public String getTest() { return test; }
    }
}