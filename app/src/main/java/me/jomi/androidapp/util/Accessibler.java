package me.jomi.androidapp.util;

import java.lang.reflect.Field;

public class Accessibler {
    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
    public static <T> T get(Object obj, String fieldName) {
        return get(obj.getClass(), obj, fieldName);
    }
    public static <T> T getStatic(Class<?> clazz, String fieldName) {
        return get(clazz, null, fieldName);
    }
    private static <T> T get(Class<?> clazz, Object obj, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
