package com.temporal.producer.example.config;

import com.maersk.composition.service.TemporalClientProvider;
import io.temporal.client.WorkflowClient;
import io.temporal.worker.WorkerFactory;
import io.temporal.worker.WorkerFactoryOptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor
public class TemporalWorkerProducerConfiguration implements ApplicationRunner {

    private WorkflowClient client;
    private final TemporalClientProvider temporalClientProvider;

    @Value("${temporal.activity-plan.task-queue-name}")
    private String activityPlanTaskQueueName;


    @Override
    public void run(ApplicationArguments args) {
        client = temporalClientProvider.getTemporalClientInstance();
        WorkerFactoryOptions workerFactoryOptions = temporalClientProvider.getWorkerFactoryOptions();
        WorkerFactory factory = WorkerFactory.newInstance(client, workerFactoryOptions);
        factory.newWorker(activityPlanTaskQueueName);
        factory.start();
    }

}
