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

    public void addEntry(String fieldName, double value) {
        reportEntries.add(new ReportEntry(fieldName, value));
    }

    @Override
    public String toString() {
        return title;
    }
}