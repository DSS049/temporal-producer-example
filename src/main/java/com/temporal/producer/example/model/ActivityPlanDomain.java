package com.temporal.producer.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityPlanDomain {
    private String productName;
    private String domainName;
    private String orderId;
    private String bookingId;
    private String eventName;
    private String userId;
    private String userName;
    private String activityDateTime;
    private ActivityPlanStatusEnum status;
    private String domainData;
}
