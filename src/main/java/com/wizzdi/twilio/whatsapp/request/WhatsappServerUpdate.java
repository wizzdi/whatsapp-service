package com.wizzdi.twilio.whatsapp.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.interfaces.dynamic.IdRefFieldInfo;
import com.wizzdi.twilio.whatsapp.model.WhatsappServer;

public class WhatsappServerUpdate extends WhatsappServerCreate{

    @IdRefFieldInfo(list = false,refType = WhatsappServer.class,displayName = "whatsapp server")
    private String id;
    @JsonIgnore
    private WhatsappServer whatsappServer;


    public String getId() {
        return id;
    }

    public <T extends WhatsappServerUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public WhatsappServer getWhatsappServer() {
        return whatsappServer;
    }

    public <T extends WhatsappServerUpdate> T setWhatsappServer(WhatsappServer whatsappServer) {
        this.whatsappServer = whatsappServer;
        return (T) this;
    }
}
