package com.mei.imageloader;

import android.os.Build;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mxb
 * @date 2020/10/19
 * @desc 线程池
 * @desired
 */
public class ThreadPoolManager {

    // cpu 核心数
    private final static int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    // 核心线程数
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;

    // 最大线程数
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;

    // 同时可存活的线程数
    private static final long KEEP_ALIVE = 10l;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "ImageLoader#" + mCount.getAndIncrement());
        }
    };

    // 初始化先线程池对象
    private static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
            sThreadFactory);

    /**
     * 执行线程
     */
    public static void run(Runnable runnable) {
        THREAD_POOL_EXECUTOR.execute(runnable);
    }

}
