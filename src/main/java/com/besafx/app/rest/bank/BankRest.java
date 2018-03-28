package com.besafx.app.rest.bank;

import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.entity.admin.Person;
import com.besafx.app.entity.bank.Bank;
import com.besafx.app.service.admin.PersonService;
import com.besafx.app.service.bank.BankService;
import com.besafx.app.util.JSONConverter;
import com.besafx.app.util.Options;
import com.besafx.app.ws.Notification;
import com.besafx.app.ws.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/bank/")
public class BankRest {

    public static final String FILTER_TABLE = "**,bankReceipts[**,receipt[**,lastPerson[id,nickname,name]],-bank]";
    private final static Logger log = LoggerFactory.getLogger(BankRest.class);
    @Autowired
    private BankService bankService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BANK_UPDATE')")
    public String update(@RequestBody Bank bank) {
        Bank object = bankService.findOne(bank.getId());
        if (object != null) {
            bank = bankService.save(bank);
            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification.builder().message(lang.equals("AR") ? "تم تعديل بيانات حساب البنك بنجاح" : "Update Bank Account Information Successfully").type("warning").build(), caller.getEmail());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), bank);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String get() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), bankService.findFirstBy());
    }

}
