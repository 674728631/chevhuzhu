package com.zccbh.util.base;

public class DataSourceContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDbType(String dbType) {
        contextHolder.set(dbType);
    }

    public static String getDbType() {
        return (contextHolder.get());
    }

    public static void clearDbType() {
        contextHolder.remove();
    }
}