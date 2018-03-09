package com.jiakaiyang.bundlehandle.lib.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by jia on 2018/2/12.
 * some Utils for reflect
 */

public class ReflectUtils {


    public static @Nullable
    <F> F getField(Object target, String fieldName) {
        try {
            Class clazz = target.getClass();
            Field field = null;

            // find in this class and super class
            while (clazz != null) {

                try {
                    field = clazz.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    // do nothing
                }
                if (field != null) {
                    break;
                }

                clazz = clazz.getSuperclass();
            }

            field.setAccessible(true);
            return (F) field.get(target);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * todo add implement with param types
     * <p>
     * invoke method
     * <p>
     * this method is not correct for now
     *
     * @param target
     * @param methodName
     * @param args
     * @param <R>
     * @return
     */
    private static @Nullable
    <R> R invokeMethod(Object target, String methodName
            , Class<?>[] paramTypes
            , @NonNull Object... args) {
        try {
            int argsLength = args.length;

            Class<?>[] argsTypes = new Class<?>[argsLength];
            for (int i = 0; i < argsLength; i++) {
                Object object = args[i];
                argsTypes[i] = object.getClass();
            }

            Class clazz = target.getClass();
            Method[] methods = null;
            Method targetMethod = null;

            while (clazz != null) {
                methods = clazz.getDeclaredMethods();

                for (Method method : methods) {
                    String name = method.getName();
                    if (!methodName.equals(name)) {
                        continue;
                    }

                    Class<?>[] types = method.getParameterTypes();

                    boolean typeMatch = Arrays.equals(argsTypes, types);
                    if (!typeMatch) {
                        continue;
                    }

                    targetMethod = method;
                }

                if (targetMethod != null) {
                    break;
                }

                clazz = clazz.getSuperclass();
            }

            targetMethod.setAccessible(true);
            return (R) targetMethod.invoke(target, args);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * @param target
     * @param methodName
     * @param parameterTypes
     * @return
     */
    public static Method getMethod(Object target, String methodName, Class<?>... parameterTypes) {
        try {
            Class clazz = target.getClass();
            Method method = null;

            while (clazz != null) {

                try {
                    method = clazz.getDeclaredMethod(methodName, parameterTypes);
                } catch (NoSuchMethodException e) {
                    // do nothing
                }

                if (method != null) {
                    break;
                }

                clazz = clazz.getSuperclass();
            }

            method.setAccessible(true);
            return method;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return null;
    }
}
