package com.temporal.producer.example.config;

import com.temporal.producer.example.model.Feedback;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface BookingFeedbackWorkFlow {
    @WorkflowMethod
    void sendFeedback(Feedback feedback);
}
