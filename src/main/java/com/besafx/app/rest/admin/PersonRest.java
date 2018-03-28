package com.besafx.app.rest.admin;

import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.entity.admin.Person;
import com.besafx.app.service.admin.PersonService;
import com.besafx.app.util.JSONConverter;
import com.besafx.app.util.Options;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/person/")
public class PersonRest {

    public static final String FILTER_TABLE = "**,-hiddenPassword,team[**,-persons]";

    @Autowired
    private PersonService personService;

    @RequestMapping(value = "setGUILang/{lang}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_PROFILE_UPDATE')")
    public void setGUILang(@PathVariable(value = "lang") String lang) {
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        Options options = JSONConverter.toObject(caller.getOptions(), Options.class);
        options.setLang(lang);
        caller.setOptions(JSONConverter.toString(options));
        personService.save(caller);
    }

    @RequestMapping(value = "setDateType/{type}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_PROFILE_UPDATE')")
    public void setDateType(@PathVariable(value = "type") String type) {
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        Options options = JSONConverter.toObject(caller.getOptions(), Options.class);
        options.setDateType(type);
        caller.setOptions(JSONConverter.toString(options));
        personService.save(caller);
    }

    @RequestMapping(value = "setStyle/{style}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_PROFILE_UPDATE')")
    public void setStyle(@PathVariable(value = "style") String style) {
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        Options options = JSONConverter.toObject(caller.getOptions(), Options.class);
        options.setStyle(style);
        caller.setOptions(JSONConverter.toString(options));
        personService.save(caller);
    }

    @RequestMapping("findActivePerson")
    @ResponseBody
    public String findActivePerson() {
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), caller);
    }

}
