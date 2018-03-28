package com.besafx.app.search;

import com.besafx.app.entity.admin.Supplier;
import com.besafx.app.service.admin.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class SupplierSearch {

    private final Logger log = LoggerFactory.getLogger(SupplierSearch.class);

    @Autowired
    private SupplierService customerService;

    public List<Supplier> filter(
            final String code,
            final String name,
            final String mobile,
            final String identityNumber,
            final String email
    ) {
        List<Specification> predicates = new ArrayList<>();

        Optional.ofNullable(code).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("code").as(String.class), "%" + value + "%")));
        Optional.ofNullable(name).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("contact").get("name"), "%" + value + "%")));
        Optional.ofNullable(mobile).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("contact").get("mobile1"), "%" + value + "%")));
        Optional.ofNullable(mobile).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("contact").get("mobile2"), "%" + value + "%")));
        Optional.ofNullable(identityNumber).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("contact").get("identityNumber"), "%" + value + "%")));
        Optional.ofNullable(email).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("contact").get("email"), "%" + value + "%")));

        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            List<Supplier> list = customerService.findAll(result);
            list.sort(Comparator.comparing(Supplier::getCode));
            return list;
        } else {
            return new ArrayList<>();
        }
    }
}
