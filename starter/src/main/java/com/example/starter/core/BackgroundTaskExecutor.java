package com.example.starter.core;

public interface BackgroundTaskExecutor {

    void schedule(String taskId, Runnable task);
}
