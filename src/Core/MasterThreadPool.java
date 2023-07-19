package Core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class MasterThreadPool {
    private static volatile ExecutorService threadPool;

    private MasterThreadPool() {
        threadPool = Executors.newCachedThreadPool();
    }

    public static ExecutorService getPool() {
        if (threadPool == null) {
            synchronized (MasterThreadPool.class) {
                if (threadPool == null) {
                    new MasterThreadPool();
                }
            }
        }
        return threadPool;
    }
}