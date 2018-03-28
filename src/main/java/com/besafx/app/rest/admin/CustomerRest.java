package com.besafx.app.rest.admin;

import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.config.CustomException;
import com.besafx.app.entity.admin.Customer;
import com.besafx.app.entity.admin.Person;
import com.besafx.app.search.CustomerSearch;
import com.besafx.app.service.admin.ContactService;
import com.besafx.app.service.admin.CustomerService;
import com.besafx.app.util.JSONConverter;
import com.besafx.app.util.Options;
import com.besafx.app.ws.Notification;
import com.besafx.app.ws.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(value = "/api/customer/")
public class CustomerRest {

    public static final String FILTER_TABLE = "" +
            "**," +
            "contact[id,name,email]";
    public static final String FILTER_DETAILS = "" +
            "**," +
            "contact[**]";
    public static final String FILTER_COMBO = "" +
            "**," +
            "contact[id,name]";

    private final static Logger log = LoggerFactory.getLogger(CustomerRest.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private CustomerSearch customerSearch;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_CUSTOMER_CREATE')")
    public String create(@RequestBody Customer customer) {
        Customer topCustomer = customerService.findTopByOrderByCodeDesc();
        if (topCustomer == null) {
            customer.setCode(1);
        } else {
            customer.setCode(topCustomer.getCode() + 1);
        }
        customer.setEnabled(true);
        customer.getContact().setRegisterDate(new DateTime().toDate());
        customer.setContact(contactService.save(customer.getContact()));
        customer = customerService.save(customer);
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .message(lang.equals("AR") ? "تم انشاء حساب العميل بنجاح" : "Create Customer Account Successfully")
                .type("success")
                .build(), caller.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), customer);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_CUSTOMER_UPDATE')")
    public String update(@RequestBody Customer customer) {
        if (customerService.findByCodeAndIdIsNot(customer.getCode(), customer.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        Customer object = customerService.findOne(customer.getId());
        if (object != null) {
            customer.setContact(contactService.save(customer.getContact()));
            customer = customerService.save(customer);
            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .message(lang.equals("AR") ? "تم تعديل بيانات حساب العميل بنجاح" : "Update Customer Account Information Successfully")
                    .type("warning")
                    .build(), caller.getEmail());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), customer);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "enable/{customerId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_CUSTOMER_ENABLE')")
    @Transactional
    public String enable(@PathVariable(value = "customerId") Long customerId) {
        Customer customer = customerService.findOne(customerId);
        if (customer != null) {
            customer.setEnabled(true);
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), customerService.save(customer));
        } else {
            throw new CustomException("هذا العميل غير موجود");
        }
    }

    @RequestMapping(value = "disable/{customerId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_CUSTOMER_DISABLE')")
    @Transactional
    public String disable(@PathVariable(value = "customerId") Long customerId) {
        Customer customer = customerService.findOne(customerId);
        if (customer != null) {
            customer.setEnabled(false);
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), customerService.save(customer));
        } else {
            throw new CustomException("هذا العميل غير موجود");
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_CUSTOMER_DELETE')")
    public void delete(@PathVariable Long id) {
        Customer customer = customerService.findOne(id);
        if (customer != null) {
            customerService.delete(id);
            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .message(lang.equals("AR") ? "تم حذف حساب العميل وكل ما يتعلق به من حسابات بنجاح" : "Delete Customer Account With All Related Successfully")
                    .type("error")
                    .build(), caller.getEmail());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<Customer> list = Lists.newArrayList(customerService.findAll());
        list.sort(Comparator.comparing(Customer::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findAllCombo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAllCombo() {
        List<Customer> list = Lists.newArrayList(customerService.findAll());
        list.sort(Comparator.comparing(Customer::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_COMBO), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_DETAILS), customerService.findOne(id));
    }

    @RequestMapping(value = "filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            @RequestParam(value = "code", required = false) final String code,
            @RequestParam(value = "name", required = false) final String name,
            @RequestParam(value = "mobile", required = false) final String mobile,
            @RequestParam(value = "identityNumber", required = false) final String identityNumber,
            @RequestParam(value = "email", required = false) final String email) {
        List<Customer> list = customerSearch.filter(code, name, mobile, identityNumber, email);
        list.sort(Comparator.comparing(Customer::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }
}
