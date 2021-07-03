package org.sertia.client.controllers;

import org.sertia.contracts.reports.request.GetCinemaReports;
import org.sertia.contracts.reports.request.GetSertiaReports;
import org.sertia.contracts.reports.response.ClientReportsResponse;

public class ClientReportsControl extends ClientControl {

//    public ClientReportsResponse getSertiaReports() {
//        ClientReportsResponse response = client.request(new GetSertiaReports(), ClientReportsResponse.class);
//        if(response.isSuccessful) {
//            return response;
//        }
//        ClientReportsResponse clientReportsResponse = new ClientReportsResponse(false);
//        clientReportsResponse.failReason = response.failReason;
//
//        return clientReportsResponse;
//    }
//
//    public ClientReportsResponse getCinemaReports(int cinemaId) {
//        ClientReportsResponse response = client.request(new GetCinemaReports(cinemaId), ClientReportsResponse.class);
//        if(response.isSuccessful) {
//            return response;
//        }
//        ClientReportsResponse clientReportsResponse = new ClientReportsResponse(false);
//        clientReportsResponse.failReason = response.failReason;
//
//        return clientReportsResponse;
//    }
}