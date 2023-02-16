package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTask;

public abstract class Service {
    public void execute(BackgroundTask task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    public void executeWithProvided(ExecutorService executor, BackgroundTask task) {
        executor.execute(task);
    }
}
