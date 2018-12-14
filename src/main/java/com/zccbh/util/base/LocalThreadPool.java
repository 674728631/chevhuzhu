package com.zccbh.util.base;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocalThreadPool {

    public static final ExecutorService executorService = Executors.newCachedThreadPool();
}
