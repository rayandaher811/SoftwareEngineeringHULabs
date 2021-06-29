package org.sertia.client.controllers;

import org.sertia.client.communication.SertiaClient;

public class ClientControl {
    protected final SertiaClient client;

    public ClientControl() {
        client = SertiaClient.getInstance();
    }
}
