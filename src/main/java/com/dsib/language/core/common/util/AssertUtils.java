package com.dsib.language.core.common.util;

public class AssertUtils {

    public static void assertNotNull(String name, Object value) {
        assertNotNull("Can't be null: ", name, value);
    }

    public static void assertNotNull(String message, String name, Object value) {
        if (value == null) {
            throw new IllegalStateException(message + name);
        }
    }
}
