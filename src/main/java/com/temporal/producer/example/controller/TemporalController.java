package com.temporal.producer.example.controller;

import com.maersk.composition.propagator.MDCContextPropagator;
import com.temporal.producer.example.config.BookingFeedbackWorkFlow;
import com.temporal.producer.example.config.TemporalWorkerProducerConfiguration;
import com.temporal.producer.example.model.ActivityPlanDomain;
import io.temporal.api.enums.v1.WorkflowIdReusePolicy;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/temporal")
public class TemporalController {

    @Autowired
    private TemporalWorkerProducerConfiguration worker;


    @GetMapping("/send")
    public String hello() {
                ActivityPlanDomain activityPlanDomain = ActivityPlanDomain.builder()
                .eventName("Ready For Planning")
                .activityDateTime(Instant.now().toString())
                .productName("Export Intermodal FCL")
                .domainName("Booking")
                .orderId(getServicePlanNo())
                .bookingId(getServicePlanNo())
                .userId("dhananjaya.singhar@maersk.com")
                .status("")
                .userName("Dhananjaya Samanta Singhar")
                .domainData("").build();

        sendToActivityTemporalQueue(activityPlanDomain);

        log.info("Data sent with orderId : {}", activityPlanDomain.getOrderId());
        return  "Data sent with orderId : " + activityPlanDomain.getOrderId();
    }


    private void sendToActivityTemporalQueue(ActivityPlanDomain activityPlanDomain) {
        BookingFeedbackWorkFlow workFlow = worker.getClient().newWorkflowStub(
                BookingFeedbackWorkFlow.class, WorkflowOptions.newBuilder()
                        .setTaskQueue("feedbackActivityTaskQueueWF")
                        .setContextPropagators(Collections.singletonList(new MDCContextPropagator()))
                        .setWorkflowIdReusePolicy(WorkflowIdReusePolicy.WORKFLOW_ID_REUSE_POLICY_REJECT_DUPLICATE)
                        .setWorkflowId(activityPlanDomain.getOrderId())
                        .build());
        workFlow.sendFeedback(activityPlanDomain);
    }

    private void sendToWorkFlowTemporalQueue(ActivityPlanDomain activityPlanDomain) {
        WorkflowStub activityPlanWorkLlowStub = worker.getClient().newUntypedWorkflowStub(
                "ActivityPlanWorkflow", WorkflowOptions.newBuilder()
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
