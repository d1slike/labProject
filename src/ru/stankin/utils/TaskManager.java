package ru.stankin.utils;

import java.util.concurrent.*;

/**
 * Created by DisDev on 09.02.2016.
 */
public class TaskManager {
    private final static TaskManager ourInstance = new TaskManager();
    private final ExecutorService executorService;

    private TaskManager() {
        executorService = Executors.newFixedThreadPool(2, new TDeamonFactory("Executor_Thread_"));
    }

    public void execute(Executable executable) {
        executorService.execute(executable);
    }

    public static TaskManager getInstance() {
        return ourInstance;
    }

    @FunctionalInterface
    public interface Executable extends Runnable {
        @Override
        default void run() {
            try {
                action();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        void action();
    }

    private static class TDeamonFactory implements ThreadFactory {

        private static volatile int threadNum = 0;

        private final String threadNames;

        private TDeamonFactory(String threadNames) {
            this.threadNames = threadNames;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName(threadNames + threadNum++);
            return thread;
        }
    }
}
