package com.temporal.producer.example.temporal;


import com.temporal.producer.example.config.ActivityPlanWorkflow;
import com.temporal.producer.example.model.ActivityPlanDomain;
import com.temporal.producer.example.model.Feedback;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ActivityPlanWorkflowImpl implements ActivityPlanWorkflow {

    private boolean isBookingCompleted;

    @Override
    public ActivityPlanDomain consumeActivityPlan(ActivityPlanDomain activityPlan) {
        log.info("The received activity plan event is " + activityPlan.toString());
        log.info("Received booking Id is - " + activityPlan.getBookingId());
        Feedback feedback = Feedback.builder()
                .bookingId(activityPlan.getBookingId())
                .workProcessStatus(activityPlan.getEventName())
                .workProcessEndDatetime(Instant.now()).build();

        sendFeedback(feedback);
        log.info("Data sent with orderId : {}", feedback.getBookingId());
        Workflow.await(() -> isBookingCompleted);
        return activityPlan;
    }

    @Override
    public void consumeActivityPlanSignal(ActivityPlanDomain activityPlan) {
        log.info("The received activity plan signal is " + activityPlan.toString());
        log.info("Received booking Id is - " + activityPlan.getBookingId());
        Feedback feedback = Feedback.builder()
                .bookingId(activityPlan.getBookingId())
                .workProcessStatus(activityPlan.getEventName())
                .workProcessEndDatetime(Instant.now()).build();

        sendFeedback(feedback);
        if (activityPlan.getEventName().equals("BOOKING_CONFIRMED")) {
            isBookingCompleted = true;
        }
        log.info("Data sent with orderId : {}", feedback.getBookingId());
    }

    private void sendFeedback(Feedback feedback) {
        log.info("sendFeedback for bookingId {}", feedback.getBookingId());
        io.temporal.workflow.ActivityStub feedbackActivity =
                getActivity(System.getProperty("FEED_BACK_QUEUE"));
        log.info("sendFeedback for feedback {}", feedback.getWorkProcessStatus());
        feedbackActivity.executeAsync("feedbackActivity", String.class, feedback).get();
    }

    private static io.temporal.workflow.ActivityStub getActivity(String queue) {
        ActivityOptions feedbackActivityOptions =
                ActivityOptions.newBuilder()
                        .setTaskQueue(queue)
                        .setStartToCloseTimeout(Duration.ofMinutes(10))
                        .setRetryOptions(
                                RetryOptions.newBuilder().setInitialInterval(Duration.ofSeconds(20)).build())
                        .build();

        return Workflow.newUntypedActivityStub(feedbackActivityOptions);
    }
}