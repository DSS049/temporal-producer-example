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
    private String eventName;
    private String activityDateTime;
    private String productName;
    private String domainName;
    private String orderId;
    private String bookingId;
    private String userId;
    private String status;
    private String userName;
    private String domainData;
}
