package com.besafx.app.controller;

import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.config.DropboxManager;
import com.besafx.app.entity.admin.Company;
import com.besafx.app.entity.admin.Person;
import com.besafx.app.service.admin.CompanyService;
import com.besafx.app.service.admin.PersonService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.concurrent.Future;

@RestController
public class FileUploadController {

    private final static Logger log = LoggerFactory.getLogger(FileUploadController.class);

    private SecureRandom random;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PersonService personService;

    @Autowired
    private DropboxManager dropboxManager;

    @PostConstruct
    public void init() {
        random = new SecureRandom();
    }

    @RequestMapping(value = "/uploadCompanyLogo", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String uploadCompanyLogo(@RequestParam("file") MultipartFile file) throws Exception {
        String fileName = "LOGO" + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        Future<Boolean> uploadTask = dropboxManager.uploadFile(file, "/SHIELD/COMPANY/" + fileName);
        if (uploadTask.get()) {
            Future<String> shareTask = dropboxManager.shareFile("/SHIELD/COMPANY/" + fileName);
            String photoLink = shareTask.get();
            Company company = companyService.findFirstBy();
            company.setLogo(photoLink);
            companyService.save(company);
            return photoLink;
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/uploadUserPhoto", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String uploadUserPhoto(@RequestParam("file") MultipartFile file) throws Exception {
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        String fileName = new BigInteger(130, random).toString(32) + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        Future<Boolean> uploadTask = dropboxManager.uploadFile(file, "/SHIELD/USERS/" + fileName);
        if (uploadTask.get()) {
            Future<String> shareTask = dropboxManager.shareFile("/SHIELD/USERS/" + fileName);
            String photoLink = shareTask.get();
            caller.setPhoto(photoLink);
            personService.save(caller);
            return photoLink;
        } else {
            return null;
        }
    }
}
