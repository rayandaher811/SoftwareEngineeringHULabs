package org.sertia.contracts.screening.ticket.request;

import org.joda.time.DateTime;

public class StreamingPaymentRequest extends BasicPaymentRequest {
    private String movieId;
    private DateTime startTime;
    private int extraDays;
}