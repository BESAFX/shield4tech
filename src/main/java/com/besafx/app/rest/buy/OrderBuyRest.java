package com.besafx.app.rest.buy;

import com.besafx.app.entity.buy.OrderBuy;
import com.besafx.app.search.OrderBuySearch;
import com.besafx.app.service.buy.OrderBuyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(value = "/api/order/")
public class OrderBuyRest {

    public static final String FILTER_TABLE = "**,orderReceipts[id,receipt[**,lastPerson[id,nickname,name]]],lastPerson[id,nickname,name],falcon[id,code,type,weight,customer[id,code,name]],doctor[**,person[id,code,name,mobile,identityNumber]],diagnoses[**,-order,drug[id,code,nameArabic,nameEnglish,medicalNameArabic,medicalNameEnglish],drugUnit[id,name]],orderDetectionTypes[id,done,detectionType[id,code,nameArabic,nameEnglish,cost]],orderAttaches[**,attach[**,person[id,nickname,name]],-order]";
    public static final String FILTER_TABLE_DEBT = "**,-orderReceipts,lastPerson[id,nickname,name],falcon[id,code,type,weight,customer[id,code,name]],-doctor,-diagnoses,-orderDetectionTypes,-orderAttaches";
    public static final String FILTER_ORDER_COMBO = "id,code";

    private final Logger log = LoggerFactory.getLogger(OrderBuyRest.class);

    @Autowired
    private OrderBuyService orderService;

    @Autowired
    private OrderBuySearch orderBuySearch;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_CREATE')")
    @Transactional
    public String create(@RequestBody OrderBuy order) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), order);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) throws Exception {

    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<OrderBuy> list = Lists.newArrayList(orderService.findAll());
        list.sort(Comparator.comparing(OrderBuy::getCode).reversed());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findAllCombo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAllCombo() {
        List<OrderBuy> list = Lists.newArrayList(orderService.findAll());
        list.sort(Comparator.comparing(OrderBuy::getCode).reversed());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_ORDER_COMBO), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderService.findOne(id));
    }

    @RequestMapping(value = "filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            @RequestParam(value = "codeFrom", required = false) final Long codeFrom,
            @RequestParam(value = "codeTo", required = false) final Long codeTo,
            @RequestParam(value = "dateFrom", required = false) final Long dateFrom,
            @RequestParam(value = "dateTo", required = false) final Long dateTo) {
        List<OrderBuy> list = orderBuySearch.filter(codeFrom, codeTo, dateFrom, dateTo);
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "filterDebt", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filterDebt(
            @RequestParam(value = "codeFrom", required = false) final Long codeFrom,
            @RequestParam(value = "codeTo", required = false) final Long codeTo,
            @RequestParam(value = "dateFrom", required = false) final Long dateFrom,
            @RequestParam(value = "dateTo", required = false) final Long dateTo) {
        List<OrderBuy> list = orderBuySearch.filter(codeFrom, codeTo, dateFrom, dateTo);
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE_DEBT), list);
    }

    @RequestMapping(value = "findByToday", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByToday(@RequestParam(value = "filter", required = false) final String filter) throws Exception {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), filter == null ? FILTER_TABLE : (String) this.getClass().getField(filter).get(this)), orderBuySearch.findByToday());
    }

    @RequestMapping(value = "findByWeek", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByWeek(@RequestParam(value = "filter", required = false) final String filter) throws Exception {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), filter == null ? FILTER_TABLE : (String) this.getClass().getField(filter).get(this)), orderBuySearch.findByWeek());
    }

    @RequestMapping(value = "findByMonth", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByMonth(@RequestParam(value = "filter", required = false) final String filter) throws Exception {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), filter == null ? FILTER_TABLE : (String) this.getClass().getField(filter).get(this)), orderBuySearch.findByMonth());
    }

    @RequestMapping(value = "findByYear", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByYear(@RequestParam(value = "filter", required = false) final String filter) throws Exception {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), filter == null ? FILTER_TABLE : (String) this.getClass().getField(filter).get(this)), orderBuySearch.findByYear());
    }
}
