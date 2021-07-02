module org.sertia.contracts {
    requires org.joda.time;

    opens org.sertia.contracts.movies.catalog to org.joda.time;
    opens org.sertia.contracts.screening.ticket to org.joda.time;

    exports org.sertia.contracts.user.login;
    exports org.sertia.contracts.complaints;
    exports org.sertia.contracts.movies.catalog;
    exports org.sertia.contracts.price.change;
    exports org.sertia.contracts.reports;
    exports org.sertia.contracts.screening.ticket;
    exports org.sertia.contracts.price.change.request;
    exports org.sertia.contracts.movies.catalog.request;
    exports org.sertia.contracts.movies.catalog.response;
    exports org.sertia.contracts.screening.ticket.response;
    exports org.sertia.contracts;
    exports org.sertia.contracts.price.change.responses;
    exports org.sertia.contracts.complaints.requests;
    exports org.sertia.contracts.complaints.responses;
    exports org.sertia.contracts.user.login.request;
    exports org.sertia.contracts.user.login.response;
    exports org.sertia.contracts.screening.ticket.request;
    exports org.sertia.contracts.covidRegulations.requests;
    exports org.sertia.contracts.covidRegulations.responses;
}