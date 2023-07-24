package com.temporal.producer.example.controller;

import com.maersk.composition.propagator.MDCContextPropagator;
import com.temporal.producer.example.config.BookingFeedbackWorkFlow;
import com.temporal.producer.example.config.TemporalWorkerProducerConfiguration;
import com.temporal.producer.example.model.ActivityPlanDomain;
import com.temporal.producer.example.model.Feedback;
import io.temporal.api.enums.v1.WorkflowIdReusePolicy;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/temporal")
public class TemporalController {

    @Autowired
    private TemporalWorkerProducerConfiguration worker;


    @GetMapping("/send/{bookingId}")
    public String hello(@PathVariable String bookingId, @RequestHeader String status) {
        Feedback feedback = Feedback.builder()
                .bookingId(bookingId)
                .workProcessStatus(status).workProcessEndDatetime(Instant.now()).build();

        sendToActivityTemporalQueue(feedback);
        log.info("Data sent with orderId : {}", feedback.getBookingId());
        return "Data sent with orderId : " + feedback.getBookingId();
    }


    private void sendToActivityTemporalQueue(Feedback feedback) {
        BookingFeedbackWorkFlow workFlow = worker.getClient()
                .newWorkflowStub(BookingFeedbackWorkFlow.class, WorkflowOptions.newBuilder()
                        .setTaskQueue("feedbackActivityTaskQueueWF")
                        .setContextPropagators(Collections.singletonList(new MDCContextPropagator()))
                        .setWorkflowIdReusePolicy(WorkflowIdReusePolicy.WORKFLOW_ID_REUSE_POLICY_REJECT_DUPLICATE)
                        .setWorkflowId(feedback.getBookingId() + ":" + Instant.now()).build());
        workFlow.sendFeedback(feedback);
    }

    private void sendToWorkFlowTemporalQueue(ActivityPlanDomain activityPlanDomain) {
        WorkflowStub activityPlanWorkLlowStub = worker.getClient()
                .newUntypedWorkflowStub("ActivityPlanWorkflow", WorkflowOptions.newBuilder()
                        .setTaskQueue(worker.getActivityPlanTaskQueueName())
                        .setContextPropagators(Collections.singletonList(new MDCContextPropagator()))
                        .setWorkflowIdReusePolicy(WorkflowIdReusePolicy.WORKFLOW_ID_REUSE_POLICY_REJECT_DUPLICATE)
                        .setWorkflowId(activityPlanDomain.getOrderId())
                        .build());

        activityPlanWorkLlowStub.start(activityPlanDomain);
        activityPlanWorkLlowStub.signal(activityPlanDomain.getEventName(), activityPlanDomain);
    }

    public String getServicePlanNo() {
        String id = UUID.randomUUID().toString().replace("-", "");
        return "DJ" + id.substring(0, Math.min(id.length(), 10)).toUpperCase();
    }

}
