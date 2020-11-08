package com.wizzdi.twilio.whatsapp.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.security.SecurityContext;
import com.wizzdi.twilio.whatsapp.model.WhatsappServer;
import com.wizzdi.twilio.whatsapp.model.WhatsappServerFilter;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Extension
@Component
@PluginInfo(version = 1)
public class WhatsappServerRepository extends AbstractRepositoryPlugin {


    public List<WhatsappServer> listAllWhatsappServers(WhatsappServerFilter whatsappServerFilter, SecurityContext securityContext){
        CriteriaBuilder cb= em.getCriteriaBuilder();
        CriteriaQuery<WhatsappServer> q=cb.createQuery(WhatsappServer.class);
        Root<WhatsappServer> r=q.from(WhatsappServer.class);
        List<Predicate> preds=new ArrayList<>();
        addWhatsappServerPredicate(whatsappServerFilter,r,cb,preds);
        QueryInformationHolder<WhatsappServer> queryInformationHolder=new QueryInformationHolder<>(whatsappServerFilter,WhatsappServer.class,securityContext);
        return getAllFiltered(queryInformationHolder,preds,cb,q,r);
    }

    private void addWhatsappServerPredicate(WhatsappServerFilter whatsappServerFilter, Root<WhatsappServer> r, CriteriaBuilder cb, List<Predicate> preds) {

    }

    public long countAllWhatsappServers(WhatsappServerFilter whatsappServerFilter, SecurityContext securityContext){
        CriteriaBuilder cb= em.getCriteriaBuilder();
        CriteriaQuery<Long> q=cb.createQuery(Long.class);
        Root<WhatsappServer> r=q.from(WhatsappServer.class);
        List<Predicate> preds=new ArrayList<>();
        addWhatsappServerPredicate(whatsappServerFilter,r,cb,preds);
        QueryInformationHolder<WhatsappServer> queryInformationHolder=new QueryInformationHolder<>(whatsappServerFilter,WhatsappServer.class,securityContext);
        return countAllFiltered(queryInformationHolder,preds,cb,q,r);
    }
}
