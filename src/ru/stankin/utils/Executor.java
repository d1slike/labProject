package ru.stankin.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by DisDev on 09.02.2016.
 */
public class Executor {
    private final static Executor ourInstance = new Executor();

    public static Executor getInstance() {
        return ourInstance;
    }

    private final ExecutorService service;

    private Executor() {
        service = Executors.newCachedThreadPool();
    }

    public void execute(Executable executable) {
        service.execute(executable);
    }

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

    public void shutdown() {
        try {
            service.shutdown();
            service.awaitTermination(1, TimeUnit.SECONDS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
