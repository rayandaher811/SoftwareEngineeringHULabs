package org.sertia.contracts.reports;

import java.io.Serializable;

public class ReportEntry implements Serializable {
    public String fieldName;
    public double value;

    public ReportEntry(String fieldName, double value) {
        this.fieldName = fieldName;
        this.value = value;
    }
}
