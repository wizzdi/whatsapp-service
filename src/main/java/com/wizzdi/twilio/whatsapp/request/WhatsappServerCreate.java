package com.wizzdi.twilio.whatsapp.request;

import com.flexicore.interfaces.dynamic.FieldInfo;
import com.flexicore.product.iot.request.ExternalServerCreate;

public class WhatsappServerCreate extends ExternalServerCreate {

    @FieldInfo(mandatory = true,displayName = "twilio account sid")
    private String twilioAccountSid;
    @FieldInfo(mandatory = true,displayName ="twilio authentication token")
    private String authenticationToken;

    public String getTwilioAccountSid() {
        return twilioAccountSid;
    }

    public <T extends WhatsappServerCreate> T setTwilioAccountSid(String twilioAccountSid) {
        this.twilioAccountSid = twilioAccountSid;
        return (T) this;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public <T extends WhatsappServerCreate> T setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
        return (T) this;
    }
}
