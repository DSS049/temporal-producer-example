package com.temporal.producer.example.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Feedback {
    private String bookingId;
    private String workProcessStatus;
    private Instant timestamp;

}
