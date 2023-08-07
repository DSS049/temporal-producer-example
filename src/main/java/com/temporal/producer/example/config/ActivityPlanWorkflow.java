package com.temporal.producer.example.config;


import com.temporal.producer.example.model.ActivityPlanDomain;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ActivityPlanWorkflow {

    @WorkflowMethod
    ActivityPlanDomain consumeActivityPlan(ActivityPlanDomain activityPlan);

    @SignalMethod(name = "booking_signal")
    void consumeActivityPlanSignal(ActivityPlanDomain activityPlan);
}
