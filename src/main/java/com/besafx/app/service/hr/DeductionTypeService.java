package com.besafx.app.service.hr;

import com.besafx.app.entity.hr.DeductionType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface DeductionTypeService extends PagingAndSortingRepository<DeductionType, Long>, JpaSpecificationExecutor<DeductionType> {
    DeductionType findTopByOrderByCodeDesc();

    DeductionType findByCodeAndIdIsNot(Integer code, Long id);
}
