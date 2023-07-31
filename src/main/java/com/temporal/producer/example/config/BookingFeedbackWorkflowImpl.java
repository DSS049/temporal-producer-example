package com.temporal.producer.example.config;

import com.maersk.composition.propagator.MDCContextPropagator;
import com.temporal.producer.example.model.ActivityPlanDomain;
import com.temporal.producer.example.model.Feedback;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.ActivityStub;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import java.util.Collections;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookingFeedbackWorkflowImpl implements BookingFeedbackWorkFlow {

    @Override
    @SneakyThrows
    public void sendFeedback(Feedback feedback) {
       log.info("Sending feedback to bookingService with bookingId: {}", feedback.getBookingId());
        sendToActivityQueue(feedback);
        log.info("Workflow completed for feedback to bookingService with bookingId: {}", feedback.getBookingId());
    }

    private void sendToActivityQueue(Feedback feedback) {
        ActivityOptions options = ActivityOptions.newBuilder()
                .setRetryOptions(
                        RetryOptions.newBuilder()
                                .setMaximumAttempts(3)
                                .setInitialInterval(Duration.ofSeconds(1))
                                .setBackoffCoefficient(2)
                                .setMaximumInterval(Duration.ofHours(1))
                                .build())
                .setStartToCloseTimeout(Duration.ofHours(2))
                .setTaskQueue("FeedbackActivityDevTaskQueue")
                .setContextPropagators(Collections.singletonList(new MDCContextPropagator()))
                .build();
        ActivityStub activity = Workflow.newUntypedActivityStub(options);

        activity.executeAsync("feedbackActivity", String.class, feedback).get();

        System.out.println("received response");
    }
}