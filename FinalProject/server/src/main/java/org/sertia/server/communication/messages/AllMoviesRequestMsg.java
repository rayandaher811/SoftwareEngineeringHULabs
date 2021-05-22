package org.sertia.server.communication.messages;

import com.google.gson.Gson;

public class AllMoviesRequestMsg {
    private final String messageName = "ALL_MOVIES_REQ";
    private BaseClientServerMessage metadata;

    public AllMoviesRequestMsg(String clientId) {
        metadata = new BaseClientServerMessage(clientId);
    }

    public String getMessageId(){
        return metadata.getMessageId();
    }

}
