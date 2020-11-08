package com.wizzdi.twilio.whatsapp.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.product.service.EquipmentService;
import com.flexicore.product.service.ExternalServerService;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.EncryptionService;
import com.twilio.Twilio;
import com.twilio.http.TwilioRestClient;
import com.wizzdi.twilio.whatsapp.data.WhatsappServerRepository;
import com.wizzdi.twilio.whatsapp.model.SendWhatsappMessage;
import com.wizzdi.twilio.whatsapp.model.WhatsappServer;
import com.wizzdi.twilio.whatsapp.model.WhatsappServerFilter;
import com.wizzdi.twilio.whatsapp.request.WhatsappServerCreate;
import com.wizzdi.twilio.whatsapp.request.WhatsappServerUpdate;
import com.twilio.rest.api.v2010.account.Message;

import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.List;

@PluginInfo(version = 1)
@Component
@Extension
public class WhatsappServerService implements ServicePlugin {

    private static final Logger logger= LoggerFactory.getLogger(WhatsappServerService.class);

    @Autowired
    @PluginInfo(version = 1)
    private ExternalServerService externalServerService;

    @Autowired
    @PluginInfo(version = 1)
    private EquipmentService equipmentService;

    @Autowired
    @PluginInfo(version = 1)
    private WhatsappServerRepository whatsappServerRepository;

    @Autowired
    private EncryptionService encryptionService;



    public WhatsappServer createWhatsappServer(WhatsappServerCreate whatsappServerCreate, SecurityContext securityContext){
        WhatsappServer whatsappServer=createWhatsappServerNoMerge(whatsappServerCreate,securityContext);
        whatsappServerRepository.merge(whatsappServer);
        return whatsappServer;
    }

    public void validate(WhatsappServerCreate whatsappServerCreate,SecurityContext securityContext){
        equipmentService.validateEquipmentCreate(whatsappServerCreate,securityContext);
    }

    public void validate(WhatsappServerFilter whatsappServerFilter,SecurityContext securityContext){
        externalServerService.validate(whatsappServerFilter,securityContext);
    }

    public WhatsappServer createWhatsappServerNoMerge(WhatsappServerCreate whatsappServerCreate, SecurityContext securityContext) {
        WhatsappServer whatsappServer=new WhatsappServer(whatsappServerCreate.getName(),securityContext);
        updateWhatsappServerNoMerge(whatsappServerCreate, whatsappServer);
        return whatsappServer;
    }

    public boolean updateWhatsappServerNoMerge(WhatsappServerCreate whatsappServerCreate,WhatsappServer whatsappServer ) {
       boolean update=externalServerService.updateExternalServerNoMerge(whatsappServerCreate, whatsappServer);
       byte[] data=whatsappServer.getId().getBytes();
        String authenticationToken = whatsappServerCreate.getAuthenticationToken();
        if(authenticationToken !=null){
            try {
                String encrypted = Base64.getEncoder().encodeToString(this.encryptionService.encrypt(authenticationToken.getBytes(StandardCharsets.UTF_8), data));
                if (!encrypted.equals(whatsappServer.getAuthenticationToken())) {
                    whatsappServer.setAuthenticationToken(encrypted);
                    update = true;
                }
            }
            catch (Exception e){
                logger.error("failed encrypting authentication key",e);
            }

       }
        String twilioSidAccount = whatsappServerCreate.getTwilioAccountSid();
        if(twilioSidAccount !=null){
            try {
                String encrypted = Base64.getEncoder().encodeToString(this.encryptionService.encrypt(twilioSidAccount.getBytes(StandardCharsets.UTF_8), data));
                if (!encrypted.equals(whatsappServer.getTwilioAccountSid())) {
                    whatsappServer.setTwilioAccountSid(encrypted);
                    update = true;
                }
            }
            catch (Exception e){
                logger.error("failed encrypting twilio sid account",e);
            }

        }
        return update;
    }


    public WhatsappServer updateWhatsappServer(WhatsappServerUpdate whatsappServerUpdate,SecurityContext securityContext){
        WhatsappServer whatsappServer=whatsappServerUpdate.getWhatsappServer();
        if(updateWhatsappServerNoMerge(whatsappServerUpdate,whatsappServer)){
            whatsappServerRepository.merge(whatsappServer);
        }
        return whatsappServer;
    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return whatsappServerRepository.getByIdOrNull(id, c, batchString, securityContext);
    }

    public PaginationResponse<WhatsappServer> getAllWhatsappServers(WhatsappServerFilter whatsappServerFilter, SecurityContext securityContext){
        List<WhatsappServer> list=listAllWhatsappServers(whatsappServerFilter,securityContext);
        long count=countAllWhatsappServers(whatsappServerFilter,securityContext);
        return new PaginationResponse<>(list,whatsappServerFilter,count);
    }

    public List<WhatsappServer> listAllWhatsappServers(WhatsappServerFilter whatsappServerFilter, SecurityContext securityContext) {
        return whatsappServerRepository.listAllWhatsappServers(whatsappServerFilter, securityContext);
    }

    public long countAllWhatsappServers(WhatsappServerFilter whatsappServerFilter, SecurityContext securityContext) {
        return whatsappServerRepository.countAllWhatsappServers(whatsappServerFilter, securityContext);
    }

    public void validate(SendWhatsappMessage sendWhatsappMessage, SecurityContext securityContext) {
        String whatsappServerId=sendWhatsappMessage.getWhatsAppServerId();
        WhatsappServer whatsappServer=whatsappServerId!=null?getByIdOrNull(whatsappServerId,WhatsappServer.class,null,securityContext):null;
        if(whatsappServer==null){
            throw new BadRequestException("No Whatsapp Server with id "+whatsappServerId);
        }
        sendWhatsappMessage.setWhatsappServer(whatsappServer);
    }

    public Message sendWhatsappMessage(SendWhatsappMessage sendWhatsappMessage, SecurityContext securityContext) {
        WhatsappServer whatsappServer = sendWhatsappMessage.getWhatsappServer();
        byte[] data=whatsappServer.getId().getBytes();
        try {
            String decryptedSid=new String(this.encryptionService.decrypt(Base64.getDecoder().decode(whatsappServer.getTwilioAccountSid()),data), StandardCharsets.UTF_8);
            String decryptedAuthenticationKey=new String(this.encryptionService.decrypt(Base64.getDecoder().decode(whatsappServer.getAuthenticationToken()),data), StandardCharsets.UTF_8);
            Twilio.init(whatsappServer.getTwilioAccountSid(), whatsappServer.getAuthenticationToken());
            TwilioRestClient twilioRestClient=new TwilioRestClient.Builder(decryptedSid,decryptedAuthenticationKey).build();
            return Message.creator(
                    new com.twilio.type.PhoneNumber("whatsapp:"+sendWhatsappMessage.getSendTo()),
                    new com.twilio.type.PhoneNumber("whatsapp:"+sendWhatsappMessage.getSendFrom()),
                    sendWhatsappMessage.getContent()).create(twilioRestClient);



        } catch (GeneralSecurityException e) {
            logger.error("failed decrypting data",e);
        }
        return null;

    }


}
