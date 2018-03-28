package com.besafx.app.service.admin;

import com.besafx.app.entity.admin.Supplier;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface SupplierService extends PagingAndSortingRepository<Supplier, Long>, JpaSpecificationExecutor<Supplier> {
    Supplier findTopByOrderByCodeDesc();

    Supplier findByCodeAndIdIsNot(Integer code, Long id);
}

