package com.besafx.app.rest.admin;

import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.config.CustomException;
import com.besafx.app.entity.admin.Person;
import com.besafx.app.entity.admin.Product;
import com.besafx.app.search.ProductSearch;
import com.besafx.app.service.admin.ProductService;
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

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(value = "/api/product/")
public class ProductRest {

    private final static Logger log = LoggerFactory.getLogger(ProductRest.class);

    public static final String FILTER_TABLE = "" +
            "**," +
            "productCategory[id,code,name]";
    public static final String FILTER_COMBO = "" +
            "id," +
            "code," +
            "nameArabic," +
            "nameEnglish";

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductSearch productSearch;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_PRODUCT_CREATE')")
    @Transactional
    public String create(@RequestBody Product product) {
        Product topProduct = productService.findTopByOrderByCodeDesc();
        if (topProduct == null) {
            product.setCode(1);
        } else {
            product.setCode(topProduct.getCode() + 1);
        }
        product = productService.save(product);
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .message(lang.equals("AR") ? "تم انشاء صنف جديد بنجاح" : "Create Product Successfully")
                .type("success")
                .build(), caller.getEmail());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), product);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_PRODUCT_UPDATE')")
    @Transactional
    public String update(@RequestBody Product product) {
        if (productService.findByCodeAndIdIsNot(product.getCode(), product.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        Product object = productService.findOne(product.getId());
        if (object != null) {
            product = productService.save(product);
            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .message(lang.equals("AR") ? "تم تعديل بيانات الصنف بنجاح" : "Update Product Successfully")
                    .type("warning")
                    .build(), caller.getEmail());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), product);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_PRODUCT_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {
        Product product = productService.findOne(id);
        if (product != null) {
            productService.delete(id);
            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .message(lang.equals("AR") ? "تم حذف الصنف بنجاح" : "Delete Product Successfully")
                    .type("error")
                    .build(), caller.getEmail());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<Product> list = Lists.newArrayList(productService.findAll());
        list.sort(Comparator.comparing(Product::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findAllCombo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAllCombo() {
        List<Product> list = Lists.newArrayList(productService.findAll());
        list.sort(Comparator.comparing(Product::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_COMBO), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), productService.findOne(id));
    }

    @RequestMapping(value = "filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            @RequestParam(value = "codeFrom", required = false) final Long codeFrom,
            @RequestParam(value = "codeTo", required = false) final Long codeTo,
            @RequestParam(value = "nameArabic", required = false) final String nameArabic,
            @RequestParam(value = "nameEnglish", required = false) final String nameEnglish,
            @RequestParam(value = "productCategoriesIds", required = false) final List<Long> productCategoriesIds) {
        List<Product> list = productSearch.filter(codeFrom, codeTo, nameArabic, nameEnglish, productCategoriesIds);
        list.sort(Comparator.comparing(Product::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }
}
