package com.besafx.app.rest.admin;

import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.config.CustomException;
import com.besafx.app.entity.admin.Person;
import com.besafx.app.entity.admin.ProductCategory;
import com.besafx.app.service.admin.PersonService;
import com.besafx.app.service.admin.ProductCategoryService;
import com.besafx.app.util.JSONConverter;
import com.besafx.app.util.Options;
import com.besafx.app.ws.Notification;
import com.besafx.app.ws.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.google.common.collect.Lists;
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
@RequestMapping(value = "/api/productCategory/")
public class ProductCategoryRest {

    public static final String FILTER_TABLE = "" +
            "**," +
            "products[id]";
    public static final String FILTER_DETAILS = "" +
            "**," +
            "products[**,-productCategory]";
    public static final String FILTER_COMBO = "" +
            "id," +
            "code," +
            "nameArabic," +
            "nameEnglish";

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_PRODUCT_CATEGORY_CREATE')")
    @Transactional
    public String create(@RequestBody ProductCategory productCategory) {
        ProductCategory topProductCategory = productCategoryService.findTopByOrderByCodeDesc();
        if (topProductCategory == null) {
            productCategory.setCode(1);
        } else {
            productCategory.setCode(topProductCategory.getCode() + 1);
        }
        productCategory = productCategoryService.save(productCategory);
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .message(lang.equals("AR") ? "تم انشاء تصنيف جديد بنجاح" : "Create Category Successfully")
                .type("success")
                .build(), caller.getEmail());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), productCategory);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_PRODUCT_CATEGORY_UPDATE')")
    @Transactional
    public String update(@RequestBody ProductCategory productCategory) {
        if (productCategoryService.findByCodeAndIdIsNot(productCategory.getCode(), productCategory.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        ProductCategory object = productCategoryService.findOne(productCategory.getId());
        if (object != null) {
            productCategory = productCategoryService.save(productCategory);
            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .message(lang.equals("AR") ? "تم تعديل بيانات التصنيف بنجاح" : "Update Category Successfully")
                    .type("warning")
                    .build(), caller.getEmail());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), productCategory);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_PRODUCT_CATEGORY_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {
        ProductCategory productCategory = productCategoryService.findOne(id);
        if (productCategory != null) {
            productCategoryService.delete(id);
            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .message(lang.equals("AR") ? "تم حذف التصنيف بنجاح" : "Delete Category Successfully")
                    .type("error")
                    .build(), caller.getEmail());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<ProductCategory> list = Lists.newArrayList(productCategoryService.findAll());
        list.sort(Comparator.comparing(ProductCategory::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findAllCombo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAllCombo() {
        List<ProductCategory> list = Lists.newArrayList(productCategoryService.findAll());
        list.sort(Comparator.comparing(ProductCategory::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_COMBO), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_DETAILS), productCategoryService.findOne(id));
    }
}
