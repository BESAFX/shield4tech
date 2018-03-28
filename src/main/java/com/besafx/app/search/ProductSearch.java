package com.besafx.app.search;

import com.besafx.app.entity.admin.Product;
import com.besafx.app.entity.admin.ProductCategory;
import com.besafx.app.service.admin.ProductService;
import com.besafx.app.util.DateConverter;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProductSearch {

    private final Logger log = LoggerFactory.getLogger(ProductSearch.class);

    @Autowired
    private ProductService billBuyService;

    public List<Product> filter(
            final Long codeFrom,
            final Long codeTo,
            final String nameArabic,
            final String nameEnglish,
            final List<Long> productCategoriesIds
    ) {
        List<Specification> predicates = new ArrayList<>();
        Optional.ofNullable(codeFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("code"), value)));
        Optional.ofNullable(codeTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("code"), value)));
        Optional.ofNullable(nameArabic).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("nameArabic"), value)));
        Optional.ofNullable(nameEnglish).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("nameEnglish"), value)));
        Optional.ofNullable(productCategoriesIds).ifPresent(value -> predicates.add((root, cq, cb) -> root.get("productCategory").get("id").in(value)));
        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            List<Product> list = billBuyService.findAll(result);
            list.sort(Comparator.comparing(Product::getCode).reversed());
            return list;
        } else {
            return new ArrayList<>();
        }

    }
}
