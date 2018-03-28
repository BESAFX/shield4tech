package com.besafx.app.rest.buy;

import com.besafx.app.entity.buy.BillBuy;
import com.besafx.app.search.BillBuySearch;
import com.besafx.app.service.buy.BillBuyService;
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
@RequestMapping(value = "/api/billBuy/")
public class BillBuyRest {

    private final Logger log = LoggerFactory.getLogger(BillBuyRest.class);

    public static final String FILTER_TABLE = "**,supplier[id,code,name],transactionBuys[id],-billBuyReceipts";
    public static final String FILTER_TABLE_DETAILS = "**,supplier[id,code,name],transactionBuys[**,drugUnit[id,code,name],drug[id,code,nameArabic,nameEnglish],-billBuy,-transactionSells],billBuyReceipts[**,-billBuy,receipt[**,lastPerson[id,name]]]";
    public static final String FILTER_BILL_BUY_COMBO = "id,code";

    @Autowired
    private BillBuyService billBuyService;

    @Autowired
    private BillBuySearch billBuySearch;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_BUY_CREATE')")
    @Transactional
    public String create(@RequestBody BillBuy billBuy) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billBuy);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_BUY_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {

    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<BillBuy> list = Lists.newArrayList(billBuyService.findAll());
        list.sort(Comparator.comparing(BillBuy::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findAllCombo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAllCombo() {
        List<BillBuy> list = Lists.newArrayList(billBuyService.findAll());
        list.sort(Comparator.comparing(BillBuy::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_BILL_BUY_COMBO), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE_DETAILS), billBuyService.findOne(id));
    }

    @RequestMapping(value = "filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            @RequestParam(value = "codeFrom", required = false) final Long codeFrom,
            @RequestParam(value = "codeTo", required = false) final Long codeTo,
            @RequestParam(value = "dateFrom", required = false) final Long dateFrom,
            @RequestParam(value = "dateTo", required = false) final Long dateTo,
            @RequestParam(value = "suppliers", required = false) final List<Long> suppliers) {
        List<BillBuy> list = billBuySearch.filter(codeFrom, codeTo, dateFrom, dateTo, suppliers);
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findByToday", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByToday() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billBuySearch.findByToday());
    }

    @RequestMapping(value = "findByWeek", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByWeek() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billBuySearch.findByWeek());
    }

    @RequestMapping(value = "findByMonth", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByMonth() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billBuySearch.findByMonth());
    }

    @RequestMapping(value = "findByYear", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByYear() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billBuySearch.findByYear());
    }
}
