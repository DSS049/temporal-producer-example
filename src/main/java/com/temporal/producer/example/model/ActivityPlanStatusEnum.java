package com.temporal.producer.example.model;

public enum ActivityPlanStatusEnum {

    SUCCESS("SUCCESS"),
    FAILED("FAILED"),
    DEFAULT("");

    private final String activityPlanStatusEnumValue;

    private ActivityPlanStatusEnum(String activityPlanStatusEnumValue) {
        this.activityPlanStatusEnumValue = activityPlanStatusEnumValue;
    }

    public String getActivityPlanStatusEnumValue() {
        return this.activityPlanStatusEnumValue;
    }
}
