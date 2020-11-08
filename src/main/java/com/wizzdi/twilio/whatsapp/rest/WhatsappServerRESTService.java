package com.wizzdi.twilio.whatsapp.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.security.SecurityContext;
import com.twilio.rest.api.v2010.account.Message;
import com.wizzdi.twilio.whatsapp.model.SendWhatsappMessage;
import com.wizzdi.twilio.whatsapp.model.WhatsappServer;
import com.wizzdi.twilio.whatsapp.model.WhatsappServerFilter;
import com.wizzdi.twilio.whatsapp.request.WhatsappServerCreate;
import com.wizzdi.twilio.whatsapp.request.WhatsappServerUpdate;
import com.wizzdi.twilio.whatsapp.service.WhatsappServerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;

@PluginInfo(version = 1)
@Extension
@Component
@OperationsInside
@Path("plugins/whatsappServer")
@Tag(name = "whatsappServer")
@ProtectedREST
public class WhatsappServerRESTService implements RestServicePlugin {

    @Autowired
    @PluginInfo(version = 1)
    private WhatsappServerService whatsappServerService;

    @POST
    @Operation(summary = "returns all whatsapp servers",description = "returns all whatsapp servers")
    @Path("getAllWhatsappServers")
    public PaginationResponse<WhatsappServer> getAllWhatsappServers(@HeaderParam("authenticationKey")String authenticationKey,
                                                                    WhatsappServerFilter whatsappServerFilter, @Context SecurityContext securityContext){
        whatsappServerService.validate(whatsappServerFilter,securityContext);
        return whatsappServerService.getAllWhatsappServers(whatsappServerFilter,securityContext);
    }


    @POST
    @Operation(summary = "creates whatsapp server",description = "creates whatsapp server")
    @Path("createWhatsappServer")
    public WhatsappServer createWhatsappServer(@HeaderParam("authenticationKey")String authenticationKey,
                                                                   WhatsappServerCreate whatsappServerCreate, @Context SecurityContext securityContext){
        whatsappServerService.validate(whatsappServerCreate,securityContext);
        return whatsappServerService.createWhatsappServer(whatsappServerCreate,securityContext);
    }

    @POST
    @Operation(summary = "sends whatsapp message",description = "sends whatsapp message")
    @Path("sendWhatsappMessage")
    public Message sendWhatsappMessage(@HeaderParam("authenticationKey")String authenticationKey,
                                       SendWhatsappMessage sendWhatsappMessage, @Context SecurityContext securityContext){
        whatsappServerService.validate(sendWhatsappMessage,securityContext);
        return whatsappServerService.sendWhatsappMessage(sendWhatsappMessage,securityContext);
    }

    @PUT
    @Operation(summary = "updates whatsapp server",description = "updates whatsapp server")
    @Path("updateWhatsappServer")
    public WhatsappServer updateWhatsappServer(@HeaderParam("authenticationKey")String authenticationKey,
                                               WhatsappServerUpdate whatsappServerUpdate, @Context SecurityContext securityContext){
        String id=whatsappServerUpdate.getId();
        WhatsappServer whatsappServer=id!=null?whatsappServerService.getByIdOrNull(id,WhatsappServer.class,null,securityContext):null;
        if(whatsappServer==null){
            throw new BadRequestException("No Whatsapp Server with id "+id);
        }
        whatsappServerUpdate.setWhatsappServer(whatsappServer);
        whatsappServerService.validate(whatsappServerUpdate,securityContext);
        return whatsappServerService.updateWhatsappServer(whatsappServerUpdate,securityContext);
    }
}
