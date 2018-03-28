package com.besafx.app.service.admin;

import com.besafx.app.entity.admin.ProductCategory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface ProductCategoryService extends PagingAndSortingRepository<ProductCategory, Long>, JpaSpecificationExecutor<ProductCategory> {

    ProductCategory findTopByOrderByCodeDesc();

    ProductCategory findByCodeAndIdIsNot(Integer code, Long id);
}
