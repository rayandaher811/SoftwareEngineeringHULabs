package org.sertia.contracts.screening.ticket.request;

import java.time.LocalDateTime;

public class StreamingPaymentRequest extends BasicPaymentRequest {
    public int movieId;
    public LocalDateTime startTime;
    public int extraDays;
}