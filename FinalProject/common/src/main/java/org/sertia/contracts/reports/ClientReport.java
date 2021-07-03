package org.sertia.contracts.reports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClientReport implements Serializable {
    public String title;
    public String reportText;
    public List<ReportEntry> reportEntries;

    public ClientReport() {
        reportEntries = new ArrayList<>();
    }

    public void addEntry(String fieldName, int value) {
        reportEntries.add(new ReportEntry(fieldName, value));
    }
}