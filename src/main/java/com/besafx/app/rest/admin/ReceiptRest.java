package com.besafx.app.rest.admin;

import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.config.CustomException;
import com.besafx.app.entity.admin.Person;
import com.besafx.app.entity.admin.Receipt;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.entity.enums.ReceiptType;
import com.besafx.app.search.ReceiptSearch;
import com.besafx.app.service.admin.PersonService;
import com.besafx.app.service.admin.ReceiptService;
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
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/receipt/")
public class ReceiptRest {

    public static final String FILTER_TABLE = "**,lastPerson[id,nickname,name]";
    private final static Logger log = LoggerFactory.getLogger(ReceiptRest.class);
    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private ReceiptSearch receiptSearch;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_RECEIPT_CREATE')")
    public String create(@RequestBody Receipt receipt) {
        Receipt topReceipt = receiptService.findTopByOrderByCodeDesc();
        if (topReceipt == null) {
            receipt.setCode(Long.valueOf(1));
        } else {
            receipt.setCode(topReceipt.getCode() + 1);
        }
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        receipt.setDate(new Date());
        receipt.setLastUpdate(new Date());
        receipt.setLastPerson(caller);
        receipt = receiptService.save(receipt);
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .message(lang.equals("AR") ? "تم انشاء السند بنجاح" : "Create Receipt Account Successfully")
                .type("success")
                .build(), caller.getEmail());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), receipt);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_RECEIPT_UPDATE')")
    public String update(@RequestBody Receipt receipt) {
        if (receiptService.findByCodeAndIdIsNot(receipt.getCode(), receipt.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        Receipt object = receiptService.findOne(receipt.getId());
        if (object != null) {
            receipt = receiptService.save(receipt);
            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .message(lang.equals("AR") ? "تم تعديل بيانات السند بنجاح" : "Update Receipt Account Information Successfully")
                    .type("warning")
                    .build(), caller.getEmail());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), receipt);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_RECEIPT_DELETE')")
    public void delete(@PathVariable Long id) {
        Receipt receipt = receiptService.findOne(id);
        if (receipt != null) {
            receiptService.delete(id);
            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .message(lang.equals("AR") ? "تم حذف السند وكل ما يتعلق به من حسابات بنجاح" : "Delete Receipt Account With All Related Successfully")
                    .type("error")
                    .build(), caller.getEmail());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<Receipt> list = Lists.newArrayList(receiptService.findAll());
        list.sort(Comparator.comparing(Receipt::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), receiptService.findOne(id));
    }

    @RequestMapping(value = "filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            @RequestParam(value = "code", required = false) final Long code,
            @RequestParam(value = "dateFrom", required = false) final Long dateFrom,
            @RequestParam(value = "dateTo", required = false) final Long dateTo,
            @RequestParam(value = "lastUpdateFrom", required = false) final Long lastUpdateFrom,
            @RequestParam(value = "lastUpdateTo", required = false) final Long lastUpdateTo,
            @RequestParam(value = "paymentMethods", required = false) final List<PaymentMethod> paymentMethods,
            @RequestParam(value = "checkCode", required = false) final Long checkCode,
            @RequestParam(value = "amountFrom", required = false) final Double amountFrom,
            @RequestParam(value = "amountTo", required = false) final Double amountTo,
            @RequestParam(value = "receiptTypes", required = false) final List<ReceiptType> receiptTypes,
            @RequestParam(value = "personIds", required = false) final List<Long> personIds) {
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification.builder().message(lang.equals("AR") ? "جاري تصفية النتائج، فضلاً انتظر قليلا" : "Filtering Data").type("success").build(), caller.getEmail());
        List<Receipt> list = receiptSearch.filter(code, dateFrom, dateTo, lastUpdateFrom, lastUpdateTo, paymentMethods, checkCode, amountFrom, amountTo, receiptTypes, personIds);
        notificationService.notifyOne(Notification.builder().message(lang.equals("AR") ? "تمت العملية بنجاح" : "job Done").type("success").build(), caller.getEmail());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }
}
