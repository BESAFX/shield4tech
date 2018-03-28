package com.besafx.app.service.hr;

import com.besafx.app.entity.hr.VacationType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface VacationTypeService extends PagingAndSortingRepository<VacationType, Long>, JpaSpecificationExecutor<VacationType> {
    VacationType findTopByOrderByCodeDesc();

    VacationType findByCodeAndIdIsNot(Integer code, Long id);
}
