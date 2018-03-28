package com.besafx.app.service.admin;

import com.besafx.app.entity.admin.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface ProductService extends PagingAndSortingRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Product findTopByOrderByCodeDesc();

    Product findByCode(Integer code);

    Product findByCodeAndIdIsNot(Integer code, Long id);

    List<Product> findByIdIn(List<Long> ids);
}
