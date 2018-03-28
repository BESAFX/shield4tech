package com.besafx.app.rest.hr;

import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.entity.admin.Person;
import com.besafx.app.entity.hr.Deduction;
import com.besafx.app.service.admin.PersonService;
import com.besafx.app.service.hr.DeductionService;
import com.besafx.app.util.JSONConverter;
import com.besafx.app.util.Options;
import com.besafx.app.ws.Notification;
import com.besafx.app.ws.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/deduction/")
public class DeductionRest {

    public static final String FILTER_TABLE = "**,employee[id,-salaries,person[id,nickname,name,mobile]]";
    private final static Logger log = LoggerFactory.getLogger(DeductionRest.class);
    @Autowired
    private DeductionService deductionService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DEDUCTION_CREATE')")
    public String create(@RequestBody Deduction deduction) {
        deduction = deductionService.save(deduction);
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "سجل الاستقطاعات" : "Data Processing")
                .message(lang.equals("AR") ? "تم انشاء الاستقطاع بنجاح" : "Create Deduction Successfully")
                .type("success")


                .build(), caller.getEmail());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), deduction);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DEDUCTION_UPDATE')")
    public String update(@RequestBody Deduction deduction) {
        Deduction object = deductionService.findOne(deduction.getId());
        if (object != null) {
            deduction = deductionService.save(deduction);
            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "سجل الاستقطاعات" : "Data Processing")
                    .message(lang.equals("AR") ? "تم تعديل بيانات الاستقطاع بنجاح" : "Update Deduction Information Successfully")
                    .type("warning")


                    .build(), caller.getEmail());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), deduction);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DEDUCTION_DELETE')")
    public void delete(@PathVariable Long id) {
        Deduction deduction = deductionService.findOne(id);
        if (deduction != null) {
            deductionService.delete(id);
            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "سجل الاستقطاعات" : "Data Processing")
                    .message(lang.equals("AR") ? "تم حذف الاستقطاع بنجاح" : "Delete Deduction With All Related Successfully")
                    .type("error")


                    .build(), caller.getEmail());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<Deduction> list = Lists.newArrayList(deductionService.findAll());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), deductionService.findOne(id));
    }
}
