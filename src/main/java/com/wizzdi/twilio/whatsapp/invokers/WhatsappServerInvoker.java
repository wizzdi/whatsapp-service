package com.wizzdi.twilio.whatsapp.invokers;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.dynamic.InvokerInfo;
import com.flexicore.interfaces.dynamic.InvokerMethodInfo;
import com.flexicore.interfaces.dynamic.ListingInvoker;
import com.flexicore.security.SecurityContext;
import com.twilio.rest.api.v2010.account.Message;
import com.wizzdi.twilio.whatsapp.model.SendWhatsappMessage;
import com.wizzdi.twilio.whatsapp.model.WhatsappServer;
import com.wizzdi.twilio.whatsapp.model.WhatsappServerFilter;
import com.wizzdi.twilio.whatsapp.request.WhatsappServerCreate;
import com.wizzdi.twilio.whatsapp.request.WhatsappServerUpdate;
import com.wizzdi.twilio.whatsapp.service.WhatsappServerService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Extension
@Component
@InvokerInfo(displayName = "Whatsapp Server Invoker",description = "invoker for whatsapp server")
public class WhatsappServerInvoker implements ListingInvoker<WhatsappServer, WhatsappServerFilter> {

    @Autowired
    @PluginInfo(version = 1)
    private WhatsappServerService whatsappServerService;


    @Override
    @InvokerMethodInfo(displayName = "get all Whatsapp Servers",description = "returns all whatsapp servers",relatedClasses = WhatsappServer.class)
    public PaginationResponse<WhatsappServer> listAll(WhatsappServerFilter filter, SecurityContext securityContext) {
        whatsappServerService.validate(filter, securityContext);
        return whatsappServerService.getAllWhatsappServers(filter,securityContext);
    }

    @InvokerMethodInfo(displayName = "creates Whatsapp Server",description = "creates whatsapp servers",relatedClasses = WhatsappServer.class)
    public WhatsappServer create(WhatsappServerCreate whatsappServerCreate, SecurityContext securityContext) {
        whatsappServerService.validate(whatsappServerCreate, securityContext);
        return whatsappServerService.createWhatsappServer(whatsappServerCreate,securityContext);
    }

    @InvokerMethodInfo(displayName = "updates Whatsapp Server",description = "updates whatsapp servers",relatedClasses = WhatsappServer.class)
    public WhatsappServer update(WhatsappServerUpdate whatsappServerUpdate, SecurityContext securityContext) {
        whatsappServerService.validate(whatsappServerUpdate, securityContext);
        return whatsappServerService.updateWhatsappServer(whatsappServerUpdate,securityContext);
    }

    @InvokerMethodInfo(displayName = "sends Whatsapp message",description = "sends whatsapp message",relatedClasses = WhatsappServer.class,categories = "ACTION")
    public Message sendWhatsappMessage(SendWhatsappMessage sendWhatsappMessage, SecurityContext securityContext) {
        whatsappServerService.validate(sendWhatsappMessage, securityContext);
        return whatsappServerService.sendWhatsappMessage(sendWhatsappMessage,securityContext);
    }


    @Override
    public Class<WhatsappServerFilter> getFilterClass() {
        return WhatsappServerFilter.class;
    }

    @Override
    public Class<WhatsappServer> getHandlingClass() {
        return WhatsappServer.class;
    }
}
