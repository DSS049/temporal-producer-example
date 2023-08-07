package com.temporal.producer.example.config;

import com.temporal.producer.example.model.Feedback;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface FeedbackActivity {

    @ActivityMethod(name = "feedbackActivity")
    String feedbackActivity(Feedback feedback);
}
