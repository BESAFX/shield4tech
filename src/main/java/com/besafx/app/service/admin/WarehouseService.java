package com.besafx.app.service.admin;

import com.besafx.app.entity.admin.Warehouse;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface WarehouseService extends PagingAndSortingRepository<Warehouse, Long>, JpaSpecificationExecutor<Warehouse> {
    Warehouse findTopByOrderByCodeDesc();

    Warehouse findByCode(Integer code);

    Warehouse findByCodeAndIdIsNot(Integer code, Long id);

    List<Warehouse> findByIdIn(List<Long> ids);
}
