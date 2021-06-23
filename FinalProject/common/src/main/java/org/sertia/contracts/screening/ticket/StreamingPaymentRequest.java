package org.sertia.contracts.screening.ticket;

import org.joda.time.DateTime;

public class StreamingPaymentRequest extends ClientPaymentRequest {
    private String movieId;
    private DateTime startTime;
    private DateTime endTime;
}