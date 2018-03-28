package com.besafx.app.rest.hr;

import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.config.CustomException;
import com.besafx.app.entity.admin.Person;
import com.besafx.app.entity.hr.DeductionType;
import com.besafx.app.service.admin.PersonService;
import com.besafx.app.service.hr.DeductionTypeService;
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
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(value = "/api/deductionType/")
public class DeductionTypeRest {

    public static final String FILTER_TABLE = "**";
    private final static Logger log = LoggerFactory.getLogger(DeductionTypeRest.class);
    @Autowired
    private DeductionTypeService deductionTypeService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DEDUCTION_TYPE_CREATE')")
    public String create(@RequestBody DeductionType deductionType) {
        DeductionType topDeductionType = deductionTypeService.findTopByOrderByCodeDesc();
        if (topDeductionType == null) {
            deductionType.setCode(1);
        } else {
            deductionType.setCode(topDeductionType.getCode() + 1);
        }
        deductionType = deductionTypeService.save(deductionType);
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "بنود الاستقطاعات" : "Data Processing")
                .message(lang.equals("AR") ? "تم انشاء البند بنجاح" : "Create Deduction Type Successfully")
                .type("success")


                .build(), caller.getEmail());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), deductionType);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DEDUCTION_TYPE_UPDATE')")
    public String update(@RequestBody DeductionType deductionType) {
        if (deductionTypeService.findByCodeAndIdIsNot(deductionType.getCode(), deductionType.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        DeductionType object = deductionTypeService.findOne(deductionType.getId());
        if (object != null) {
            deductionType = deductionTypeService.save(deductionType);
            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "بنود الاستقطاعات" : "Data Processing")
                    .message(lang.equals("AR") ? "تم تعديل بيانات البند بنجاح" : "Update Deduction Type Information Successfully")
                    .type("warning")


                    .build(), caller.getEmail());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), deductionType);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DEDUCTION_TYPE_DELETE')")
    public void delete(@PathVariable Long id) {
        DeductionType deductionType = deductionTypeService.findOne(id);
        if (deductionType != null) {
            deductionTypeService.delete(id);
            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "بنود الاستقطاعات" : "Data Processing")
                    .message(lang.equals("AR") ? "تم حذف البند وكل ما يتعلق به من حسابات بنجاح" : "Delete Deduction Type With All Related Successfully")
                    .type("error")


                    .build(), caller.getEmail());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<DeductionType> list = Lists.newArrayList(deductionTypeService.findAll());
        list.sort(Comparator.comparing(DeductionType::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), deductionTypeService.findOne(id));
    }
}
