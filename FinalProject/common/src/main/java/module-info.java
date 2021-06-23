module org.sertia.contracts {
    requires org.joda.time;

    opens org.sertia.contracts.movies.catalog.controller to org.joda.time;
    opens org.sertia.contracts.screening.ticket to org.joda.time;

    exports org.sertia.contracts.user.login;
    exports org.sertia.contracts.complaints.controller;
    exports org.sertia.contracts.movies.catalog.controller;
    exports org.sertia.contracts.price.change;
    exports org.sertia.contracts.reports.controller;
    exports org.sertia.contracts.screening.ticket;
}