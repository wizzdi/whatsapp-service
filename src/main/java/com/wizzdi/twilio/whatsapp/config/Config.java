package com.wizzdi.twilio.whatsapp.config;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.CrossLoaderResolver;
import com.flexicore.events.PluginsLoadedEvent;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.service.BaseclassService;
import com.wizzdi.twilio.whatsapp.model.SendWhatsappMessage;
import com.wizzdi.twilio.whatsapp.model.WhatsappServer;
import com.wizzdi.twilio.whatsapp.model.WhatsappServerFilter;
import com.wizzdi.twilio.whatsapp.request.WhatsappServerCreate;
import com.wizzdi.twilio.whatsapp.request.WhatsappServerUpdate;
import org.pf4j.Extension;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Extension
@PluginInfo(version = 1)
public class Config implements ServicePlugin {

    @EventListener
    public void init(PluginsLoadedEvent pluginsLoadedEvent){
        BaseclassService.registerFilterClass(WhatsappServerFilter.class, WhatsappServer.class);
        CrossLoaderResolver.registerClass(SendWhatsappMessage.class, WhatsappServerCreate.class, WhatsappServerUpdate.class);
    }
}
