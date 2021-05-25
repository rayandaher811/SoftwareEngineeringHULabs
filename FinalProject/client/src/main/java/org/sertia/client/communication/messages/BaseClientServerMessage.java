package org.sertia.client.communication.messages;

import java.util.UUID;

public class BaseClientServerMessage {
    private String messageId;
    private String clientId;

    public BaseClientServerMessage(String clientId) {
        messageId = UUID.randomUUID().toString();
        this.clientId = clientId;
    }

    public String getMessageId(){
        return messageId;
    }
}
