package com.example.starter.core.impl;

import com.example.starter.configuration.properties.BackgroundTaskProperties;
import com.example.starter.core.BackgroundTaskExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnExpression("'${background-executor.default-executor}'.equals('cron') and ${background-executor.enabled:true}")
public class CronBackgroundExecutor implements BackgroundTaskExecutor {

    private final BackgroundTaskProperties backgroundTaskProperties;

    private final ConcurrentTaskScheduler concurrentTaskScheduler;

    private final TaskStopper taskStopper;


    @Override
    public void schedule(String taskId, Runnable task) {
        log.debug("CronBackgroundExecutor: task with ID {} preparing for schedule", taskId);

        var future = concurrentTaskScheduler.schedule(task, new CronTrigger(backgroundTaskProperties
                .getCron().getExpression()));

        taskStopper.registryTask(taskId, future);
    }
}