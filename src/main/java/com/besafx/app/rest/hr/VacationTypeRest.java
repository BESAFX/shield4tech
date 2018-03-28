package com.besafx.app.rest.hr;

import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.config.CustomException;
import com.besafx.app.entity.admin.Person;
import com.besafx.app.entity.hr.VacationType;
import com.besafx.app.service.admin.PersonService;
import com.besafx.app.service.hr.VacationTypeService;
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
@RequestMapping(value = "/api/vacationType/")
public class VacationTypeRest {

    public static final String FILTER_TABLE = "**";
    private final static Logger log = LoggerFactory.getLogger(VacationTypeRest.class);
    @Autowired
    private VacationTypeService vacationTypeService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_VACATION_TYPE_CREATE')")
    public String create(@RequestBody VacationType vacationType) {
        VacationType topVacationType = vacationTypeService.findTopByOrderByCodeDesc();
        if (topVacationType == null) {
            vacationType.setCode(1);
        } else {
            vacationType.setCode(topVacationType.getCode() + 1);
        }
        vacationType = vacationTypeService.save(vacationType);
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "بنود الاجازات" : "Data Processing")
                .message(lang.equals("AR") ? "تم انشاء البند بنجاح" : "Create Vacation Type Successfully")
                .type("success")


                .build(), caller.getEmail());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), vacationType);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_VACATION_TYPE_UPDATE')")
    public String update(@RequestBody VacationType vacationType) {
        if (vacationTypeService.findByCodeAndIdIsNot(vacationType.getCode(), vacationType.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        VacationType object = vacationTypeService.findOne(vacationType.getId());
        if (object != null) {
            vacationType = vacationTypeService.save(vacationType);
            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "بنود الاجازات" : "Data Processing")
                    .message(lang.equals("AR") ? "تم تعديل بيانات البند بنجاح" : "Update Vacation Type Information Successfully")
                    .type("warning")


                    .build(), caller.getEmail());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), vacationType);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_VACATION_TYPE_DELETE')")
    public void delete(@PathVariable Long id) {
        VacationType vacationType = vacationTypeService.findOne(id);
        if (vacationType != null) {
            vacationTypeService.delete(id);
            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "بنود الاجازات" : "Data Processing")
                    .message(lang.equals("AR") ? "تم حذف البند وكل ما يتعلق به من حسابات بنجاح" : "Delete Vacation Type With All Related Successfully")
                    .type("error")


                    .build(), caller.getEmail());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<VacationType> list = Lists.newArrayList(vacationTypeService.findAll());
        list.sort(Comparator.comparing(VacationType::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), vacationTypeService.findOne(id));
    }
}
