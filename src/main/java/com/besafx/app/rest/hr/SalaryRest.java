package com.besafx.app.rest.hr;

import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.entity.admin.Person;
import com.besafx.app.entity.admin.Receipt;
import com.besafx.app.entity.enums.ReceiptType;
import com.besafx.app.entity.hr.Salary;
import com.besafx.app.service.admin.PersonService;
import com.besafx.app.service.admin.ReceiptService;
import com.besafx.app.service.hr.SalaryService;
import com.besafx.app.util.ArabicLiteralNumberParser;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/salary/")
public class SalaryRest {

    public static final String FILTER_TABLE = "**,receipt[id,code,amountNumber],employee[id,-salaries,person[id,nickname,name,mobile]]";
    private final static Logger log = LoggerFactory.getLogger(SalaryRest.class);
    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private SalaryService salaryService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_SALARY_CREATE')")
    @Transactional
    public String create(@RequestBody Salary salary) {
        log.info("إنشاء سند القبض");
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        Receipt topReceipt = receiptService.findTopByOrderByCodeDesc();
        if (topReceipt == null) {
            salary.getReceipt().setCode(Long.valueOf(1));
        } else {
            salary.getReceipt().setCode(topReceipt.getCode() + 1);
        }
        salary.getReceipt().setReceiptType(ReceiptType.In);
        salary.getReceipt().setAmountString(ArabicLiteralNumberParser.literalValueOf(salary.getReceipt().getAmountNumber()));
        salary.getReceipt().setSender(caller.getNickname() + " / " + caller.getNickname());
        salary.getReceipt().setReceiver(salary.getEmployee().getPerson().getNickname() + " / " + salary.getEmployee().getPerson().getNickname());
        salary.getReceipt().setDate(new Date());
        salary.getReceipt().setLastUpdate(new Date());
        salary.getReceipt().setLastPerson(caller);
        salary.setReceipt(receiptService.save(salary.getReceipt()));
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "السندات" : "Data Processing")
                .message(lang.equals("AR") ? "تم انشاء سند القبض بنجاح" : "Create Receipt Account Successfully")
                .type("success")


                .build(), caller.getEmail());
        log.info("إنشاء الراتب");
        salary = salaryService.save(salary);
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "سجل الرواتب" : "Data Processing")
                .message(lang.equals("AR") ? "تم انشاء سند الراتب بنجاح" : "Create Salary Successfully")
                .type("success")


                .build(), caller.getEmail());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), salary);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_SALARY_UPDATE')")
    @Transactional
    public String update(@RequestBody Salary salary) {
        Salary object = salaryService.findOne(salary.getId());
        if (object != null) {
            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            log.info("تعديل بيانات سند القبض");
            salary.getReceipt().setAmountString(ArabicLiteralNumberParser.literalValueOf(object.getReceipt().getAmountNumber()));
            salary.getReceipt().setSender(caller.getNickname() + " / " + caller.getNickname());
            salary.getReceipt().setReceiver(salary.getEmployee().getPerson().getNickname() + " / " + salary.getEmployee().getPerson().getNickname());
            salary.getReceipt().setLastUpdate(new Date());
            salary.getReceipt().setLastPerson(caller);
            salary.setReceipt(receiptService.save(salary.getReceipt()));
            log.info("تعديل بيانات الراتب");
            salary = salaryService.save(salary);
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "سجل الرواتب" : "Data Processing")
                    .message(lang.equals("AR") ? "تم تعديل بيانات سند الراتب بنجاح" : "Update Salary Information Successfully")
                    .type("warning")


                    .build(), caller.getEmail());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), salary);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_SALARY_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {
        Salary salary = salaryService.findOne(id);
        if (salary != null) {
            receiptService.delete(salary.getReceipt());
            salaryService.delete(salary);
            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "سجل الرواتب" : "Data Processing")
                    .message(lang.equals("AR") ? "تم حذف سند الراتب بنجاح" : "Delete Salary With All Related Successfully")
                    .type("error")


                    .build(), caller.getEmail());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<Salary> list = Lists.newArrayList(salaryService.findAll());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), salaryService.findOne(id));
    }
}
