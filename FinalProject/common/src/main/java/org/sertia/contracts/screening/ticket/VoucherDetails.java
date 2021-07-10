package org.sertia.contracts.screening.ticket;

import java.io.Serializable;

public class VoucherDetails implements Serializable {
    public String buyerId;
    public int voucherId;

    public VoucherDetails() {
    }

    public VoucherDetails(String buyerId, int voucherId) {
        this.buyerId = buyerId;
        this.voucherId = voucherId;
    }
}
