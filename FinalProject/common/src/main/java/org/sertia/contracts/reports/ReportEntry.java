package org.sertia.contracts.reports;

import java.io.Serializable;

public class ReportEntry implements Serializable {
    public String fieldName;
    public int value;

    public ReportEntry(String fieldName, int value) {
        this.fieldName = fieldName;
        this.value = value;
    }
}
