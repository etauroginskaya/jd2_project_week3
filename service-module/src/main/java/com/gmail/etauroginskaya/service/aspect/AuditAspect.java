package com.gmail.etauroginskaya.service.aspect;

import com.gmail.etauroginskaya.data.model.AuditActionEnum;
import com.gmail.etauroginskaya.data.model.AuditItem;
import com.gmail.etauroginskaya.service.AuditItemService;
import com.gmail.etauroginskaya.service.model.ItemDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class AuditAspect {

    private static final Logger logger = LoggerFactory.getLogger(AuditAspect.class);
    private final AuditItemService auditItemService;

    @Autowired
    public AuditAspect(AuditItemService auditItemService) {
        this.auditItemService = auditItemService;
    }

    @Pointcut("execution(* com.gmail.etauroginskaya.service.ItemService.add(..))")
    public void addItemPointcut() {
    }

    @Pointcut("execution(* com.gmail.etauroginskaya.service.ItemService.update(..))")
    public void updateStatusItemPointcut() {
    }

    @AfterReturning(pointcut = "addItemPointcut()", returning = "itemDTO")
    public void afterSuccessAddedItem(ItemDTO itemDTO) {
        AuditItem auditItem = new AuditItem();
        auditItem.setItem_id(itemDTO.getId());
        auditItem.setAction(AuditActionEnum.CREATE.name());
        auditItem.setDate(LocalDateTime.now().toString());
        AuditItem saveAuditItem = auditItemService.save(auditItem);
        logger.info(saveAuditItem.toString() + " : " + itemDTO.toString());
    }

    @AfterReturning(pointcut = "updateStatusItemPointcut()")
    public void afterSuccessUpdateItem(JoinPoint jp) {
        AuditItem auditItem = new AuditItem();
        Object[] args = jp.getArgs();
        auditItem.setItem_id(args[0].toString());
        auditItem.setAction(AuditActionEnum.UPDATED.name());
        auditItem.setDate(LocalDateTime.now().toString());
        AuditItem saveAuditItem = auditItemService.save(auditItem);
        logger.info(saveAuditItem.toString() + " to " + args[1]);
    }
}
